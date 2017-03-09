package com.scalepoint.automation.pageobjects.modules;

import com.scalepoint.automation.Actions;
import com.scalepoint.automation.utils.threadlocal.Browser;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.qatools.htmlelements.loader.HtmlElementLoader;

public abstract class Module implements Actions {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected WebDriver driver;

    public Module() {
        this.driver = Browser.driver();
        HtmlElementLoader.populatePageObject(this, this.driver);
    }
}
