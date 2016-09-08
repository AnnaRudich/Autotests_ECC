package com.scalepoint.automation.utils.driver;

import java.util.Arrays;

public enum DriverType {
    IE("ie.local"),
    IE_REMOTE("ie.remote"),
    FF("ff.remote"),
    CHROME("chrome.local");

    private String profile;

    DriverType(String profile) {
        this.profile = profile;
    }

    public static DriverType findByProfile(String[] activeProfiles) {
        for (DriverType driverType : DriverType.values()) {
            for (String activeProfile : activeProfiles) {
                if (driverType.profile.equalsIgnoreCase(activeProfile)) {
                    return driverType;
                }
            }
        }
        throw new IllegalArgumentException("Profiles not found in application: "+ Arrays.toString(activeProfiles));
    }
}
