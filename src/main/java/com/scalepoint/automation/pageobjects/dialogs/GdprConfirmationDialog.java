package com.scalepoint.automation.pageobjects.dialogs;

import com.codeborne.selenide.Condition;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;

public class GdprConfirmationDialog extends BaseDialog {
    @FindBy(css = ".x-message-box")
    private WebElement gdprDialog;

    @Override
    protected void ensureWeAreAt() {
        waitForAjaxCompletedAndJsRecalculation();
        $(gdprDialog).waitUntil(Condition.visible, 5000);
    }

    public void confirm(){
        $$(".x-message-box a[role=button]").get(0).click();
    }
}
