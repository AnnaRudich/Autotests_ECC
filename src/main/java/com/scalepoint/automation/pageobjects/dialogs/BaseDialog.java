package com.scalepoint.automation.pageobjects.dialogs;

import com.scalepoint.automation.Actions;
import com.scalepoint.automation.utils.threadlocal.Browser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import ru.yandex.qatools.htmlelements.loader.HtmlElementLoader;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

import static com.codeborne.selenide.Selenide.$$;

public abstract class BaseDialog implements Actions {

    protected Logger logger = LogManager.getLogger(BaseDialog.class);
    private static Logger innerLogger = LogManager.getLogger(BaseDialog.class);

    protected WebDriver driver;

    public BaseDialog() {
        this.driver = Browser.driver();
        HtmlElementLoader.populatePageObject(this, this.driver);
    }

    protected abstract void ensureWeAreAt();

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

    protected void clickButton(DialogButton button){

        $$(".x-window a[role=button][aria-hidden=false]").stream()
                .filter(element -> DialogButton.findByText(element.getText()).equals(button))
                .findFirst()
                .orElseThrow(NoSuchElementException::new)
                .hover()
                .click();
    }

    protected enum DialogButton {

        OK("OK"),
        CANCEL("Fortryd"),
        YES("Ja"),
        NO("Nej"),
        SAVE("Gem"),
        ABORT("Annuller");

        private String text;

        DialogButton(String text) {

            this.text = text;
        }

        public static DialogButton findByText(String text) {

            return Arrays.stream(DialogButton.values())
                    .filter(button -> button.text.equals(text))
                    .findFirst()
                    .orElseThrow(NoSuchElementException::new);
        }
    }
}
