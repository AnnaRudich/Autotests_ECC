package com.scalepoint.automation.utils.driver;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Browser {

    private static final Logger logger = LoggerFactory.getLogger(Browser.class);

    private static ThreadLocal<DriverData> holder = new ThreadLocal<>();

    public static void init(WebDriver driver) {
        holder.set(new DriverData(driver));
    }

    public static WebDriver driver() {
        return holder.get().getDriver();
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
            holder.remove();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    public static void open(String url) {
        DriverData data = holder.get();
        data.getDriver().get(url);
    }
}

