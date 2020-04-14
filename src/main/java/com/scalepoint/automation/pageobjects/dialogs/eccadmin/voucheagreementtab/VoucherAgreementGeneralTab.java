package com.scalepoint.automation.pageobjects.dialogs.eccadmin.voucheagreementtab;

import com.codeborne.selenide.Condition;
import com.scalepoint.automation.pageobjects.dialogs.BaseDialog;
import com.scalepoint.automation.pageobjects.extjs.ExtComboBox;
import com.scalepoint.automation.pageobjects.extjs.ExtInput;
import com.scalepoint.automation.utils.JavascriptHelper;
import com.scalepoint.automation.utils.Wait;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

import java.util.function.Consumer;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompleted;
import static org.testng.Assert.assertTrue;

public class VoucherAgreementGeneralTab extends BaseDialog implements VoucherAgreementTabs {

    @FindBy(name = "voucherName")
    private WebElement voucherNameInput;

    @FindBy(name = "agreementDiscount")
    private WebElement agreementDiscount;

    @FindBy(xpath = "//table[contains(@class, 'supplier-voucher-agreement-status')]")
    private ExtComboBox agreementStatusCombo;

    @FindBy(name = "agreementDiscount")
    private ExtInput discountInput;

    @FindBy(xpath = "//table[contains(@class, 'supplier-voucher-custom-logo-radio')]//td[contains(@class, 'x-form-item-body')]//label")
    private WebElement useCustomLogoRadio;

    @FindBy(name = "minimumAmount")
    private ExtInput minimumAmountInput;

    private By logoImageXpath = By.xpath("//div[@id='voucherLogoImageId']//img[@class='voucherImageUploadImg']");
    private By imageXpath = By.xpath("//div[@id='voucherImageId']//img[@class='voucherImageUploadImg']");

    @FindBy(name = "stepAmount")
    private ExtInput stepAmountInput;

    public static class FormFiller {

        private VoucherAgreementGeneralTab dialog;

        public FormFiller(VoucherAgreementGeneralTab dialog) {
            this.dialog = dialog;
        }

        public FormFiller withDiscount(Integer discount) {
            dialog.discountInput.clear();
            dialog.discountInput.setValue(discount.toString());
            return this;
        }

        public FormFiller withFaceValue(Integer faceValue) {
            dialog.minimumAmountInput.clear();
            dialog.minimumAmountInput.setValue(faceValue.toString());
            return this;
        }

        public FormFiller withFaceValueStep(Integer faceValueStep) {
            dialog.stepAmountInput.clear();
            dialog.stepAmountInput.setValue(faceValueStep.toString());
            return this;
        }

        public FormFiller withActive(boolean active) {
            waitForAjaxCompleted();
            Wait.waitForVisible(dialog.agreementStatusCombo).select(active ? "Active" : "Inactive");
            return this;
        }

        public FormFiller withLogo(String logoPath) {
            dialog.useCustomLogoRadio.click();
            WebElement elem = dialog.find(By.xpath("//input[contains(@id, 'voucherLogoFileId') and contains(@type, 'file')]"));
            dialog.enterToHiddenUploadFileField(elem, logoPath);
            Wait.waitForDisplayed(dialog.logoImageXpath);
            return this;
        }

        public FormFiller withImage(String logoPath) {
            WebElement elem = dialog.find(By.xpath("//input[contains(@id, 'voucherImageFileId') and contains(@type, 'file')]"));
            dialog.enterToHiddenUploadFileField(elem, logoPath);
            Wait.waitForDisplayed(dialog.imageXpath);
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
        $(voucherNameInput).waitUntil(Condition.visible, TIME_OUT_IN_MILISECONDS);
    }

    public VoucherAgreementGeneralTab doAssert(Consumer<Asserts> assertFunc) {
        assertFunc.accept(new VoucherAgreementGeneralTab.Asserts());
        return VoucherAgreementGeneralTab.this;
    }

    public class Asserts {

        public Asserts assertImagePresent() {
            assertTrue(JavascriptHelper.isImagePresent(driver.findElement(imageXpath)));
            return this;
        }

        public Asserts assertLogoPresent() {
            assertTrue(JavascriptHelper.isImagePresent(driver.findElement(logoImageXpath)));
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
            Assert.assertEquals(minimumAmountInput.getText(), faceValue.toString());
            return this;
        }

        public Asserts assertFaceValueStep(Integer faceValueStep) {
            Assert.assertEquals(stepAmountInput.getText(), faceValueStep.toString());
            return this;
        }

        public Asserts assertStatus(boolean active) {
            Assert.assertEquals(agreementStatusCombo.getValue(), active ? "Active" : "Inactive");
            return this;
        }
    }

}
