package com.scalepoint.automation.pageobjects.dialogs.eccadmin;

import com.codeborne.selenide.Condition;
import com.scalepoint.automation.pageobjects.dialogs.BaseDialog;
import com.scalepoint.automation.pageobjects.dialogs.GdprConfirmationDialog;
import com.scalepoint.automation.utils.data.entity.Shop;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompleted;
import static org.testng.Assert.assertTrue;

public class AddShopDialog extends BaseDialog {

    public enum ShopType {
        RETAIL,
        RNV
    }

    private static final String SUPPLIER_CREATE_SHOP_BTN = "//a[contains(@class,'supplier-create-shop-btn')]";
    private static final String SUPPLIER_SAVE_SHOP_BTN = "//a[contains(@class,'supplier-save-shop-btn')]";
    private static final String SUPPLIER_CANCEL_EDIT_SHOP_BTN = "//a[contains(@class,'supplier-cancel-edit-shop-btn')]";

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
    protected AddShopDialog ensureWeAreAt() {
        waitForJavascriptRecalculation();
        waitForAjaxCompleted();
        $(nameLabel).waitUntil(Condition.visible, TIME_OUT_IN_MILISECONDS);
        return this;
    }

    public SupplierDialog.ShopsTab createShop(Shop shop, ShopType shopType, boolean gdpr) {
        fillShopForm(shop);
        chooseShopType(shopType);
        find(By.xpath(SUPPLIER_CREATE_SHOP_BTN)).click();
        if(gdpr){
            at(GdprConfirmationDialog.class)
                    .confirm();
        }
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
        find(By.xpath(SUPPLIER_SAVE_SHOP_BTN)).click();
        return at(SupplierDialog.ShopsTab.class);
    }

    public SupplierDialog.ShopsTab cancel() {
        find(By.xpath(SUPPLIER_CANCEL_EDIT_SHOP_BTN)).click();
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
        eVoucherNotificationEmails.sendKeys(shop.getEvoucherEmail());
    }

}
