package com.scalepoint.automation;

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

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;
import static com.scalepoint.automation.utils.Wait.waitForVisibleAndEnabled;

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

    default void switchToFrame(){

        Browser.driver().switchTo().frame(0);
    }

    default void switchToParentFrame(){

        Browser.driver().switchTo().parentFrame();
    }

    default void refresh(){
        Browser.driver().navigate().refresh();
    }

    default void dragAndDrop(WebElement element, WebElement elementWhereToMove) {
        org.openqa.selenium.interactions.Actions action = new org.openqa.selenium.interactions.Actions(Browser.driver());
        Action dragAndDrop = action.clickAndHold(element).moveToElement(elementWhereToMove).release(elementWhereToMove).build();
        dragAndDrop.perform();
    }

    default boolean isSelected(WebElement element) {
        try {
            return $(element).is(selected);
        } catch (Error e) {
            return false;
        }
    }

    default boolean isDisplayed(WebElement element) {
        try {
            return $(element).waitUntil(visible, TIME_OUT_IN_MILISECONDS).isDisplayed();
        } catch (Error e) {
            return false;
        }
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

    default SelenideElement hoverAndClick(SelenideElement element){

        element
                .waitUntil(and("can be clickable", visible, enabled), TIME_OUT_IN_MILISECONDS)
                .hover()
                .click();
        waitForAjaxCompletedAndJsRecalculation();
        return element;
    }
}

