package com.scalepoint.automation.utils;

import com.scalepoint.automation.pageobjects.extjs.ExtElement;
import com.scalepoint.automation.utils.driver.Browser;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.internal.Locatable;

import java.util.Set;

public class EccActions {
    private WebDriver driver;

    public EccActions(WebDriver driver) {
        this.driver = driver;
    }

    public void doubleClick(WebElement element) {
        new Actions(driver).doubleClick(element).perform();
    }

    public void doubleClick(ExtElement element) {
        doubleClick(element.getWrappedElement());
    }

    public void switchToWindow() {
        String mainWindowHandle = driver.getWindowHandle();
        Set<String> s = driver.getWindowHandles();
        for (Object value : s) {
            String popupHandle = value.toString();
            if (!popupHandle.contains(mainWindowHandle)) {
                driver.switchTo().window(popupHandle);
            }
        }
    }

    public void switchBetweenWindow() {
        Set<String> w = driver.getWindowHandles();
        driver.switchTo().window(w.iterator().next());
    }


    public void close() {
        driver.close();
    }

    public static void AcceptAlert() {
        for (int i = 0; i < 5; i++) {
            if (isAlertPresent())
                try {
                    Alert alert = Browser.current().switchTo().alert();
                    alert.accept();
                } catch (NoAlertPresentException e) {
                    System.out.println("No Alert Present");
                }
            else
                break;
        }
    }

    public void navigateToLink(String link) {
        driver.get(link);
        driver.navigate().to(link);
    }

    public static boolean isAlertPresent() {
        try {
            Browser.current().switchTo().alert();
            return true;
        } catch (NoAlertPresentException Ex) {
            return false;
        }
    }

    public void scrollIntoView(WebElement element) {
        ((Locatable) element).getCoordinates();
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0,250)", "");
    }
}
