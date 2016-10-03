package com.scalepoint.automation.utils;

import com.scalepoint.automation.domain.Locale;
import org.springframework.util.Assert;

public class Configuration {

    public static final String KEY_LOCALE = "locale";
    public static final String KEY_ECC_CONTEXT = "eccContext";
    public static final String KEY_SERVER_URL = "serverUrl";
    public static final String KEY_ECC_ADMIN_CONTEXT = "eccAdminContext";

    private static final String SLASH = "/";
    private static final String HTTP = "http://";

    private static Locale locale;
    private static String serverUrl;
    private static String eccContext;
    private static String eccAdminContext;
    private static String eccBaseUrl;
    private static String eccAdminBaseUrl;

    public static void init(String locale, String serverUrl, String eccContext, String eccAdminContext) {
        Assert.notNull(locale, errorMessage(KEY_LOCALE));
        Assert.notNull(serverUrl, errorMessage(KEY_SERVER_URL));
        Assert.notNull(eccContext, errorMessage(KEY_ECC_CONTEXT));
        Assert.notNull(eccAdminContext, errorMessage(KEY_ECC_ADMIN_CONTEXT));

        String httpServerUrl = HTTP +serverUrl;

        Configuration.locale = Locale.get(locale);
        Configuration.serverUrl =  httpServerUrl;
        Configuration.eccContext = eccContext;
        Configuration.eccAdminContext = eccAdminContext;

        Configuration.eccBaseUrl = httpServerUrl + SLASH + eccContext + SLASH + locale + SLASH;
        Configuration.eccAdminBaseUrl = httpServerUrl + SLASH + eccAdminContext + SLASH + locale + SLASH;
    }

    private static String errorMessage(String parameter) {
        return parameter + "is mandatory field. Must be set in application.properties or passed as system variable";
    }

    public static Locale getLocale() {
        return locale;
    }

    public static String getServerUrl() {
        return serverUrl;
    }

    public static String getEccContext() {
        return eccContext;
    }

    public static String getEccAdminContext() {
        return eccAdminContext;
    }

    public static String getEccUrl() {
        return eccBaseUrl;
    }

    public static String getEccAdminUrl() {
        return eccAdminBaseUrl;
    }

    public static boolean isDK() {
        return locale.equals(Locale.DK);
    }
}

