package com.scalepoint.automation.utils;

import com.scalepoint.automation.services.usersmanagement.UsersManager;
import com.scalepoint.automation.utils.data.entity.credentials.User;

public class CurrentUser {

    private static ThreadLocal<User> userHolder = new ThreadLocal<>();
    private static ThreadLocal<String> claimIdHolder = new ThreadLocal<>();

    public static void setUser(User user) {
        userHolder.set(user);
    }

    public static User getUser() {
        return userHolder.get();
    }

    public static void setClaimId(String claimId) {
        claimIdHolder.set(claimId);
    }

    public static String getClaimId() {
        return claimIdHolder.get();
    }

    public static void cleanUp() {
        if (getUser() != null) {
            UsersManager.releaseUser(getUser());
        }
        userHolder.remove();

        if (claimIdHolder.get() != null) {
            claimIdHolder.remove();
        }
    }

}
