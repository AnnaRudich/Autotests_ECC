package com.scalepoint.automation.pageobjects.dialogs;

import com.codeborne.selenide.Condition;
import com.scalepoint.automation.utils.OperationalUtils;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.function.Consumer;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;

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

    @FindBy(id = "distribution-voucher-cancel-button")
    private WebElement cancelButton;


    @Override
    protected void ensureWeAreAt() {
        waitForAjaxCompletedAndJsRecalculation();
        $(saveButton).waitUntil(Condition.visible, TIME_OUT_IN_MILISECONDS);
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


