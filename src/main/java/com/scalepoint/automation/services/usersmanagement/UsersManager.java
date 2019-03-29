package com.scalepoint.automation.services.usersmanagement;

import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.data.entity.credentials.ExistingUsers;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class UsersManager {

    private static Logger logger = LogManager.getLogger(UsersManager.class);

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
        while (!fetched) {
            fetched = fetchUsersIfAvailable(companyMethodArguments);
            if (!fetched) {
                Wait.wait(15);
            }
        }
        return companyMethodArguments;
    }

    private static synchronized boolean fetchUsersIfAvailable(Map<CompanyMethodArgument, User> companyMethodArguments) {
        String users = companyMethodArguments.entrySet().stream().map(e -> (e.getKey().companyCode + ":" + (e.getValue() != null ? e.getValue().getLogin() : "?"))).collect(Collectors.joining(", "));
        logger.info("Requested: {}", users);

        int requestedBasicUsersCount = (int) companyMethodArguments.keySet().stream().filter(companyCode -> usersInfo.get(companyCode.companyCode.name()).isBasic()).count();
        boolean basicUsersAvailable = basicUsersQueue.size() >= requestedBasicUsersCount;

        int requestedExceptionalUsersCount = (int) companyMethodArguments.keySet().stream().filter(companyCode -> !usersInfo.get(companyCode.companyCode.name()).isBasic()).count();
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
        logger.info("Found exceptional users: {}", count);
        boolean exceptionalUsersAvailable = count == requestedExceptionalUsersCount;

        logger.info("Basic users available: {} Exceptional Users Available: {}", basicUsersAvailable, exceptionalUsersAvailable);

        if (basicUsersAvailable && exceptionalUsersAvailable) {
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

        @Override
        public String toString() {
            return "CompanyMethodArgument{" +
                    "index=" + index +
                    ", companyCode=" + companyCode +
                    '}';
        }
    }


    private static User takeUser(CompanyCode companyCode) {
        try {
            User taken = exceptionalUsersQueues.getOrDefault(companyCode, basicUsersQueue).take();
            logger.info("Requested: {} Taken: {}", companyCode.name(), taken.getLogin());
            return taken;
        } catch (Exception e) {
            logger.error("Can't take user for {} cause {}", companyCode.name(), e.toString());
            throw new IllegalStateException(e);
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
