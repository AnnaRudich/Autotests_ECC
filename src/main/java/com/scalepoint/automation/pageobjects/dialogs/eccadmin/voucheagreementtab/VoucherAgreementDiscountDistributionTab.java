package com.scalepoint.automation.pageobjects.dialogs.eccadmin.voucheagreementtab;

import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.pageobjects.dialogs.BaseDialog;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

import java.util.function.Consumer;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;

public class VoucherAgreementDiscountDistributionTab extends BaseDialog implements VoucherAgreementTabs {

    @FindBy(name = "discountToIC")
    private WebElement discountToICInput;

    @FindBy(name = "discountToClaimant")
    private WebElement discountToClaimantInput;

    public VoucherAgreementDiscountDistributionTab setDiscountToIc(Integer discount) {
        SelenideElement element = $(discountToICInput);
        element.clear();
        element.setValue(discount.toString());
        return this;
    }

    public VoucherAgreementDiscountDistributionTab setDiscountToClaimant(Integer discount) {
        SelenideElement element = $(discountToClaimantInput);
        element.clear();
        element.setValue(discount.toString());
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
            Assert.assertEquals($(discountToICInput).getValue(), discount.toString());
            return this;
        }

        public Asserts assertDiscountToClaimant(Integer discount) {
            Assert.assertEquals($(discountToClaimantInput).getValue(), discount.toString());
            return this;
        }
    }
}
