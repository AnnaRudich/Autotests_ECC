package com.scalepoint.automation.pageobjects.dialogs;

import com.codeborne.selenide.Condition;
import com.scalepoint.automation.pageobjects.extjs.ExtText;
import com.scalepoint.automation.utils.Wait;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.TextInput;

import java.util.function.Consumer;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;
import static org.testng.Assert.assertEquals;

public class EditVoucherValuationDialog extends BaseDialog {

    @FindBy(name = "edit-voucher-brands-text-inputEl")
    private TextInput brandsText;

    @FindBy(id = "edit-voucher-tags-text-inputEl")
    private WebElement tags;

    @FindBy(id = "edit-voucher-ok-button")
    private WebElement ok;

    @FindBy(id = "edit-voucher-terms-button")
    private Button termsAndConditions;

    @FindBy(id = "edit-voucher-to-customer-input")
    private ExtText discountDistribution;

    @FindBy(id = "edit-voucher-face-value-input-inputEl")
    private ExtText voucherFaceValue;

    @FindBy(id = "edit-voucher-cash-value-input-inputEl")
    private ExtText voucherCashValue;

    @FindBy(id = "edit-voucher-to-company-input-inputEl")
    private ExtText companyPercentageInput;

    @FindBy(id = "edit-voucher-to-customer-input-inputEl")
    private ExtText customerPercentageInput;

    @FindBy(id = "edit-voucher-rebate-display-inputEl")
    private WebElement voucherRebate;

    @FindBy(id = "edit-voucher-distribution-button")
    private WebElement editDistributionButton;

    @Override
    protected void ensureWeAreAt() {
        waitForAjaxCompletedAndJsRecalculation();
        $(ok).waitUntil(Condition.visible, TIME_OUT_IN_MILISECONDS);
        $(voucherFaceValue).waitUntil(Condition.visible, TIME_OUT_IN_MILISECONDS);
    }

    public EditVoucherValuationDialog updatePercentage(DistributeTo distributeTo, Integer percentage) {
        if (DistributeTo.COMPANY.equals(distributeTo)) {
            companyPercentageInput.sendKeys(Keys.HOME, Keys.chord(Keys.SHIFT, Keys.END), percentage.toString());
        } else {
            customerPercentageInput.enter(percentage.toString());
        }
        return this;
    }

    public EditDiscountDistributionDialog updatePercentageFromDialog(DistributeTo distributeTo, Integer percentage) {
        hoverAndClick($(editDistributionButton));
        return BaseDialog.at(EditDiscountDistributionDialog.class).updatePercentage(distributeTo, percentage);
    }

    public String getBrands() {
        return $(By.name("edit-voucher-brands-text-inputEl")).getValue();
    }

    public String getTags() {
        return $(tags).getValue();
    }

    public SettlementDialog closeDialogWithOk() {
        hoverAndClick($(ok));
        return at(SettlementDialog.class);
    }

    public VoucherTermsAndConditionsDialog openTermsAndConditions() {
        termsAndConditions.click();
        return BaseDialogSelenide.at(VoucherTermsAndConditionsDialog.class);
    }

    public EditVoucherValuationDialog discountDistribution(String discountDistributionValue) {
        discountDistribution.enter(discountDistributionValue);
        return this;
    }

    public SettlementDialog saveVoucherValuation() {
        hoverAndClick($(ok));
        return at(SettlementDialog.class);
    }

    public String getVoucherCashValue() {
        return voucherCashValue.getText();
    }


    public Integer getVoucherPercentage() {
        Wait.waitForAjaxCompleted();
        String text = voucherRebate.getText();
        return Integer.valueOf(text.replaceAll("([0-9]+),.*", "$1"));
    }

    public enum DistributeTo {
        CUSTOMER,
        COMPANY
    }

    public EditVoucherValuationDialog doAssert(Consumer<Asserts> assertFunc) {
        assertFunc.accept(new Asserts());
        return EditVoucherValuationDialog.this;
    }

    public class Asserts {
        public Asserts assertBrandsTextIs(String expectedBrandsText) {
            assertEquals(getBrands(), expectedBrandsText, "Wrong Brand is displayed.");
            return this;
        }

        public Asserts assertTagsTextIs(String expectedTagsText) {
            assertEquals(getTags(), expectedTagsText, "Wrong Tags are displayed");
            return this;
        }
    }

}

