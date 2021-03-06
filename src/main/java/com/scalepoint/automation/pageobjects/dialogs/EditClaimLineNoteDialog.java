package com.scalepoint.automation.pageobjects.dialogs;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;

import java.util.function.Consumer;

import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;
import static org.assertj.core.api.Assertions.assertThat;

public class EditClaimLineNoteDialog extends BaseDialog {

    private By textAreaPath = By.cssSelector("[role=textbox]");

    @FindBy(xpath = "//div[text()='Rediger note']/../../../../..")
    private SelenideElement elementEditClaimLineNoteDialogElement;

    @Override
    protected void ensureWeAreAt() {

        waitForAjaxCompletedAndJsRecalculation();
        elementEditClaimLineNoteDialogElement.should(Condition.visible);
    }

    public ClaimLineNotesDialog confirm(){

        clickButton(DialogButton.SAVE);
        return BaseDialog.at(ClaimLineNotesDialog.class);
    }

    public ClaimLineNotesDialog cancel(){

        clickButton(DialogButton.ABORT);
        return BaseDialog.at(ClaimLineNotesDialog.class);
    }

    public EditClaimLineNoteDialog enterNote(String text){

        elementEditClaimLineNoteDialogElement.find(textAreaPath).setValue(text);
        return this;
    }

    private String getNoteText(){

        return elementEditClaimLineNoteDialogElement.find(textAreaPath).getValue();
    }

    public EditClaimLineNoteDialog doAssert(Consumer<Asserts> assertFunc) {

        assertFunc.accept(new Asserts());
        return EditClaimLineNoteDialog.this;
    }

    public class Asserts {

        public Asserts assertNoteText(String originalNoteText) {

            String noteText = getNoteText();
            assertThat(originalNoteText.equals(noteText)).
                    as("Note text is: " + noteText + "but should be the same as original note text: " + originalNoteText).isTrue();
            return this;
        }
    }
}