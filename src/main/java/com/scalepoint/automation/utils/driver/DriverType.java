package com.scalepoint.automation.utils.driver;

public enum DriverType {
    IE("ie.local"),
    IE_REMOTE("ie.remote"),
    FF_REMOTE("ff.remote"),
    CHROME("chrome.local"),
    CHROME_REMOTE("chrome.remote"),
    FF("ff.local"),
    EDGE("edge.local");

    private String profile;

    DriverType(String profile) {
        this.profile = profile;
    }

    public static DriverType findByProfile(String browserMode) {
        for (DriverType driverType : DriverType.values()) {
            if (driverType.profile.equalsIgnoreCase(browserMode)) {
                return driverType;
            }
        }
        throw new IllegalArgumentException("Profiles not found in application: " + browserMode);
    }
}
