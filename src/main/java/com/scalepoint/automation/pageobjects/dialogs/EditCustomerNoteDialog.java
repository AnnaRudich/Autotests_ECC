package com.scalepoint.automation.pageobjects.dialogs;

import com.scalepoint.automation.pageobjects.pages.NotesPage;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static com.scalepoint.automation.utils.Wait.waitForVisible;

@EccPage
public class EditCustomerNoteDialog extends BaseDialog {

    @FindBy(id = "editCustomerNoteOkButton")
    private WebElement ok;

    @Override
    protected BaseDialog ensureWeAreAt() {
        waitForVisible(ok);
        return this;
    }

    public NotesPage addCustomerNote(String note) {
        ((JavascriptExecutor) driver).executeScript("populateRichTextEditorWithText('noteContainer', '" + note + "');");
         driver.switchTo().defaultContent();
        ok.click();
        return Page.at(NotesPage.class);
    }
}
