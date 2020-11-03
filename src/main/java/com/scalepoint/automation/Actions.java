package com.scalepoint.automation;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.threadlocal.Browser;
import com.scalepoint.automation.utils.threadlocal.Window;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.*;

public interface Actions {

    Logger logger = LogManager.getLogger(Actions.class);

    int TIME_OUT_IN_MILISECONDS = 12000;

    default void switchToLast() {
        Window.get().switchToLast();
    }

    default void closeAlert() {
        Window.get().acceptAlert();
    }

    default void acceptAlert() {
        if (isAlertPresent()) {
            Window.get().acceptAlert();
        }
    }

    default boolean isAlertPresent() {
        try {
            String text = Browser.driver().switchTo().alert().getText();
            return StringUtils.isNotBlank(text);
        } catch (NoAlertPresentException Ex) {
            return false;
        }
    }

    default String getAlertTextAndAccept() {
        try {
            Wait.forCondition(ExpectedConditions.alertIsPresent());
            Alert alert = Browser.driver().switchTo().alert();
            String text = alert.getText();
            alert.accept();
            return text;
        } catch (NoAlertPresentException Ex) {
            return "";
        }
    }

    default void pressKeys(Keys... keys) {
        org.openqa.selenium.interactions.Actions action = new org.openqa.selenium.interactions.Actions(Browser.driver());
        action.sendKeys((CharSequence[]) keys);
        action.perform();
    }

    default void scrollTo(WebElement element) {
        ((JavascriptExecutor) Browser.driver()).executeScript("arguments[0].scrollIntoView();", element);
    }

    default void scrollToElement(WebElement element) {
        ((JavascriptExecutor) Browser.driver()).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    default void switchToFrame(){

        Browser.driver().switchTo().frame(0);
    }

    default void switchToParentFrame(){

        Browser.driver().switchTo().parentFrame();
    }

    default void refresh(){
        Browser.driver().navigate().refresh();
    }

    default String getCookies() {
        Set<Cookie> cookies = Browser.driver().manage().getCookies();
        StringBuilder cookieString = new StringBuilder();
        for (Cookie cookie : cookies) {
            cookieString.append(cookie.getName());
            cookieString.append("=");
            cookieString.append(cookie.getValue());
            cookieString.append(";");
        }
        return cookieString.toString();
    }

    default void enterToHiddenUploadFileField(WebElement element, String filePath) {
        JavascriptExecutor js = (JavascriptExecutor) Browser.driver();
        js.executeScript("arguments[0].setAttribute('class', ' ');", element);
        element.sendKeys(filePath);
    }

    default void enterToHiddenUploadFileFieldSS(WebElement element, String filePath) {
        JavascriptExecutor js = (JavascriptExecutor) Browser.driver();
        js.executeScript("arguments[0].setAttribute('style', '');", element);
        element.sendKeys(filePath);
    }

    default void dragAndDrop(WebElement element, WebElement elementWhereToMove) {
        org.openqa.selenium.interactions.Actions action = new org.openqa.selenium.interactions.Actions(Browser.driver());
        Action dragAndDrop = action.clickAndHold(element).moveToElement(elementWhereToMove).release(elementWhereToMove).build();
        dragAndDrop.perform();
    }

    default void mouseOver(WebElement webElement) {
        new org.openqa.selenium.interactions.Actions(Browser.driver()).moveToElement(webElement, 5, 5).perform();
    }


    default void clear(By byElement) {
        find(byElement).clear();
    }

    default void clear(WebElement element) {
        element.clear();
    }

    default void sendKeys(WebElement element, String keys) {
        element.sendKeys(keys);
    }

    default void sendKeys(By byElement, String keys) {
        find(byElement).sendKeys(keys);
    }

    default String getText(By byElement) {
        return find(byElement).getText();
    }

    default String getText(WebElement element) {
        return element.getText();
    }

    default void waitForJavascriptRecalculation() {
        Wait.waitForJavascriptRecalculation();
    }

    default void clickAndWaitForDisplaying(WebElement element, By byWaitForElement) {
        clickUsingJavaScriptIfClickDoesNotWork(element);
        $(byWaitForElement).waitUntil(Condition.visible, 60000);
    }

    default void clickAndWaitForDisplaying(By byElement, By byWaitForElement) {
        clickAndWaitForDisplaying(find(byElement), byWaitForElement);
    }

    default void clickAndWaitForEnabling(WebElement element, By byEnabledElement) {
        element.click();
        Wait.waitForVisibleAndEnabled(byEnabledElement);
    }


    default void clickAndWaitForStable(WebElement element, By byWaitForElement) {
        element.click();
        Wait.waitForStaleElement(byWaitForElement);
    }

    default void clickAndWaitForStable(By element, By byWaitForElement) {
        clickAndWaitForStable(Browser.driver().findElement(element), byWaitForElement);
    }

    default boolean isSelected(WebElement element) {
        try {
            return element.isSelected();
        } catch (Exception e) {
            return false;
        }
    }

    default boolean isElementPresent(By by) {
        try {
            Browser.driver().findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    default boolean isDisplayed(By locator) {
        try {
            return Browser.driver().findElement(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    default boolean isDisplayed(WebElement element) {
        try {
            new FluentWait<>(element).
                    withTimeout(5, TimeUnit.SECONDS).
                    pollingEvery(1000, TimeUnit.MILLISECONDS).
                    until(e -> element.isDisplayed());
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    default WebElement find(By by) {
        return Browser.driver().findElement(by);
    }

    default String getInputValue(WebElement webElement) {
        return webElement.getAttribute("value");
    }

    default WebElement find(String xpath, String... params) {
        for (int i = 1; i <= params.length; i++) {
            xpath = xpath.replace("$" + i, params[i - 1]);
        }
        return Wait.waitForDisplayed(By.xpath(xpath));
    }

    /**
     * this method decrements element index to translate java enumeration to human readable one
     */
    default WebElement find(String xpath, int param) {
        String decrementedParam = Integer.toString(--param);
        xpath = xpath.replace("$", decrementedParam);
        return Wait.waitForDisplayed(By.xpath(xpath));
    }

    default void setValue(WebElement element, String value) {
        waitForVisible(element);
        logger.info("SetValue {} --> {}", getElementIdentifier(element), value);
        JavascriptExecutor executor = (JavascriptExecutor) Browser.driver();
        for (int i = 0; i < 3; i++) {
            if (element.getText().equals(value)) {
                break;
            } else {
                executor.executeScript("arguments[0].value=arguments[1];", element, value);
            }
        }
    }

    default String getElementIdentifier(WebElement element) {
        String value = null;
        try {
            value = element.getAttribute("name");
            if (StringUtils.isBlank(value)) {
                value = element.getAttribute("id");
            }
        } catch (Exception ignored) {
        }

        return StringUtils.isBlank(value) ? "unknown" : value;
    }

    default void clickUsingJS(WebElement element) {
        logger.warn("clicking on element with java script click");
        ((JavascriptExecutor) Browser.driver()).executeScript("arguments[0].click();", element);
    }

    default void clickUsingJavaScriptIfClickDoesNotWork(WebElement element){
        try {
            waitForVisibleAndEnabled(element);
            element.click();
        } catch (StaleElementReferenceException e) {
            logger.warn("Element is not attached to the page document " + e);
            clickUsingJS(element);
        } catch (NoSuchElementException e) {
            logger.warn("Element was not found in DOM " + e);
            clickUsingJS(element);
        } catch (Exception e) {
            logger.warn("Unable to click on element " + e);
            clickUsingJS(element);
        }
    }

    default void doubleClick(WebElement element) {
        try {
            waitForVisibleAndEnabled(element);
            org.openqa.selenium.interactions.Actions action = new org.openqa.selenium.interactions.Actions(Browser.driver());
            action.doubleClick(element);
            action.perform();
        } catch (StaleElementReferenceException e) {
            logger.warn("Element is not attached to the page document " + e);
        } catch (NoSuchElementException e) {
            logger.warn("Element was not found in DOM " + e);
        } catch (Exception e) {
            logger.error("Unable to doubleClick on element " + e);
        }
    }

    default SelenideElement hoverAndClick(SelenideElement element){

        element
                .waitUntil(and("can be clickable", visible, enabled), TIME_OUT_IN_MILISECONDS)
                .hover()
                .click();
        waitForAjaxCompletedAndJsRecalculation();
        return element;
    }

    default void doubleClick(By by) {
        doubleClick(find(by));
    }

    default void replaceAmpInUrl() {
        String currentUrl = Browser.driver().getCurrentUrl();
        if (currentUrl.contains("&amp;")) {
            Browser.driver().get(currentUrl.replace("&amp;", "&"));
        }
    }
}

