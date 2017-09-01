package com.scalepoint.automation.pageobjects.dialogs.eccadmin;

import com.scalepoint.automation.pageobjects.dialogs.BaseDialog;
import com.scalepoint.automation.pageobjects.extjs.ExtCheckbox;
import com.scalepoint.automation.pageobjects.extjs.ExtComboBox;
import com.scalepoint.automation.pageobjects.extjs.ExtInput;
import com.scalepoint.automation.utils.JavascriptHelper;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.data.entity.Shop;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

import java.util.List;
import java.util.function.Consumer;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompleted;
import static com.scalepoint.automation.utils.Wait.waitForEnabled;
import static com.scalepoint.automation.utils.Wait.waitForVisible;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class SupplierDialog extends BaseDialog implements SupplierTabs {

    public enum OrderMailFormat {
        PLAIN("Plain text", "PLAIN_TEXT_MAIL"),
        XML_ATTACHMENT("XML attachment", "NAVISION_XML_MAIL_ATTACHMENT"),
        XML_MAIL_BODY("XML mail body", "XML_MAIL_BODY"),
        VOUCHERS_ONLY_PLAIN("Voucher orders only Plain text", "VOUCHER_ORDER_ONLY_PLAIN_TEXT_MAIL"),
        VOUCHERS_ONLY_XML_ATTACHMENT("Voucher orders only XML attachment", "VOUCHER_ORDER_ONLY_XML_ATTACHMENT"),
        VOUCHERS_ONLY_XML_MAIL_BODY("Voucher orders only XML mail body", "VOUCHER_ORDER_ONLY_XML");

        private String option;
        private String value;

        OrderMailFormat(String optionText, String value) {
            this.option = optionText;
            this.value = value;
        }
    }

    @Override
    protected BaseDialog ensureWeAreAt() {
        Wait.waitForAjaxCompleted();
        return this;
    }

    public static class OrdersTab extends BaseDialog implements SupplierTabs {

        @FindBy(name = "orderEmail")
        private ExtInput emailField;

        @FindBy(xpath = "//table[contains(@class, 'supplier-order-mail-format')]")
        private ExtComboBox orderMailFormatSelect;

        @FindBy(id = "deliverySupportedId")
        private ExtCheckbox deliverySupportedCheckbox;

        @FindBy(name = "deliveryTime")
        private ExtInput defaultDeliveryTimeField;

        @FindBy(xpath = "//table[contains(@class, 'supplier-add-freight-price')]")
        private ExtCheckbox addFreightPriceCheckbox;

        @FindBy(xpath = "//table[contains(@class, 'supplier-products-only-for-claim-handling')]")
        private ExtCheckbox claimHandlingProductsCheckbox;

        public OrdersTab setOrderEmail(String email) {
            emailField.clear();
            emailField.sendKeys(email);
            return this;
        }

        public OrdersTab setOrderMailFormat(OrderMailFormat orderMailFormat) {
            orderMailFormatSelect.select(orderMailFormat.option);
            return this;
        }

        public OrdersTab setDefaultDeliveryTime(Integer deliveryTime) {
            deliverySupportedCheckbox.set(true);
            defaultDeliveryTimeField.clear();
            defaultDeliveryTimeField.sendKeys(deliveryTime.toString());
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

        public OrdersTab doAssert(Consumer<OrdersTab.Asserts> assertFunc) {
            assertFunc.accept(new OrdersTab.Asserts());
            return OrdersTab.this;
        }

        public class Asserts {
            public Asserts assertOrderEmailIs(String email) {
                Assert.assertEquals(emailField.getText(), email);
                return this;
            }

            public Asserts assertOrderEmailFormatIs(OrderMailFormat orderMailFormat) {
                Assert.assertEquals(orderMailFormatSelect.getValue(), orderMailFormat.value);
                return this;
            }

            public Asserts assertDeliveryTimeIs(Integer deliveryTime) {
                Assert.assertTrue(deliverySupportedCheckbox.isSelected());
                Assert.assertEquals(defaultDeliveryTimeField.getText(), deliveryTime.toString());
                return this;
            }

            public Asserts assertFreightPriceUsed() {
                Assert.assertTrue(addFreightPriceCheckbox.isSelected());
                return this;
            }

            public Asserts assertProductsUsedAsVouchers() {
                Assert.assertTrue(claimHandlingProductsCheckbox.isSelected());
                return this;
            }
        }

        @Override
        protected BaseDialog ensureWeAreAt() {
            waitForAjaxCompleted();
            return this;
        }
    }

    public static class BannerTab extends BaseDialog implements SupplierTabs {

        public BannerTab uploadBanner(String bannerPath) {
            WebElement elem = find(By.xpath("//input[contains(@id, 'supplierBannerFileId') and contains(@type, 'file')]"));
            enterToHiddenUploadFileField(elem, bannerPath);
            return this;
        }

        public BannerTab doAssert(Consumer<BannerTab.Asserts> assertFunc) {
            assertFunc.accept(new BannerTab.Asserts());
            return BannerTab.this;
        }

        public class Asserts {
            public BannerTab.Asserts assertBannerIsPresent() {
                assertTrue(JavascriptHelper.isImagePresent(driver.findElement(By.className("bannerUploadImg"))));
                return this;
            }
        }

        @Override
        protected BaseDialog ensureWeAreAt() {
            waitForAjaxCompleted();
            return this;
        }
    }

    public static class GeneralTab extends BaseDialog implements SupplierTabs {

        @FindBy(name = "name")
        private WebElement name;

        @FindBy(name = "cvr")
        private WebElement cvr;

        @FindBy(name = "address")
        private WebElement address1;

        @FindBy(name = "address2")
        private WebElement address2;

        @FindBy(name = "city")
        private WebElement city;

        @FindBy(name = "postalCode")
        private WebElement postalCode;

        @FindBy(name = "phone")
        private WebElement phone;

        @FindBy(xpath = ".//*[contains(@class,'x-window-header-text')]")
        private WebElement windowHeader;

        @FindBy(xpath = ".//*[contains(@class,'add-supplier-create-btn')]")
        private WebElement createSupplierButton;

        @FindBy(name = "website")
        private WebElement website;

        @FindBy(name = "fileData")
        private WebElement supplierLogo;

        @FindBy(id = "editSupplierTabPanelId")
        private WebElement editableSupplierDialog;

        @Override
        protected boolean areWeAt() {
            Wait.waitForAjaxCompleted();
            try {
                return name.isDisplayed();
            }catch (Exception e){
                logger.error(e.getMessage());
                return false;
            }
        }

        public GeneralTab setName(String name) {
            this.name.clear();
            this.name.sendKeys(name);
            return this;
        }

        public GeneralTab fill(Consumer<GeneralTab> fillFunc) {
            fillFunc.accept(this);
            return this;
        }

        public GeneralTab setWebsite(String webSite) {
            this.website.sendKeys(webSite);
            return this;
        }

        public GeneralTab uploadLogo(String logoPath) {
            WebElement elem = find(By.xpath("//input[contains(@id, 'supplierLogoFileId') and contains(@type, 'file')]"));
            enterToHiddenUploadFileField(elem, logoPath);
            Wait.waitForDisplayed(By.cssSelector(("img.imageUploadImg")));
            return this;
        }

        public GeneralTab doAssert(Consumer<GeneralTab.Asserts> assertFunc) {
            assertFunc.accept(new GeneralTab.Asserts());
            return GeneralTab.this;
        }

        public class Asserts {
            public GeneralTab.Asserts assertLogoPresent() {
                assertTrue(JavascriptHelper.isImagePresent(driver.findElement(By.className("imageUploadImg"))));
                return this;
            }

            public Asserts assertCvr(String cvrValue) {
                Assert.assertEquals(cvr.getAttribute("value"), cvrValue);
                return this;
            }

            public Asserts assertAddress(String address1Value, String address2Value) {
                Assert.assertEquals(address1.getAttribute("value"), address1Value);
                Assert.assertEquals(address2.getAttribute("value"), address2Value);
                return this;
            }

            public Asserts assertCity(String cityValue) {
                Assert.assertEquals(waitForVisible(city).getAttribute("value"), cityValue);
                return this;
            }

            public Asserts assertPostalCode(String postalCodeValue) {
                Assert.assertEquals(postalCode.getAttribute("value"), postalCodeValue);
                return this;
            }

            public Asserts assertWebsite(String websiteValue) {
                Assert.assertEquals(website.getAttribute("value"), websiteValue);
                return this;
            }

            public Asserts assertIsDialogNotEditable(){
                Assert.assertTrue(Wait.invisibleOfElement(By.id("editSupplierTabPanelId")));
                return this;
            }
        }

        public static class FormFiller {

            private GeneralTab dialog;

            public FormFiller(GeneralTab dialog) {
                this.dialog = dialog;
            }

            public GeneralTab.FormFiller withSupplierName(String supplierName) {
                clearField(dialog.name);
                dialog.name.sendKeys(supplierName);
                return this;
            }

            public GeneralTab.FormFiller withCvr(String cvr) {
                clearField(dialog.cvr);
                dialog.cvr.sendKeys(cvr);
                return this;
            }

            public GeneralTab.FormFiller withAddress1(String address1) {
                clearField(dialog.address1);
                dialog.address1.sendKeys(address1);
                return this;
            }

            public GeneralTab.FormFiller withAddress2(String address2) {
                clearField(dialog.address2);
                dialog.address2.sendKeys(address2);
                return this;
            }

            public GeneralTab.FormFiller withCity(String city) {
                clearField(dialog.city);
                dialog.city.sendKeys(city);
                return this;
            }

            public GeneralTab.FormFiller withPostalCode(String postalCode) {
                clearField(dialog.postalCode);
                dialog.postalCode.sendKeys(postalCode);
                return this;
            }

            public GeneralTab.FormFiller withWebsite(String website) {
                clearField(dialog.website);
                dialog.website.sendKeys(website);
                return this;
            }

            private void clearField(WebElement element) {
                element.sendKeys(Keys.chord(Keys.CONTROL, "a"), "");
                if(!element.equals("")){
                    element.clear();
                }
            }

        }

        @Override
        protected BaseDialog ensureWeAreAt() {
            return this;
        }
    }

    public static class AgreementsTab extends BaseDialog implements SupplierTabs {

        @FindBy(xpath = "//td[contains(@class, 'agreementsPanelExclusiveId')]")
        private WebElement exclusiveGridCell;

        @FindBy(xpath = "//td[contains(@class, 'voucherNameId')]/div")
        private List<WebElement> voucherNameGridCell;

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

        public VoucherAgreementDialog.GeneralTab editVoucherAgreement(String agreementName) {
            int i = 0;
            while (!isOn(VoucherAgreementDialog.GeneralTab.class) && i<2){
                i++;
                doubleClick(By.xpath("id('supplierVouchersGridId-body')//div[contains(text(),'"+agreementName+"')]"));
            }
            Wait.waitForAjaxCompleted();
            return at(VoucherAgreementDialog.GeneralTab.class);
        }

        public enum  ActionType {
            LEAVE,
            JOIN
        }

        public boolean isExclusiveTickForFirstVoucherAvailable(){
            return exclusiveGridCell.getAttribute("class").contains("tick");
        }

        private WebElement findVoucher(String voucherName){
            return voucherNameGridCell.stream()
                    .filter(element -> element.getText().equals(voucherName))
                    .findAny()
                    .get();
        }

        public boolean isExclusiveTickForVoucherAvailable(String voucherName){
            return findVoucher(voucherName).findElement(By.xpath("./ancestor::tr//td[contains(@class, 'agreementsPanelExclusiveId')]"))
                    .getAttribute("class").contains("tick");
        }

        public AgreementsTab doWithAgreement(String voucherAgreementName, ActionType actionType) {
            By voucherRow = By.xpath("//div[@id='supplierVouchersGridId']//div[text()='" + voucherAgreementName + "']/ancestor::tr");
            $(voucherRow).click();

            By actionButtonBy = By.className("supplier-join-leave-voucher-agreement-btn");
            Wait.waitForEnabled(actionButtonBy);

            WebElement actionButton = $(actionButtonBy);
            Assert.assertEquals(actionButton.getText(), actionType == ActionType.JOIN ? "Join" : "Leave");
            actionButton.click();

            By alertMessageBy = By.xpath(".//div[contains(@id, 'messagebox')]//span[text()='Yes']//ancestor::a");
            Wait.waitForDisplayed(alertMessageBy);
            $(alertMessageBy).click();

            Wait.waitForAjaxCompleted();
            $(voucherRow).click();

            Assert.assertEquals($(actionButton).getText(), actionType == ActionType.JOIN ? "Leave" : "Join");
            return this;
        }

        public AgreementsTab doAssert(Consumer<Asserts> assertFunc) {
            assertFunc.accept(new Asserts());
            return AgreementsTab.this;
        }

        public class Asserts {
            public Asserts assertVoucherStatus(String voucherName, boolean active) {
                By voucherRowActive = By.xpath("//div[@id='supplierVouchersGridId']//div[text()='" + voucherName + "']/ancestor::tr//td[4]");
                assertEquals($(voucherRowActive).getText(), active ? "Yes" : "No");
                return this;
            }

            public Asserts assertVoucherAbsent(String voucherName) {
                Assert.assertFalse($(By.xpath("//div[@id='supplierVouchersGridId']//div[text()='" + voucherName + "']")).exists());
                return this;
            }

            public Asserts assertIsExclusiveTickForVoucherVisible(){
                Assert.assertTrue(isExclusiveTickForFirstVoucherAvailable());
                return this;
            }

            public Asserts assertIsExclusiveTickForVoucherNotVisible(String voucherName){
                Assert.assertFalse(isExclusiveTickForVoucherAvailable(voucherName));
                return this;
            }

            public Asserts assertIsExclusiveTickForVoucherNotVisible(){
                Assert.assertFalse(isExclusiveTickForFirstVoucherAvailable());
                return this;
            }
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

        @FindBy(id = "supplierShopsGridId")
        private WebElement shopsGridId;

        private String byShopNameXpath = "id('supplierShopsGridId')//div[contains(text(),'$1')]";

        @Override
        protected BaseDialog ensureWeAreAt() {
            Wait.waitForVisible(shopsGridId);
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

        public AddShopDialog openShop(String shopName) {
            WebElement item = find(byShopNameXpath, shopName);
            scrollTo(item);
            doubleClick(item);
            return at(AddShopDialog.class);
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
            clickElementUsingJS(deleteShopYesButton);
            waitForAjaxCompleted();
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
