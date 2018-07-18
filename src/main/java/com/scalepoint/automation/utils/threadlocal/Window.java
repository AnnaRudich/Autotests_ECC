package com.scalepoint.automation.utils.threadlocal;

import com.google.common.base.Function;
import com.scalepoint.automation.Actions;
import com.scalepoint.automation.utils.JavascriptHelper;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Set;

import static com.codeborne.selenide.Selenide.$;

@SuppressWarnings("ConstantConditions")
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

    public static class WindowManager implements Actions {

        private static Logger logger = Logger.getLogger(WindowManager.class);

        private WebDriver driver;

        WindowManager(WebDriver driver) {
            this.driver = driver;
        }

        public boolean openDialog(WebElement openButton) {
            Set<String> windowHandlesBefore = driver.getWindowHandles();
            if (openButton.isDisplayed()) {
                $(openButton).click();
            } else {
                logger.error("Button is not displayed");
            }
            if (isAlertPresent()) {
                String alertText = getAlertTextAndAccept();
                logger.info("Text of alert: " + alertText);
                return false;
            } else {
                waitForNewModalWindow(windowHandlesBefore);
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
                waitForModalWindowDisappear();
            } catch (Exception ignored) {
            }
        }

        public void closeDialog() {
            Set<String> windowHandles = driver.getWindowHandles();
            driver.close();
            waitForCloseModalWindow(windowHandles);
            switchToLast();
        }

        public void closeDialog(WebElement closeButton) {
            Set<String> windowHandlesBefore = driver.getWindowHandles();
            closeButton.click();
            waitForCloseModalWindow(windowHandlesBefore);
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
            logger.debug("url after switchToLast: " + driver.getCurrentUrl());
        }
    }


    private static void waitForModalWindowDisappear() {
        wrap((WebDriver driver) -> {
            Set<String> allWindows = driver.getWindowHandles();
            return allWindows.size() == 1;
        });
        Browser.driver().switchTo().window(Browser.getMainWindowHandle());
    }

    private static String waitForNewModalWindow(final Set<String> oldWindows) {
        return wrap((WebDriver driver) -> {
            Set<String> allWindows = driver.getWindowHandles();
            allWindows.removeAll(oldWindows);
            return allWindows.size() > 0 ? allWindows.iterator().next() : null;
        });
    }

    private static void waitForCloseModalWindow(final Set<String> oldWindows) {
        wrap((WebDriver driver) -> {
            Set<String> allWindows = driver.getWindowHandles();
            return oldWindows.size() > allWindows.size();
        });
    }

    private static <T> T wrap(Function<WebDriver, T> condition) {
        return new WebDriverWait(Browser.driver(), 500L).until(condition);
    }
}
