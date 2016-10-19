package com.scalepoint.automation.utils;

import com.codeborne.selenide.Selenide;
import com.scalepoint.automation.Actions;
import com.scalepoint.automation.utils.driver.Browser;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import java.util.Set;

public class Window implements Actions {

    private static ThreadLocal<WindowManager> holder = new ThreadLocal<>();

    public static void init(WebDriver driver) {
        holder.set(new WindowManager(driver));
    }

    public static WindowManager get() {
        return holder.get();
    }

    public static void cleanUp() {
        holder.remove();
    }

    public static class WindowManager implements Actions {

        private static Logger logger = Logger.getLogger(WindowManager.class);

        private WebDriver driver;

        WindowManager(WebDriver driver) {
            this.driver = driver;
        }

        public boolean openDialog(WebElement openButton) {
            Set<String> windowHandlesBefore = driver.getWindowHandles();
            if (openButton.isDisplayed()) {
                openButton.click();
            } else {
                logger.error("Button is not displayed");
            }
            if (isAlertPresent()) {
                String alertText = getAlertTextAndAccept();
                logger.info("Text of alert: " + alertText);
                return false;
            } else {
                Wait.waitForNewModalWindow(windowHandlesBefore);
                switchToLast();
            }
            return true;
        }

        public void openDialogWithJavascriptHelper(WebElement openButton) {
            openDialog(openButton);
            JavascriptHelper.initializeCommonFunctions();
        }

        public void acceptAlert() {
            try {
                driver.switchTo().alert().accept();
                Wait.waitForModalWindowDisappear();
            } catch (Exception ignored) {
            }
        }

        public void closeDialog() {
            Set<String> windowHandles = driver.getWindowHandles();
            driver.close();
            Wait.waitForCloseModalWindow(windowHandles);
            switchToLast();
        }

        public void closeDialog(WebElement closeButton) {
            Set<String> windowHandlesBefore = driver.getWindowHandles();
            closeButton.click();
            Wait.waitForCloseModalWindow(windowHandlesBefore);
            switchToLast();
        }

        public void switchToLast() {
            Set<String> windowHandles = driver.getWindowHandles();
            if (windowHandles.size() > 1) {
                windowHandles.remove(Browser.getMainWindowHandle());
            }
            for (String winHandle : windowHandles) {
                driver.switchTo().window(winHandle);
            }
            logger.info("url after switch: " + driver.getCurrentUrl());
        }

    }
}
