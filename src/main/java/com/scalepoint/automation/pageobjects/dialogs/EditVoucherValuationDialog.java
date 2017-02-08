package com.scalepoint.automation.pageobjects.dialogs;

import com.scalepoint.automation.pageobjects.extjs.ExtInput;
import com.scalepoint.automation.pageobjects.extjs.ExtText;
import com.scalepoint.automation.utils.Wait;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.TextInput;

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

    @Override
    protected BaseDialog ensureWeAreAt() {
        Wait.waitForVisible(ok);
        Wait.waitForVisible(voucherFaceValue);
        return null;
    }

    public String getBrands() {
        return brandsText.getText();
    }

    public String getTags() {
        return tags.getWrappedElement().getAttribute("textContent");
    }

    public SettlementDialog closeDialogWithOk() {
        ok.click();
        return at(SettlementDialog.class);
    }

    public VoucherTermsAndConditionsDialog openTermsAndConditions() {
        termsAndConditions.click();
        return at(VoucherTermsAndConditionsDialog.class);
    }

    public EditVoucherValuationDialog discountDistribution(String _discountDistribution) {
        discountDistribution.enter(_discountDistribution);
        return this;
    }

    public String getVoucherFaceValue() {
        return voucherFaceValue.getText();
    }

    public String getVoucherCashValue() {
        return voucherCashValue.getText();
    }

    public EditVoucherValuationDialog assertBrandsTextIs(String expectedBrandsText) {
        assertEquals(getBrands(), expectedBrandsText, "Wrong Brand is displayed.");
        return this;
    }

    public EditVoucherValuationDialog assertTagsTextIs(String expectedTagsText) {
        assertEquals(getTags(), expectedTagsText, "Wrong Tags are displayed");
        return this;
    }

}

