package com.scalepoint.automation.pageobjects.dialogs;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.support.FindBy;

import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;

public class RemoveClaimLineNoteWarningDialog extends BaseDialogSelenide{

    @FindBy(css = ".x-window")
    private SelenideElement removeClaimLineNoteWarningDialog;

    @Override
    protected void ensureWeAreAt() {

        waitForAjaxCompletedAndJsRecalculation();
        removeClaimLineNoteWarningDialog.should(Condition.visible);
    }

    public ClaimLineNotesDialog confirm(){

        clickButton(DialogButton.YES);
        return BaseDialogSelenide.at(ClaimLineNotesDialog.class);
    }

    public ClaimLineNotesDialog cancel(){

        clickButton(DialogButton.NO);
        return BaseDialogSelenide.at(ClaimLineNotesDialog.class);
    }
}
