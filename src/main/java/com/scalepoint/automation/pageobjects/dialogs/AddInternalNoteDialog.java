package com.scalepoint.automation.pageobjects.dialogs;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import org.openqa.selenium.support.FindBy;

import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;

@EccPage
public class AddInternalNoteDialog extends BaseDialogSelenide {

    @FindBy(name = "addNoteTextArea-inputEl")
    private SelenideElement internalNote;

    @FindBy(id = "addCustomerNoteOkButton")
    private SelenideElement ok;

    @Override
    protected void ensureWeAreAt() {

        waitForAjaxCompletedAndJsRecalculation();
        internalNote.should(Condition.visible);
    }

    public <T extends Page> T addInternalNote(String note, Class<T> pageClass) {

        internalNote.setValue(note);
        ok.click();
        return Page.at(pageClass);
    }
}
