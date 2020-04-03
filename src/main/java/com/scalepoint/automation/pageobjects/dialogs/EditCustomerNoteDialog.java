package com.scalepoint.automation.pageobjects.dialogs;

import com.codeborne.selenide.Condition;
import com.scalepoint.automation.pageobjects.pages.NotesPage;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;

@EccPage
public class EditCustomerNoteDialog extends BaseDialog {

    @FindBy(id = "editCustomerNoteOkButton")
    private WebElement ok;

    @Override
    protected void ensureWeAreAt() {
        waitForAjaxCompletedAndJsRecalculation();
       $(ok).waitUntil(Condition.visible, TIME_OUT_IN_MILISECONDS);
    }

    public NotesPage addCustomerNote(String note) {
        ((JavascriptExecutor) driver).executeScript("populateRichTextEditorWithText('noteContainer', '" + note + "');");
        driver.switchTo().defaultContent();
        ok.click();
        return Page.at(NotesPage.class);
    }
}
