package com.scalepoint.automation.utils;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.ex.ElementNotFound;
import com.codeborne.selenide.ex.ElementShould;
import com.google.common.base.Function;
import com.scalepoint.automation.utils.driver.DriversFactory;
import com.scalepoint.automation.utils.threadlocal.Browser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.codeborne.selenide.Selenide.$;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfAllElements;

@SuppressWarnings({"Guava", "ConstantConditions"})
public class Wait {
    private static final int TIME_OUT_IN_SECONDS = 12;
    private static final Duration TIMEOUT = Duration.ofSeconds(12);
    private static final int POLL_IN_MS = 1000;

    private static Logger log = LogManager.getLogger(Wait.class);

    private static FluentWait<WebDriver> getWebDriverWaitWithDefaultTimeoutAndPooling() {
        return new WebDriverWait(Browser.driver(), TIME_OUT_IN_SECONDS)
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

            log.warn("Javascript exception: {}", e.getMessage());
        }
        waitForJavascriptRecalculation();
    }

    public static void waitForSpinnerToDisappear() {

        try {

            $(By.xpath("//div[contains(@class, 'loader')]")).should(Condition.empty);
        }catch (ElementNotFound e){
            log.info("Spinner not found");
        }
    }

    public static void waitMillis(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
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

    public static List<WebElement> waitForAllElementsVisible(List<WebElement> elements) {
        long start = System.currentTimeMillis();
        try {
            wrap(visibilityOfAllElements(elements));
            return elements;
        } finally {
            logIfLong(start, "waitForAllElementsVisible");
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
}
