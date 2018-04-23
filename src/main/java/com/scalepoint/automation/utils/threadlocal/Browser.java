package com.scalepoint.automation.utils.threadlocal;

import com.scalepoint.automation.utils.driver.DriverType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;


public class Browser {

    private static final Logger logger = LogManager.getLogger(Browser.class);

    private static ThreadLocal<DriverData> holder = new ThreadLocal<>();

    public static void init(WebDriver driver, DriverType driverType) {
        holder.set(new DriverData(driver, driverType));
    }

    public static WebDriver driver() {
        return holder.get() != null ? holder.get().getDriver() : null;
    }

    public static String getDriverType() {
        return holder.get() != null ? holder.get().getDriverType().name() : "no_browser";
    }

    public static String getMainWindowHandle() {
        return holder.get().getWindowHandle();
    }

    public static void quit() {
        try {
            logger.info("Quit requested");
            DriverData data = holder.get();
            logger.info("Data found {}", data);
            data.getDriver().quit();
            logger.info("Holder remove");
            holder.remove();
            logger.info("Completed browser quit!");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    public static void clear() {
        DriverData data = holder.get();
        data.getDriver().manage().deleteAllCookies();
    }

    public static void open(String url) {
        DriverData data = holder.get();
        data.getDriver().get(url);
    }
}

