package com.scalepoint.automation.services.usersmanagement;

import com.scalepoint.automation.utils.annotations.UserAttributes;
import com.scalepoint.automation.utils.data.entity.credentials.ExistingUsers;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.threadlocal.CurrentUser;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.awaitility.Duration;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static org.awaitility.Awaitility.await;

public class UsersManager {

    private static Logger logger = LogManager.getLogger(UsersManager.class);

    private static BlockingQueue<User> basicUsersQueue = new LinkedBlockingQueue<>();
    private static ConcurrentMap<CompanyCode, BlockingQueue<User>> exceptionalUsersQueues = new ConcurrentHashMap<>();
    private static ConcurrentMap<CompanyCode, BlockingQueue<User>> scalepointIdUsersQueue = new ConcurrentHashMap<>();
    private static User systemUser;

    private static Map<String, Set<User>> usersInfo = new HashMap<>();

    public static void initManager(ExistingUsers existingUsers) {

        logger.info("Initializing UsersManager");
        existingUsers.getUsers().forEach(user -> {

            if (user.getType().equals(User.UserType.SYSTEM)) {

                systemUser = user;
                usersInfo.put(user.getCompanyCode(), new HashSet<>(Arrays.asList(new User[]{user})));
                return;
            }
            if (user.getType().equals(User.UserType.BASIC)) {

                basicUsersQueue.add(user);
                usersInfo.put(user.getCompanyCode(), new HashSet<>(Arrays.asList(new User[]{user})));
            }
            if(user.getType().equals(User.UserType.SCALEPOINT_ID)){
                
                CompanyCode key = CompanyCode.valueOf(user.getCompanyCode());
                if(!scalepointIdUsersQueue.containsKey(key)) {

                    scalepointIdUsersQueue.put(CompanyCode.valueOf(user.getCompanyCode()),
                            new ArrayBlockingQueue<>(6, true, Collections.singleton(user)));
                }else{

                    exceptionalUsersQueues.get(key).add(user);
                }
                usersInfo.put(user.getCompanyCode(), new HashSet<>(Arrays.asList(new User[]{user})));
            }
            if(user.getType().equals(User.UserType.EXCEPTIONAL)) {

                CompanyCode key = CompanyCode.valueOf(user.getCompanyCode());
                if(!exceptionalUsersQueues.containsKey(key)) {

                    exceptionalUsersQueues.put(CompanyCode.valueOf(user.getCompanyCode()),
                            new ArrayBlockingQueue<>(6, true, Collections.singleton(user)));
                }else{

                    exceptionalUsersQueues.get(key).add(user);
                }

                usersInfo.put(user.getCompanyCode(), new HashSet<>(exceptionalUsersQueues.get(key)));
            }
        });

        printQueues();
    }

    public static List<User> fetchUsersWhenAvailable(List<RequestedUserAttributes> requestedUsers) {

        await()
                .pollInterval(Duration.TEN_SECONDS)
                .atMost(1, TimeUnit.HOURS)
                .untilTrue(usersAvailable(requestedUsers));

        List<User> fetchedUsers = requestedUsers.stream()
                .map(requestedUserAttributes ->  takeUser(requestedUserAttributes))
                .collect(Collectors.toList());

        return fetchedUsers;
    }

    private static synchronized AtomicBoolean usersAvailable(List<RequestedUserAttributes> requestedUsers) {

        long basicUsersRequestedCount = requestedUsers.stream()
                .filter(requestedUserAttributes -> requestedUserAttributes.getType().equals(User.UserType.BASIC))
                .count();

        Map<CompanyCode, Long> scalepointIdUsersRequestedCount = new HashMap<>();

        List<RequestedUserAttributes> scalepointIdUsersUsersRequested = requestedUsers.stream()
                .filter(requestedUserAttributes -> requestedUserAttributes.getType().equals(User.UserType.SCALEPOINT_ID))
                .collect(Collectors.toList());

        scalepointIdUsersUsersRequested.forEach(requestedUserAttributes -> {

            CompanyCode companyCode = requestedUserAttributes.getCompanyCode();

            if(scalepointIdUsersRequestedCount.containsKey(companyCode)){

                scalepointIdUsersRequestedCount.put(companyCode, scalepointIdUsersRequestedCount.get(companyCode) + 1L);
            }
            else {

                scalepointIdUsersRequestedCount.put(companyCode, 1L);
            }
        });

        Map<CompanyCode, Long> exceptionalUsersRequestedCount = new HashMap<>();

        List<RequestedUserAttributes> exceptionalUsersRequested = requestedUsers.stream()
                .filter(requestedUserAttributes -> requestedUserAttributes.getType().equals(User.UserType.EXCEPTIONAL))
                .collect(Collectors.toList());

        exceptionalUsersRequested.forEach(requestedUserAttributes -> {

            CompanyCode companyCode = requestedUserAttributes.getCompanyCode();

            if(exceptionalUsersRequestedCount.containsKey(companyCode)){

                exceptionalUsersRequestedCount.put(companyCode, exceptionalUsersRequestedCount.get(companyCode) + 1L);
            }
            else {

                exceptionalUsersRequestedCount.put(companyCode, 1L);
            }
        });

        boolean basicUsersAvailable = basicUsersQueue.size() >= basicUsersRequestedCount;

        boolean scalepointIdUsersAvailable = scalepointIdUsersRequestedCount.entrySet().stream()
                .map(entry -> scalepointIdUsersQueue.get(entry.getKey()).size() >= entry.getValue())
                .allMatch(b -> b.equals(true));

        boolean exceptionalUsersAvailable = exceptionalUsersRequestedCount.entrySet().stream()
                .map(entry -> exceptionalUsersQueues.get(entry.getKey()).size() >= entry.getValue())
                .allMatch(b -> b.equals(true));

        logger.info(String.format("basicUsersAvailable: %b, scalepointIdUsersAvailable: %b, exceptionalUsersAvailable: %b", basicUsersAvailable, scalepointIdUsersAvailable, exceptionalUsersAvailable));
        AtomicBoolean result =  new AtomicBoolean(basicUsersAvailable && scalepointIdUsersAvailable && exceptionalUsersAvailable);

        return result;
    }

    private static User takeUser(RequestedUserAttributes requestedUserAttributes) {

        CompanyCode companyCode = requestedUserAttributes.getCompanyCode();

        try {

            User taken = null;

            if(requestedUserAttributes.getType().equals(User.UserType.EXCEPTIONAL)) {
                taken = exceptionalUsersQueues.get(companyCode).take();
                logger.info("Requested: {} Taken: {}", companyCode.name(), taken.getLogin());
            }
            if(requestedUserAttributes.getType().equals(User.UserType.BASIC)){

                taken = basicUsersQueue.take();
                logger.info("Requested: {} Taken: {}", requestedUserAttributes.getType().name(), taken.getLogin());
            }
            if(requestedUserAttributes.getType().equals(User.UserType.SCALEPOINT_ID)) {

                taken = scalepointIdUsersQueue.get(companyCode).take();
                logger.info("Requested: {} Taken: {}", requestedUserAttributes.getType().name(), taken.getLogin());
            }

            return taken;

        } catch (Exception e) {

            logger.error("Can't take user for {} cause {}", companyCode.name(), e.toString());
            throw new IllegalStateException(e);
        }
    }

    public static void releaseUser(User user) {

        logger.info("Returned: {}", user.getLogin());
        if (user.getType().equals(User.UserType.BASIC)) {

            basicUsersQueue.add(user);
        }
        if(user.getType().equals(User.UserType.EXCEPTIONAL)){

            exceptionalUsersQueues.get(CompanyCode.valueOf(user.getCompanyCode())).add(user);
        }
        if(user.getType().equals(User.UserType.SCALEPOINT_ID)){

            scalepointIdUsersQueue.get(CompanyCode.valueOf(user.getCompanyCode())).add(user);
        }
        logger.info("User: {} released", user.getLogin());
    }

    private static void printQueues() {
        logger.info(" * * * basic * * *");
        for (User user : basicUsersQueue) {
            logger.info(user.getCompanyCode());
        }
        logger.info(" * * * specific * * *");
        for (CompanyCode companyCode : exceptionalUsersQueues.keySet()) {
            logger.info(companyCode.name());
        }
        logger.info(" * * * end * * *");
    }

    public static synchronized User getSystemUser() {
        return systemUser;
    }

    public static class RequestedUserAttributes {

        @Getter
        private CompanyCode companyCode;
        @Getter
        @Setter
        private User.UserType type;

        public RequestedUserAttributes(CompanyCode companyCode, User.UserType type) {

            this.companyCode = companyCode;
            this.type =  type;
        }

        static public RequestedUserAttributes getRequestedUserAttributes(Annotation[] listOfRequestedUserAttributes) {

            Optional<Annotation> userAttributesAnnotation = Arrays.stream(listOfRequestedUserAttributes)
                    .filter(annotation -> annotation.annotationType().equals(UserAttributes.class))
                    .findFirst();

            if(userAttributesAnnotation.isPresent()){

                UserAttributes userAttributes = (UserAttributes) userAttributesAnnotation.get();

                return new RequestedUserAttributes(userAttributes.company(), userAttributes.type());
            }else {

                return new RequestedUserAttributes(null, User.UserType.BASIC);
            }
        }
    }
}
