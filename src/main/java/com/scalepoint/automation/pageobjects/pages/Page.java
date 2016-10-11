package com.scalepoint.automation.pageobjects.pages;

import com.scalepoint.automation.Actions;
import com.scalepoint.automation.utils.Configuration;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.Window;
import com.scalepoint.automation.utils.annotations.page.EccAdminPage;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import com.scalepoint.automation.utils.driver.Browser;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import ru.yandex.qatools.htmlelements.loader.HtmlElementLoader;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;


import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

public abstract class Page implements Actions {

    private static Map<Class, String> pageAnnotationToBaseUrl = new HashMap<>();

    static {
        pageAnnotationToBaseUrl.put(EccPage.class, Configuration.getEccUrl());
        pageAnnotationToBaseUrl.put(EccAdminPage.class, Configuration.getEccAdminUrl());
    }

    protected Logger logger = LogManager.getLogger(Page.class);
    protected WebDriver driver;

    public Page() {
        this.driver = Browser.driver();
        HtmlElementLoader.populatePageObject(this, this.driver);
    }

    protected abstract Page ensureWeAreOnPage();

    protected abstract String getRelativeUrl();

    protected void waitForUrl(String expectedUrl) {
        waitForUrl(expectedUrl, null);
    }

    protected void waitForUrl(String expectedUrl, String alternativeUrl) {
        if (StringUtils.isBlank(expectedUrl)) {
            return;
        }

        int totalTimeoutInSeconds = 10;
        int pollingMs = 1000;

        logger.info(" Expected: {}", expectedUrl);
        Wait.For(webDriver -> {
            try {
                String currentUrl = driver.getCurrentUrl();
                logger.info("Current url: {}", currentUrl);

                assert webDriver != null;
                logger.info("Windows count: {}", webDriver.getWindowHandles().size());
                switchToLast();

                return currentUrl.contains(expectedUrl) || (alternativeUrl != null && currentUrl.contains(alternativeUrl));
            } catch (UnhandledAlertException e) {
                closeAlert();
            }
            return false;
        }, totalTimeoutInSeconds, pollingMs);
    }

    public static <T extends Page> T to(Class<T> pageClass) {
        return to(pageClass, "");
    }

    public static <T extends Page> String getUrl(Class<T> pageClass) {
        String relativeUrl = PagesCache.get(pageClass).getRelativeUrl();
        String baseUrl = null;
        if (relativeUrl != null) {
            for (Map.Entry<Class, String> entry : pageAnnotationToBaseUrl.entrySet()) {
                Annotation annotation = pageClass.getAnnotation(entry.getKey());
                if (annotation != null) {
                    baseUrl = entry.getValue();
                    break;
                }
            }
        }
        if (baseUrl == null) {
            throw new IllegalArgumentException("No page type annotation found for " + pageClass);
        }
        return baseUrl + relativeUrl;
    }

    public static <T extends Page> T to(Class<T> pageClass, String parameters) {
        String urlValue = getUrl(pageClass) + parameters;
        Browser.open(urlValue);
        return at(pageClass);
    }

    public static <T extends Page> T toWithNoAt(Class<T> pageClass) {
        String urlValue = getUrl(pageClass);
        Browser.open(urlValue);
        try {
            return pageClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T extends Page> T at(Class<T> pageClass) {
        T page = PagesCache.get(pageClass);
        page.ensureWeAreOnPage();
        return page;
    }

    @SuppressWarnings("unchecked")
    public static class PagesCache {

        private static ThreadLocal<Map<Class<? extends Page>, Page>> holder = new ThreadLocal<>();

        public static <T extends Page> T get(Class<T> pageClass) {
            Map<Class<? extends Page>, Page> classPageMap = holder.get();
            if (classPageMap == null) {
                classPageMap = new HashMap<>();
                holder.set(classPageMap);
            }
            return (T) classPageMap.computeIfAbsent(pageClass, aClass -> {
                try {
                    return aClass.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    throw new RuntimeException("Can't instantiate page cause: " + e.getMessage(), e);
                }
            });
        }

        public static void cleanUp() {
            holder.remove();
        }
    }

    protected String errorMessage(String text, Object... params) {
        return String.format(text, params);
    }
}
