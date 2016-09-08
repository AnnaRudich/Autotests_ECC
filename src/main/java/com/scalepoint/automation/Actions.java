package com.scalepoint.automation;

import com.google.common.base.Predicate;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.Window;
import com.scalepoint.automation.utils.driver.Browser;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.support.ui.FluentWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public abstract class Actions {

    private static Logger log = LoggerFactory.getLogger(Actions.class);

    public static void MouseOver(WebElement webElement) {
        new org.openqa.selenium.interactions.Actions(Browser.current()).moveToElement(webElement, 5, 5).perform();

    }

    protected void closeDialog(WebElement closeButton) {
        Window.get().closeDialog(closeButton);
    }

    protected void closeDialogWithModal(WebElement closeButton) {
        Window.get().closeDialog(closeButton);
    }

    protected void closeDialog() {
        Window.get().closeDialog();
    }

    protected void openDialog(WebElement openButton) {
        Window.get().openDialog(openButton);
    }

    protected void click(By byElement) {
        find(byElement).click();
    }

    protected void click(WebElement element) {
        element.click();
    }

    protected void clear(By byElement) {
        find(byElement).clear();
    }

    protected void clear(WebElement element) {
        element.clear();
    }

    protected void sendKeys(By byElement, String keys) {
        find(byElement).sendKeys(keys);
    }

    protected void sendKeys(WebElement element, String keys) {
        element.sendKeys(keys);
    }

    protected String getText(By byElement) {
        return find(byElement).getText();
    }

    protected String getText(WebElement element) {
        return element.getText();
    }

    protected String getTextFromSearchField(WebElement element) {
        return element.getAttribute("value");
    }

    protected String getAttributeValue(By byElement) {
        return find(byElement).getAttribute("value");
    }

    protected String getAttributeValue(WebElement element) {
        return element.getAttribute("value");
    }

    protected String getAttributeText(By byElement) {
        return find(byElement).getAttribute("text");
    }

    protected String getAttributeLink(WebElement element) {
        return element.getAttribute("href");
    }

    protected String getAttributeClass(WebElement element) {
        return element.getAttribute("class");
    }

    protected String getAttributeStyle(WebElement element) {
        return element.getAttribute("style");
    }

    public void clickWithPause(By byElement) {
        clickWithPause(find(byElement));
    }

    public void clickWithPause(WebElement element) {
        element.click();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
    }

    protected void clickAndWaitForEnabling(By byElement, By byEnabledElement) {
        clickAndWaitForEnabling(find(byElement), byEnabledElement);
    }

    protected void clickAndWaitForEnabling(WebElement element, By byEnabledElement) {
        element.click();
        Wait.waitForElementEnabling(byEnabledElement);
    }

    protected void clickAndWaitForDisplaying(By byElement, By byWaitForElement) {
        clickAndWaitForDisplaying(find(byElement), byWaitForElement);
    }

    protected void clickAndWaitForDisplaying(WebElement element, By byWaitForElement) {
        element.click();
        Wait.waitForElementDisplaying(byWaitForElement);
    }

    protected void clickDismissAlertAndWaitForDisplaying(WebElement element) {
        element.click();
        dismissAlert();
    }

    protected void clickAndWaitForStable(By byElement, By byWaitForElement) {
        clickAndWaitForStable(find(byElement), byWaitForElement);
    }

    protected void clickAndWaitForStables(By byElement, By byWaitForElement) {
        clickAndWaitForStables(find(byElement), byWaitForElement);
    }

    protected void clickAndWaitForStable(WebElement element, By byWaitForElement) {
        element.click();
        Wait.waitForStableElement(byWaitForElement);
    }

    protected void clickAndWaitForStables(WebElement element, By byWaitForElement) {
        element.click();
        Wait.waitForStableElements(byWaitForElement);
    }

    protected void clickAndAcceptAlertAndWaitForStables(WebElement element, By byWaitForElement) {
        element.click();
        acceptAlert();
        Wait.waitForStableElements(byWaitForElement);

    }

    protected void clickAndAcceptAlertIfPresentAndWaitForStables(WebElement element, By byWaitForElement) {
        element.click();
        if (isAlertPresent()) {
            acceptAlert();
        }
        Wait.waitForStableElements(byWaitForElement);

    }

    private boolean isAlertPresent() {
        try {
            Browser.current().switchTo().alert();
            return true;
        }   // try
        catch (NoAlertPresentException Ex) {
            return false;
        }   // catch
    }

    protected void doubleClick(By byElement) {
        org.openqa.selenium.interactions.Actions action = new org.openqa.selenium.interactions.Actions(Browser.current());
        action.doubleClick(find(byElement));
        action.perform();
    }

    protected void doubleClick(WebElement element) {
        org.openqa.selenium.interactions.Actions action = new org.openqa.selenium.interactions.Actions(Browser.current());
        action.doubleClick(element);
        action.perform();
    }

    protected boolean isDisplayed(By byElement) {
        try {
            return find(byElement).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    protected boolean isDisplayed(WebElement element) {
        try {
            waitForWebElementFluently(element);
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void waitForWebElementFluently(WebElement webElement) {
        new FluentWait<>(webElement).
                withTimeout(5, TimeUnit.SECONDS).
                pollingEvery(1000, TimeUnit.MILLISECONDS).
                until((Predicate<WebElement>) WebElement::isDisplayed);
    }

    protected boolean isSelected(By byElement) {
        try {
            return find(byElement).isSelected();
        } catch (Exception e) {
            return false;
        }
    }

    protected boolean isSelected(WebElement element) {
        try {
            return element.isSelected();
        } catch (Exception e) {
            return false;
        }
    }

    protected boolean isElementPresent(By locator) {
        try {
            return find(locator) != null;
        } catch (Exception e) {
            return false;
        }
    }

    protected boolean isElementPresent(WebElement element) {
        try {
            return element != null;
        } catch (Exception e) {
            return false;
        }
    }


    protected WebElement find(By by) {
        return Browser.current().findElement(by);
    }

    protected WebElement find(String xpath, String... params) {
        for (int i = 1; i <= params.length; i++) {
            xpath = xpath.replace("$" + i, params[i - 1]);
        }
        return Wait.waitForElementDisplaying(By.xpath(xpath)); //find(By.xpath(xpath));
    }

    protected List<WebElement> findMany(By by) {
        return Browser.current().findElements(by);
    }

    protected void pressKeys(Keys... keys) {
        org.openqa.selenium.interactions.Actions action = new org.openqa.selenium.interactions.Actions(Browser.current());
        action.sendKeys(keys);
        action.perform();
    }

    protected void acceptAlert() {
        Alert alert = Browser.current().switchTo().alert();
        alert.accept();
        Wait.waitForModalWindowDisappear();
    }

    protected String getAlertTextAndAccept() {
        Alert alert = Browser.current().switchTo().alert();
        String alertText = alert.getText();
        alert.accept();
        Wait.waitForModalWindowDisappear();
        return alertText;
    }

    protected String getAlertText() {
        Alert alert = Browser.current().switchTo().alert();
        return alert.getText();
    }

    protected void dismissAlert() {
        Alert alert = Browser.current().switchTo().alert();
        alert.dismiss();
    }

    protected void scrollTo(WebElement element) {
        ((Locatable) element).getCoordinates();
        ((JavascriptExecutor) Browser.current()).executeScript("arguments[0].scrollIntoView();", element);
    }

    protected void setImplicitlyWait(long seconds) {
        Browser.current().manage().timeouts().implicitlyWait(seconds, TimeUnit.SECONDS);
    }

    public String getCookies() {
        Set<Cookie> cookies = Browser.current().manage().getCookies();
        StringBuilder cookieString = new StringBuilder();
        for (Cookie cookie : cookies) {
            cookieString.append(cookie.getName());
            cookieString.append("=");
            cookieString.append(cookie.getValue());
            cookieString.append(";");
        }
        return cookieString.toString();
    }

    protected void clickAndAcceptAlert(WebElement element) {
        element.click();
        //acceptAlert();
        getAlertTextAndAccept();
        Wait.waitForModalWindowDisappear();
    }

    public void switchToLastWindow() {
        for (String winHandle : Browser.current().getWindowHandles()) {
            Browser.current().switchTo().window(winHandle);
        }
    }

    public String getXpathByWebElement(WebElement webElement) {
        String webElementXpath = webElement.toString().split("xpath: ")[1];
        webElementXpath = webElementXpath.substring(0, webElementXpath.length() - 1);
        return webElementXpath;
    }

    public void enterToHiddenUploadFileField(WebElement element, String text) {
        JavascriptExecutor js = (JavascriptExecutor) Browser.current();
        js.executeScript("arguments[0].setAttribute('class', ' ');", element);
        element.sendKeys(text);

    }

    public void dragAndDrop(WebElement element, WebElement elementWhereToMove) {
        org.openqa.selenium.interactions.Actions action = new org.openqa.selenium.interactions.Actions(Browser.current());
        Action dragAndDrop = action.clickAndHold(element).moveToElement(elementWhereToMove).release(elementWhereToMove).build();
        dragAndDrop.perform();
    }


    public boolean isChecked(WebElement element) {
        return getAttributeClass(element).contains("checked");
    }

    public boolean isDisabled(WebElement element) {
        return getAttributeClass(element).contains("disabled");
    }

    public void updateElementValue(WebElement element, String value) {
        JavascriptExecutor js = (JavascriptExecutor) Browser.current();
        js.executeScript("arguments[0].setAttribute('value', '" + value + "');", element, "");
    }

    public void selectFromDropDown(WebElement dropdown, String item) {
//        click(dropdown);
        click(find("//option[contains(text(), '$1')]", item));
    }

    public boolean isAttributeExistsForTheElement(String xpath, String attribute) {
        try {
            return find(xpath).getAttribute(attribute).isEmpty();
        } catch (Exception e) {
            return true;
        }
    }

}

