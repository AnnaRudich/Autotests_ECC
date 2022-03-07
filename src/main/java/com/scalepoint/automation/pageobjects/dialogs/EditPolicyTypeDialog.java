package com.scalepoint.automation.pageobjects.dialogs;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.pageobjects.extjs.ExtComboBoxDivBoundList;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.FindBy;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;

public class EditPolicyTypeDialog extends BaseDialogSelenide {

    @FindBy(css = ".x-window")
    private SelenideElement editPolicyTypeDialog;

    private ExtComboBoxDivBoundList getPolicyTypeCombobox(){

        return new ExtComboBoxDivBoundList($(By.id("edit-policy-combo")));
    }

    @Override
    protected void ensureWeAreAt() {

        waitForAjaxCompletedAndJsRecalculation();
        editPolicyTypeDialog.should(Condition.visible);
    }

    @Override
    protected boolean areWeAt() {

        try {

            return getPolicyTypeCombobox().isDisplayed();
        } catch (NoSuchElementException e){

            return false;
        }
    }

    public SettlementPage confirm(){

        clickButton(DialogButton.OK);
        return Page.at(SettlementPage.class);
    }

    public SettlementPage cancel(){

        clickButton(DialogButton.ABORT);
        return Page.at(SettlementPage.class);
    }

    public EditPolicyTypeDialog selectPolicyType(String policyType){

        getPolicyTypeCombobox().select(policyType);
        return this;
    }
}
