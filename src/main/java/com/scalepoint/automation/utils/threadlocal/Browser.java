package com.scalepoint.automation.utils.threadlocal;

import com.scalepoint.automation.utils.driver.DriverType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


public class Browser {

    private static final Logger logger = LogManager.getLogger(Browser.class);

    private static ThreadLocal<DriverData> holder = new ThreadLocal<>();

    public static void init(WebDriver driver, DriverType driverType) {
        holder.set(new DriverData(driver, driverType));
    }

    public static WebDriver driver() {
        if (holder.get() != null) return holder.get().getDriver();
        else throw new IllegalStateException("Driver is not initialized");
    }

    public static Map<String, String> cookies(){
        
        return driver().manage().getCookies().stream().collect(Collectors.toMap(Cookie::getName, Cookie::getValue, (x1, x2) -> x1));
    }

    public static boolean hasDriver() {
        return holder.get() != null;
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
            if (data != null) {
                data.getDriver().quit();
            }
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

