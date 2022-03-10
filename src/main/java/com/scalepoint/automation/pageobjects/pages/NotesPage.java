package com.scalepoint.automation.pageobjects.pages;

import com.scalepoint.automation.pageobjects.RequiresJavascriptHelpers;
import com.scalepoint.automation.pageobjects.dialogs.AddInternalNoteDialog;
import com.scalepoint.automation.pageobjects.dialogs.BaseDialog;
import com.scalepoint.automation.pageobjects.dialogs.BaseDialogSelenide;
import com.scalepoint.automation.pageobjects.dialogs.EditCustomerNoteDialog;
import com.scalepoint.automation.utils.annotations.page.ClaimSpecificPage;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;
import ru.yandex.qatools.htmlelements.element.Button;

import java.util.function.Consumer;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.verifyElementVisible;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;

@EccPage
@ClaimSpecificPage
public class NotesPage extends BaseClaimPage implements RequiresJavascriptHelpers {

    @FindBy(id = "btnEditCustomerNote")
    private Button editCustomerNote;

    @FindBy(id = "btnAddInternalNote")
    private Button addInternalNote;

    @FindBy(id = "pending_notes_area")
    private WebElement customerNote;

    @FindBy(xpath = "//span[@class='author']/following-sibling::span")
    private WebElement internalNote;

    @FindBy(xpath = "//div[contains(@class,'table-header')]/span[contains(text(),'Interne noter')]")
    private WebElement headerInternalNote;

    @Override
    protected String getRelativeUrl() {
        return "webshop/jsp/matching_engine/notes.jsp";
    }

    @Override
    protected void ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
        waitForAjaxCompletedAndJsRecalculation();
    }

    public EditCustomerNoteDialog editCustomerNote() {
        verifyElementVisible($(editCustomerNote));
        editCustomerNote.click();
        return BaseDialog.at(EditCustomerNoteDialog.class);
    }

    public NotesPage addInternalNote(String note) {
        verifyElementVisible($(addInternalNote));
        $(addInternalNote).click();
        return BaseDialogSelenide.at(AddInternalNoteDialog.class).addInternalNote(note, NotesPage.class);
    }

    public NotesPage addCustomerNote(String customerNote) {
        return editCustomerNote().
                addCustomerNote(customerNote);
    }

    public boolean isCustomerNotesPresent(String _customerNote) {
        verifyElementVisible($(customerNote));
        return customerNote.getText().contains(_customerNote);
    }

    public boolean isInternalNotesPresent(String _internalNote) {
        verifyElementVisible($(internalNote));
        return internalNote.getText().contains(_internalNote);
    }

    public boolean isEditCustomerNoteButtonPresent() {
        return editCustomerNote.isDisplayed();
    }

    public boolean isAddInternalNoteButtonPresent() {

        return verifyElementVisible($(addInternalNote));
    }

    public boolean isInternalNotePresent() {
        return internalNote.isDisplayed();
    }

    public boolean isAddInternalNoteButtonDisplayed() {
        return addInternalNote.isDisplayed();
    }

    public NotesPage doAssert(Consumer<Asserts> assertFunc) {
        assertFunc.accept(new Asserts());
        return NotesPage.this;
    }

    public class Asserts {
        public Asserts assertCustomerNotePresent(String customerNote) {
            Assert.assertTrue(isCustomerNotesPresent(customerNote), errorMessage("Customer Note has not been added"));
            return this;
        }

        public Asserts assertInternalNotePresent(String internalNote) {
            Assert.assertTrue(isInternalNotesPresent(internalNote), errorMessage("Internal Note has not been added"));
            return this;
        }

        public Asserts assertEditCustomerNoteButtonPresent() {
            Assert.assertTrue(isEditCustomerNoteButtonPresent(), errorMessage("Edit Customer Note button is not visible"));
            return this;
        }

        public Asserts assertInternalNoteButtonNotPresent() {
            Assert.assertFalse(isAddInternalNoteButtonPresent(), errorMessage("Add Internal Note button is visible"));
            return this;
        }

        public Asserts assertInternalNoteFieldsPresent() {
            Assert.assertTrue(isInternalNotePresent(), errorMessage("Internal Note field is not visible"));
            Assert.assertTrue(isAddInternalNoteButtonDisplayed(), errorMessage("Add Internal Note button is not visible"));
            return this;
        }
    }
}
