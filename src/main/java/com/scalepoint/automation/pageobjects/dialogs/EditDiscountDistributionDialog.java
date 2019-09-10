package com.scalepoint.automation.pageobjects.dialogs;

import com.scalepoint.automation.utils.OperationalUtils;
import com.scalepoint.automation.utils.Wait;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.function.Consumer;

public class EditDiscountDistributionDialog extends BaseDialog {

    @FindBy(xpath = "//input[@data-componentid='distribution-voucher-to-company-input']")
    private WebElement companyPercentageInput;

    @FindBy(xpath = "//input[@data-componentid='distribution-voucher-to-customer-input']")
    private WebElement customerPercentageInput;

    @FindBy(id = "distribution-voucher-face-value-display")
    private WebElement faceValueText;

    @FindBy(id = "distribution-voucher-cash-value-display")
    private WebElement cashValueText;

    @FindBy(id = "distribution-voucher-ok-button")
    private WebElement saveButton;

    @FindBy(id = "distribution-voucher-confirmImportSummary-button")
    private WebElement cancelButton;


    @Override
    protected BaseDialog ensureWeAreAt() {
        Wait.waitForVisible(saveButton);
        return this;
    }

    public EditDiscountDistributionDialog updatePercentage(EditVoucherValuationDialog.DistributeTo distributeTo, Integer percentage) {
        if (EditVoucherValuationDialog.DistributeTo.COMPANY.equals(distributeTo)) {
            companyPercentageInput.sendKeys(Keys.HOME, Keys.chord(Keys.SHIFT, Keys.END), percentage.toString());
            companyPercentageInput.sendKeys(Keys.TAB);
        } else {
            customerPercentageInput.sendKeys(Keys.HOME, Keys.chord(Keys.SHIFT, Keys.END), percentage.toString());
            companyPercentageInput.sendKeys(Keys.TAB);
        }
        return this;
    }

    public EditVoucherValuationDialog saveDiscountDistribution() {
        saveButton.click();
        return BaseDialog.at(EditVoucherValuationDialog.class);
    }

    public EditDiscountDistributionDialog doAssert(Consumer<Asserts> assertFunc) {
        assertFunc.accept(new Asserts());
        return EditDiscountDistributionDialog.this;
    }

    public class Asserts {
        public Asserts assertFaceValueIs(Double expectedFaceValue) {
            Double currentValue = OperationalUtils.toNumber(faceValueText.getText());
            OperationalUtils.assertEqualsDouble(currentValue, expectedFaceValue);
            return this;
        }

        public Asserts assertCashValueIs(Double expectedCashValue) {
            Double currentValue = OperationalUtils.toNumber(cashValueText.getText());
            OperationalUtils.assertEqualsDouble(currentValue, expectedCashValue);
            return this;
        }
    }


}


