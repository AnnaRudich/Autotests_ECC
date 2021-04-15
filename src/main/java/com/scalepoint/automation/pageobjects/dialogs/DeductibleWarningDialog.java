package com.scalepoint.automation.pageobjects.dialogs;

import com.codeborne.selenide.Condition;
import com.scalepoint.automation.pageobjects.pages.CompleteClaimPage;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;

public class DeductibleWarningDialog extends BaseDialog{

    @FindBy(css = ".x-window")
    private WebElement deductibleWarningDialog;

    @Override
    protected void ensureWeAreAt() {

        waitForAjaxCompletedAndJsRecalculation();
        $(deductibleWarningDialog).waitUntil(Condition.visible, 5000);
    }

    public CompleteClaimPage confirm(){

        clickButton(DialogButton.OK);
        return Page.at(CompleteClaimPage.class);
    }

    public SettlementPage cancel(){

        clickButton(DialogButton.CANCEL);
        return Page.at(SettlementPage.class);
    }
}
