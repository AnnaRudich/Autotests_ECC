package com.scalepoint.automation.pageobjects.modules;

import com.scalepoint.automation.Actions;
import com.scalepoint.automation.utils.threadlocal.Browser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

import static com.codeborne.selenide.Selenide.page;

public abstract class Module implements Actions {

    protected Logger logger = LogManager.getLogger(getClass());

    protected WebDriver driver;

    public Module() {
        this.driver = Browser.driver();
        page(this);
    }
}
