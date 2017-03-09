package com.scalepoint.automation.pageobjects.dialogs.eccadmin;

import com.scalepoint.automation.pageobjects.dialogs.BaseDialog;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.data.entity.Shop;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

import java.util.function.Consumer;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForEnabled;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class SupplierDialog extends BaseDialog implements SupplierTabs {

    @Override
    protected BaseDialog ensureWeAreAt() {
        Wait.waitForAjaxCompleted();
        return this;
    }

    public static class GeneralTab extends BaseDialog implements SupplierTabs {

        @FindBy(name = "website")
        private WebElement website;

        @Override
        protected BaseDialog ensureWeAreAt() {
            return this;
        }
    }

    public static class AgreementsTab extends BaseDialog implements SupplierTabs {

        @FindBy(className = "supplier-new-voucher-agreement-btn")
        private WebElement createNewVoucherAgreementBtn;

        @Override
        protected BaseDialog ensureWeAreAt() {
            Wait.waitForVisible(createNewVoucherAgreementBtn);
            return this;
        }

        public CreateVoucherAgreementDialog openCreateVoucherAgreementDialog() {
            createNewVoucherAgreementBtn.click();
            return at(CreateVoucherAgreementDialog.class);
        }

        public AgreementsTab leaveAgreement(String voucherAgreementName) {
            By voucherRow = By.xpath("//div[@id='supplierVouchersGridId']//div[text()='" + voucherAgreementName + "']/ancestor::tr");
            $(voucherRow).click();

            By leaveButtonBy = By.className("supplier-join-leave-voucher-agreement-btn");
            Wait.waitForEnabled(leaveButtonBy);

            WebElement leaveButton = $(leaveButtonBy);
            Assert.assertEquals(leaveButton.getText(), "Leave");
            leaveButton.click();

            By alertMessageBy = By.xpath(".//div[contains(@id, 'messagebox')]//span[text()='Yes']//ancestor::a");
            Wait.waitForDisplayed(alertMessageBy);
            $(alertMessageBy).click();

            Wait.waitForAjaxCompleted();
            $(voucherRow).click();

            Assert.assertEquals($(leaveButton).getText(), "Join");
            return this;
        }
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

        boolean isNewShopExists(Shop shop) {
            try {
                WebElement item = find(byShopNameXpath, shop.getShopName());
                scrollTo(item);
                return true;
            } catch (Exception e) {
                return false;
            }
        }

        ShopsTab selectShop(Shop shop) {
            WebElement item = find(byShopNameXpath, shop.getShopName());
            scrollTo(item);
            clickAndWaitForEnabling(item, By.xpath("//div[contains(@class,'SupplierWindow ')]//span[contains(text(),'Delete shop')]"));
            return this;
        }

        public AddShopDialog openEditShopDialog(String shopName) {
            WebElement item = find(byShopNameXpath, shopName);
            scrollTo(item);
            doubleClick(item);
            waitForEnabled(By.name("shopName"));
            return at(AddShopDialog.class);
        }

        public SupplierDialog.ShopsTab deleteShop(Shop shop) {
            selectShop(shop);
            deleteShopButton.click();
            deleteShopYesButton.click();
            return this;
        }

        public ShopsTab doAssert(Consumer<Asserts> assertFunc) {
            assertFunc.accept(new Asserts());
            return ShopsTab.this;
        }

        public class Asserts {
            public Asserts assertNewShopExists(Shop shop) {
                assertTrue(isNewShopExists(shop));
                return this;
            }

            public Asserts assertNewShopNotExists(Shop shop) {
                assertFalse(isNewShopExists(shop));
                return this;
            }
        }
    }
}
