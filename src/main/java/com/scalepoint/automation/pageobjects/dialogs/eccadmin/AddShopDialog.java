package com.scalepoint.automation.pageobjects.dialogs.eccadmin;

import com.scalepoint.automation.pageobjects.dialogs.BaseDialog;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.data.entity.Shop;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

import java.util.function.Consumer;

import static org.testng.Assert.assertTrue;

public class AddShopDialog extends BaseDialog {

    public enum ShopType {
        RETAIL,
        RNV
    }

    private static final String createShopButtonXpath = "//a[contains(@class,'supplier-create-shop-btn')]";
    private static final String updateShopButtonXpath = "//a[contains(@class,'supplier-save-shop-btn')]";
    private static final String cancelButtonXpath = "//a[contains(@class,'supplier-cancel-edit-shop-btn')]";

    @FindBy(xpath = "//label[contains(text(),'Name:')]")
    private WebElement nameLabel;
    @FindBy(name = "shopName")
    private WebElement nameField;
    @FindBy(name = "shopAddress")
    private WebElement address1Field;
    @FindBy(name = "shopAddress2")
    private WebElement address2Field;
    @FindBy(name = "shopPostalCode")
    private WebElement postalCodeField;
    @FindBy(name = "shopCity")
    private WebElement cityField;
    @FindBy(name = "shopPhone")
    private WebElement phoneField;
    @FindBy(name = "portalCode")
    private WebElement portalCode;
    @FindBy(name = "eVoucherNotificationEmails")
    private WebElement eVoucherNotificationEmails;
    @FindBy(css = "td.x-form-check-group table:nth-child(1) td input")
    private WebElement retailTypeCheckbox;
    @FindBy(css = "td.x-form-check-group table:nth-child(2) td input")
    private WebElement rnVTypeCheckbox;
    @FindBy(id = "cancelCreateSupplierBtnId")
    private WebElement cancelButton;

    @FindBy(id = "editSupplierShopTabFormId")
    private WebElement editableShopDialog;

    @Override
    protected BaseDialog ensureWeAreAt() {
        Wait.waitForVisible(nameLabel);
        return this;
    }

    public SupplierDialog.ShopsTab createShop(Shop shop, ShopType shopType) {
        fillShopForm(shop);
        chooseShopType(shopType);
        find(By.xpath(createShopButtonXpath)).click();
        return at(SupplierDialog.ShopsTab.class);
    }

    private boolean isShopDataEqualsWith(Shop shop) {
        return (getInputValue(nameField).contains(shop.getShopName()) &&
                getInputValue(address1Field).contains(shop.getShopAddress1()) &&
                getInputValue(address2Field).contains(shop.getShopAddress2()) &&
                getInputValue(postalCodeField).contains(shop.getPostalCode()) &&
                getInputValue(cityField).contains(shop.getShopCity()) &&
                getInputValue(phoneField).contains(shop.getPhone()));
    }


    public AddShopDialog assertShopDataIsEqualTo(Shop expectedData) {
        assertTrue(isShopDataEqualsWith(expectedData));
        return this;
    }

    public SupplierDialog.ShopsTab updateShop(Shop shop) {
        clearShopForm();
        fillShopForm(shop);
        find(By.xpath(updateShopButtonXpath)).click();
        return at(SupplierDialog.ShopsTab.class);
    }

    public SupplierDialog.ShopsTab cancel() {
        find(By.xpath(cancelButtonXpath)).click();
        return at(SupplierDialog.ShopsTab.class);
    }

    private void chooseShopType(ShopType shopType) {
        switch (shopType) {
            case RETAIL:
                retailTypeCheckbox.click();
                break;
            case RNV:
                rnVTypeCheckbox.click();
                break;
        }
    }

    private void clearShopForm() {
        nameField.clear();
        address1Field.clear();
        address2Field.clear();
        postalCodeField.clear();
        cityField.clear();
        phoneField.clear();
        eVoucherNotificationEmails.clear();
    }

    private void fillShopForm(Shop shop) {
        nameField.sendKeys(shop.getShopName());
        address1Field.sendKeys(shop.getShopAddress1());
        address2Field.sendKeys(shop.getShopAddress2());
        postalCodeField.sendKeys(shop.getPostalCode());
        cityField.sendKeys(shop.getShopCity());
        phoneField.sendKeys(shop.getPhone());
        eVoucherNotificationEmails.sendKeys(shop.geteVoucherEmail());
    }

    public AddShopDialog doAssert(Consumer<Asserts> assertFunc) {
        assertFunc.accept(new Asserts());
        return AddShopDialog.this;
    }

    public class Asserts {

        public Asserts assertIsShopDialogNotEditable() {
            Assert.assertTrue(Wait.invisible(editableShopDialog));
            return this;
        }
    }

}
