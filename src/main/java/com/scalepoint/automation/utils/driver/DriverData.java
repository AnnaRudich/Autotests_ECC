package com.scalepoint.automation.utils.driver;

import org.openqa.selenium.WebDriver;

class DriverData {

    private WebDriver driver;
    private String windowHandle;

    public DriverData(WebDriver driver) {
        this.driver = driver;
        this.windowHandle = driver.getWindowHandle();
    }

    public WebDriver getDriver() {
        return driver;
    }

    public String getWindowHandle() {
        return windowHandle;
    }
}
