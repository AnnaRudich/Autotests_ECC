package com.scalepoint.automation.pageobjects.dialogs.eccadmin.voucheagreementtab;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.pageobjects.dialogs.BaseDialog;
import com.scalepoint.automation.pageobjects.extjs.ExtCheckboxTypeDiv;
import com.scalepoint.automation.pageobjects.extjs.ExtRadioButton;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

import java.util.function.Consumer;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;

public class VoucherAgreementAdvancedTab extends BaseDialog implements VoucherAgreementTabs {

    @FindBy(name = "otherEmail")
    private SelenideElement otherEmailInput;
    @FindBy(name = "deliveryCost")
    private SelenideElement deliveryCostInput;
    @FindBy(name = "deliveryType")
    private SelenideElement deliveryTypeInput;
    @FindBy(name = "popularity")
    private SelenideElement popularityInput;

    private ExtRadioButton getUseSupplierOrderEmailRadio(){

        return new ExtRadioButton($(By.xpath("//table[contains(@class, 'supplier-voucher-general-order-email-radio')]")));
    }

    private ExtRadioButton getUseOtherEmailRadio(){

        return new ExtRadioButton($(By.xpath("//table[contains(@class, 'supplier-voucher-other-email-radio')]")));
    }

    private ExtRadioButton getScalepointHandlesEvouchersRadio(){

        return new ExtRadioButton($(By.xpath("//table[contains(@class, 'supplier-voucher-scalepoint-handles-voucher-radio')]")));
    }

    private ExtCheckboxTypeDiv getUseDeliveryCostCheckbox(){

        return new ExtCheckboxTypeDiv($(By.xpath("//table[contains(@class, 'supplier-voucher-delivery-cost-checkbox')]")));
    }

    private ExtCheckboxTypeDiv getUseElectronicVoucherCheckbox(){

        return new ExtCheckboxTypeDiv($(By.xpath("//table[contains(@class, 'supplier-electronic-voucher-checkbox')]")));
    }

    private ExtCheckboxTypeDiv geteVoucherEmailRequired(){

        return new ExtCheckboxTypeDiv($(By.xpath("//table[contains(@class, 'supplier-voucher-evoucher-email-checkbox')]")));
    }

    private ExtCheckboxTypeDiv geteVoucherPhoneRequired(){

        return new ExtCheckboxTypeDiv($(By.xpath("//table[contains(@class, 'supplier-voucher-evoucher-phone-checkbox')]")));
    }

    private ExtCheckboxTypeDiv geteVoucherPersonalCodeRequired(){

        return new ExtCheckboxTypeDiv($(By.xpath("//table[contains(@class, 'supplier-voucher-evoucher-code-checkbox')]")));
    }

    private ExtCheckboxTypeDiv getUseEVoucherPortal(){

        return new ExtCheckboxTypeDiv($(By.xpath("//table[contains(@class, 'supplier-voucher-evoucher-portal-checkbox')]")));
    }

    private ExtCheckboxTypeDiv getAddDeliveryCostImSupplierMailCheckBox(){

        return new ExtCheckboxTypeDiv($(By.xpath("//table[contains(@class, 'supplier-voucher-add-delivery-cost-in-email-checkbox')]")));
    }

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

                    dialog.getUseOtherEmailRadio().set(true);
                    SelenideElement element = $(dialog.otherEmailInput);
                    element.clear();
                    element.setValue(otherEmail);
                    break;

                case SCALEPOINT_HANDLES:

                    dialog.getScalepointHandlesEvouchersRadio().set(true);
                    break;

                case SUPPLIER:

                    dialog.getUseSupplierOrderEmailRadio().set(true);
                    break;
            }
            return this;
        }

        public FormFiller withDeliveryCost(Integer deliveryCost) {

            dialog.getUseDeliveryCostCheckbox().set(true);
            dialog.deliveryCostInput.setValue(deliveryCost.toString());
            return this;
        }

        public FormFiller withDeliveryType(String deliveryType) {

            SelenideElement element = dialog.deliveryTypeInput;
            element.clear();
            element.setValue(deliveryType);
            return this;
        }

        public FormFiller withPopularity(Integer popularity) {

            SelenideElement element = dialog.popularityInput;
            element.clear();
            element.setValue(popularity.toString());
            return this;
        }

        public void useAsEVoucher(EVoucherOptions... eVoucherOptions) {

            dialog.getUseElectronicVoucherCheckbox().set(true);
            for (EVoucherOptions eVoucherOption : eVoucherOptions) {

                switch (eVoucherOption) {

                    case EMAIL_REQUIRED:

                        dialog.geteVoucherEmailRequired().set(true);
                        break;

                    case PERSONAL_CODE_REQUIRED:

                        dialog.geteVoucherPersonalCodeRequired().set(true);
                        break;

                    case PHONE_REQUIRED:

                        dialog.geteVoucherPhoneRequired().set(true);
                        break;

                    case USE_PORTAL_REQUIRED:

                        dialog.getUseEVoucherPortal().set(true);
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
                .should(Condition.appear)
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

                    Assert.assertTrue(getUseOtherEmailRadio().isChecked());
                    break;

                case SCALEPOINT_HANDLES:

                    Assert.assertTrue(getScalepointHandlesEvouchersRadio().isChecked());
                    break;

                case SUPPLIER:

                    Assert.assertTrue(getUseSupplierOrderEmailRadio().isChecked());
                    break;
            }
            return this;
        }

        public Asserts assertOtherEmail(String email) {

            Assert.assertEquals(otherEmailInput.getValue(), email);
            return this;
        }

        public Asserts assertDeliveryType(String deliveryType) {

            Assert.assertEquals(deliveryTypeInput.getValue(), deliveryType);
            return this;
        }

        public Asserts assertPopularity(Integer popularity) {

            Assert.assertEquals(popularityInput.getValue(), popularity.toString());
            return this;
        }

        public Asserts assertUsedAsEVoucher(EVoucherOptions... eVoucherOptions) {

            Assert.assertTrue(getUseElectronicVoucherCheckbox().isChecked());

            for (EVoucherOptions eVoucherOption : eVoucherOptions) {

                switch (eVoucherOption) {

                    case EMAIL_REQUIRED:

                        Assert.assertTrue(geteVoucherEmailRequired().isChecked());
                        break;

                    case PERSONAL_CODE_REQUIRED:

                        Assert.assertTrue(geteVoucherPersonalCodeRequired().isChecked());
                        break;

                    case PHONE_REQUIRED:

                        Assert.assertTrue(geteVoucherPhoneRequired().isChecked());
                        break;

                    case USE_PORTAL_REQUIRED:

                        Assert.assertTrue(getUseEVoucherPortal().isChecked());
                        break;
                }
            }
            return this;
        }
    }
}
