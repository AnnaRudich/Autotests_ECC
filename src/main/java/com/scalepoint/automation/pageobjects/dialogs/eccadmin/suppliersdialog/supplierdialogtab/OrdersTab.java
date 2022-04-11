package com.scalepoint.automation.pageobjects.dialogs.eccadmin.suppliersdialog.supplierdialogtab;

import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.pageobjects.dialogs.eccadmin.suppliersdialog.SupplierDialog;
import com.scalepoint.automation.pageobjects.extjs.ExtCheckboxTypeInput;
import com.scalepoint.automation.pageobjects.extjs.ExtComboBoxBoundList;
import com.scalepoint.automation.pageobjects.extjs.ExtRadioGroupTypeInput;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

import java.util.function.Consumer;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;

public  class OrdersTab extends SupplierDialog{

    @FindBy(name = "orderEmail")
    private SelenideElement emailField;
    @FindBy(id = "orderFlowOldFlow")
    private SelenideElement radioOldOrderFlow;
    @FindBy(id = "orderFlowOrderService")
    private SelenideElement radioOrderService;
    @FindBy(name = "deliveryTime")
    private SelenideElement defaultDeliveryTimeField;

    private ExtComboBoxBoundList getOrderMailFormatSelect(){

        return new ExtComboBoxBoundList($(By.cssSelector(".supplier-order-mail-format")));
    }

    private ExtCheckboxTypeInput getAddFreightPriceCheckbox(){

        return new ExtCheckboxTypeInput($(By.xpath("//table[contains(@class, 'supplier-add-freight-price')]")));
    }

    private ExtCheckboxTypeInput getDeliverySupportedCheckbox(){

        return new ExtCheckboxTypeInput($(By.id("deliverySupportedId")));
    }

    private ExtCheckboxTypeInput getClaimHandlingProductsCheckbox(){

        return new ExtCheckboxTypeInput($(By.xpath("//table[contains(@class, 'supplier-products-only-for-claim-handling')]")));
    }

    private ExtRadioGroupTypeInput getInvoiceSettings(){

        return new ExtRadioGroupTypeInput($(By.id("invoiceSettingRadioGroup")));
    }

    public OrdersTab selectInvoiceSetting(int value) {

        getInvoiceSettings().select(value);
        return this;
    }

    public OrdersTab setOrderEmail(String email) {

        emailField.clear();
        emailField.setValue(email);
        return this;
    }

    public OrdersTab selectRadioOldOrderFlow() {

        radioOldOrderFlow.click();
        return this;
    }

    public OrdersTab selectRadioOrderService() {

        radioOrderService.click();
        return this;
    }

    public OrdersTab setOrderMailFormat(SupplierDialog.OrderMailFormat orderMailFormat) {

        getOrderMailFormatSelect().select(orderMailFormat.getOption());
        return this;
    }

    public OrdersTab setDefaultDeliveryTime(Integer deliveryTime) {

        getDeliverySupportedCheckbox().set(true);
        defaultDeliveryTimeField.clear();
        defaultDeliveryTimeField.setValue(deliveryTime.toString());
        return this;
    }

    public OrdersTab useFreightPrice() {

        getAddFreightPriceCheckbox().set(true);
        return this;
    }

    public OrdersTab useProductsAsVouchers() {

        getClaimHandlingProductsCheckbox().set(true);
        return this;
    }

    public OrdersTab doAssert(Consumer<Asserts> assertFunc) {

        assertFunc.accept(new OrdersTab.Asserts());
        return OrdersTab.this;
    }

    public class Asserts {
        public Asserts assertOrderEmailIs(String email) {

            Assert.assertEquals(emailField.getValue(), email);
            return this;
        }

        public Asserts assertOrderEmailFormatIs(SupplierDialog.OrderMailFormat orderMailFormat) {

            Assert.assertEquals(getOrderMailFormatSelect().getValue(), orderMailFormat.getValue());
            return this;
        }

        public Asserts assertDeliveryTimeIs(Integer deliveryTime) {

            Assert.assertTrue(getDeliverySupportedCheckbox().isChecked());
            Assert.assertEquals(defaultDeliveryTimeField.getValue(), deliveryTime.toString());
            return this;
        }

        public Asserts assertFreightPriceUsed() {

            Assert.assertTrue(getAddFreightPriceCheckbox().isChecked());
            return this;
        }

        public Asserts assertProductsUsedAsVouchers() {

            Assert.assertTrue(getClaimHandlingProductsCheckbox().isChecked());
            return this;
        }

        public Asserts assertInvoiceSettingIs(int value) {

            Assert.assertEquals(getInvoiceSettings().getSelected(), value);
            return this;
        }

        public Asserts assertOldOrderFlowItemsDisabled() {

            Assert.assertFalse(getOrderMailFormatSelect().isInputElementEnabled());
            Assert.assertFalse(getAddFreightPriceCheckbox().isChecked());
            return this;
        }
    }

    @Override
    protected void ensureWeAreAt() {

        waitForAjaxCompletedAndJsRecalculation();
    }
}
