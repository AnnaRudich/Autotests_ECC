package com.scalepoint.automation.services.usersmanagement;

import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.data.entity.credentials.ExistingUsers;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

public class UsersManager {

    private static Logger logger = LoggerFactory.getLogger(UsersManager.class);

    private static BlockingQueue<User> basicUsersQueue = new LinkedBlockingQueue<>();
    private static ConcurrentMap<CompanyCode, BlockingQueue<User>> exceptionalUsersQueues = new ConcurrentHashMap<>();
    private static User systemUser;

    private static Map<String, User> usersInfo = new HashMap<>();

    public static void initManager(ExistingUsers existingUsers) {
        logger.info("Initializing UsersManager");
        existingUsers.getUsers().forEach(user -> {
            if (user.isSystem()) {
                systemUser = user;
                return;
            }
            if (user.isBasic()) {
                basicUsersQueue.add(user);
            } else {
                exceptionalUsersQueues.put(CompanyCode.valueOf(user.getCompanyCode()),
                        new ArrayBlockingQueue<>(1, true, Collections.singleton(user)));
            }
            usersInfo.put(user.getCompanyCode(), user);
        });
//        printQueues();
    }

    public static Map<CompanyMethodArgument, User> fetchUsersWhenAvailable(Map<CompanyMethodArgument, User> companyMethodArguments) {
        boolean fetched = false;
        logger.info("Before while");
        while (!fetched) {
            logger.info("Before fetch");
            fetched = fetchUsersIfAvailable(companyMethodArguments);
            logger.info("Users fetched = {}", fetched);
            logger.info("Before timer");
            if (!fetched) {
                Wait.wait(15);
            }
        }
        return companyMethodArguments;
    }

    private synchronized static boolean fetchUsersIfAvailable(Map<CompanyMethodArgument, User> companyMethodArguments) {
        logger.info("Requested: {}", companyMethodArguments.size());

        logger.info("Before count basic users");
        int requestedBasicUsersCount = (int)companyMethodArguments.keySet().stream().filter(companyCode -> usersInfo.get(companyCode.companyCode.name()).isBasic()).count();
        boolean basicUsersAvailable = basicUsersQueue.size() >= requestedBasicUsersCount;

        int requestedExceptionalUsersCount = (int)companyMethodArguments.keySet().stream().filter(companyCode -> !usersInfo.get(companyCode.companyCode.name()).isBasic()).count();
        logger.info("Before count exceptional users");
        long count = companyMethodArguments.keySet()
                .stream()
                .filter(companyMethodArgument -> {
                    CompanyCode companyCode = companyMethodArgument.companyCode;
                    boolean notBasicUser = !usersInfo.get(companyCode.name()).isBasic();
                    if (notBasicUser && exceptionalUsersQueues.containsKey(companyCode)) {
                        return !exceptionalUsersQueues.get(companyCode).isEmpty();
                    }
                    return notBasicUser;
                })
                .count();
        logger.info("Found exceptional users: "+count);
        boolean exceptionalUsersAvailable = count==requestedExceptionalUsersCount;

        logger.info("Basic users available: {} Exceptional Users Available: {}", basicUsersAvailable, exceptionalUsersAvailable);

        if (basicUsersAvailable && exceptionalUsersAvailable) {
            logger.info("Before take users");
            companyMethodArguments.replaceAll((companyMethodArgument, user) -> takeUser(companyMethodArgument.companyCode));
            return true;
        } else {
            return false;
        }
    }

    public static class CompanyMethodArgument {

        private int index;
        private CompanyCode companyCode;
        private User user;

        private CompanyMethodArgument(int index, CompanyCode companyCode) {
            this.index = index;
            this.companyCode = companyCode;
        }

        public static CompanyMethodArgument create(int index, CompanyCode companyCode) {
            return new CompanyMethodArgument(index, companyCode);
        }

        public CompanyMethodArgument setUser(User user) {
            this.user = user;
            return this;
        }

        public User getUser() {
            return user;
        }

        public int getIndex() {
            return index;
        }

        public CompanyMethodArgument setIndex(int index) {
            this.index = index;
            return this;
        }

        public CompanyCode getCompanyCode() {
            return companyCode;
        }

        public CompanyMethodArgument setCompanyCode(CompanyCode companyCode) {
            this.companyCode = companyCode;
            return this;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            CompanyMethodArgument that = (CompanyMethodArgument) o;

            if (index != that.index) return false;
            return companyCode == that.companyCode;
        }

        @Override
        public int hashCode() {
            int result = index;
            result = 31 * result + (companyCode != null ? companyCode.hashCode() : 0);
            return result;
        }
    }


    private static User takeUser(CompanyCode companyCode) {
        logger.info("Requested: {}", companyCode.name());
        try {
            User taken = exceptionalUsersQueues.getOrDefault(companyCode, basicUsersQueue).take();
            logger.info("Taken: {}", taken.getLogin());
            return taken;
        } catch (Exception e) {
            logger.error("Can't take user for {} cause {}", companyCode.name(), e.toString());
            throw new RuntimeException(e);
        }
    }

    public static void releaseUser(User user) {
        logger.info("Returned: {}", user.getLogin());
        if (user.isBasic()) {
            basicUsersQueue.add(user);
        } else {
            exceptionalUsersQueues.get(CompanyCode.valueOf(user.getCompanyCode())).add(user);
        }
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
}
