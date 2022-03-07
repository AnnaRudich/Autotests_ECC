package com.scalepoint.automation.pageobjects.dialogs;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.pageobjects.pages.NotesPage;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.FindBy;

import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;

@EccPage
public class EditCustomerNoteDialog extends BaseDialogSelenide {

    @FindBy(id = "editCustomerNoteOkButton")
    private SelenideElement ok;

    @Override
    protected void ensureWeAreAt() {

        waitForAjaxCompletedAndJsRecalculation();
        ok.should(Condition.visible);
    }

    public NotesPage addCustomerNote(String note) {

        ((JavascriptExecutor) driver).executeScript("populateRichTextEditorWithText('noteContainer', '" + note + "');");
        driver.switchTo().defaultContent();
        ok.click();
        return Page.at(NotesPage.class);
    }
}
