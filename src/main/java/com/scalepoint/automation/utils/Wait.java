package com.scalepoint.automation.utils;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.scalepoint.automation.pageobjects.extjs.ExtElement;
import com.scalepoint.automation.utils.driver.DriversFactory;
import com.scalepoint.automation.utils.threadlocal.Browser;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.qatools.htmlelements.element.TypifiedElement;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf;

@SuppressWarnings({"Guava", "ConstantConditions"})
public class Wait {
    private static final int TIME_OUT_IN_SECONDS = 60;
    private static final int POLL_IN_MS = 1000;
    public static final int DEFAULT_TIMEOUT = 30;

    private static Logger log = LoggerFactory.getLogger(Wait.class);

    public static void waitForAjaxCompleted() {
        new WebDriverWait(Browser.driver(), DEFAULT_TIMEOUT).until((ExpectedCondition<Boolean>) wrapWait ->
                !(Boolean) ((JavascriptExecutor) wrapWait).executeScript("return Ext.Ajax.isLoading();"));
    }

    public static void waitForPageLoaded() {
        new WebDriverWait(Browser.driver(), DEFAULT_TIMEOUT).until((ExpectedCondition<Boolean>) wrapWait ->
                ((JavascriptExecutor) wrapWait).executeScript("return document.readyState").equals("complete"));
    }

    public static void wait(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException ignored) {
        }
    }

    public static Boolean visible(WebElement element) {
        List<WebElement> webElements = wrapShort(ExpectedConditions.visibilityOfAllElements(Lists.newArrayList(element)));
        return webElements.size() == 1;
    }

    public static Boolean invisible(WebElement element) {
        return wrapShort(ExpectedConditions.invisibilityOfAllElements(Lists.newArrayList(element)));
    }

    public static WebElement waitForEnabled(By locator) {
        return wrapShort(ExpectedConditions.elementToBeClickable(locator));
    }

    public static WebElement waitForDisplayed(By locator) {
        return wrapShort(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static WebElement waitForStaleElement(final By locator) {
        return wrap((WebDriver d) -> {
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

    public static void waitElementDisappeared(By element) {
        Browser.driver().manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        new WebDriverWait(Browser.driver(), DEFAULT_TIMEOUT).until((Function<WebDriver, Boolean>) webDriver -> {
            try {
                Browser.driver().findElement(element);
                return false;
            } catch (Exception e) {
                return true;
            }
        });
        Browser.driver().manage().timeouts().implicitlyWait(DriversFactory.Timeout.defaultImplicitWait, TimeUnit.SECONDS);
    }

    public static <T> T forCondition(Function<WebDriver, T> condition) {
        return wrap(condition);
    }

    public static <T> T forCondition(Function<WebDriver, T> condition, long timeoutSeconds, long pollMs) {
        return new WebDriverWait(Browser.driver(), timeoutSeconds, pollMs).until(condition);
    }

    private static <T> T wrap(Function<WebDriver, T> condition) {
        return new WebDriverWait(Browser.driver(), TIME_OUT_IN_SECONDS, POLL_IN_MS).ignoring(StaleElementReferenceException.class).until(condition);
    }

    private static <T> T wrap(ExpectedCondition<T> expectedCondition) {
        return new WebDriverWait(Browser.driver(), TIME_OUT_IN_SECONDS, POLL_IN_MS).until(expectedCondition);
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
        wrap(visibilityOf(element));
        return element;
    }

    public static <E extends TypifiedElement> void waitForInvisible(E element) {
        waitForInvisible(element.getWrappedElement());
    }

    private static void waitForInvisible(final WebElement element) {
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
        return new WebDriverWait(Browser.driver(), 15, 1000).until(expectedCondition);
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
