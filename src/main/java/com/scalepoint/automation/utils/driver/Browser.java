package com.scalepoint.automation.utils.driver;

import org.openqa.selenium.WebDriver;


public class Browser {

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
            DriverData data = holder.get();
            data.getDriver().switchTo().window(getMainWindowHandle()).quit();
            holder.remove();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void open(String url) {
        DriverData data = holder.get();
        data.getDriver().get(url);
    }
}

