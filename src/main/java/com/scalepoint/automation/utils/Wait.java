package com.scalepoint.automation.utils;

import com.google.common.base.Function;
import com.scalepoint.automation.pageobjects.extjs.ExtElement;
import com.scalepoint.automation.utils.driver.Browser;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;
import ru.yandex.qatools.htmlelements.element.TypifiedElement;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf;

public class Wait {
    public static final int STABLE_ELEMENT_TIMEOUT = 60;
    private static final int TIME_OUT_IN_SECONDS = 60;
    private static final int POLL_IN_MS = 1000;
    private static final int TIME_OUT_IN_MINUTES_REINDEXATION = 20;
    private static Logger log = LoggerFactory.getLogger(Wait.class);
    private static StopWatch watch = new StopWatch();

    public static void waitForAjaxComplete() {
        new WebDriverWait(Browser.driver(), 30).until((ExpectedCondition<Boolean>) wrapWait ->
                !(Boolean) ((JavascriptExecutor) wrapWait).executeScript("return Ext.Ajax.isLoading();"));
    }

    public static void waitForPageLoaded() {
        new WebDriverWait(Browser.driver(), 30).until((ExpectedCondition<Boolean>) wrapWait ->
                ((JavascriptExecutor) wrapWait).executeScript("return document.readyState").equals("complete"));
    }

    public static WebElement waitForElement(By elementLocator) {
        return wrap(presenceOfElementLocated(elementLocator));
    }

    public static WebElement waitForElementEnabling(By elementLocator) {
        return wrap(enablingOfElementLocated(elementLocator));
    }

    public static WebElement waitForElementDisplaying(By elementLocator) {
        return wrap(displayingOfElementLocated(elementLocator));
    }

    public static WebElement waitForElementFocusing(By elementLocator) {
        return wrap(focusingOfElementLocated(elementLocator));
    }

    public static void waitForReindexation(final By elementLocator) {
        FluentWait<WebDriver> wait = new FluentWait<>(Browser.driver())
                .withTimeout(TIME_OUT_IN_MINUTES_REINDEXATION, TimeUnit.MINUTES)
                .pollingEvery(10, TimeUnit.SECONDS)
                .ignoring(NoSuchElementException.class, StaleElementReferenceException.class);
        wait.until(displayingOfElementLocated(elementLocator));
        log.info("Reindexation was performed after " + watch.toString());
    }


    public static <T, U extends WebElement> T doAndGet(U element, java.util.function.Function<U, T> action) {
        FluentWait<WebDriver> wait = new FluentWait<>(Browser.driver())
                .withTimeout(30, TimeUnit.SECONDS)
                .pollingEvery(2, TimeUnit.SECONDS)
                .ignoring(NoSuchElementException.class, StaleElementReferenceException.class);
        return wait.until(new Function<WebDriver, T>() {
            @Override
            public T apply(WebDriver webDriver) {
                try {
                    return action.apply(element);
                } catch (Exception e) {
                    System.out.println("ERROR:"+e.getMessage());
                    throw e;
                }
            }
        });
    }

    public static void waitForModalWindowAppear() {
        Set<String> wh = wrap(presenceOfWindowCount(2));
        wh.remove(Browser.getMainWindowHandle());
        wh.stream().forEach(h -> Browser.driver().switchTo().window(h));
    }

    public static String waitForNewModalWindow(final Set<String> oldWindows) {
        return wrap((WebDriver driver) -> {
            Set<String> allWindows = driver.getWindowHandles();
            allWindows.removeAll(oldWindows);
            return allWindows.size() > 0 ? allWindows.iterator().next() : null;
        });
    }

    public static void waitForCloseModalWindow(final Set<String> oldWindows) {
        wrap((WebDriver driver) -> {
            Set<String> allWindows = driver.getWindowHandles();
            return oldWindows.size() > allWindows.size();
        });
    }

    public static void waitForModalWindowDisappear() {
        wrap(presenceOfWindowCount(1));
        Browser.driver().switchTo().window(Browser.getMainWindowHandle());
    }

    private static Function<WebDriver, WebElement> presenceOfElementLocated(final By locator) {
        return driver -> driver.findElement(locator);
    }

    private static Function<WebDriver, WebElement> enablingOfElementLocated(final By locator) {
        return driver -> {
            WebElement element = driver.findElement(locator);
            try {
                if (element.isEnabled()) {
                    return element;
                }
            } catch (Exception e) {
//                logger.error("enablingOfElementLocated: " + e.getMessage() + " for: " + locator.toString());
                return null;
            }
            return null;
        };
    }

    private static Function<WebDriver, WebElement> displayingOfElementLocated(final By locator) {
        return driver -> {
            try {
                WebElement element = driver.findElement(locator);
                return element.isDisplayed() ? element : null;
            } catch (WebDriverException e) {
                return null;
            }
        };
    }

    private static Function<WebDriver, WebElement> focusingOfElementLocated(final By locator) {
        return driver -> {
            WebElement element = driver.findElement(locator);
            return element.equals(driver.switchTo().activeElement()) ? element : null;
        };
    }


    private static Function<WebDriver, Set<String>> presenceOfWindowCount(final int count) {
        return webDriver -> {
            Set<String> windowHandles = webDriver.getWindowHandles();
            return windowHandles.size() != count ? null : windowHandles;
        };
    }

    public static WebElement waitForStableElement(final By locator) {
        return wrap((WebDriver d) -> {
            try {
                return d.findElement(locator);
            } catch (StaleElementReferenceException ex) {
                log.error("waitForStableElement: " + ex.getMessage() + " for: " + locator.toString());
                return null;
            }
        });
    }

    public static List<WebElement> waitForStableElements(final By locator) {
        return wrap((WebDriver d) -> {
            try {
                List<WebElement> elements = d.findElements(locator);
                if (elements.isEmpty()) {
                    return null;
                }
                return elements;
            } catch (StaleElementReferenceException ex) {
                log.error("waitForStableElements: " + ex.getMessage() + " for: " + locator.toString());
                return null;
            }
        });
    }

    public static void waitForElementWithPageReload(final By locator) {
        FluentWait<WebDriver> wait = new FluentWait<>(Browser.driver()).withTimeout(10, TimeUnit.MINUTES).pollingEvery(10, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
        wait.until((WebDriver driver) -> {
            driver.navigate().refresh();
            return driver.findElement(locator);
        });
        watch.stop();
    }

    public static void waitForElementDisappear(By locator) {
        wrap(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public static void waitForDialog() {
        wrap((WebDriver d) -> ((JavascriptExecutor) d).executeScript("Ext.WindowManager.getActive();"));
    }

    public static <T> T For(Function<WebDriver, T> condition) {
        return wrap(condition);
    }

    public static <T> T For(Function<WebDriver, T> condition, long timeoutSeconds, long pollMs) {
        return new WebDriverWait(Browser.driver(), timeoutSeconds, pollMs).until(condition);
    }

    private static <T> T wrap(Function<WebDriver, T> condition) {
        offImplicit();
        try {
            return new WebDriverWait(Browser.driver(), TIME_OUT_IN_SECONDS, POLL_IN_MS).until(condition);
        } finally {
            onImplicit();
        }
    }

    private static void offImplicit() {
        Browser.driver().manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
    }

    private static void onImplicit() {
        Browser.driver().manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
    }

    public static <E extends ExtElement> E waitForVisible(E element) {
        waitForVisible(element.getRootElement());
        return element;
    }

    public static <E extends TypifiedElement> E waitForVisible(E element) {
        waitForVisible(element.getWrappedElement());
        return element;
    }

    public static WebElement waitForVisible(WebElement element) {
        wrapWait(visibilityOf(element));
        return element;
    }

    public static <E extends TypifiedElement> void waitForInvisible(E element) {
        waitForInvisible(element.getWrappedElement());
    }

    public static void waitForInvisible(final WebElement element) {
        wrapWait(d -> {
            try {
                return !element.isDisplayed();
            } catch (NoSuchElementException | StaleElementReferenceException e) {
                return true;
            }
        });
    }

    public static void waitUntilVisible(WebElement element) {
        wrapWait(d -> {
            try {
                return element.isDisplayed();
            } catch (NoSuchElementException | StaleElementReferenceException e) {
                return true;
            }
        });

    }

    public static <E extends TypifiedElement> E waitForEnabled(E element) {
        waitForEnabled(element.getWrappedElement());
        return element;
    }

    public static WebElement waitForEnabled(WebElement element) {
        wrapWait(d -> element.isEnabled());
        return element;
    }

    public static void waitForLoaded() {
        wrapWait(d -> {
            try {
                return !d.findElements(By.xpath("//div[@class='x-mask-msg-text' and text()='Loading...']")).stream()
                        .anyMatch(WebElement::isDisplayed);
            } catch (NoSuchElementException | StaleElementReferenceException e) {
                return true;
            }
        });
    }

    public static Alert waitForAlert(int timeout) {
        return new WebDriverWait(Browser.driver(), timeout).until((ExpectedCondition<Alert>) d -> {
            try {
                return d.switchTo().alert();
            } catch (NoAlertPresentException e) {
                return null;
            }
        });
    }

    public static <V> V wrapWait(ExpectedCondition<V> expectedCondition) {
        return new WebDriverWait(Browser.driver(), 15, 1000).until(expectedCondition);
    }

    public static void waitUntilCountOfWindow(int windowCount) {
        wrapWait(ExpectedConditions.numberOfWindowsToBe(windowCount));
    }
}
