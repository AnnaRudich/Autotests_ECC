package com.scalepoint.automation.pageobjects.dialogs.eccadmin.voucheagreementtab;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.pageobjects.dialogs.BaseDialog;
import com.scalepoint.automation.pageobjects.extjs.ExtCheckboxTypeDiv;
import com.scalepoint.automation.pageobjects.extjs.ExtRadioButton;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

import java.util.function.Consumer;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;

public class VoucherAgreementAdvancedTab extends BaseDialog implements VoucherAgreementTabs {

    @FindBy(xpath = "//table[contains(@class, 'supplier-voucher-general-order-email-radio')]")
    private ExtRadioButton useSupplierOrderEmailRadio;

    @FindBy(xpath = "//table[contains(@class, 'supplier-voucher-other-email-radio')]")
    private ExtRadioButton useOtherEmailRadio;

    @FindBy(xpath = "//table[contains(@class, 'supplier-voucher-scalepoint-handles-voucher-radio')]")
    private ExtRadioButton scalepointHandlesEvouchersRadio;

    @FindBy(xpath = "//table[contains(@class, 'supplier-voucher-delivery-cost-checkbox')]")
    private ExtCheckboxTypeDiv useDeliveryCostCheckbox;

    @FindBy(xpath = "//table[contains(@class, 'supplier-electronic-voucher-checkbox')]")
    private ExtCheckboxTypeDiv useElectronicVoucherCheckbox;

    @FindBy(xpath = "//table[contains(@class, 'supplier-voucher-evoucher-email-checkbox')]")
    private ExtCheckboxTypeDiv eVoucherEmailRequired;

    @FindBy(xpath = "//table[contains(@class, 'supplier-voucher-evoucher-phone-checkbox')]")
    private ExtCheckboxTypeDiv eVoucherPhoneRequired;

    @FindBy(xpath = "//table[contains(@class, 'supplier-voucher-evoucher-code-checkbox')]")
    private ExtCheckboxTypeDiv eVoucherPersonalCodeRequired;

    @FindBy(xpath = "//table[contains(@class, 'supplier-voucher-evoucher-portal-checkbox')]")
    private ExtCheckboxTypeDiv useEVoucherPortal;

    @FindBy(xpath = "//table[contains(@class, 'supplier-voucher-add-delivery-cost-in-email-checkbox')]")
    private ExtCheckboxTypeDiv addDeliveryCostImSupplierMailCheckBox;

    @FindBy(name = "otherEmail")
    private WebElement otherEmailInput;

    @FindBy(name = "deliveryCost")
    private WebElement deliveryCostInput;

    @FindBy(name = "deliveryType")
    private WebElement deliveryTypeInput;

    @FindBy(name = "popularity")
    private WebElement popularityInput;

    public enum OrderMailType {
        SUPPLIER,
        OTHER,
        SCALEPOINT_HANDLES
    }

    public enum EVoucherOptions {
        EMAIL_REQUIRED,
        PHONE_REQUIRED,
        PERSONAL_CODE_REQUIRED,
        USE_PORTAL_REQUIRED
    }

    public static class FormFiller {

        private VoucherAgreementAdvancedTab dialog;

        public FormFiller(VoucherAgreementAdvancedTab dialog) {
            this.dialog = dialog;
        }

        public FormFiller useOrderEmail(OrderMailType orderMailType) {
            return useOrderEmail(orderMailType, null);
        }

        public FormFiller useOrderEmail(OrderMailType orderMailType, String otherEmail) {
            switch (orderMailType) {
                case OTHER:
                    dialog.useOtherEmailRadio.set(true);
                    SelenideElement element = $(dialog.otherEmailInput);
                    element.clear();
                    element.setValue(otherEmail);
                    break;
                case SCALEPOINT_HANDLES:
                    dialog.scalepointHandlesEvouchersRadio.set(true);
                    break;
                case SUPPLIER:
                    dialog.useSupplierOrderEmailRadio.set(true);
                    break;
            }
            return this;
        }

        public FormFiller withDeliveryCost(Integer deliveryCost) {
            dialog.useDeliveryCostCheckbox.set(true);
            $(dialog.deliveryCostInput).setValue(deliveryCost.toString());
            return this;
        }

        public FormFiller withDeliveryType(String deliveryType) {
            SelenideElement element = $(dialog.deliveryTypeInput);
            element.clear();
            element.setValue(deliveryType);
            return this;
        }

        public FormFiller withPopularity(Integer popularity) {
            SelenideElement element = $(dialog.popularityInput);
            element.clear();
            element.setValue(popularity.toString());
            return this;
        }

        public void useAsEVoucher(EVoucherOptions... eVoucherOptions) {
            dialog.useElectronicVoucherCheckbox.set(true);
            for (EVoucherOptions eVoucherOption : eVoucherOptions) {
                switch (eVoucherOption) {
                    case EMAIL_REQUIRED:
                        dialog.eVoucherEmailRequired.set(true);
                        break;
                    case PERSONAL_CODE_REQUIRED:
                        dialog.eVoucherPersonalCodeRequired.set(true);
                        break;
                    case PHONE_REQUIRED:
                        dialog.eVoucherPhoneRequired.set(true);
                        break;
                    case USE_PORTAL_REQUIRED:
                        dialog.useEVoucherPortal.set(true);
                        break;
                }
            }
        }
    }

    public VoucherAgreementAdvancedTab fill(Consumer<VoucherAgreementAdvancedTab> fillFunc) {
        fillFunc.accept(this);
        return this;
    }

    @Override
    protected void ensureWeAreAt() {
        waitForAjaxCompletedAndJsRecalculation();
    }

    public VoucherAgreementAdvancedTab createShopOnlyVoucher(){
        $("a[data-qtip='Create-button'] span").click();
        confirmShopVoucherCreation();
        return VoucherAgreementAdvancedTab.this;
    }

    private void confirmShopVoucherCreation(){
        $(".x-message-box")
                .find("span[class='x-btn-button']")
                .waitUntil(Condition.appear, TIME_OUT_IN_MILISECONDS)
                .click();
    }

    public VoucherAgreementAdvancedTab doAssert(Consumer<Asserts> assertFunc) {
        assertFunc.accept(new Asserts());
        return VoucherAgreementAdvancedTab.this;
    }

    public class Asserts {

        public Asserts assertOrderType(OrderMailType orderMailType) {
            switch (orderMailType) {
                case OTHER:
                    Assert.assertTrue(useOtherEmailRadio.isChecked());
                    break;
                case SCALEPOINT_HANDLES:
                    Assert.assertTrue(scalepointHandlesEvouchersRadio.isChecked());
                    break;
                case SUPPLIER:
                    Assert.assertTrue(useSupplierOrderEmailRadio.isChecked());
                    break;
            }
            return this;
        }

        public Asserts assertOtherEmail(String email) {
            Assert.assertEquals($(otherEmailInput).getValue(), email);
            return this;
        }

        public Asserts assertDeliveryType(String deliveryType) {
            Assert.assertEquals($(deliveryTypeInput).getValue(), deliveryType);
            return this;
        }

        public Asserts assertPopularity(Integer popularity) {
            Assert.assertEquals($(popularityInput).getValue(), popularity.toString());
            return this;
        }

        public Asserts assertUsedAsEVoucher(EVoucherOptions... eVoucherOptions) {
            Assert.assertTrue(useElectronicVoucherCheckbox.isChecked());
            for (EVoucherOptions eVoucherOption : eVoucherOptions) {
                switch (eVoucherOption) {
                    case EMAIL_REQUIRED:
                        Assert.assertTrue(eVoucherEmailRequired.isChecked());
                        break;
                    case PERSONAL_CODE_REQUIRED:
                        Assert.assertTrue(eVoucherPersonalCodeRequired.isChecked());
                        break;
                    case PHONE_REQUIRED:
                        Assert.assertTrue(eVoucherPhoneRequired.isChecked());
                        break;
                    case USE_PORTAL_REQUIRED:
                        Assert.assertTrue(useEVoucherPortal.isChecked());
                        break;
                }
            }
            return this;
        }

    }

}
