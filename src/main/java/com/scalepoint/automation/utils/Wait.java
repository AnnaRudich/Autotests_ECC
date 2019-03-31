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

import static com.codeborne.selenide.Selenide.$$;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfAllElements;

@SuppressWarnings({"Guava", "ConstantConditions"})
public class Wait {
    private static final int TIME_OUT_IN_SECONDS = 30;
    private static final int POLL_IN_MS = 1000;
    private static final int DEFAULT_TIMEOUT = 30;

    private static Logger log = LogManager.getLogger(Wait.class);

    private static FluentWait<WebDriver> getWebDriverWaitWithDefaultTimeoutAndPooling() {
        return new WebDriverWait(Browser.driver(), DEFAULT_TIMEOUT)
                .pollingEvery(500, TimeUnit.MILLISECONDS)
                .ignoring(StaleElementReferenceException.class);
    }

    public static void waitForAjaxCompleted() {
        getWebDriverWaitWithDefaultTimeoutAndPooling().until((ExpectedCondition<Boolean>) wrapWait ->
                !(Boolean) ((JavascriptExecutor) wrapWait).executeScript("return Ext.Ajax.isLoading();"));
    }

    //TODO remove when https://jira.scalepoint.com/browse/CONTENTS-4484 is done
    public static void waitForXhrAjaxCompleted() {
        getWebDriverWaitWithDefaultTimeoutAndPooling().until(new ExpectedCondition<Boolean>() {
            @Nullable
            @Override
            public Boolean apply(@Nullable WebDriver wrapWait) {
                return (Boolean) ((JavascriptExecutor) wrapWait).executeScript("var searchReq = getXmlHttpRequestObject(); return (searchReq.readyState == 4 || searchReq.readyState == 0);");
            }
        });
    }

    public static void waitForSpinnerToDisappear() {
        long start = System.currentTimeMillis();
        forConditionShort(new Function<WebDriver, Object>() {
            @Nullable
            @Override
            public Object apply(@Nullable WebDriver webDriver) {
                return webDriver.findElements(By.xpath("//div[contains(@class, 'loader')]")).isEmpty();
            }
        }, 1, 100);
        log.info("waitForSpinnerToDisappear: {}", (System.currentTimeMillis() - start));
    }

    public static void waitForPageLoaded() {
        getWebDriverWaitWithDefaultTimeoutAndPooling().until((ExpectedCondition<Boolean>) wrapWait ->
                ((JavascriptExecutor) wrapWait).executeScript("return document.readyState").equals("complete"));
    }

    public static void wait(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
    }

    public static void waitMillis(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
    }

    public static Boolean visible(WebElement element) {
        List<WebElement> webElements = wrapShort(visibilityOfAllElements(Lists.newArrayList(element)));
        return webElements.size() == 1;
    }

    public static Boolean isElementNotPresent(By locator) {
        return $$(locator).filter(Condition.visible).size() == 0;
    }

    public static Boolean invisibleOfElement(By locator) {
        return wrapShort(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public static WebElement waitForEnabled(By locator) {
        return wrapShort(ExpectedConditions.elementToBeClickable(locator));
    }

    public static WebElement waitForDisplayed(By locator) {
        return wrapShort(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static void waitForStaleElement(final By locator) {
        wrap((WebDriver d) -> {
            try {
                return d.findElement(locator);
            } catch (StaleElementReferenceException ex) {
                log.error("waitForStableElement: " + ex.getMessage() + " for: " + locator.toString());
                return null;
            }
        });
    }

    public static List<WebElement> waitForStaleElements(final By locator) {
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
    }

    public static void waitForElementContainsText(WebElement element, String text) {
        Boolean contains = wrapShort((WebDriver d) -> element.getText().contains(text));
        if (!contains) {
            throw new IllegalStateException("Elements doesn't contain: " + text);
        }
    }

    public static void waitElementDisappeared(By element) {
        forConditionShort(ExpectedConditions.invisibilityOfElementLocated(element), 5, 100);
    }

    public static <T> T forCondition(Function<WebDriver, T> condition) {
        return new WebDriverWait(Browser.driver(), TIME_OUT_IN_SECONDS, POLL_IN_MS).ignoring(StaleElementReferenceException.class).until(condition);
    }

    public static <T> T forConditionShort(Function<WebDriver, T> condition, long timeoutSeconds, long pollMs) {
        Browser.driver().manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
        try {
            FluentWait<WebDriver> wait = new FluentWait<WebDriver>(Browser.driver())
                    .withTimeout(Duration.ofSeconds(timeoutSeconds))
                    .pollingEvery(Duration.ofMillis(pollMs))
                    .ignoring(StaleElementReferenceException.class);
            return wait.until(condition);
        } finally {
            Browser.driver().manage().timeouts().implicitlyWait(DriversFactory.Timeout.DEFAULT_IMPLICIT_WAIT, TimeUnit.SECONDS);

        }
    }

    public static <T> T forCondition1s(Function<WebDriver, T> condition) {
        Browser.driver().manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
        try {
            FluentWait<WebDriver> wait = new FluentWait<>(Browser.driver())
                    .withTimeout(Duration.ofSeconds(1))
                    .pollingEvery(Duration.ofMillis(100))
                    .ignoring(StaleElementReferenceException.class);
            return wait.until(condition);
        } finally {
            Browser.driver().manage().timeouts().implicitlyWait(DriversFactory.Timeout.DEFAULT_IMPLICIT_WAIT, TimeUnit.SECONDS);
        }
    }

    public static <T> T forConditionLong(Function<WebDriver, T> condition, long timeoutSeconds, long pollMs) {
        FluentWait<WebDriver> wait = new FluentWait<>(Browser.driver())
                .withTimeout(Duration.ofSeconds(timeoutSeconds))
                .pollingEvery(Duration.ofMillis(pollMs))
                .ignoring(StaleElementReferenceException.class);
        return wait.until(condition);
    }

    public static <T> T forCondition(Function<WebDriver, T> condition, long timeoutSeconds) {
        return new WebDriverWait(Browser.driver(), timeoutSeconds, POLL_IN_MS).ignoring(StaleElementReferenceException.class).until(condition);
    }

    private static <T> T wrap(ExpectedCondition<T> expectedCondition) {
        return new WebDriverWait(Browser.driver(), TIME_OUT_IN_SECONDS, POLL_IN_MS).ignoring(StaleElementReferenceException.class).until(expectedCondition);
    }

    public static <E extends ExtElement> E waitForVisible(E element) {
        waitForVisible(element.getRootElement());
        return element;
    }

    public static List<WebElement> waitForAllElementsVisible(List<WebElement> elements) {
        wrap(visibilityOfAllElements(elements));
        return elements;
    }

    public static <E extends TypifiedElement> E waitForVisible(E element) {
        waitForVisible(element.getWrappedElement());
        return element;
    }

    public static WebElement waitForVisible(By by) {
        return waitForVisible(Browser.driver().findElement(by));
    }

    public static WebElement waitForVisible(WebElement element) {
        wrap(visibilityOf(element));
        return element;
    }

    public static boolean checkIsDisplayed(WebElement element) {
        long start = System.currentTimeMillis();
        try {
            return forCondition1s(d -> element.isDisplayed());
        } catch (NoSuchElementException e) {
            log.info("Element [{}] is not displayed", element.toString());
            return false;
        } finally {
            log.info("checkIsDisplayed: {} ms.", (System.currentTimeMillis() - start));
        }
    }

    public static <E extends TypifiedElement> void waitForInvisible(E element) {
        waitForInvisible(element.getWrappedElement());
    }

    public static void waitForInvisible(final WebElement element) {
        wrap(d -> {
            try {
                return !element.isDisplayed();
            } catch (NoSuchElementException | StaleElementReferenceException e) {
                return true;
            }
        });
    }

    public static void waitUntilVisible(WebElement element) {
        wrap(d -> {
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

    private static WebElement waitForEnabled(WebElement element) {
        wrap(d -> element.isEnabled());
        return element;
    }

    public static void waitForLoaded() {
        wrap(d -> {
            try {
                assert d != null;
                return d.findElements(By.xpath("//div[@class='x-mask-msg-text' and text()='Loading...']")).stream()
                        .noneMatch(WebElement::isDisplayed);
            } catch (NoSuchElementException | StaleElementReferenceException e) {
                return true;
            }
        });
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
