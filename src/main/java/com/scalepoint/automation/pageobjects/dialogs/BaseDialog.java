package com.scalepoint.automation.pageobjects.dialogs;

import com.codeborne.selenide.ElementsCollection;
import com.scalepoint.automation.Actions;
import com.scalepoint.automation.utils.threadlocal.Browser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.page;

public abstract class BaseDialog implements Actions {

    protected Logger logger = LogManager.getLogger(BaseDialog.class);
    private static Logger innerLogger = LogManager.getLogger(BaseDialog.class);

    protected WebDriver driver;

    public BaseDialog() {

        this.driver = Browser.driver();
    }

    protected abstract void ensureWeAreAt();

    protected boolean areWeAt() {
        return false;
    }

    public static <T extends BaseDialog> boolean isOn(Class<T> baseDialogClass) {
        try {

            T t = page(baseDialogClass);
            return t.areWeAt();
        } catch (Exception e) {

            throw new RuntimeException("Can't instantiate page cause: " + e.getMessage(), e);
        }
    }

    public static <T extends BaseDialog> T at(Class<T> baseDialogClass) {

        long start = System.currentTimeMillis();
        try {

            T t = page(baseDialogClass);
            t.ensureWeAreAt();
            innerLogger.info("At {} -> {} ms.", baseDialogClass.getSimpleName(), (System.currentTimeMillis() - start));
            return t;
        } catch (Exception e) {

            throw new RuntimeException("Can't instantiate page cause: " + e.getMessage(), e);
        }
    }

    public <T extends BaseDialog> T apply(Class<T> currentClass, Consumer<T> func) {

        func.accept((T) this);
        return at(currentClass);
    }

    protected void clickButton(DialogButton button){

        ElementsCollection selenideElement = $$(".x-window a[role=button][aria-hidden=false]");
        selenideElement.stream()
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
        ABORT("Annuller"),
        UNDEFINED("");

        private String text;

        DialogButton(String text) {

            this.text = text;
        }

        public static DialogButton findByText(String text) {

            return Arrays.stream(DialogButton.values())
                    .filter(button -> button.text.equals(text))
                    .findFirst()
                    .orElse(DialogButton.UNDEFINED);
        }
    }
}
