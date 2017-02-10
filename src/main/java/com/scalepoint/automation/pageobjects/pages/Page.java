package com.scalepoint.automation.pageobjects.pages;

import com.scalepoint.automation.Actions;
import com.scalepoint.automation.pageobjects.RequiresJavascriptHelpers;
import com.scalepoint.automation.utils.Configuration;
import com.scalepoint.automation.utils.CurrentUser;
import com.scalepoint.automation.utils.JavascriptHelper;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.annotations.page.ClaimSpecificPage;
import com.scalepoint.automation.utils.annotations.page.EccAdminPage;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import com.scalepoint.automation.utils.annotations.page.RequiredParameters;
import com.scalepoint.automation.utils.driver.Browser;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import ru.yandex.qatools.htmlelements.loader.HtmlElementLoader;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
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

        int totalTimeoutInSeconds = 20;
        int pollingMs = 1000;

        logger.info(" Expected: {}", expectedUrl);
        Wait.forCondition(webDriver -> {
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

    public static <T extends Page> String getUrl(Class<T> pageClass, Object... params) {
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

        boolean claimIdPresent = StringUtils.isNotBlank(CurrentUser.getClaimId());
        boolean pageIsClaimSpecific = pageClass.isAnnotationPresent(ClaimSpecificPage.class);

        String claimId = claimIdPresent && pageIsClaimSpecific ? CurrentUser.getClaimId() + "/" : "";
        String fullBasePath = baseUrl + claimId + relativeUrl;

        return buildFullUrl(pageClass, fullBasePath, params);
    }

    public static <T extends Page> T to(Class<T> pageClass, Object... parameters) {
        String initialUrl = buildFullUrl(pageClass, getUrl(pageClass), parameters);

        LogManager.getLogger(Page.class).info("Open page: " + initialUrl);
        Browser.open(initialUrl);
        Wait.waitForPageLoaded();

        return at(pageClass);
    }

    private static <T extends Page> String buildFullUrl(Class<T> pageClass, String initialUrl, Object[] parameters) {
        String queryString = "";
        RequiredParameters requiredParametersAnnotation = pageClass.getAnnotation(RequiredParameters.class);
        if (requiredParametersAnnotation != null && parameters.length > 0) {
            queryString = String.format(requiredParametersAnnotation.value(), parameters);
        }

        if (StringUtils.isNotBlank(queryString)) {
            initialUrl = initialUrl + (initialUrl.contains("?") ? "&" : "?") + queryString;
        }
        return initialUrl;
    }

    public static <T extends Page> T at(Class<T> pageClass) {
        T page = PagesCache.get(pageClass);
        loadJavascriptHelpersIfNeeds(page);
        page.ensureWeAreOnPage();
        return page;
    }

    private static <T extends Page> void loadJavascriptHelpersIfNeeds(T page) {
        if (page instanceof RequiresJavascriptHelpers) {
            Object o = ((JavascriptExecutor) Browser.driver()).executeScript("try { return isUtilsInitialized(); } catch (e) { return false;}");
            if (!(o instanceof Boolean && Boolean.valueOf(o.toString()))) {
                JavascriptHelper.initializeCommonFunctions();
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static class PagesCache {

        private static ThreadLocal<Map<Class<? extends Page>, Page>> holder = new ThreadLocal<>();
        private static ThreadLocal<String> savePointPageUrl = new ThreadLocal<>();

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

        public static void setSavePointUrl(String url) {
            savePointPageUrl.set(url);
        }

        public static String getSavePointUrl() {
            String url = savePointPageUrl.get();
            savePointPageUrl.remove();
            return url;
        }
    }

    protected String errorMessage(String text, Object... params) {
        return String.format(text, params);
    }

    public <T extends Page> T savePoint(Class<T> currentPageClass) {
        PagesCache.setSavePointUrl(Browser.driver().getCurrentUrl());
        return at(currentPageClass);
    }

    public <T extends Page> T backToSavePoint(Class<T> pointPageClass) {
        Browser.driver().get(PagesCache.getSavePointUrl());
        return at(pointPageClass);
    }
}
