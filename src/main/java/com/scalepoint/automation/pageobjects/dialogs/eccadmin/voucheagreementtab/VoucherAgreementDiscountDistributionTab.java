package com.scalepoint.automation.pageobjects.dialogs.eccadmin.voucheagreementtab;

import com.scalepoint.automation.pageobjects.dialogs.BaseDialog;
import com.scalepoint.automation.pageobjects.extjs.ExtInput;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

import java.util.function.Consumer;

import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;

public class VoucherAgreementDiscountDistributionTab extends BaseDialog implements VoucherAgreementTabs {

    @FindBy(name = "discountToIC")
    private ExtInput discountToICInput;

    @FindBy(name = "discountToClaimant")
    private ExtInput discountToClaimantInput;

    public VoucherAgreementDiscountDistributionTab setDiscountToIc(Integer discount) {
        discountToICInput.clear();
        discountToICInput.sendKeys(discount.toString());
        return this;
    }

    public VoucherAgreementDiscountDistributionTab setDiscountToClaimant(Integer discount) {
        discountToClaimantInput.clear();
        discountToClaimantInput.sendKeys(discount.toString());
        return this;
    }

    @Override
    protected void ensureWeAreAt() {
        waitForAjaxCompletedAndJsRecalculation();
    }

    public VoucherAgreementDiscountDistributionTab doAssert(Consumer<Asserts> assertFunc) {
        assertFunc.accept(new Asserts());
        return VoucherAgreementDiscountDistributionTab.this;
    }

    public class Asserts {

        public Asserts assertDiscountToIc(Integer discount) {
            Assert.assertEquals(discountToICInput.getText(), discount.toString());
            return this;
        }

        public Asserts assertDiscountToClaimant(Integer discount) {
            Assert.assertEquals(discountToClaimantInput.getText(), discount.toString());
            return this;
        }
    }
}
