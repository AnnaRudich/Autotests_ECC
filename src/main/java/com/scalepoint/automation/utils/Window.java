package com.scalepoint.automation.utils;

import com.scalepoint.automation.utils.driver.Browser;
import org.apache.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Set;

public class Window {

    private static ThreadLocal<WindowManager> holder = new ThreadLocal<>();

    public static void init(WebDriver browser) {
        holder.set(new WindowManager(browser.getWindowHandle()));
    }

    public static WindowManager get() {
        return holder.get();
    }

    public static void cleanUp() {
        holder.remove();
    }

    public static class WindowManager {

        private static Logger logger = Logger.getLogger(WindowManager.class);

        private Deque<String> deque = new LinkedList<>();

        WindowManager(String mainWindowHandle) {
            deque.push(mainWindowHandle);
        }

        public void openDialog(WebElement openButton) {
            WebDriver driver = Browser.current();
            Set<String> windowHandlesBefore = driver.getWindowHandles();
            if (openButton.isDisplayed()) {
                int i = 0;
                while (driver.getWindowHandles().size() < 2 && i < 2) {
                    openButton.click();
                    i++;
                }
            } else {
                logger.error("Button is not displayed");
            }
            String newWindowHandle = Wait.waitForNewModalWindow(windowHandlesBefore);
            driver.switchTo().window(newWindowHandle);
            deque.push(newWindowHandle);
        }

        public void openDialogWithJavascriptHelper(WebElement openButton)  {
            WebDriver driver = Browser.current();
            Set<String> windowHandlesBefore = driver.getWindowHandles();
            openButton.click();
            String newWindowHandle = Wait.waitForNewModalWindow(windowHandlesBefore);
            driver.switchTo().window(newWindowHandle);
            deque.push(newWindowHandle);
            JavascriptHelper.initializeCommonFunctions(driver);
        }

        public void openDialogWithAlert(WebElement openButton) {
            openButton.click();
            try {
                Alert alert = Browser.current().switchTo().alert();
                alert.accept();
                Wait.waitForModalWindowDisappear1();
            } catch (Exception ignored) {
            }
            Wait.waitForModalWindowAppear();
        }

        public void closeDialog() {
            deque.pop();
            Browser.current().close();
            Browser.current().switchTo().window(deque.peek());
        }

        public void closeDialog(WebElement closeButton) {
            deque.pop();
            WebDriver driver = Browser.current();
            Set<String> windowHandlesBefore = driver.getWindowHandles();
            closeButton.click();
            Wait.waitForCloseModalWindow(windowHandlesBefore);
            driver.switchTo().window(deque.peek());
        }

        public void switchToLast() {
            WebDriver driver = Browser.current();
            for (String winHandle : driver.getWindowHandles()) {
                driver.switchTo().window(winHandle); // switch focus of WebDriver to the next found window handle (that's your newly opened window)
            }
        }

        public void waitForNewWindowAndSwithToIt() {
            WebDriver driver = Browser.current();
            WebDriverWait wait = new WebDriverWait(driver, 5, 300);
            wait.until((WebDriver d) -> d.getWindowHandles().size() > 1);
            for (String winHandle : driver.getWindowHandles()) {
                if (!Browser.getMainWindowHandle().equals(winHandle))
                    driver.switchTo().window(winHandle);
            }
        }

        public void closeAllButMain() {
            WebDriver driver = Browser.current();
            for (String winHandle : driver.getWindowHandles()) {
                if (!Browser.getMainWindowHandle().equals(winHandle))
                    try {
                        driver.switchTo().window(winHandle).close();
                    } catch (NoSuchWindowException ignored) {

                    }
            }
            driver.switchTo().window(Browser.getMainWindowHandle());

            deque.clear();
            deque.add(driver.getWindowHandle());
        }
    }
}
