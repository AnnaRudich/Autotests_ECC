package com.scalepoint.automation.utils;

import com.scalepoint.automation.domain.Locale;
import org.springframework.util.Assert;

public class Configuration {

    public static final String KEY_LOCALE = "app.locale";
    public static final String KEY_CONTEXT_ECC = "context.ecc";
    public static final String KEY_CONTEXT_ECC_ADMIN = "context.ecc.admin";
    public static final String KEY_SERVER_URL = "url.base.server";
    public static final String KEY_ECC_SOLR_PRODUCTS_URL = "url.solr.products";
    public static final String KEY_ECC_DB_URL = "url";
    public static final String KEY_ECC_DB_USERNAME = "username";
    public static final String KEY_ECC_DB_PASSWORD = "password";

    private static final String SLASH = "/";
    private static final String HTTP = "http://";

    private static Locale locale;
    private static String serverUrl;
    private static String eccContext;
    private static String eccAdminContext;
    private static String eccBaseUrl;
    private static String eccAdminBaseUrl;
    private static String solrProductsUrl;

    public static void init(String locale, String serverUrl, String eccContext, String eccAdminContext, String solrProductsUrl) {
        Assert.notNull(locale, errorMessage(KEY_LOCALE));
        Assert.notNull(serverUrl, errorMessage(KEY_SERVER_URL));
        Assert.notNull(eccContext, errorMessage(KEY_CONTEXT_ECC));
        Assert.notNull(eccAdminContext, errorMessage(KEY_CONTEXT_ECC_ADMIN));
        Assert.notNull(solrProductsUrl, errorMessage(KEY_ECC_SOLR_PRODUCTS_URL));

        String httpServerUrl = HTTP +serverUrl;

        Configuration.locale = Locale.get(locale);
        Configuration.serverUrl =  httpServerUrl;
        Configuration.eccContext = eccContext;
        Configuration.eccAdminContext = eccAdminContext;
        Configuration.solrProductsUrl = solrProductsUrl;

        Configuration.eccBaseUrl = httpServerUrl + SLASH + eccContext + SLASH + locale + SLASH;
        Configuration.eccAdminBaseUrl = httpServerUrl + SLASH + eccAdminContext + SLASH + locale + SLASH;
    }

    private static String errorMessage(String parameter) {
        return parameter + " is mandatory field. Must be set in application.properties or passed as system variable";
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

    public static String getSolrProductsUrl() {
        return solrProductsUrl;
    }
}

