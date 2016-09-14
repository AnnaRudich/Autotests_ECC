package com.scalepoint.automation.pageobjects.dialogs;

import com.scalepoint.automation.utils.driver.Browser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import ru.yandex.qatools.htmlelements.loader.HtmlElementLoader;

public abstract class BaseDialog {

    protected Logger logger = LogManager.getLogger(BaseDialog.class);

    protected WebDriver driver;

    public BaseDialog() {
        this.driver = Browser.driver();
        HtmlElementLoader.populatePageObject(this, this.driver);
    }

    protected abstract BaseDialog ensureWeAreAt();

    public static <T extends BaseDialog> T at(Class<T> baseDialogClass) {
        try {
            T t = baseDialogClass.newInstance();
            t.ensureWeAreAt();
            return t;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Can't instantiate page cause: " + e.getMessage(), e);
        }
    }
}
