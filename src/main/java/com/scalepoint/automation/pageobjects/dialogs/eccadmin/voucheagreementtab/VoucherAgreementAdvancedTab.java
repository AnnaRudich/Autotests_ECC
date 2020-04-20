package com.scalepoint.automation.pageobjects.dialogs.eccadmin.voucheagreementtab;

import com.codeborne.selenide.Condition;
import com.scalepoint.automation.pageobjects.dialogs.BaseDialog;
import com.scalepoint.automation.pageobjects.extjs.ExtCheckbox;
import com.scalepoint.automation.pageobjects.extjs.ExtInput;
import com.scalepoint.automation.pageobjects.extjs.ExtRadioButton;
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
    private ExtCheckbox useDeliveryCostCheckbox;

    @FindBy(xpath = "//table[contains(@class, 'supplier-electronic-voucher-checkbox')]")
    private ExtCheckbox useElectronicVoucherCheckbox;

    @FindBy(xpath = "//table[contains(@class, 'supplier-voucher-evoucher-email-checkbox')]")
    private ExtCheckbox eVoucherEmailRequired;

    @FindBy(xpath = "//table[contains(@class, 'supplier-voucher-evoucher-phone-checkbox')]")
    private ExtCheckbox eVoucherPhoneRequired;

    @FindBy(xpath = "//table[contains(@class, 'supplier-voucher-evoucher-code-checkbox')]")
    private ExtCheckbox eVoucherPersonalCodeRequired;

    @FindBy(xpath = "//table[contains(@class, 'supplier-voucher-evoucher-portal-checkbox')]")
    private ExtCheckbox useEVoucherPortal;

    @FindBy(xpath = "//table[contains(@class, 'supplier-voucher-add-delivery-cost-in-email-checkbox')]")
    private ExtCheckbox addDeliveryCostImSupplierMailCheckBox;

    @FindBy(name = "otherEmail")
    private ExtInput otherEmailInput;

    @FindBy(name = "deliveryCost")
    private ExtInput deliveryCostInput;

    @FindBy(name = "deliveryType")
    private ExtInput deliveryTypeInput;

    @FindBy(name = "popularity")
    private ExtInput popularityInput;

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
                    dialog.otherEmailInput.clear();
                    dialog.otherEmailInput.sendKeys(otherEmail);
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
            dialog.deliveryCostInput.sendKeys(deliveryCost.toString());
            return this;
        }

        public FormFiller withDeliveryType(String deliveryType) {
            dialog.deliveryTypeInput.clear();
            dialog.deliveryTypeInput.sendKeys(deliveryType);
            return this;
        }

        public FormFiller withPopularity(Integer popularity) {
            dialog.popularityInput.clear();
            dialog.popularityInput.sendKeys(popularity.toString());
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
                .$("span[class='x-btn-button']")
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
                    Assert.assertTrue(useOtherEmailRadio.isSelected());
                    break;
                case SCALEPOINT_HANDLES:
                    Assert.assertTrue(scalepointHandlesEvouchersRadio.isSelected());
                    break;
                case SUPPLIER:
                    Assert.assertTrue(useSupplierOrderEmailRadio.isSelected());
                    break;
            }
            return this;
        }

        public Asserts assertOtherEmail(String email) {
            Assert.assertEquals(otherEmailInput.getText(), email);
            return this;
        }

        public Asserts assertDeliveryType(String deliveryType) {
            Assert.assertEquals(deliveryTypeInput.getText(), deliveryType);
            return this;
        }

        public Asserts assertPopularity(Integer popularity) {
            Assert.assertEquals(popularityInput.getText(), popularity.toString());
            return this;
        }

        public Asserts assertUsedAsEVoucher(EVoucherOptions... eVoucherOptions) {
            Assert.assertTrue(useElectronicVoucherCheckbox.isSelected());
            for (EVoucherOptions eVoucherOption : eVoucherOptions) {
                switch (eVoucherOption) {
                    case EMAIL_REQUIRED:
                        Assert.assertTrue(eVoucherEmailRequired.isSelected());
                        break;
                    case PERSONAL_CODE_REQUIRED:
                        Assert.assertTrue(eVoucherPersonalCodeRequired.isSelected());
                        break;
                    case PHONE_REQUIRED:
                        Assert.assertTrue(eVoucherPhoneRequired.isSelected());
                        break;
                    case USE_PORTAL_REQUIRED:
                        Assert.assertTrue(useEVoucherPortal.isSelected());
                        break;
                }
            }
            return this;
        }

    }

}
