package com.scalepoint.automation.utils.threadlocal;

import com.scalepoint.automation.utils.driver.DriverType;
import org.openqa.selenium.WebDriver;

class DriverData {

    private WebDriver driver;
    private String windowHandle;
    private DriverType driverType;

    DriverData(WebDriver driver, DriverType driverType) {
        this.driver = driver;
        this.driverType = driverType;
        this.windowHandle = driver.getWindowHandle();
    }

    WebDriver getDriver() {
        return driver;
    }

    DriverType getDriverType() {
        return driverType;
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
