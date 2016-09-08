package com.scalepoint.automation.pageobjects.modules;

import com.scalepoint.automation.Actions;
import com.scalepoint.automation.utils.driver.Browser;
import org.openqa.selenium.WebDriver;
import ru.yandex.qatools.htmlelements.loader.HtmlElementLoader;

public abstract class Module extends Actions {

    protected WebDriver browser;

    public Module() {
        this.browser = Browser.current();
        HtmlElementLoader.populatePageObject(this, this.browser);
    }
}
