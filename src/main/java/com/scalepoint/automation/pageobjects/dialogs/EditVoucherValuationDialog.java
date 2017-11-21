package com.scalepoint.automation.pageobjects.dialogs;

import com.scalepoint.automation.pageobjects.extjs.ExtInput;
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
import static org.testng.Assert.assertEquals;

public class EditVoucherValuationDialog extends BaseDialog {

    @FindBy(name = "edit-voucher-brands-text-inputEl")
    private TextInput brandsText;

    @FindBy(id = "edit-voucher-tags-text-inputEl")
    private ExtInput tags;

    @FindBy(id = "edit-voucher-ok-button")
    private Button ok;

    @FindBy(id = "edit-voucher-terms-button")
    private Button termsAndConditions;

    @FindBy(id = "edit-voucher-to-customer-input")
    private ExtText discountDistribution;

    @FindBy(id = "edit-voucher-face-value-input-inputEl")
    private ExtText voucherFaceValue;

    @FindBy(id = "edit-voucher-cash-value-input-inputEl")
    private ExtText voucherCashValue;

    @FindBy(id = "edit-voucher-to-company-input-inputEl")
    private WebElement companyPercentageInput;

    @FindBy(id = "edit-voucher-to-customer-input-inputEl")
    private WebElement customerPercentageInput;

    @FindBy(id = "edit-voucher-rebate-display-inputEl")
    private WebElement voucherRebate;

    @FindBy(id = "edit-voucher-distribution-button")
    private WebElement editDistributionButton;

    @Override
    protected BaseDialog ensureWeAreAt() {
        Wait.waitForVisible(ok);
        Wait.waitForVisible(voucherFaceValue);
        return null;
    }

    public EditVoucherValuationDialog updatePercentage(DistributeTo distributeTo, Integer percentage) {
        if (DistributeTo.COMPANY.equals(distributeTo)) {
            companyPercentageInput.sendKeys(Keys.HOME, Keys.chord(Keys.SHIFT, Keys.END), percentage.toString());
        } else {
            customerPercentageInput.sendKeys(Keys.HOME, Keys.chord(Keys.SHIFT, Keys.END), percentage.toString());
        }
        return this;
    }

    public EditDiscountDistributionDialog updatePercentageFromDialog(DistributeTo distributeTo, Integer percentage) {
        clickUsingJsIfSeleniumClickReturnError(editDistributionButton);
        return BaseDialog.at(EditDiscountDistributionDialog.class).updatePercentage(distributeTo, percentage);
    }

    public String getBrands() {
        return $(By.name("edit-voucher-brands-text-inputEl")).getValue();
    }

    public String getTags() {
        return tags.getText();
    }

    public SettlementDialog closeDialogWithOk() {
        ok.click();
        return at(SettlementDialog.class);
    }

    public VoucherTermsAndConditionsDialog openTermsAndConditions() {
        termsAndConditions.click();
        return at(VoucherTermsAndConditionsDialog.class);
    }

    public EditVoucherValuationDialog discountDistribution(String discountDistributionValue) {
        discountDistribution.enter(discountDistributionValue);
        return this;
    }

    public SettlementDialog saveVoucherValuation() {
        clickUsingJsIfSeleniumClickReturnError(ok);
        return at(SettlementDialog.class);
    }

    public String getVoucherCashValue() {
        return voucherCashValue.getText();
    }


    public Integer getVoucherPercentage() {
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

