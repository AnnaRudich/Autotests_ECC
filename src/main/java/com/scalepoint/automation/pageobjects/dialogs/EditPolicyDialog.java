package com.scalepoint.automation.pageobjects.dialogs;

import com.codeborne.selenide.Condition;
import com.scalepoint.automation.pageobjects.extjs.ExtComboBoxBoundView;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompleted;

@EccPage
public class EditPolicyDialog extends BaseDialog {

    @FindBy(id = "edit-policy-ok-button")
    private WebElement ok;

    @FindBy(id = "edit-policy-combo")
    private ExtComboBoxBoundView policiesCombo;

    @Override
    protected void ensureWeAreAt() {
        waitForAjaxCompleted();
        $(policiesCombo).waitUntil(Condition.visible, TIME_OUT_IN_MILISECONDS);
    }

    public SettlementPage chooseAny() {
        policiesCombo.select(1);
        ok.click();
        return Page.at(SettlementPage.class);
    }

    public SettlementPage choose(String policyTypeValue) {
        policiesCombo.select(policyTypeValue);
        ok.click();
        return Page.at(SettlementPage.class);
    }
}
