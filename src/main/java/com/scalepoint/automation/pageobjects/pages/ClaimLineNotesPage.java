package com.scalepoint.automation.pageobjects.pages;

import com.scalepoint.automation.pageobjects.RequiresJavascriptHelpers;
import com.scalepoint.automation.pageobjects.extjs.ExtInput;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.annotations.page.ClaimSpecificPage;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;
import ru.yandex.qatools.htmlelements.element.Button;

import java.util.function.Consumer;

@EccPage
@ClaimSpecificPage
public class ClaimLineNotesPage extends BaseClaimPage implements RequiresJavascriptHelpers {

    @FindBy(id = "closeNotesButton")
    private WebElement closeNotesButton;

    @FindBy(xpath = "//div[@id='notesTreePanel']//div[contains(@class,'x-tree-elbow-empty')]")
    private WebElement claimLine;

    @FindBy(name = "note")
    private ExtInput noteTextArea;

    @FindBy(id = "addNoteButton")
    private Button addNoteButton;

    @FindBy(className = "copy-note-text")
    private WebElement copyNoteTextButton;

    @Override
    protected Page ensureWeAreOnPage() {
        Wait.waitForVisible(closeNotesButton);
        return this;
    }

    @Override
    protected String getRelativeUrl() {
        return "webshop/jsp/matching_engine/settlement.jsp";
    }

    public SettlementPage clickCloseWindow() {
        closeNotesButton.click();
        return at(SettlementPage.class);
    }

    public ClaimLineNotesPage clickAddNoteButton() {
        addNoteButton.click();
        return this;
    }

    public ClaimLineNotesPage clickClaimLine() {
        claimLine.click();
        return this;
    }

    public ClaimLineNotesPage clickCopyNoteTextButton() {
        copyNoteTextButton.click();
        return this;
    }

    public ClaimLineNotesPage pasteClipboardInNoteWindow() {
        noteTextArea.click();
        noteTextArea.sendKeys(Keys.CONTROL + "v");
        return this;
    }

    public ClaimLineNotesPage enterClaimLineNote(String note) {
        enterNote(note).
                clickAddNoteButton();
        return this;
    }


    private ClaimLineNotesPage enterNote(String note) {
        noteTextArea.setValue(note);
        return this;
    }

    public ClaimLineNotesPage doAssert(Consumer<Asserts> assertFunc) {
        assertFunc.accept(new Asserts());
        return ClaimLineNotesPage.this;
    }
    public class Asserts {
        public Asserts assertNoteIsSameAsInTextarea(String originalNoteText) {
            String text = noteTextArea.getText();
            Assert.assertTrue(originalNoteText.equals(text), "Copy note is not working");
            return this;
            //'\ue009'
        }
    }
}