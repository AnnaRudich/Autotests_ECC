package com.scalepoint.automation.pageobjects.dialogs;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.support.FindBy;

import static com.codeborne.selenide.Selenide.$$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;

public class GdprConfirmationDialog extends BaseDialogSelenide {

    @FindBy(css = ".x-message-box")
    private SelenideElement gdprDialog;

    @Override
    protected void ensureWeAreAt() {

        waitForAjaxCompletedAndJsRecalculation();
        gdprDialog.should(Condition.visible);
    }

    public void confirm(){
        $$(".x-message-box a[role=button]").get(0).click();
    }
}
