package com.scalepoint.automation.utils.threadlocal;

import com.scalepoint.automation.services.usersmanagement.UsersManager;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class CurrentUser {

    private static Logger logger = LoggerFactory.getLogger(CurrentUser.class);

    private static ThreadLocal<List<User>> usersHolder = new ThreadLocal<>();
    private static ThreadLocal<String> claimIdHolder = new ThreadLocal<>();

    public static void setUser(User user) {
        List<User> users = usersHolder.get();
        if (users == null) {
            users = new ArrayList<>();
        }
        users.add(user);
    }

    private static boolean hasUsers() {
        List<User> users = usersHolder.get();
        return users != null && !users.isEmpty();
    }

    public static void setClaimId(String claimId) {
        claimIdHolder.set(claimId);
    }

    public static String getClaimId() {
        return claimIdHolder.get();
    }

    public static void cleanUp() {
        logger.info("Clean up CurrentUser");
        if (hasUsers()) {
            usersHolder.get().forEach(UsersManager::releaseUser);
        }

        usersHolder.remove();

        if (claimIdHolder.get() != null) {
            claimIdHolder.remove();
        }
    }
}
