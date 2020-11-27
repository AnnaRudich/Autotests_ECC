package com.scalepoint.automation.utils;

import com.codeborne.selenide.Condition;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.scalepoint.automation.pageobjects.extjs.ExtElement;
import com.scalepoint.automation.utils.driver.DriversFactory;
import com.scalepoint.automation.utils.threadlocal.Browser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.yandex.qatools.htmlelements.element.TypifiedElement;

import javax.annotation.Nullable;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfAllElements;

@SuppressWarnings({"Guava", "ConstantConditions"})
public class Wait {
    private static final int TIME_OUT_IN_SECONDS = 12;
    private static final int POLL_IN_MS = 1000;
    private static final int DEFAULT_TIMEOUT = 12;

    private static Logger log = LogManager.getLogger(Wait.class);

    private static FluentWait<WebDriver> getWebDriverWaitWithDefaultTimeoutAndPooling() {
        return new WebDriverWait(Browser.driver(), DEFAULT_TIMEOUT)
                .pollingEvery(500, TimeUnit.MILLISECONDS)
                .ignoring(StaleElementReferenceException.class);
    }

    public static void waitForAjaxCompleted() {
        getWebDriverWaitWithDefaultTimeoutAndPooling().until((ExpectedCondition<Boolean>) wrapWait -> !(Boolean) ((JavascriptExecutor) wrapWait).executeScript("return Ext.Ajax.isLoading();"));
    }

    public static void waitForJavascriptRecalculation() {
        getWebDriverWaitWithDefaultTimeoutAndPooling().until((ExpectedCondition<Boolean>) wrapWait -> ((JavascriptExecutor) wrapWait).executeScript("return document.readyState").equals("complete"));
    }

    public static void waitForAjaxCompletedAndJsRecalculation(){

        try{

            waitForAjaxCompleted();
        }catch (TimeoutException e){

            log.warn("waitForAjaxCompleted Timeout");
        }catch (JavascriptException e){

            log.warn("Javascript exception: ", e);
        }
        waitForJavascriptRecalculation();
    }

    public static void waitForSpinnerToDisappear() {
        forCondition1s(new Function<WebDriver, Object>() {
            @Nullable
            @Override
            public Object apply(@Nullable WebDriver webDriver) {
                return webDriver.findElements(By.xpath("//div[contains(@class, 'loader')]")).isEmpty();
            }
        });
    }


    public static void waitForPageLoaded() {
        getWebDriverWaitWithDefaultTimeoutAndPooling().until((ExpectedCondition<Boolean>) wrapWait ->
                ((JavascriptExecutor) wrapWait).executeScript("return document.readyState").equals("complete"));
    }

    public static void waitMillis(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
    }

    public static Boolean visible(WebElement element) {
        long start = System.currentTimeMillis();
        try {
            List<WebElement> webElements = wrapShort(visibilityOfAllElements(Lists.newArrayList(element)));
            return webElements.size() == 1;
        } finally {
            logIfLong(start, "visible");
        }
    }

    private static void logIfLong(long start, String method) {
        long diff = System.currentTimeMillis() - start;
        if (diff > 5000) {
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
            StringBuilder from = new StringBuilder();
            for (int i = stackTrace.length - 1; i >= 0; i--) {
                StackTraceElement stackTraceElement = stackTrace[i];
                if (stackTraceElement.getClassName().contains("com.scalepoint.")) {
                    from.append(stackTraceElement.getMethodName()+":"+stackTraceElement.getLineNumber());
                    if (i > 1) {
                        from.append(" -> ");
                    }
                }
            }
            log.warn("Long timeout {}: [{}] From: {}", method, diff, from.toString());
        }
    }

    public static Boolean isElementNotPresent(By locator) {
        long start = System.currentTimeMillis();
        try {
            return $$(locator).filter(Condition.visible).size() == 0;
        } finally {
            logIfLong(start, "isElementNotPresent");
        }
    }

    public static Boolean invisibleOfElement(By locator) {
        long start = System.currentTimeMillis();
        try {
            return wrapShort(ExpectedConditions.invisibilityOfElementLocated(locator));
        } finally {
            logIfLong(start, "invisibleOfElement");
        }
    }

    public static WebElement waitForVisibleAndEnabled(By locator) {
        long start = System.currentTimeMillis();
        try {
            return wrapShort(ExpectedConditions.elementToBeClickable(locator));
        } finally {
            logIfLong(start, "wait for element to be visible and enabled");
        }
    }

    public static WebElement waitForVisibleAndEnabled(WebElement element) {
        long start = System.currentTimeMillis();
        try {
            return wrapShort(ExpectedConditions.elementToBeClickable(element));
        } finally {
            logIfLong(start, "wait for element to be visible and enabled");
        }
    }

    public static WebElement waitForDisplayed(By locator) {
        long start = System.currentTimeMillis();
        try {
            return wrapShort(ExpectedConditions.visibilityOfElementLocated(locator));
        } finally {
            logIfLong(start, "waitForDisplayed");
        }
    }

    public static void waitForStaleElement(final By locator) {
        long start = System.currentTimeMillis();
        try {
            wrap((WebDriver d) -> {
                try {
                    return d.findElement(locator);
                } catch (StaleElementReferenceException ex) {
                    log.error("waitForStableElement: " + ex.getMessage() + " for: " + locator.toString());
                    return null;
                }
            });
        } finally {
            logIfLong(start, "waitForStaleElement");
        }
    }

    public static List<WebElement> waitForStaleElements(final By locator) {
        long start = System.currentTimeMillis();
        try {
            return wrapShort((WebDriver d) -> {
                try {
                    List<WebElement> elements = d.findElements(locator);
                    if (elements.isEmpty()) {
                        return null;
                    }
                    return elements;
                } catch (StaleElementReferenceException ex) {
                    log.error("waitForStaleElements: " + ex.getMessage() + " for: " + locator.toString());
                    return null;
                }
            });
        } finally {
            logIfLong(start, "waitForStaleElements");
        }
    }

    public static void waitForElementContainsText(WebElement element, String text) {
        long start = System.currentTimeMillis();
        try {
            Boolean contains = wrapShort((WebDriver d) -> element.getText().contains(text));
            if (!contains) {
                throw new IllegalStateException("Elements doesn't contain: " + text);
            }
        } finally {
            logIfLong(start, "waitForStaleElements");
        }
    }

    public static void waitElementDisappeared(By locator) {
        long start = System.currentTimeMillis();
        try {
            forCondition(ExpectedConditions.invisibilityOfElementLocated(locator), 5);
        } finally {
            logIfLong(start, "waitElementDisappeared");
        }
    }

    public static <T> T forCondition(Function<WebDriver, T> condition) {
        long start = System.currentTimeMillis();
        try {
            return new WebDriverWait(Browser.driver(), TIME_OUT_IN_SECONDS, POLL_IN_MS).ignoring(StaleElementReferenceException.class).until(condition);
        } finally {
            logIfLong(start, "forCondition");
        }
    }

    public static <T> T forCondition(Function<WebDriver, T> condition, long timeoutSeconds, long pollMs, Class<? extends Throwable>... ignoringExceptionType) {
        long start = System.currentTimeMillis();
        Browser.driver().manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
        try {
            FluentWait<WebDriver> wait = new FluentWait<>(Browser.driver())
                    .withTimeout(Duration.ofSeconds(timeoutSeconds))
                    .pollingEvery(Duration.ofMillis(pollMs))
                    .ignoring(StaleElementReferenceException.class);
            if (ignoringExceptionType != null && ignoringExceptionType.length > 0) {
                for (Class<? extends Throwable> aClass : ignoringExceptionType) {
                    wait = wait.ignoring(aClass);
                }
            }
            return wait.until(condition);
        } finally {
            Browser.driver().manage().timeouts().implicitlyWait(DriversFactory.Timeout.DEFAULT_IMPLICIT_WAIT, TimeUnit.SECONDS);
            logIfLong(start, "forCondition");
        }
    }

    public static <T> T forCondition1s(Function<WebDriver, T> condition) {
        Browser.driver().manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
        long start = System.currentTimeMillis();
        try {
            FluentWait<WebDriver> wait = new FluentWait<>(Browser.driver())
                    .withTimeout(Duration.ofSeconds(1))
                    .pollingEvery(Duration.ofMillis(100))
                    .ignoring(StaleElementReferenceException.class);
            return wait.until(condition);
        } finally {
            Browser.driver().manage().timeouts().implicitlyWait(DriversFactory.Timeout.DEFAULT_IMPLICIT_WAIT, TimeUnit.SECONDS);
            logIfLong(start, "forCondition1s");
        }
    }

    public static <T> T forCondition(Function<WebDriver, T> condition, long timeoutSeconds) {
        return forCondition(condition, timeoutSeconds, 100);
    }

    private static <T> T wrap(ExpectedCondition<T> expectedCondition) {
        long start = System.currentTimeMillis();
        try {
            return new WebDriverWait(Browser.driver(), TIME_OUT_IN_SECONDS, POLL_IN_MS).ignoring(StaleElementReferenceException.class).until(expectedCondition);
        } finally {
            logIfLong(start, "wrap");
        }
    }

    public static <E extends ExtElement> E waitForVisible(E element) {
        long start = System.currentTimeMillis();
        try {
            waitForVisible(element.getRootElement());
            return element;
        } finally {
            logIfLong(start, "waitForVisible ExtElement");
        }
    }

    public static List<WebElement> waitForAllElementsVisible(List<WebElement> elements) {
        long start = System.currentTimeMillis();
        try {
            wrap(visibilityOfAllElements(elements));
            return elements;
        } finally {
            logIfLong(start, "waitForAllElementsVisible");
        }
    }

    public static <E extends TypifiedElement> E waitForVisible(E element) {
        long start = System.currentTimeMillis();
        try {
            waitForVisible(element.getWrappedElement());
            return element;
        } finally {
            logIfLong(start, "waitForVisible TypifiedElement");
        }
    }

    public static WebElement waitForVisible(By by) {
        return waitForVisible(Browser.driver().findElement(by));
    }

    public static WebElement waitForVisible(WebElement element) {
        long start = System.currentTimeMillis();
        try {
            wrap(visibilityOf(element));
            return element;
        } finally {
            logIfLong(start, "waitForVisible");
        }
    }

    public static boolean checkIsDisplayed(WebElement element) {
        long start = System.currentTimeMillis();
        try {
            return forCondition1s(d -> element.isDisplayed());
        } catch (NoSuchElementException e) {
            log.info("Element [{}] is not displayed", element.toString());
            return false;
        } finally {
            logIfLong(start, "checkIsDisplayed");
        }
    }

    public static <E extends TypifiedElement> void waitForInvisible(E element) {
        waitForInvisible(element.getWrappedElement());
    }

    public static void waitForInvisible(final WebElement element) {
        long start = System.currentTimeMillis();
        try {
            forCondition(d -> {
                try {
                    return !element.isDisplayed();
                } catch (NoSuchElementException | StaleElementReferenceException e) {
                    return true;
                }
            }, TIME_OUT_IN_SECONDS, 200);
        } finally {
            logIfLong(start, "waitForInvisible");
        }
    }

    public static void waitUntilVisible(WebElement element) {
        long start = System.currentTimeMillis();
        try {
            wrap(d -> {
                try {
                    return element.isDisplayed();
                } catch (NoSuchElementException | StaleElementReferenceException e) {
                    return true;
                }
            });
        } finally {
            logIfLong(start, "waitUntilVisible");
        }
    }

    public static <E extends TypifiedElement> E waitForEnabled(E element) {
        waitForEnabled(element.getWrappedElement());
        return element;
    }

    private static WebElement waitForEnabled(WebElement element) {
        long start = System.currentTimeMillis();
        try {
            wrap(d -> element.isEnabled());
            return element;
        } finally {
            logIfLong(start, "waitForEnabled");
        }
    }

    public static void waitForLoaded() {
        long start = System.currentTimeMillis();
        try {
            wrap(d -> {
                try {
                    assert d != null;
                    return d.findElements(By.xpath("//div[@class='x-mask-msg-text' and text()='Loading...']")).stream()
                            .noneMatch(WebElement::isDisplayed);
                } catch (NoSuchElementException | StaleElementReferenceException e) {
                    return true;
                }
            });
        } finally {
            logIfLong(start, "waitForLoaded");
        }
    }

    private static <V> V wrapShort(ExpectedCondition<V> expectedCondition) {
        return new WebDriverWait(Browser.driver(), DEFAULT_TIMEOUT, 1000).until(expectedCondition);
    }

    public static void waitForElementWithPageReload(By locator) {
        FluentWait<WebDriver> wait = new FluentWait<>(Browser.driver()).withTimeout(1, TimeUnit.MINUTES)
                .pollingEvery(10, TimeUnit.SECONDS)
                .ignoring(NoSuchElementException.class);
        wait.until((WebDriver driver) -> {
            driver.navigate().refresh();
            return driver.findElement(locator);
        });
    }
}
