package com.scalepoint.automation.pageobjects.pages;

import com.scalepoint.automation.Actions;
import com.scalepoint.automation.utils.Configuration;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.Window;
import com.scalepoint.automation.utils.annotations.EccAdminPage;
import com.scalepoint.automation.utils.annotations.EccPage;
import com.scalepoint.automation.utils.driver.Browser;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import ru.yandex.qatools.htmlelements.loader.HtmlElementLoader;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public abstract class Page extends Actions {

    private static Properties textProperties = new Properties();

    private static Map<Class, String> pageAnnotationToBaseUrl = new HashMap<>();

    static {
        pageAnnotationToBaseUrl.put(EccPage.class, Configuration.getEccUrl());
        pageAnnotationToBaseUrl.put(EccAdminPage.class, Configuration.getEccAdminUrl());

        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        String resourceFile = "data/" + Configuration.getLocale().toLowerCase() + "/textResources.properties";
        InputStream stream = loader.getResourceAsStream(resourceFile);
        try {
            textProperties.load(stream);
        } catch (IOException e) {
            throw new IllegalStateException("Can't load: " + resourceFile);
        } finally {
            IOUtils.closeQuietly(stream);
        }
    }

    protected Logger logger = LogManager.getLogger(Page.class);

    protected WebDriver driver;

    public Page() {
        this.driver = Browser.driver();
        HtmlElementLoader.populatePageObject(this, this.driver);
    }

    protected abstract Page ensureWeAreOnPage();

    protected abstract String geRelativeUrl();

    protected void waitForUrl(String expectedUrl) {
        int totalTimeoutInSeconds = 10;
        int pollingMs = 1000;

        logger.info(" Expected: {}", expectedUrl);
        Wait.For(webDriver -> {
            String currentUrl = driver.getCurrentUrl();
            logger.info("Current url: {}", currentUrl);
            logger.info("Windows count: {}", webDriver.getWindowHandles().size());
            Window.get().switchToLast();
            return currentUrl.contains(expectedUrl);
        }, totalTimeoutInSeconds, pollingMs);
    }

    public static <T extends Page> T to(Class<T> pageClass) {
        return to(pageClass, "");
    }

    public static <T extends Page> String getUrl(Class<T> pageClass) {
        try {
            String relativeUrl = PagesCache.get(pageClass).geRelativeUrl();
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
        } catch (Exception e) {
            return "";
        }
    }

    public static <T extends Page> T to(Class<T> pageClass, String parameters) {
        String urlValue = getUrl(pageClass) + parameters;
        Browser.open(urlValue);
        return at(pageClass);
    }

    public static <T extends Page> T at(Class<T> pageClass) {
        T page = PagesCache.get(pageClass);
        page.ensureWeAreOnPage();
        return page;
    }

    public static String getText(String key) {
        return textProperties.getProperty(key);
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
}
