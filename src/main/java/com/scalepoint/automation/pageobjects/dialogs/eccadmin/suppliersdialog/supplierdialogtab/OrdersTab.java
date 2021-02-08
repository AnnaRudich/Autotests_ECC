package com.scalepoint.automation.pageobjects.dialogs.eccadmin.suppliersdialog.supplierdialogtab;

import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.pageobjects.dialogs.eccadmin.suppliersdialog.SupplierDialog;
import com.scalepoint.automation.pageobjects.extjs.ExtCheckboxTypeInput;
import com.scalepoint.automation.pageobjects.extjs.ExtComboBoxBoundList;
import com.scalepoint.automation.pageobjects.extjs.ExtRadioGroupTypeInput;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

import java.util.function.Consumer;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;

public  class OrdersTab extends SupplierDialog{

    @FindBy(name = "orderEmail")
    private WebElement emailField;

    @FindBy(id = "orderFlowOldFlow")
    private WebElement radioOldOrderFlow;

    @FindBy(id = "orderFlowOrderService")
    private WebElement radioOrderService;

    @FindBy(css = ".supplier-order-mail-format")
    private ExtComboBoxBoundList orderMailFormatSelect;

    @FindBy(xpath = "//table[contains(@class, 'supplier-add-freight-price')]")
    private ExtCheckboxTypeInput addFreightPriceCheckbox;

    @FindBy(id = "deliverySupportedId")
    private ExtCheckboxTypeInput deliverySupportedCheckbox;

    @FindBy(name = "deliveryTime")
    private WebElement defaultDeliveryTimeField;

    @FindBy(xpath = "//table[contains(@class, 'supplier-products-only-for-claim-handling')]")
    private ExtCheckboxTypeInput claimHandlingProductsCheckbox;

    @FindBy(id = "invoiceSettingRadioGroup")
    private ExtRadioGroupTypeInput invoiceSettings;

    public ExtRadioGroupTypeInput getInvoiceSettings() {
        return invoiceSettings;
    }

    public OrdersTab selectInvoiceSetting(int value) {
        invoiceSettings.select(value);
        return this;
    }

    public OrdersTab setOrderEmail(String email) {
        SelenideElement element = $(emailField);
        element.clear();
        element.setValue(email);
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
        orderMailFormatSelect.select(orderMailFormat.getOption());
        return this;
    }

    public OrdersTab setDefaultDeliveryTime(Integer deliveryTime) {
        deliverySupportedCheckbox.set(true);
        SelenideElement element = $(defaultDeliveryTimeField);
        element.clear();
        element.setValue(deliveryTime.toString());
        return this;
    }

    public OrdersTab useFreightPrice() {
        addFreightPriceCheckbox.set(true);
        return this;
    }

    public OrdersTab useProductsAsVouchers() {
        claimHandlingProductsCheckbox.set(true);
        return this;
    }

    public OrdersTab doAssert(Consumer<Asserts> assertFunc) {
        assertFunc.accept(new OrdersTab.Asserts());
        return OrdersTab.this;
    }

    public class Asserts {
        public Asserts assertOrderEmailIs(String email) {
            Assert.assertEquals($(emailField).getValue(), email);
            return this;
        }

        public Asserts assertOrderEmailFormatIs(SupplierDialog.OrderMailFormat orderMailFormat) {
            Assert.assertEquals(orderMailFormatSelect.getValue(), orderMailFormat.getValue());
            return this;
        }

        public Asserts assertDeliveryTimeIs(Integer deliveryTime) {
            Assert.assertTrue(deliverySupportedCheckbox.isChecked());
            Assert.assertEquals($(defaultDeliveryTimeField).getValue(), deliveryTime.toString());
            return this;
        }

        public Asserts assertFreightPriceUsed() {
            Assert.assertTrue(addFreightPriceCheckbox.isChecked());
            return this;
        }

        public Asserts assertProductsUsedAsVouchers() {
            Assert.assertTrue(claimHandlingProductsCheckbox.isChecked());
            return this;
        }

        public Asserts assertInvoiceSettingIs(int value) {
            Assert.assertEquals(invoiceSettings.getSelected(), value);
            return this;
        }

        public Asserts assertOldOrderFlowItemsDisabled() {
            Assert.assertFalse(orderMailFormatSelect.isInputElementEnabled());
            Assert.assertFalse(addFreightPriceCheckbox.isChecked());
            return this;
        }
    }

    @Override
    protected void ensureWeAreAt() {
        waitForAjaxCompletedAndJsRecalculation();
    }
}
