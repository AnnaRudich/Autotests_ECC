package com.scalepoint.automation.pageobjects.dialogs;

import com.codeborne.selenide.Condition;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;

@EccPage
public class AddInternalNoteDialog extends BaseDialog {

    @FindBy(name = "addNoteTextArea-inputEl")
    private WebElement internalNote;

    @FindBy(id = "addCustomerNoteOkButton")
    private WebElement ok;

    @Override
    protected void ensureWeAreAt() {
        waitForAjaxCompletedAndJsRecalculation();
        $(internalNote).waitUntil(Condition.visible, TIME_OUT_IN_MILISECONDS);
    }

    public <T extends Page> T addInternalNote(String note, Class<T> pageClass) {
        internalNote.sendKeys(note);
        ok.click();
        return Page.at(pageClass);
    }
}
