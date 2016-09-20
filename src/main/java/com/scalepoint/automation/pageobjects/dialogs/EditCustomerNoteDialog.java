package com.scalepoint.automation.pageobjects.dialogs;

import com.scalepoint.automation.pageobjects.pages.NotesPage;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.Window;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Button;

import static com.scalepoint.automation.utils.Wait.waitForVisible;

@EccPage
public class EditCustomerNoteDialog extends Page {

    private static final String URL = "webshop/jsp/matching_engine/dialog/edit_note_dialog.jsp";

    @FindBy(id = "_OK_button")
    private Button ok;

    @FindBy(tagName = "body")
    private WebElement note;

    @Override
    protected String getRelativeUrl() {
        return URL;
    }

    @Override
    public EditCustomerNoteDialog ensureWeAreOnPage() {
        Window.get().switchToLast();
        waitForUrl(URL);
        waitForVisible(note);
        return this;
    }

    public NotesPage addCustomerNote(String note){
        Wait.waitForLoaded();
        ((JavascriptExecutor)driver).executeScript("populateRichTextEditorWithText('noteHtmlEditorId-inputCmp-iframeEl', '"+ note+"');");
        driver.switchTo().defaultContent();
        Window.get().closeDialog(ok);
        return at(NotesPage.class);
    }
}
