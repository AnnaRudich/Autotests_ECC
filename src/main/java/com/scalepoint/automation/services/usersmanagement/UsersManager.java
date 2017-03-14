package com.scalepoint.automation.services.usersmanagement;

import com.scalepoint.automation.utils.data.entity.credentials.ExistingUsers;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class UsersManager {

    private static Logger logger = LoggerFactory.getLogger(UsersManager.class);

    private static BlockingQueue<User> basicUsersQueue = new LinkedBlockingQueue<>();
    private static ConcurrentMap<CompanyCode, BlockingQueue<User>> exceptionalUsersQueues = new ConcurrentHashMap<>();
    private static User systemUser;

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
        });
        printQueues();
    }

    public synchronized static User takeUser(CompanyCode companyCode) {
        logger.info("Requested: {}", companyCode.name());
        try {
            User taken = exceptionalUsersQueues.getOrDefault(companyCode, basicUsersQueue).take();
            logger.info("Taken: {}", taken.getLogin());
            printQueues();
            return taken;
        } catch (Exception e) {
            logger.error("Can't take user for {} cause {}", companyCode.name(), e.toString());
            throw new RuntimeException(e);
        }
    }

    public synchronized static void releaseUser(User user) {
        logger.info("Returned: {}", user.getLogin());
        if (user.isBasic()) {
            basicUsersQueue.add(user);
        } else {
            exceptionalUsersQueues.get(CompanyCode.valueOf(user.getCompanyCode())).add(user);
        }
        printQueues();
    }

    private synchronized static void printQueues() {
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
