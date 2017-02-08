package com.scalepoint.automation.pageobjects.dialogs;

import com.scalepoint.automation.pageobjects.extjs.ExtInput;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Button;

import static com.scalepoint.automation.utils.Wait.waitForVisible;

@EccPage
public class AddInternalNoteDialogOld extends Page {

    @FindBy(name = "internal_note")
    private ExtInput internalNote;

    @FindBy(id = "_OK_button")
    private Button ok;

    @Override
    protected String getRelativeUrl() {
        return "webshop/jsp/matching_engine/dialog/add_note_dialog.jsp";
    }

    @Override
    public AddInternalNoteDialogOld ensureWeAreOnPage() {
        switchToLast();
        waitForUrl(getRelativeUrl());
        waitForVisible(internalNote);
        return this;
    }

    public <T extends Page> T addInternalNote(String note, Class<T> pageClass) {
        waitForVisible(internalNote);
        internalNote.sendKeys(note);
        closeDialog(ok);
        return Page.at(pageClass);
    }
}