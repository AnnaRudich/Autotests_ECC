package com.scalepoint.automation.pageobjects.dialogs;

import com.codeborne.selenide.Condition;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;

public class RemoveClaimLineNoteWarningDialog extends BaseDialog{

    @FindBy(css = ".x-window")
    private WebElement removeClaimLineNoteWarningDialog;

    @Override
    protected void ensureWeAreAt() {

        waitForAjaxCompletedAndJsRecalculation();
        $(removeClaimLineNoteWarningDialog).waitUntil(Condition.visible, 5000);
    }

    public ClaimLineNotesDialog confirm(){

        clickButton(DialogButton.YES);
        return BaseDialog.at(ClaimLineNotesDialog.class);
    }

    public ClaimLineNotesDialog cancel(){

        clickButton(DialogButton.NO);
        return BaseDialog.at(ClaimLineNotesDialog.class);
    }
}
