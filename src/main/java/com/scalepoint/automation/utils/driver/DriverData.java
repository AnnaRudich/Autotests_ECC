package com.scalepoint.automation.utils.driver;

import org.openqa.selenium.WebDriver;

class DriverData {

    private WebDriver browser;
    private String windowHandle;

    public DriverData(WebDriver browser) {
        this.browser = browser;
        this.windowHandle = browser.getWindowHandle();
    }

    public WebDriver getBrowser() {
        return browser;
    }

    public String getWindowHandle() {
        return windowHandle;
    }
}
