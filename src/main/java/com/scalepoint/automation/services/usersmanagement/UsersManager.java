package com.scalepoint.automation.services.usersmanagement;

import com.scalepoint.automation.utils.data.entity.credentials.ExistingUsers;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class UsersManager {

    private static final Lock lock = new ReentrantLock();

    private static Logger logger = LoggerFactory.getLogger(UsersManager.class);

    private static BlockingQueue<User> basicUsersQueue = new LinkedBlockingQueue<>();
    private static ConcurrentMap<CompanyCode, BlockingQueue<User>> exceptionalUsersQueues = new ConcurrentHashMap<>();
    private static User systemUser;

    public static void initManager(ExistingUsers existingUsers) {
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
    }

    public static User takeUser(CompanyCode companyCode) {
        lockQueue();
        logger.info("Requested: {}", companyCode.name());
        try {
            User taken = exceptionalUsersQueues.getOrDefault(companyCode, basicUsersQueue).take();
            logger.info("Taken: {}", taken.getLogin());
            return taken;
        } catch (Exception e) {
            logger.error("Can't driver user for {} cause {}", companyCode.name(), e.toString());
            throw new RuntimeException(e);
        } finally {
            unlockQueue();
        }
    }

    public static void releaseUser(User user) {
        logger.info("Returned: {}", user.getLogin());
        if (user.isBasic()) {
            basicUsersQueue.add(user);
            return;
        }
        exceptionalUsersQueues.get(CompanyCode.valueOf(user.getCompanyCode())).add(user);
    }

    public static void lockQueue() {
        lock.lock();
    }

    public static void unlockQueue() {
        lock.unlock();
    }

    public static User getSystemUser() {
        return systemUser;
    }
}
