package com.scalepoint.automation.pageobjects.dialogs.eccadmin;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.pageobjects.dialogs.BaseDialog;
import com.scalepoint.automation.pageobjects.dialogs.eccadmin.voucheagreementtab.VoucherAgreementGeneralTab;
import com.scalepoint.automation.pageobjects.extjs.ExtCheckbox;
import com.scalepoint.automation.pageobjects.extjs.ExtComboBox;
import com.scalepoint.automation.pageobjects.extjs.ExtInput;
import com.scalepoint.automation.pageobjects.extjs.ExtRadioGroup;
import com.scalepoint.automation.utils.JavascriptHelper;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.data.entity.input.Shop;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.*;
import static org.testng.Assert.*;

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
    protected void ensureWeAreAt() {
        waitForAjaxCompletedAndJsRecalculation();
    }

    public static class OrdersTab extends BaseDialog implements SupplierTabs {

        @FindBy(name = "orderEmail")
        private ExtInput emailField;

        @FindBy(id = "orderFlowOldFlow")
        private WebElement radioOldOrderFlow;

        @FindBy(id = "orderFlowOrderService")
        private WebElement radioOrderService;

        @FindBy(xpath = "//table[contains(@class, 'supplier-order-mail-format')]")
        private ExtComboBox orderMailFormatSelect;

        @FindBy(xpath = "//table[contains(@class, 'supplier-add-freight-price')]")
        private ExtCheckbox addFreightPriceCheckbox;

        @FindBy(id = "deliverySupportedId")
        private ExtCheckbox deliverySupportedCheckbox;

        @FindBy(name = "deliveryTime")
        private ExtInput defaultDeliveryTimeField;

        @FindBy(xpath = "//table[contains(@class, 'supplier-products-only-for-claim-handling')]")
        private ExtCheckbox claimHandlingProductsCheckbox;

        @FindBy(id = "invoiceSettingRadioGroup")
        private ExtRadioGroup invoiceSettings;

        public ExtRadioGroup getInvoiceSettings() {
            return invoiceSettings;
        }

        public OrdersTab selectInvoiceSetting(int value) {
            invoiceSettings.select(value);
            return this;
        }

        public OrdersTab setOrderEmail(String email) {
            emailField.clear();
            emailField.sendKeys(email);
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

            public Asserts assertInvoiceSettingIs(int value) {
                Assert.assertEquals(invoiceSettings.getSelected(), value);
                return this;
            }

            public Asserts assertOldOrderFlowItemsDisabled() {
                Assert.assertFalse(orderMailFormatSelect.isInputElementEnabled());
                Assert.assertFalse(addFreightPriceCheckbox.isInputElementEnabled());
                return this;
            }
        }

        @Override
        protected void ensureWeAreAt() {
            waitForAjaxCompletedAndJsRecalculation();
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
                assertTrue(JavascriptHelper.isImagePresent(waitForDisplayed(By.className("bannerUploadImg"))));
                return this;
            }
        }

        @Override
        protected void ensureWeAreAt() {
            waitForAjaxCompletedAndJsRecalculation();
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

        @FindBy(xpath = ".//div[contains(@class,'SupplierWindow')]//span[contains(@class,'x-window-header-text')]")
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
            Wait.waitForJavascriptRecalculation();
            Wait.waitForAjaxCompleted();
            try {
                return name.isDisplayed();
            } catch (Exception e) {
                logger.error(e.getMessage());
                return false;
            }
        }

        @Override
        protected void ensureWeAreAt() {
            waitForAjaxCompletedAndJsRecalculation();
            $(windowHeader).waitUntil(Condition.matchText("[(Edit)(View)] supplier .*"), TIME_OUT_IN_MILISECONDS);
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

            public static final String VALUE = "value";

            public GeneralTab.Asserts assertLogoPresent() {
                assertTrue(JavascriptHelper.isImagePresent(driver.findElement(By.className("imageUploadImg"))));
                return this;
            }

            public Asserts assertCvr(String cvrValue) {
                Assert.assertEquals(cvr.getAttribute(VALUE), cvrValue);
                return this;
            }

            public Asserts assertAddress(String address1Value, String address2Value) {
                Assert.assertEquals(address1.getAttribute(VALUE), address1Value);
                Assert.assertEquals(address2.getAttribute(VALUE), address2Value);
                return this;
            }

            public Asserts assertCity(String cityValue) {
                Assert.assertEquals(waitForVisible(city).getAttribute(VALUE), cityValue);
                return this;
            }

            public Asserts assertPostalCode(String postalCodeValue) {
                Assert.assertEquals(postalCode.getAttribute(VALUE), postalCodeValue);
                return this;
            }

            public Asserts assertWebsite(String websiteValue) {
                Assert.assertEquals(website.getAttribute(VALUE), websiteValue);
                return this;
            }

            public Asserts assertIsDialogNotEditable() {
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
                JavascriptHelper.blur();
                return this;
            }

            public GeneralTab.FormFiller withCvr(String cvr) {
                clearField(dialog.cvr);
                dialog.cvr.sendKeys(cvr);
                JavascriptHelper.blur();
                return this;
            }

            public GeneralTab.FormFiller withAddress1(String address1) {
                clearField(dialog.address1);
                dialog.address1.sendKeys(address1);
                JavascriptHelper.blur();
                return this;
            }

            public GeneralTab.FormFiller withAddress2(String address2) {
                clearField(dialog.address2);
                dialog.address2.sendKeys(address2);
                JavascriptHelper.blur();
                return this;
            }

            public GeneralTab.FormFiller withCity(String city) {
                clearField(dialog.city);
                dialog.city.sendKeys(city);
                JavascriptHelper.blur();
                return this;
            }

            public GeneralTab.FormFiller withPostalCode(String postalCode) {
                clearField(dialog.postalCode);
                dialog.postalCode.sendKeys(postalCode);
                JavascriptHelper.blur();
                return this;
            }

            public GeneralTab.FormFiller withWebsite(String website) {
                clearField(dialog.website);
                dialog.website.sendKeys(website);
                JavascriptHelper.blur();
                return this;
            }

            private void clearField(WebElement element) {
                SelenideElement field = $(element);
                field.click();
                field.clear();
                Wait.waitForJavascriptRecalculation();
                Wait.waitForAjaxCompleted();
            }

        }
    }

    public static class GeneralTabReadMode extends BaseDialog implements SupplierTabs {

        @FindBy(xpath = "//label[contains(text(),'Supplier name:')]")
        private WebElement name;

        @Override
        protected boolean areWeAt() {
            Wait.waitForAjaxCompleted();
            try {
                return this.name.isDisplayed() && driver.findElements(By.name("name")).isEmpty();
            } catch (Exception e) {
                logger.error(e.getMessage());
                return false;
            }
        }

        public GeneralTabReadMode setName(String name) {
            this.name.clear();
            this.name.sendKeys(name);
            return this;
        }

        @Override
        protected void ensureWeAreAt() {
            waitForAjaxCompletedAndJsRecalculation();
        }
    }

    public static class AgreementsTab extends BaseDialog implements SupplierTabs {

        public static final String DIV_ID_SUPPLIER_VOUCHERS_GRID_ID_DIV_TEXT = "//div[@id='supplierVouchersGridId']//div[text()='";
        @FindBy(xpath = "//td[contains(@class, 'agreementsPanelExclusiveId')]")
        private WebElement exclusiveGridCell;

        @FindBy(xpath = "//td[contains(@class, 'voucherNameId')]/div")
        private List<WebElement> voucherNameGridCell;

        @FindBy(className = "supplier-new-voucher-agreement-btn")
        private WebElement createNewVoucherAgreementBtn;

        @Override
        protected void ensureWeAreAt() {
            waitForAjaxCompletedAndJsRecalculation();
            $(createNewVoucherAgreementBtn).waitUntil(Condition.visible, TIME_OUT_IN_MILISECONDS);
        }

        public CreateVoucherAgreementDialog openCreateVoucherAgreementDialog() {
            $(createNewVoucherAgreementBtn).click();
            return at(CreateVoucherAgreementDialog.class);
        }

        public VoucherAgreementGeneralTab editVoucherAgreement(String agreementName) {
            Wait.waitForAjaxCompleted();
            doubleClick(By.xpath("id('supplierVouchersGridId-body')//div[contains(text(),'" + agreementName + "')]"));
            isOn(VoucherAgreementGeneralTab.class);
            Wait.waitForAjaxCompleted();
            return at(VoucherAgreementGeneralTab.class);
        }

        public enum ActionType {
            LEAVE,
            JOIN
        }

        public boolean isExclusiveTickForFirstVoucherAvailable() {
            return exclusiveGridCell.getAttribute("class").contains("tick");
        }

        private WebElement findVoucher(String voucherName) {
            return voucherNameGridCell.stream()
                    .filter(element -> element.getText().equals(voucherName))
                    .findAny().orElseThrow(() -> new NoSuchElementException("Can't find voucher with name " + voucherName));
        }

        public boolean isExclusiveTickForVoucherAvailable(String voucherName) {
            return findVoucher(voucherName).findElement(By.xpath("./ancestor::tr//td[contains(@class, 'agreementsPanelExclusiveId')]"))
                    .getAttribute("class").contains("tick");
        }

        public AgreementsTab doWithAgreement(String voucherAgreementName, ActionType actionType) {
            By voucherRow = By.xpath(DIV_ID_SUPPLIER_VOUCHERS_GRID_ID_DIV_TEXT + voucherAgreementName + "']/ancestor::tr");
            $(voucherRow).click();

            By actionButtonBy = By.className("supplier-join-leave-voucher-agreement-btn");
            Wait.waitForVisibleAndEnabled(actionButtonBy);

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
                By voucherRowActive = By.xpath(DIV_ID_SUPPLIER_VOUCHERS_GRID_ID_DIV_TEXT + voucherName + "']/ancestor::tr//td[4]");
                assertEquals($(voucherRowActive).getText(), active ? "Yes" : "No");
                return this;
            }

            public Asserts assertVoucherAbsent(String voucherName) {
                Assert.assertFalse($(By.xpath(DIV_ID_SUPPLIER_VOUCHERS_GRID_ID_DIV_TEXT + voucherName + "']")).exists());
                return this;
            }

            public Asserts assertShopOnlyVoucherIsPresent(String voucherName){
                Assert.assertTrue($(By.xpath(DIV_ID_SUPPLIER_VOUCHERS_GRID_ID_DIV_TEXT + voucherName + "_SHOP"+"']")).exists());
                return this;
            }

            public Asserts assertIsExclusiveTickForVoucherVisible() {
                Assert.assertTrue(isExclusiveTickForFirstVoucherAvailable());
                return this;
            }

            public Asserts assertIsExclusiveTickForVoucherNotVisible(String voucherName) {
                Assert.assertFalse(isExclusiveTickForVoucherAvailable(voucherName));
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

        private String byShopNameXpath = " //div[@id='supplierShopsGridId']//div[contains(text(),'%s')]";

        @Override
        protected void ensureWeAreAt() {
            waitForAjaxCompletedAndJsRecalculation();
            $(shopsGridId).waitUntil(Condition.visible, TIME_OUT_IN_MILISECONDS);
        }

        public AddShopDialog openAddShopDialog() {
            addShopButton.click();
            return BaseDialog.at(AddShopDialog.class);
        }

        boolean isNewShopExists(Shop shop) {
            try {
                SelenideElement element = $(By.xpath(String.format(byShopNameXpath, shop.getShopName())));
                element.scrollTo();
                return true;
            } catch (Error e) {
                return false;
            }
        }

        ShopsTab selectShop(Shop shop) {
            SelenideElement element = $(By.xpath(String.format(byShopNameXpath, shop.getShopName())));
            element.scrollTo();
            clickAndWaitForEnabling(element, By.xpath("//div[contains(@class,'SupplierWindow ')]//span[contains(text(),'Delete shop')]"));
            return this;
        }

//        public AddShopDialog openShop(String shopName) {
//            WebElement item = find(byShopNameXpath, shopName);
//            scrollTo(item);
//            doubleClick(item);
//            return at(AddShopDialog.class);
//        }

        public AddShopDialogViewMode openShopViewModel(String shopName) {
            SelenideElement element = $(By.xpath(String.format(byShopNameXpath, shopName)));
            element
                    .scrollTo()
                    .doubleClick();
            return at(AddShopDialogViewMode.class);
        }

        public AddShopDialog openEditShopDialog(String shopName) {
            SelenideElement element = $(By.xpath(String.format(byShopNameXpath, shopName)));
            element
                    .scrollTo()
                    .doubleClick();
            Wait.waitForVisibleAndEnabled(By.name("shopName"));
            return at(AddShopDialog.class);
        }

        public SupplierDialog.ShopsTab deleteShop(Shop shop) {
            selectShop(shop);
            deleteShopButton.click();
            clickUsingJavaScriptIfClickDoesNotWork(deleteShopYesButton);
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
