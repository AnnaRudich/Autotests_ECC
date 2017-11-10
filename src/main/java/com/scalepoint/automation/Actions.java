package com.scalepoint.automation;

import com.scalepoint.automation.utils.JavascriptHelper;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.threadlocal.Browser;
import com.scalepoint.automation.utils.threadlocal.Window;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.forCondition;

public interface Actions {

    default boolean openDialog(WebElement openButton) {
        return Window.get().openDialog($(openButton));
    }

    default void openDialogWithJavascriptHelper(WebElement openButton) {
        openDialog(openButton);
        JavascriptHelper.initializeCommonFunctions();
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
            forCondition(ExpectedConditions.alertIsPresent());
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

    default  void scrollToElement(WebElement element){
        ((JavascriptExecutor) Browser.driver()).executeScript("arguments[0].scrollIntoView(true);",element);
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
        clickUsingJsIfSeleniumClickReturnError(element);
        Wait.waitForDisplayed(byWaitForElement);
    }

    default void clickAndWaitForDisplaying(By byElement, By byWaitForElement) {
        clickAndWaitForDisplaying(find(byElement), byWaitForElement);
    }

    default void clickAndWaitForEnabling(WebElement element, By byEnabledElement) {
        element.click();
        Wait.waitForEnabled(byEnabledElement);
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

    default boolean isElementPresent(WebElement element) {
        try {
            return element != null;
        } catch (Exception e) {
            return false;
        }
    }

    default boolean isElementPresent(By by) {
        try {
            Browser.driver().findElement(by);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    default boolean isDisplayed(By element) {
        try {
            return Browser.driver().findElement(element).isDisplayed();
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
    default void clickJS(WebElement element) {
        Wait.forCondition(ExpectedConditions.elementToBeClickable(element));
        ((JavascriptExecutor) Browser.driver()).executeScript("arguments[0].click();", element);
    }

    default void doubleClick(WebElement element) {
        org.openqa.selenium.interactions.Actions action = new org.openqa.selenium.interactions.Actions(Browser.driver());
        action.doubleClick(element);
        action.perform();
    }

    default void doubleClick(By by) {
        Wait.forCondition(ExpectedConditions.elementToBeClickable(by));
        doubleClick(Browser.driver().findElement(by));
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
        JavascriptExecutor executor = (JavascriptExecutor) Browser.driver();
        executor.executeScript("arguments[0].value=arguments[1];", element, value);
    }

    default void clickElementUsingJS(WebElement element){
        ((JavascriptExecutor) Browser.driver()).executeScript("arguments[0].click();", element);
    }

    default void clickUsingJsIfSeleniumClickReturnError(WebElement element) {
        try {
            element.click();
        }catch (Exception e){
            clickElementUsingJS(element);
        }
    }

    default void doubleClickElementUsingJS(WebElement element){
        ((JavascriptExecutor) Browser.driver()).executeScript("arguments[0].dblclick();", element);
    }

    default void doubleClickUsingJsIfSeleniumClickReturnError(WebElement element) {
        try {
            doubleClick(element);
        }catch (Exception e){
            doubleClickElementUsingJS(element);
        }
    }
}

