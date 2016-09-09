package com.scalepoint.automation.pageobjects.modules;

import com.scalepoint.automation.Actions;
import com.scalepoint.automation.utils.driver.Browser;
import org.openqa.selenium.WebDriver;
import ru.yandex.qatools.htmlelements.loader.HtmlElementLoader;

public abstract class Module extends Actions {

    protected WebDriver driver;

    public Module() {
        this.driver = Browser.driver();
        HtmlElementLoader.populatePageObject(this, this.driver);
    }
}
