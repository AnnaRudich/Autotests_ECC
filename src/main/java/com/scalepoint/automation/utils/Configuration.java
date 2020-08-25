package com.scalepoint.automation.utils;

import com.scalepoint.automation.shared.Locale;
import org.springframework.util.Assert;

public class Configuration {

    public static final String KEY_PROTOCOL = "app.protocol";
    public static final String KEY_LOCALE = "app.locale";
    public static final String KEY_CONTEXT_ECC = "context.ecc";
    public static final String KEY_CONTEXT_ECC_ADMIN = "context.ecc.admin";
    public static final String KEY_CONTEXT_ECC_RNV = "context.ecc.rnv";
    public static final String KEY_CONTEXT_ECC_SELFSERVICE = "context.selfservice";
    public static final String KEY_SERVER_URL = "url.base.server";
    public static final String KEY_ENVIRONMENT_URL = "url.environment";
    public static final String KEY_EVENT_API_URL = "url.eventapi";
    public static final String KEY_ECC_SOLR_URL = "url.solr.base";
    public static final String KEY_ECC_DB_URL = "url";
    public static final String KEY_EVENT_API_DB_URL = "eventApiDbUrl";
    public static final String KEY_MONGO_DB_CONNECTION_STRING = "mongoDbConnectionString";
    public static final String MONGO_VOUCHER_PREDICTION_COLLECTION_NAME = "mongoVoucherPredictionCollectionName";

    public static final String KEY_HUB_REMOTE = "hub.remote";
    public static final String KEY_HUB_REMOTE_ZALENIUM = "hub.remote.zalenium";
    public static final String KEY_HUB_LOCAL_ZALENIUM = "hub.local.zalenium";

    private static final String ff4jFeaturesApiUrl = "ff4j-console/api/features/";
    private static final String ff4jToggleAdminUrl = "ff4j-console/features";

    private static final String tasksFeedbackUrl = "ws/task_feedback.action";
    private static final String pullTaskDataUrl = "ws/tasks.xml";
    private static final String repairValuationUrl = "repairValuation";
    private static final String createOrderBasePath = "resteasy/uCommerce/CreateOrder";
    private static final String createGetBalanceBasePath = "resteasy/uCommerce/GetBalance";

    private static final String SLASH = "/";

    private static String protocol;
    private static Locale locale;
    private static String serverUrl;
    private static String environmentUrl;
    private static String eventApiUrl;
    private static String eccContext;
    private static String eccAdminContext;
    private static String eccRnvContext;
    private static String selfServiceContext;
    private static String solrBaseUrl;
    private static String hubRemote;
    private static String hubRemoteZalenium;
    private static String hubLocalZalenium;

    private static Configuration instance;

    private Configuration() {
    }

    public synchronized static Configuration getInstance() {
        if (instance == null) {
            instance = new Configuration();
        }
        return instance;
    }

    public static String getHttpServerUrl(String serverUrl) {
        return protocol + serverUrl;
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

    public static String getEnvironmentUrl() {
        return environmentUrl;
    }

    public static String getEventApiUrl() {
        return eventApiUrl;
    }

    public static String getEccContext() {
        return eccContext;
    }

    public static String getEccAdminContext() {
        return eccAdminContext;
    }

    public static String getEccRnvContext() {
        return eccRnvContext;
    }

    public static String getSelfServiceContext() {
        return selfServiceContext;
    }

    public static String getEccUrl() {
        return getServerUrl() + SLASH + getEccContext() + SLASH + getLocale().getValue() + SLASH;
    }

    public static String getEccAdminUrl() {
        return getServerUrl() + SLASH + getEccAdminContext() + SLASH + getLocale().getValue() + SLASH;
    }

    public static String getEccRnvUrl() {
        return getServerUrl() + SLASH + getEccRnvContext() + SLASH + getLocale().getValue() + SLASH;
    }

    public static String getSelfServiceUrl() {
        return getServerUrl() + SLASH + getSelfServiceContext() + SLASH + getLocale().getValue() + SLASH;
    }

    public static String getLogoutUrl(){
        return getEccAdminUrl() + "logout";
    }

    public static String getFeatureToggleAdminUrl() {
        return getEccUrl() + getFf4jToggleAdminUrl();
    }

    public static String getFeaturesApiUrl() {
        return getEccUrl() + getFf4jFeaturesApiUrl();
    }


    public static String getPullTaskDataUrl() {
        return pullTaskDataUrl;
    }

    public static String getRnvWebServiceUrl() {
        return getServerUrl() + SLASH + getRepairValuationUrl() + SLASH + getLocale().getValue() + SLASH;
    }

    public static String getRnvTaskFeedbackUrl() {
        return getRnvWebServiceUrl() + getTasksFeedbackUrl();
    }

    public static String getRnvPullTaskDataUrl() {
        return getRnvWebServiceUrl() + getPullTaskDataUrl();
    }

    public static boolean isDK() {
        return locale.equals(Locale.DK);
    }

    public static String getSolrBaseUrl() {
        return solrBaseUrl;
    }

    public Configuration setProtocol(String protocol) {
        Assert.notNull(protocol, errorMessage(KEY_PROTOCOL));
        Configuration.protocol = protocol;
        return this;
    }

    public Configuration setLocale(String locale) {
        Assert.notNull(locale, errorMessage(KEY_LOCALE));
        Configuration.locale = Locale.get(locale);
        return this;
    }

    public Configuration setServerUrl(String serverUrl) {
        Assert.notNull(serverUrl, errorMessage(KEY_SERVER_URL));
        Configuration.serverUrl = getHttpServerUrl(serverUrl);
        return this;
    }

    public Configuration setEnvironmentUrl(String environmentUrl) {
        Assert.notNull(environmentUrl, errorMessage(KEY_ENVIRONMENT_URL));
        Configuration.environmentUrl = getHttpServerUrl(environmentUrl);
        return this;
    }

    public Configuration setEventApiUrl(String eventApiUrl) {
        Assert.notNull(eventApiUrl, errorMessage(KEY_EVENT_API_URL));
        Configuration.eventApiUrl = eventApiUrl;
        return this;
    }

    public Configuration setEccContext(String eccContext) {
        Assert.notNull(eccContext, errorMessage(KEY_CONTEXT_ECC));
        Configuration.eccContext = eccContext;
        return this;
    }

    public Configuration setEccAdminContext(String eccAdminContext) {
        Assert.notNull(eccAdminContext, errorMessage(KEY_CONTEXT_ECC_ADMIN));
        Configuration.eccAdminContext = eccAdminContext;
        return this;
    }

    public Configuration setEccRnvContext(String eccRnvContext) {
        Configuration.eccRnvContext = eccRnvContext;
        return this;
    }

    public Configuration setSelfServiceContext(String selfServiceContext) {
        Configuration.selfServiceContext = selfServiceContext;
        return this;
    }

    public Configuration setSolrBaseUrl(String solrBaseUrl) {
        Assert.notNull(solrBaseUrl, errorMessage(KEY_ECC_SOLR_URL));
        Configuration.solrBaseUrl = solrBaseUrl;
        return this;
    }

    public static String getHubRemote() {
        return hubRemote;
    }

    public Configuration setHubRemote(String hubRemote) {
        Configuration.hubRemote = hubRemote;
        return this;
    }

    public static String getHubRemoteZalenium() {
        return hubRemoteZalenium;
    }

    public Configuration setHubRemoteZalenium(String hubRemoteZalenium) {
        Configuration.hubRemoteZalenium = hubRemoteZalenium;
        return this;
    }

    public static String getHubLocalZalenium() {
        return hubLocalZalenium;
    }

    public Configuration setHubLocalZalenium(String hubLocalZalenium) {
        Configuration.hubLocalZalenium = hubLocalZalenium;
        return this;
    }

    private static String getFf4jFeaturesApiUrl() {
        return ff4jFeaturesApiUrl;
    }

    private static String getFf4jToggleAdminUrl() {
        return ff4jToggleAdminUrl;
    }

    public static String getTasksFeedbackUrl() {
        return tasksFeedbackUrl;
    }

    public static String getRepairValuationUrl() {
        return repairValuationUrl;
    }

    private static String getCreateOrderBasePath() {
        return createOrderBasePath;
    }

    public static String getCreateOrderWebServiceUrl(){
        return getEccUrl()+getCreateOrderBasePath();
    }

    private static String getGetBalanceBasePath() {
        return createGetBalanceBasePath;
    }

    public static String getGetBalanceWebServiceUrl(){
        return getEccUrl()+getGetBalanceBasePath();
    }


}

