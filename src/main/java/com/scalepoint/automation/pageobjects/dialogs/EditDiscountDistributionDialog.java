package com.scalepoint.automation.pageobjects.dialogs;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.pageobjects.extjs.ExtText;
import com.scalepoint.automation.utils.OperationalUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.support.FindBy;

import java.util.function.Consumer;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;

public class EditDiscountDistributionDialog extends BaseDialog {

    @FindBy(id = "distribution-voucher-face-value-display")
    private SelenideElement faceValueText;
    @FindBy(id = "distribution-voucher-cash-value-display")
    private SelenideElement cashValueText;
    @FindBy(id = "distribution-voucher-ok-button")
    private SelenideElement saveButton;
    @FindBy(id = "distribution-voucher-cancel-button")
    private SelenideElement cancelButton;

    private ExtText getCompanyPercentageInput(){

        return new ExtText($(By.xpath("//input[@data-componentid='distribution-voucher-to-company-input']")));
    }

    private ExtText getCustomerPercentageInput(){

        return new ExtText($(By.xpath("//input[@data-componentid='distribution-voucher-to-customer-input']")));
    }

    @Override
    protected void ensureWeAreAt() {

        waitForAjaxCompletedAndJsRecalculation();
        saveButton.should(Condition.visible);
    }

    public EditDiscountDistributionDialog updatePercentage(EditVoucherValuationDialog.DistributeTo distributeTo, Integer percentage) {
        if (EditVoucherValuationDialog.DistributeTo.COMPANY.equals(distributeTo)) {

            getCompanyPercentageInput().enter(percentage.toString());
            getCompanyPercentageInput().sendKeys(Keys.TAB);
        } else {

            getCustomerPercentageInput().enter(percentage.toString());
            getCompanyPercentageInput().sendKeys(Keys.TAB);
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
