package com.scalepoint.automation.pageobjects.dialogs.eccadmin;

import com.scalepoint.automation.pageobjects.dialogs.BaseDialog;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.data.entity.Shop;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static com.scalepoint.automation.utils.Wait.waitForElementEnabling;
import static com.scalepoint.automation.utils.Wait.waitForStableElements;

public class SupplierDialog extends BaseDialog implements SupplierTabs {

    @Override
    protected BaseDialog ensureWeAreAt() {
        Wait.waitForAjaxCompleted();
        return this;
    }

    public static class ShopsTab extends BaseDialog implements SupplierTabs {

        @FindBy(xpath = "//a[contains(@class,'supplier-add-shop-btn')]")
        private WebElement addShopButton;

        @FindBy(xpath = "//a[contains(@class,'supplier-delete-shop-btn')]")
        private WebElement deleteShopButton;

        @FindBy(xpath = "//a[contains(@class,'supplier-import-shop-btn')]")
        private WebElement importShopButton;

        @FindBy(xpath = "//div[contains(@class,'supplier-delete-shop-confirm-window')]//span[contains(text(),'Yes')]")
        private WebElement deleteShopYesButton;

        private String byShopNameXpath = "id('supplierShopsGridId')//div[contains(text(),'$1')]";

        @Override
        protected BaseDialog ensureWeAreAt() {
            Wait.waitForVisible(addShopButton);
            return this;
        }

        public AddShopDialog openAddShopDialog() {
            addShopButton.click();
            return BaseDialog.at(AddShopDialog.class);
        }

        public boolean isNewShopExists(Shop shop) {
            try {
                WebElement item = find(byShopNameXpath, shop.getShopName());
                scrollTo(item);
                return true;
            } catch (Exception e) {
                return false;
            }
        }

        public ShopsTab selectShop(Shop shop) {
            WebElement item = find(byShopNameXpath, shop.getShopName());
            scrollTo(item);
            clickAndWaitForEnabling(item, By.xpath("//div[contains(@class,'SupplierWindow ')]//span[contains(text(),'Delete shop')]"));
            return this;
        }

        public AddShopDialog openEditShopDialog(String shopName) {
            WebElement item = find(byShopNameXpath, shopName);
            scrollTo(item);
            doubleClick(item);
            waitForElementEnabling(By.name("shopName"));
            return at(AddShopDialog.class);
        }

        public SupplierDialog.ShopsTab deleteShop(Shop shop) {
            selectShop(shop);
            deleteShopButton.click();
            deleteShopYesButton.click();
            return this;
        }
    }
}
