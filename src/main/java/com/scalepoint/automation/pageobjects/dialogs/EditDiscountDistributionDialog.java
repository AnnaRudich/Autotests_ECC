package com.scalepoint.automation.pageobjects.dialogs;

import com.scalepoint.automation.utils.Wait;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class EditDiscountDistributionDialog extends BaseDialog {

    @FindBy(id = "edit-voucher-ok-button")
    private WebElement okButton;

    @FindBy(id = "edit-voucher-cancel-button")
    private WebElement cancelButton;

    @FindBy(id = "edit-voucher-to-company-input-inputEl")
    private WebElement companyPercentageInput;

    @FindBy(id = "edit-voucher-to-customer-input-inputEl")
    private WebElement customerPercentageInput;

    @FindBy(id = "edit-voucher-face-value-input-inputEl")
    private WebElement faceValueInput;

    @FindBy(id = "edit-voucher-cash-value-input-inputEl")
    private WebElement cashValueInput;

    @FindBy(id = "edit-voucher-rebate-display-inputEl")
    private WebElement voucherRebate;

    @Override
    protected BaseDialog ensureWeAreAt() {
        Wait.waitForVisible(okButton);
        return this;
    }

    public EditDiscountDistributionDialog updateCompanyPercentage(Integer percentage) {
        companyPercentageInput.sendKeys(Keys.HOME, Keys.chord(Keys.SHIFT, Keys.END), percentage.toString());
        return this;
    }

    public EditDiscountDistributionDialog updateCustomerPercentage(Integer percentage) {
        customerPercentageInput.sendKeys(Keys.HOME, Keys.chord(Keys.SHIFT, Keys.END), percentage.toString());
        return this;
    }

    public SettlementDialog save() {
        okButton.click();
        return at(SettlementDialog.class);
    }

    public Integer getVoucherPercentage() {
        String text = voucherRebate.getText();
        return Integer.valueOf(text.replaceAll("([0-9]+),.*", "$1"));
    }
}


