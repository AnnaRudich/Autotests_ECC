package com.scalepoint.automation.utils.threadlocal;

import org.openqa.selenium.WebDriver;

class DriverData {

    private WebDriver driver;
    private String windowHandle;

    DriverData(WebDriver driver) {
        this.driver = driver;
        this.windowHandle = driver.getWindowHandle();
    }

    WebDriver getDriver() {
        return driver;
    }

    String getWindowHandle() {
        return windowHandle;
    }

    @Override
    public String toString() {
        return "DriverData{" +
                "driver=" + driver +
                ", windowHandle='" + windowHandle + '\'' +
                '}';
    }
}
