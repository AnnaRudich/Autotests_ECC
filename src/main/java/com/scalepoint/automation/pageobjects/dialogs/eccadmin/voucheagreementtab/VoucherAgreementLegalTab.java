package com.scalepoint.automation.pageobjects.dialogs.eccadmin.voucheagreementtab;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.pageobjects.dialogs.BaseDialog;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

import java.util.function.Consumer;

import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;

public class VoucherAgreementLegalTab extends BaseDialog implements VoucherAgreementTabs {

    @FindBy(xpath = ".//textarea[contains(@name, 'conditions')]")
    private SelenideElement conditionsInput;
    @FindBy(xpath = ".//textarea[contains(@name, 'limitations')]")
    private SelenideElement limitationsInput;

    @Override
    protected void ensureWeAreAt() {

        waitForAjaxCompletedAndJsRecalculation();
        conditionsInput.should(Condition.visible);
    }

    public VoucherAgreementLegalTab setConditions(String conditionsText) {

        conditionsInput.sendKeys(conditionsText);
        return this;
    }

    public VoucherAgreementLegalTab setLimitations(String limitationsText) {

        limitationsInput.sendKeys(limitationsText);
        return this;
    }

    public VoucherAgreementLegalTab doAssert(Consumer<Asserts> assertFunc) {

        assertFunc.accept(new Asserts());
        return VoucherAgreementLegalTab.this;
    }

    public class Asserts {
        public VoucherAgreementLegalTab.Asserts assertConditions(String conditions) {

            Assert.assertEquals(conditionsInput.getText(), conditions);
            return this;
        }

        public VoucherAgreementLegalTab.Asserts assertLimitations(String limitaions) {

            Assert.assertEquals(limitationsInput.getText(), limitaions);
            return this;
        }
    }
}
