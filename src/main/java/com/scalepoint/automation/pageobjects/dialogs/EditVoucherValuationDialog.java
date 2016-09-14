package com.scalepoint.automation.pageobjects.dialogs;

import com.scalepoint.automation.pageobjects.extjs.ExtInput;
import com.scalepoint.automation.pageobjects.extjs.ExtText;
import com.scalepoint.automation.utils.Wait;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.TextInput;

public class EditVoucherValuationDialog extends BaseDialog {

    @FindBy(id = "edit-voucher-brands-text")
    private ExtInput brands;

    @FindBy(name = "edit-voucher-brands-text-inputEl")
    private TextInput brandsText;

    @FindBy(id = "edit-voucher-tags-text-inputEl")
    private ExtInput tags;

    @FindBy(id = "edit-voucher-ok-button")
    private Button ok;

    @FindBy(id = "edit-voucher-cancel-button")
    private Button cancel;

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

    public void ok() {
        ok.click();
    }

    public void Cancel() {
        cancel.click();
    }

    public EditVoucherValuationDialog openTermsAndConditions() {
        termsAndConditions.click();
        return this;
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

}

