package com.scalepoint.automation.utils;

import com.scalepoint.automation.shared.Locale;
import org.springframework.util.Assert;

public class Configuration {

  public static final String KEY_LOCALE = "app.locale";
  public static final String KEY_CONTEXT_ECC = "context.ecc";
  public static final String KEY_CONTEXT_ECC_ADMIN = "context.ecc.admin";
  public static final String KEY_CONTEXT_ECC_RNV = "context.ecc.rnv";
  public static final String KEY_SERVER_URL = "url.base.server";
  public static final String KEY_ECC_SOLR_PRODUCTS_URL = "url.solr.products";
  public static final String KEY_ECC_DB_URL = "url";
  public static final String KEY_EVENT_API_DB_URL = "eventApiDbUrl";

  public static final String KEY_HUB_REMOTE = "hub.remote";
  public static final String KEY_HUB_REMOTE_ZALENIUM = "hub.remote.zalenium";
  public static final String KEY_HUB_LOCAL_ZALENIUM = "hub.local.zalenium";

  private static final String ff4jFeaturesApiUrl = "/ff4j-console/api/features/";
  private static final String ff4jToggleAdminUrl = "/ff4j-console/features";

  private static final String SLASH = "/";
  private static final String HTTP = "http://";

  private static Locale locale;
  private static String serverUrl;
  private static String eccContext;
  private static String eccAdminContext;
  private static String eccRnvContext;
  private static String eccBaseUrl;
  private static String eccAdminBaseUrl;
  private static String eccRnvBaseUrl;
  private static String solrProductsUrl;
  private static String hubRemote;
  private static String hubRemoteZalenium;
  private static String hubLocalZalenium;

  private static Configuration instance;

  private Configuration(){}

  public synchronized static Configuration getInstance() {
    if(instance == null){
      instance = new Configuration();
    }
    return instance;
  }

  public static String getHttpServerUrl(String serverUrl) {
    return HTTP + serverUrl;
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

  public static String getEccRnvContext() {
    return eccRnvContext;
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

  public static String getFeatureToggleAdminUrl(){
    return getEccAdminUrl()+getFf4jToggleAdminUrl();
  }

  public static boolean isDK() {
    return locale.equals(Locale.DK);
  }

  public static String getSolrProductsUrl() {
    return solrProductsUrl;
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

  public static String getEccBaseUrl() {
    return eccBaseUrl;
  }

  public Configuration setEccBaseUrl(String eccBaseUrl) {
    Configuration.eccBaseUrl = eccBaseUrl;
    return this;
  }

  public static String getEccAdminBaseUrl() {
    return eccAdminBaseUrl;
  }

  public Configuration setEccAdminBaseUrl(String eccAdminBaseUrl) {
    Configuration.eccAdminBaseUrl = eccAdminBaseUrl;
    return this;
  }

  public static String getEccRnvBaseUrl() {
    return eccRnvBaseUrl;
  }

  public Configuration setEccRnvBaseUrl(String eccRnvBaseUrl) {
    Configuration.eccRnvBaseUrl = eccRnvBaseUrl;
    return this;
  }

  public Configuration setSolrProductsUrl(String solrProductsUrl) {
    Assert.notNull(solrProductsUrl, errorMessage(KEY_ECC_SOLR_PRODUCTS_URL));
    Configuration.solrProductsUrl = solrProductsUrl;
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

  public static String getFf4jFeaturesApiUrl() {
    return ff4jFeaturesApiUrl;
  }

  public static String getFf4jToggleAdminUrl() {
    return ff4jToggleAdminUrl;
  }

}

