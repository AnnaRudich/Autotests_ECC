package com.scalepoint.automation.pageobjects.dialogs.eccadmin;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.pageobjects.dialogs.BaseDialog;
import com.scalepoint.automation.pageobjects.dialogs.BaseDialogSelenide;
import com.scalepoint.automation.pageobjects.dialogs.GdprConfirmationDialog;
import com.scalepoint.automation.pageobjects.dialogs.eccadmin.suppliersdialog.supplierdialogtab.ShopsTab;
import com.scalepoint.automation.utils.data.entity.input.Shop;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;
import static org.testng.Assert.assertTrue;

public class AddShopDialog extends BaseDialogSelenide {

    public enum ShopType {

        RETAIL,
        RNV
    }

    private static final String SUPPLIER_CREATE_SHOP_BTN = "//a[contains(@class,'supplier-create-shop-btn')]";
    private static final String SUPPLIER_SAVE_SHOP_BTN = "//a[contains(@class,'supplier-save-shop-btn')]";
    private static final String SUPPLIER_CANCEL_EDIT_SHOP_BTN = "//a[contains(@class,'supplier-cancel-edit-shop-btn')]";

    @FindBy(xpath = "//label[contains(text(),'Name:')]")
    private SelenideElement nameLabel;
    @FindBy(name = "shopName")
    private SelenideElement nameField;
    @FindBy(name = "shopAddress")
    private SelenideElement address1Field;
    @FindBy(name = "shopAddress2")
    private SelenideElement address2Field;
    @FindBy(name = "shopPostalCode")
    private SelenideElement postalCodeField;
    @FindBy(name = "shopCity")
    private SelenideElement cityField;
    @FindBy(name = "shopPhone")
    private SelenideElement phoneField;
    @FindBy(name = "portalCode")
    private SelenideElement portalCode;
    @FindBy(name = "eVoucherNotificationEmails")
    private SelenideElement eVoucherNotificationEmails;
    @FindBy(css = "td.x-form-check-group table:nth-child(1) td input")
    private SelenideElement retailTypeCheckbox;
    @FindBy(css = "td.x-form-check-group table:nth-child(2) td input")
    private SelenideElement rnVTypeCheckbox;
    @FindBy(id = "cancelCreateSupplierBtnId")
    private SelenideElement cancelButton;
    @FindBy(id = "editSupplierShopTabFormId")
    private SelenideElement editableShopDialog;

    @Override
    protected void ensureWeAreAt() {

        waitForAjaxCompletedAndJsRecalculation();
        nameLabel.should(Condition.visible);
    }

    public ShopsTab createShop(Shop shop, ShopType shopType, boolean gdpr) {

        fillShopForm(shop);
        chooseShopType(shopType);
        hoverAndClick($(By.xpath(SUPPLIER_CREATE_SHOP_BTN)));

        if(gdpr){

            BaseDialogSelenide
                    .at(GdprConfirmationDialog.class)
                    .confirm();
        }
        return BaseDialog.at(ShopsTab.class);
    }

    private boolean isShopDataEqualsWith(Shop shop) {
        return (nameField.getValue().contains(shop.getShopName()) &&
                address1Field.getValue().contains(shop.getShopAddress1()) &&
                address2Field.getValue().contains(shop.getShopAddress2()) &&
                postalCodeField.getValue().contains(shop.getPostalCode()) &&
                cityField.getValue().contains(shop.getShopCity()) &&
                phoneField.getValue().contains(shop.getPhone()));
    }


    public AddShopDialog assertShopDataIsEqualTo(Shop expectedData) {

        assertTrue(isShopDataEqualsWith(expectedData));
        return this;
    }

    public ShopsTab updateShop(Shop shop) {

        clearShopForm();
        fillShopForm(shop);
        hoverAndClick($(By.xpath(SUPPLIER_SAVE_SHOP_BTN)));
        return BaseDialog.at(ShopsTab.class);
    }

    public ShopsTab cancel() {

        hoverAndClick($(By.xpath(SUPPLIER_CANCEL_EDIT_SHOP_BTN)));
        return BaseDialog.at(ShopsTab.class);
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
