package com.scalepoint.automation;

import com.scalepoint.automation.utils.JavascriptHelper;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.Window;
import com.scalepoint.automation.utils.driver.Browser;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.internal.Locatable;

import java.sql.Driver;
import java.util.Set;

public interface Actions {

    default boolean openDialog(WebElement openButton) {
        return Window.get().openDialog(openButton);
    }

    default void openDialogWithJavascriptHelper(WebElement openButton) {
        openDialog(openButton);
        JavascriptHelper.initializeCommonFunctions(Browser.driver());
    }

    default void closeDialog(WebElement closeButton) {
        Window.get().closeDialog(closeButton);
    }

    default void switchToLast() {
        Window.get().switchToLast();
    }

    default void closeAlert() {
        Window.get().acceptAlert();
    }

    default void acceptAlert() {
        Window.get().acceptAlert();
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
        ((Locatable) element).getCoordinates();
        ((JavascriptExecutor) Browser.driver()).executeScript("arguments[0].scrollIntoView();", element);
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

    default void enterToHiddenUploadFileField(WebElement element, String text) {
        JavascriptExecutor js = (JavascriptExecutor) Browser.driver();
        js.executeScript("arguments[0].setAttribute('class', ' ');", element);
        element.sendKeys(text);
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

    default void clickAndWaitForDisplaying(WebElement element, By byWaitForElement) {
        element.click();
        Wait.waitForElementDisplaying(byWaitForElement);
    }

    default void clickAndWaitForDisplaying(By byElement, By byWaitForElement) {
        clickAndWaitForDisplaying(find(byElement), byWaitForElement);
    }


    default void clickAndWaitForStable(WebElement element, By byWaitForElement) {
        element.click();
        Wait.waitForStableElement(byWaitForElement);
    }

    default boolean isSelected(WebElement element) {
        try {
            return element.isSelected();
        } catch (Exception e) {
            return false;
        }
    }

    default boolean isElementPresent(WebElement element) {
        try {
            return element != null;
        } catch (Exception e) {
            return false;
        }
    }


    default WebElement find(By by) {
        return Browser.driver().findElement(by);
    }

    default WebElement find(String xpath, String... params) {
        for (int i = 1; i <= params.length; i++) {
            xpath = xpath.replace("$" + i, params[i - 1]);
        }
        return Wait.waitForElementDisplaying(By.xpath(xpath)); //find(By.xpath(xpath));
    }

    default void setValue(WebElement element, String value) {
        JavascriptExecutor executor = (JavascriptExecutor) Browser.driver();
        executor.executeScript("arguments[0].value=arguments[1];", element, value);
    }

}

