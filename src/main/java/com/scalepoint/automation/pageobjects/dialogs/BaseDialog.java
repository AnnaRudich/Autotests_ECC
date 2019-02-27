package com.scalepoint.automation.pageobjects.dialogs;

import com.scalepoint.automation.Actions;
import com.scalepoint.automation.utils.threadlocal.Browser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import ru.yandex.qatools.htmlelements.loader.HtmlElementLoader;

import java.util.function.Consumer;

public abstract class BaseDialog implements Actions {

    protected Logger logger = LogManager.getLogger(BaseDialog.class);
    private static Logger innerLogger = LogManager.getLogger(BaseDialog.class);

    protected WebDriver driver;

    public BaseDialog() {
        this.driver = Browser.driver();
        HtmlElementLoader.populatePageObject(this, this.driver);
    }

    protected abstract BaseDialog ensureWeAreAt();

    protected boolean areWeAt() {
        return false;
    }

    public static <T extends BaseDialog> boolean isOn(Class<T> baseDialogClass) {
        try {
            T t = baseDialogClass.newInstance();
            return t.areWeAt();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Can't instantiate page cause: " + e.getMessage(), e);
        }
    }

    public static <T extends BaseDialog> T at(Class<T> baseDialogClass) {
        long start = System.currentTimeMillis();
        try {
            T t = baseDialogClass.newInstance();
            t.ensureWeAreAt();
            innerLogger.info("At {} -> {} ms.", baseDialogClass.getSimpleName(), (System.currentTimeMillis() - start));
            return t;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Can't instantiate page cause: " + e.getMessage(), e);
        }
    }

    public <T extends BaseDialog> T apply(Class<T> currentClass, Consumer<T> func) {
        func.accept((T) this);
        return at(currentClass);
    }
}
