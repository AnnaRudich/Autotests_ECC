package com.scalepoint.automation.pageobjects.pages;

import com.codeborne.selenide.Condition;
import com.scalepoint.automation.pageobjects.dialogs.BaseDialog;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompleted;

public class ReopenClaimDialog extends BaseDialog {

    @Override
    protected void ensureWeAreAt() {

        waitForAjaxCompleted();
        $(By.id("reopen-claim-button")).shouldBe(Condition.visible);
    }

    public SettlementPage reopenClaim() {

        $(By.id("reopen-claim-button")).click();
        return Page.at(SettlementPage.class);
    }

    public SettlementPage viewClaim(){

        $(By.id("view-claim-button")).click();
        return Page.at(SettlementPage.class);
    }

    public CustomerDetailsPage cancelOpenClaim(){

        $(By.id("cancel-open-claim-button")).click();
        return Page.at(CustomerDetailsPage.class);
    }


}
