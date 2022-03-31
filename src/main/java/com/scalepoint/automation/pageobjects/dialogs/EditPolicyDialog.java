package com.scalepoint.automation.pageobjects.dialogs;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.pageobjects.extjs.ExtComboBoxBoundView;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompleted;

@EccPage
public class EditPolicyDialog extends BaseDialog {

    @FindBy(id = "edit-policy-ok-button")
    private SelenideElement ok;

    private ExtComboBoxBoundView getPoliciesCombo(){

        return new ExtComboBoxBoundView($(By.id("edit-policy-combo")));
    }

    @Override
    protected void ensureWeAreAt() {

        waitForAjaxCompleted();
        getPoliciesCombo().should(Condition.visible);
    }

    public SettlementPage chooseAny() {

        getPoliciesCombo().select(1);
        ok.click();
        return Page.at(SettlementPage.class);
    }

    public SettlementPage choose(String policyTypeValue) {

        getPoliciesCombo().select(policyTypeValue);
        ok.click();
        return Page.at(SettlementPage.class);
    }
}
