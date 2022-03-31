package com.scalepoint.automation.pageobjects.pages;

import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.pageobjects.RequiresJavascriptHelpers;
import com.scalepoint.automation.pageobjects.dialogs.AddInternalNoteDialog;
import com.scalepoint.automation.pageobjects.dialogs.BaseDialog;
import com.scalepoint.automation.pageobjects.dialogs.EditCustomerNoteDialog;
import com.scalepoint.automation.utils.annotations.page.ClaimSpecificPage;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import org.openqa.selenium.By;
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

    @FindBy(id = "pending_notes_area")
    private SelenideElement customerNote;
    @FindBy(xpath = "//span[@class='author']/following-sibling::span")
    private SelenideElement internalNote;
    @FindBy(xpath = "//div[contains(@class,'table-header')]/span[contains(text(),'Interne noter')]")
    private SelenideElement headerInternalNote;

    private Button getEditCustomerNote(){

        return new Button($(By.id("btnEditCustomerNote")));
    }

    private Button getAddInternalNote(){

        return new Button($(By.id("btnAddInternalNote")));
    }

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

        verifyElementVisible($(getEditCustomerNote()));
        getEditCustomerNote().click();
        return BaseDialog.at(EditCustomerNoteDialog.class);
    }

    public NotesPage addInternalNote(String note) {

        verifyElementVisible($(getEditCustomerNote()));
        $(getEditCustomerNote()).click();
        return BaseDialog.at(AddInternalNoteDialog.class).addInternalNote(note, NotesPage.class);
    }

    public NotesPage addCustomerNote(String customerNote) {
        return editCustomerNote().
                addCustomerNote(customerNote);
    }

    public boolean isCustomerNotesPresent(String _customerNote) {

        verifyElementVisible(customerNote);
        return customerNote.getText().contains(_customerNote);
    }

    public boolean isInternalNotesPresent(String _internalNote) {

        verifyElementVisible(internalNote);
        return internalNote.getText().contains(_internalNote);
    }

    public boolean isEditCustomerNoteButtonPresent() {

        return getEditCustomerNote().isDisplayed();
    }

    public boolean isAddInternalNoteButtonPresent() {

        return verifyElementVisible($(getEditCustomerNote()));
    }

    public boolean isInternalNotePresent() {

        return internalNote.isDisplayed();
    }

    public boolean isAddInternalNoteButtonDisplayed() {

        return getAddInternalNote().isDisplayed();
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
