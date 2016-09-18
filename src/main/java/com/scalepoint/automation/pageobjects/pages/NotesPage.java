package com.scalepoint.automation.pageobjects.pages;

import com.scalepoint.automation.pageobjects.dialogs.AddInternalNoteDialog;
import com.scalepoint.automation.pageobjects.dialogs.EditCustomerNoteDialog;
import com.scalepoint.automation.pageobjects.modules.MainMenu;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.Window;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Button;

@EccPage
public class NotesPage extends BaseClaimPage {

    private static final String URL = "webshop/jsp/matching_engine/notes.jsp";

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

    private MainMenu mainMenu = new MainMenu();

    @Override
    protected String geRelativeUrl() {
        return URL;
    }

    @Override
    public NotesPage ensureWeAreOnPage() {
        waitForUrl(URL);
        return this;
    }

    public EditCustomerNoteDialog editCustomerNote() {
        Wait.waitForLoaded();
        Wait.waitForVisible(editCustomerNote);
        Window.get().openDialogWithJavascriptHelper(editCustomerNote);
        return at(EditCustomerNoteDialog.class);
    }

    public NotesPage addInternalNote(String note) {
        Wait.waitForVisible(addInternalNote);
        Window.get().openDialog(addInternalNote);
        Wait.waitForLoaded();
        new AddInternalNoteDialog().addInternalNote(note);
        return this;
    }

    public boolean isCustomerNotesPresent(String _customerNote) {
        Wait.waitForLoaded();
        Wait.waitForVisible(customerNote);
        return customerNote.getText().contains(_customerNote);
    }

    public boolean isInternalNotesPresent(String _internalNote) {
        Wait.waitForLoaded();
        Wait.waitForVisible(internalNote);
        return internalNote.getText().contains(_internalNote);
    }

    public boolean isEditCustomerNoteButtonPresent() {
        Wait.waitForLoaded();
        return editCustomerNote.isDisplayed();
    }

    public boolean isAddInternalNoteButtonPresent() {
        try {
            Wait.waitForInvisible(addInternalNote);
            return false;
        } catch (NoSuchElementException e) {
            return true;
        }
    }

    public boolean isInternalNoteHeaderPresent() {
        try {
            Wait.waitForInvisible(headerInternalNote);
            return false;
        } catch (NoSuchElementException e) {
            return true;
        }
    }

    public boolean isInternalNotePresent() {
        return internalNote.isDisplayed();
    }

    public boolean isAddInternalNoteButtonDisplayed() {
        return addInternalNote.isDisplayed();
    }
}
