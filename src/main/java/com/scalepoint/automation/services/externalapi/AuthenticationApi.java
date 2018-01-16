package com.scalepoint.automation.services.externalapi;

import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.services.externalapi.exception.ServerApiException;
import com.scalepoint.automation.utils.Configuration;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.threadlocal.Browser;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.fluent.Executor;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;

import javax.net.ssl.SSLContext;
import java.util.List;

import static com.scalepoint.automation.utils.Http.*;

public class AuthenticationApi {

    protected Logger log = LogManager.getLogger(getClass());

    public AuthenticationApi(User user) {
        login(user, null);
    }

    public AuthenticationApi(Executor executor) {
        this.executor = executor;
    }

    public AuthenticationApi(User user, Executor executor) {
        this.executor = executor;
        login(user, null);
    }

    private AuthenticationApi() {
    }

    public static AuthenticationApi createServerApi() {
        return new AuthenticationApi();
    }

    protected Executor executor;

    public <T extends Page> T login(User user, Class<T> returnPageClass) {
        return login(user, returnPageClass, null);
    }

    public <T extends Page> T login(User user, Class<T> returnPageClass, String parameters) {
        String loginUrl = Configuration.getEccAdminUrl();

        List<NameValuePair> params = ParamsBuilder.create().
                add("j_username", user.getLogin()).
                add("j_password", user.getPassword()).
                add("successurl", "").get();

        try {
            SSLContext sslContext = SSLContext.getInstance("TLSv1");
            sslContext.init(null, null, null);
            CookieStore cookieStore = new BasicCookieStore();

            HttpClient httpClient = HttpClientBuilder.create().
                    setSSLContext(sslContext).
                    setRedirectStrategy(new DefaultRedirectStrategy()).
                    setDefaultCookieStore(cookieStore).
                    build();

            executor = Executor.newInstance(httpClient);

            HttpResponse loginResponse = post(loginUrl + "loginProcess", params, executor).returnResponse();

            String successUrl = loginResponse.getHeaders("Location")[0].getValue();
            log.info("SuccessUrl: " + successUrl);
            if (!successUrl.contains("?successurl=")) {
                throw new IllegalStateException("Bad URL: " + loginUrl);
            }

            HttpResponse httpResponse = get(successUrl, executor).returnResponse();
            ensure200Code(httpResponse.getStatusLine().getStatusCode());

            copyCookiesToBrowser(cookieStore);

        } catch (Exception e) {
            log.error("Can't login", e);
            throw new ServerApiException(e);
        }
        log.info("Fast login was performed as " + user.getLogin());

        if (returnPageClass != null) {
            return Page.to(returnPageClass, parameters);
        }

        return null;
    }

    private void copyCookiesToBrowser(CookieStore cookieStore) {
        for (Cookie cookie : cookieStore.getCookies()) {
            try {
                if (!cookie.getPath().contains(Configuration.getEccContext())) {
                    continue;
                }
                org.openqa.selenium.Cookie superCookie = new org.openqa.selenium.Cookie(
                        cookie.getName(),
                        cookie.getValue(),
                        //ie will not set cookies if shared name is incorrect (localhost, nb-ian)
                        cookie.getDomain().contains(".") ? cookie.getDomain() : null,
                        cookie.getPath(),
                        cookie.getExpiryDate()
                );
                Browser.driver().manage().addCookie(superCookie);
                ((JavascriptExecutor) Browser.driver()).executeScript("document.cookie=" + "\"" + superCookie.toString() + "\"");
            } catch (Exception e) {
                log.info(e.getMessage());
            }
        }
    }

    public Executor getExecutor() {
        return executor;
    }
}
