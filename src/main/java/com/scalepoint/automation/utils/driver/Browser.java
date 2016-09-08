package com.scalepoint.automation.utils.driver;

import org.openqa.selenium.WebDriver;


public class Browser {

    private static ThreadLocal<DriverData> holder = new ThreadLocal<>();

    public static void init(WebDriver browser) {
        holder.set(new DriverData(browser));
    }

    public static WebDriver current() {
        return holder.get().getBrowser();
    }

    public static boolean isPresent() {
        return holder.get() != null;
    }

    public static String getMainWindowHandle() {
        return holder.get().getWindowHandle();
    }

    public static void quit() {
        try {
            DriverData data = holder.get();
            data.getBrowser().switchTo().window(getMainWindowHandle()).quit();
            holder.remove();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void open(String url) {
        DriverData data = holder.get();
        data.getBrowser().get(url);
    }
}

