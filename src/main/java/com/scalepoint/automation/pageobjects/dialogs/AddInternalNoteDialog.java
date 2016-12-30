package com.scalepoint.automation.pageobjects.dialogs;

import com.scalepoint.automation.pageobjects.pages.NotesPage;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static com.scalepoint.automation.utils.Wait.waitForVisible;

@EccPage
public class AddInternalNoteDialog extends BaseDialog {

    @FindBy(name = "addNoteTextArea-inputEl")
    private WebElement internalNote;

    @FindBy(id = "addCustomerNoteOkButton")
    private WebElement ok;

    @Override
    public BaseDialog ensureWeAreAt() {
        waitForVisible(internalNote);
        return this;
    }

    public <T extends Page> T addInternalNote(String note, Class<T> pageClass) {
        internalNote.sendKeys(note);
        ok.click();
        return Page.at(pageClass);
    }
}
