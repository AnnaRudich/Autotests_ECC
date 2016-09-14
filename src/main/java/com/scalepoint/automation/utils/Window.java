package com.scalepoint.automation.utils;

import com.scalepoint.automation.utils.driver.Browser;
import org.apache.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Set;

public class Window {

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

    public static class WindowManager {

        private static Logger logger = Logger.getLogger(WindowManager.class);

        private WebDriver driver;

        WindowManager(WebDriver driver) {
            this.driver = driver;
        }

        public void openDialog(WebElement openButton) {
            Set<String> windowHandlesBefore = driver.getWindowHandles();
            if (openButton.isDisplayed()) {
                openButton.click();
            } else {
                logger.error("Button is not displayed");
            }
            Wait.waitForNewModalWindow(windowHandlesBefore);
            switchToLast();
        }
        public void openDialogWithJavascriptHelper(WebElement openButton) {
            openDialog(openButton);
            JavascriptHelper.initializeCommonFunctions(driver);
        }

        public void openDialogWithAlert(WebElement openButton) {
            openButton.click();
            try {
                Alert alert = driver.switchTo().alert();
                alert.accept();
                Wait.waitForModalWindowDisappear();
            } catch (Exception ignored) {
            }
            Wait.waitForModalWindowAppear();
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
            for (String winHandle : driver.getWindowHandles()) {
                driver.switchTo().window(winHandle);
            }
        }

        public void waitForNewWindowAndSwithToIt() {
            WebDriverWait wait = new WebDriverWait(driver, 5, 300);
            wait.until((WebDriver d) -> d.getWindowHandles().size() > 1);
            for (String winHandle : driver.getWindowHandles()) {
                if (!Browser.getMainWindowHandle().equals(winHandle))
                    driver.switchTo().window(winHandle);
            }
        }

        public void closeAllButMain() {
            for (String winHandle : driver.getWindowHandles()) {
                if (!Browser.getMainWindowHandle().equals(winHandle))
                    try {
                        driver.switchTo().window(winHandle).close();
                    } catch (NoSuchWindowException ignored) {
                    }
            }
            switchToLast();
        }
    }
}
