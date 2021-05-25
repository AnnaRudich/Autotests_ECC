package com.scalepoint.automation.pageobjects.dialogs;

import com.codeborne.selenide.Condition;
import com.scalepoint.automation.pageobjects.extjs.ExtComboBoxDivBoundList;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;

public class EditPolicyTypeDialog extends BaseDialog{

    @FindBy(css = ".x-window")
    private WebElement editPolicyTypeDialog;


    @FindBy(id = "edit-policy-combo")
    private ExtComboBoxDivBoundList policyTypeCombobox;

    @Override
    protected void ensureWeAreAt() {

        waitForAjaxCompletedAndJsRecalculation();
        $(editPolicyTypeDialog).waitUntil(Condition.visible, 5000);
    }

    @Override
    protected boolean areWeAt() {

        try {

            return policyTypeCombobox.isDisplayed();
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

        policyTypeCombobox.select(policyType);
        return this;
    }
}
