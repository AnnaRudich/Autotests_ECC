package com.scalepoint.automation.pageobjects.dialogs;

import com.codeborne.selenide.Condition;
import com.scalepoint.automation.pageobjects.pages.CompleteClaimPage;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.Arrays;
import java.util.NoSuchElementException;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;

public class DeductibleWarningDialog extends BaseDialog{

    @FindBy(css = ".x-message-box")
    private WebElement deductibleWarningDialog;

    @Override
    protected void ensureWeAreAt() {

        waitForAjaxCompletedAndJsRecalculation();
        $(deductibleWarningDialog).waitUntil(Condition.visible, 5000);
    }

    public CompleteClaimPage confirm(){

        clickButton(Button.OK);
        return Page.at(CompleteClaimPage.class);
    }

    public SettlementPage cancel(){

        clickButton(Button.CANCEL);
        return Page.at(SettlementPage.class);
    }

    private void clickButton(Button button){

        $$(".x-message-box a[role=button][aria-hidden=false]").stream()
                .filter(element -> Button.findByText(element.getText()).equals(button))
                .findFirst()
                .orElseThrow(NoSuchElementException::new)
                .hover()
                .click();
    }

    private enum Button {

        OK("OK"),
        CANCEL("Fortryd");

        private String text;

        Button(String text) {

            this.text = text;
        }

        public static Button findByText(String text) {

            return Arrays.stream(Button.values())
                    .filter(button -> button.text.equals(text))
                    .findFirst()
                    .orElseThrow(NoSuchElementException::new);
        }
    }
}
