package com.scalepoint.automation.pageobjects.dialogs.eccadmin.voucheagreementtab;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.pageobjects.dialogs.BaseDialog;
import com.scalepoint.automation.pageobjects.extjs.ExtComboBoxBoundList;
import com.scalepoint.automation.utils.JavascriptHelper;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

import java.io.File;
import java.util.function.Consumer;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompleted;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;
import static org.testng.Assert.assertTrue;

public class VoucherAgreementGeneralTab extends BaseDialog implements VoucherAgreementTabs {

    Logger logger = LogManager.getLogger(VoucherAgreementGeneralTab.class);

    @FindBy(name = "voucherName")
    private SelenideElement voucherNameInput;
    @FindBy(name = "agreementDiscount")
    private SelenideElement agreementDiscount;
    @FindBy(name = "agreementDiscount")
    private SelenideElement discountInput;
    @FindBy(xpath = "//table[contains(@class, 'supplier-voucher-custom-logo-radio')]//td[contains(@class, 'x-form-item-body')]//label")
    private SelenideElement useCustomLogoRadio;
    @FindBy(name = "minimumAmount")
    private SelenideElement minimumAmountInput;
    @FindBy(name = "stepAmount")
    private SelenideElement stepAmountInput;

    private ExtComboBoxBoundList getAgreementStatusCombo(){

        return new ExtComboBoxBoundList($(By.cssSelector(".supplier-voucher-agreement-status")));
    }

    private By logoImageXpath = By.xpath("//div[@id='voucherLogoImageId']//img[@class='voucherImageUploadImg']");
    private By imageXpath = By.xpath("//div[@id='voucherImageId']//img[@class='voucherImageUploadImg']");

    public static class FormFiller {

        private VoucherAgreementGeneralTab dialog;

        public FormFiller(VoucherAgreementGeneralTab dialog) {

            this.dialog = dialog;
        }

        public FormFiller withDiscount(Integer discount) {

            SelenideElement element = dialog.discountInput;
            element.clear();
            element.setValue(discount.toString());
            return this;
        }

        public FormFiller withFaceValue(Integer faceValue) {

            SelenideElement element = dialog.minimumAmountInput;
            element.clear();
            element.setValue(faceValue.toString());
            return this;
        }

        public FormFiller withFaceValueStep(Integer faceValueStep) {

            SelenideElement element = dialog.stepAmountInput;
            element.clear();
            element.setValue(faceValueStep.toString());
            return this;
        }

        public FormFiller withActive(boolean active) {

            waitForAjaxCompleted();
            $(dialog.getAgreementStatusCombo()).should(Condition.visible);
            dialog.getAgreementStatusCombo().select(active ? "Active" : "Inactive");
            return this;
        }

        public FormFiller withLogo(String logoPath) {

            dialog.useCustomLogoRadio.click();
            WebElement elem = $(By.xpath("//input[contains(@id, 'voucherLogoFileId') and contains(@type, 'file')]"));
            $(elem).uploadFile(new File(logoPath));
            $(dialog.logoImageXpath).should(Condition.visible);
            return this;
        }

        public FormFiller withImage(String logoPath) {

            WebElement elem = $(By.xpath("//input[contains(@id, 'voucherImageFileId') and contains(@type, 'file')]"));
            $(elem).uploadFile(new File(logoPath));
            $(dialog.imageXpath).should(Condition.visible);
            return this;
        }
    }

    public VoucherAgreementGeneralTab fill(Consumer<VoucherAgreementGeneralTab> fillFunc) {

        fillFunc.accept(this);
        return this;
    }

    @Override
    protected boolean areWeAt() {

        waitForAjaxCompleted();
        try {

            return voucherNameInput.isDisplayed();
        } catch (Exception e) {

            logger.error(e.getMessage());
            return false;
        }
    }

    @Override
    protected void ensureWeAreAt() {

        waitForAjaxCompletedAndJsRecalculation();
        voucherNameInput.should(Condition.visible);
    }

    public VoucherAgreementGeneralTab doAssert(Consumer<Asserts> assertFunc) {

        assertFunc.accept(new VoucherAgreementGeneralTab.Asserts());
        return VoucherAgreementGeneralTab.this;
    }

    public class Asserts {

        public Asserts assertImagePresent() {

            assertTrue(JavascriptHelper.isImagePresent($(imageXpath)));
            return this;
        }

        public Asserts assertLogoPresent() {

            assertTrue(JavascriptHelper.isImagePresent($(logoImageXpath)));
            return this;
        }

        public Asserts assertVoucherName(String cvrValue) {

            Assert.assertEquals(voucherNameInput.getAttribute("value"), cvrValue);
            return this;
        }

        public Asserts assertDiscount(Integer discount) {

            Assert.assertEquals(agreementDiscount.getAttribute("value"), discount.toString());
            return this;
        }

        public Asserts assertFaceValue(Integer faceValue) {

            Assert.assertEquals(minimumAmountInput.getValue(), faceValue.toString());
            return this;
        }

        public Asserts assertFaceValueStep(Integer faceValueStep) {

            Assert.assertEquals(stepAmountInput.getValue(), faceValueStep.toString());
            return this;
        }

        public Asserts assertStatus(boolean active) {

            Assert.assertEquals(getAgreementStatusCombo().getValue(), active ? "Active" : "Inactive");
            return this;
        }
    }
}
