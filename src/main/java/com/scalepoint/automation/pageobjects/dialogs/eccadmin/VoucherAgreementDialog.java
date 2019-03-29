package com.scalepoint.automation.pageobjects.dialogs.eccadmin;

import com.scalepoint.automation.pageobjects.dialogs.BaseDialog;
import com.scalepoint.automation.pageobjects.extjs.ExtCheckbox;
import com.scalepoint.automation.pageobjects.extjs.ExtComboBox;
import com.scalepoint.automation.pageobjects.extjs.ExtInput;
import com.scalepoint.automation.pageobjects.extjs.ExtRadioButton;
import com.scalepoint.automation.utils.JavascriptHelper;
import com.scalepoint.automation.utils.Wait;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

import java.util.List;
import java.util.function.Consumer;

import static com.codeborne.selenide.Selenide.$;
import static org.testng.Assert.assertTrue;

public class VoucherAgreementDialog extends BaseDialog implements VoucherAgreementTabs {

    @Override
    protected BaseDialog ensureWeAreAt() {
        Wait.waitForAjaxCompleted();
        return this;
    }

    public static class GeneralTab extends BaseDialog implements VoucherAgreementTabs {

        @FindBy(name = "voucherName")
        private WebElement voucherNameInput;

        @FindBy(name = "agreementDiscount")
        private WebElement agreementDiscount;

        @FindBy(xpath = "//table[contains(@class, 'supplier-voucher-agreement-status')]")
        private ExtComboBox agreementStatusCombo;

        @FindBy(name = "agreementDiscount")
        private ExtInput discountInput;

        @FindBy(xpath = "//table[contains(@class, 'supplier-voucher-custom-logo-radio')]//td[contains(@class, 'x-form-item-body')]//label")
        private WebElement useCustomLogoRadio;

        @FindBy(name = "minimumAmount")
        private ExtInput minimumAmountInput;

        private By logoImageXpath = By.xpath("//div[@id='voucherLogoImageId']//img[@class='voucherImageUploadImg']");
        private By imageXpath = By.xpath("//div[@id='voucherImageId']//img[@class='voucherImageUploadImg']");

        @FindBy(name = "stepAmount")
        private ExtInput stepAmountInput;

        public static class FormFiller {

            private GeneralTab dialog;

            public FormFiller(GeneralTab dialog) {
                this.dialog = dialog;
            }

            public FormFiller withDiscount(Integer discount) {
                dialog.discountInput.clear();
                dialog.discountInput.setValue(discount.toString());
                return this;
            }

            public FormFiller withFaceValue(Integer faceValue) {
                dialog.minimumAmountInput.clear();
                dialog.minimumAmountInput.setValue(faceValue.toString());
                return this;
            }

            public FormFiller withFaceValueStep(Integer faceValueStep) {
                dialog.stepAmountInput.clear();
                dialog.stepAmountInput.setValue(faceValueStep.toString());
                return this;
            }

            public FormFiller withActive(boolean active) {
                Wait.waitForAjaxCompleted();
                Wait.waitForVisible(dialog.agreementStatusCombo).select(active ? "Active" : "Inactive");
                return this;
            }

            public FormFiller withLogo(String logoPath) {
                dialog.useCustomLogoRadio.click();
                WebElement elem = dialog.find(By.xpath("//input[contains(@id, 'voucherLogoFileId') and contains(@type, 'file')]"));
                dialog.enterToHiddenUploadFileField(elem, logoPath);
                Wait.waitForDisplayed(dialog.logoImageXpath);
                return this;
            }

            public FormFiller withImage(String logoPath) {
                WebElement elem = dialog.find(By.xpath("//input[contains(@id, 'voucherImageFileId') and contains(@type, 'file')]"));
                dialog.enterToHiddenUploadFileField(elem, logoPath);
                Wait.waitForDisplayed(dialog.imageXpath);
                return this;
            }
        }

        public GeneralTab fill(Consumer<GeneralTab> fillFunc) {
            fillFunc.accept(this);
            return this;
        }

        @Override
        protected boolean areWeAt() {
            Wait.waitForAjaxCompleted();
            try {
                return voucherNameInput.isDisplayed();
            } catch (Exception e) {
                logger.error(e.getMessage());
                return false;
            }
        }

        @Override
        protected BaseDialog ensureWeAreAt() {
            Wait.waitForVisible(voucherNameInput);
            return this;
        }

        public GeneralTab doAssert(Consumer<Asserts> assertFunc) {
            assertFunc.accept(new GeneralTab.Asserts());
            return GeneralTab.this;
        }

        public class Asserts {

            public Asserts assertImagePresent() {
                assertTrue(JavascriptHelper.isImagePresent(driver.findElement(imageXpath)));
                return this;
            }

            public Asserts assertLogoPresent() {
                assertTrue(JavascriptHelper.isImagePresent(driver.findElement(logoImageXpath)));
                return this;
            }

            public Asserts assertVoucherName(String cvrValue) {
                Assert.assertEquals(voucherNameInput.getAttribute("value"), cvrValue);
                return this;
            }

            public Asserts assertDiscount(Integer discount) {
                Assert.assertEquals(agreementDiscount.getAttribute("value"), discount.toString());
                return this;
            }

            public Asserts assertFaceValue(Integer faceValue) {
                Assert.assertEquals(minimumAmountInput.getText(), faceValue.toString());
                return this;
            }

            public Asserts assertFaceValueStep(Integer faceValueStep) {
                Assert.assertEquals(stepAmountInput.getText(), faceValueStep.toString());
                return this;
            }

            public Asserts assertStatus(boolean active) {
                Assert.assertEquals(agreementStatusCombo.getValue(), active ? "Active" : "Inactive");
                return this;
            }
        }

    }

    public static class AdvancedTab extends BaseDialog implements VoucherAgreementTabs {

        @FindBy(xpath = "//table[contains(@class, 'supplier-voucher-general-order-email-radio')]")
        private ExtRadioButton useSupplierOrderEmailRadio;

        @FindBy(xpath = "//table[contains(@class, 'supplier-voucher-other-email-radio')]")
        private ExtRadioButton useOtherEmailRadio;

        @FindBy(xpath = "//table[contains(@class, 'supplier-voucher-scalepoint-handles-voucher-radio')]")
        private ExtRadioButton scalepointHandlesEvouchersRadio;

        @FindBy(xpath = "//table[contains(@class, 'supplier-voucher-delivery-cost-checkbox')]")
        private ExtCheckbox useDeliveryCostCheckbox;

        @FindBy(xpath = "//table[contains(@class, 'supplier-electronic-voucher-checkbox')]")
        private ExtCheckbox useElectronicVoucherCheckbox;

        @FindBy(xpath = "//table[contains(@class, 'supplier-voucher-evoucher-email-checkbox')]")
        private ExtCheckbox eVoucherEmailRequired;

        @FindBy(xpath = "//table[contains(@class, 'supplier-voucher-evoucher-phone-checkbox')]")
        private ExtCheckbox eVoucherPhoneRequired;

        @FindBy(xpath = "//table[contains(@class, 'supplier-voucher-evoucher-code-checkbox')]")
        private ExtCheckbox eVoucherPersonalCodeRequired;

        @FindBy(xpath = "//table[contains(@class, 'supplier-voucher-evoucher-portal-checkbox')]")
        private ExtCheckbox useEVoucherPortal;

        @FindBy(xpath = "//table[contains(@class, 'supplier-voucher-add-delivery-cost-in-email-checkbox')]")
        private ExtCheckbox addDeliveryCostImSupplierMailCheckBox;

        @FindBy(name = "otherEmail")
        private ExtInput otherEmailInput;

        @FindBy(name = "deliveryCost")
        private ExtInput deliveryCostInput;

        @FindBy(name = "deliveryType")
        private ExtInput deliveryTypeInput;

        @FindBy(name = "popularity")
        private ExtInput popularityInput;

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

            private AdvancedTab dialog;

            public FormFiller(AdvancedTab dialog) {
                this.dialog = dialog;
            }

            public FormFiller useOrderEmail(OrderMailType orderMailType) {
                return useOrderEmail(orderMailType, null);
            }

            public FormFiller useOrderEmail(OrderMailType orderMailType, String otherEmail) {
                switch (orderMailType) {
                    case OTHER:
                        dialog.useOtherEmailRadio.set(true);
                        dialog.otherEmailInput.clear();
                        dialog.otherEmailInput.sendKeys(otherEmail);
                        break;
                    case SCALEPOINT_HANDLES:
                        dialog.scalepointHandlesEvouchersRadio.set(true);
                        break;
                    case SUPPLIER:
                        dialog.useSupplierOrderEmailRadio.set(true);
                        break;
                }
                return this;
            }

            public FormFiller withDeliveryCost(Integer deliveryCost) {
                dialog.useDeliveryCostCheckbox.set(true);
                dialog.deliveryCostInput.sendKeys(deliveryCost.toString());
                return this;
            }

            public FormFiller withDeliveryType(String deliveryType) {
                dialog.deliveryTypeInput.clear();
                dialog.deliveryTypeInput.sendKeys(deliveryType);
                return this;
            }

            public FormFiller withPopularity(Integer popularity) {
                dialog.popularityInput.clear();
                dialog.popularityInput.sendKeys(popularity.toString());
                return this;
            }

            public void useAsEVoucher(EVoucherOptions... eVoucherOptions) {
                dialog.useElectronicVoucherCheckbox.set(true);
                for (EVoucherOptions eVoucherOption : eVoucherOptions) {
                    switch (eVoucherOption) {
                        case EMAIL_REQUIRED:
                            dialog.eVoucherEmailRequired.set(true);
                            break;
                        case PERSONAL_CODE_REQUIRED:
                            dialog.eVoucherPersonalCodeRequired.set(true);
                            break;
                        case PHONE_REQUIRED:
                            dialog.eVoucherPhoneRequired.set(true);
                            break;
                        case USE_PORTAL_REQUIRED:
                            dialog.useEVoucherPortal.set(true);
                            break;
                    }
                }
            }
        }

        public AdvancedTab fill(Consumer<AdvancedTab> fillFunc) {
            fillFunc.accept(this);
            return this;
        }

        @Override
        protected BaseDialog ensureWeAreAt() {
            return this;
        }

        public AdvancedTab doAssert(Consumer<Asserts> assertFunc) {
            assertFunc.accept(new Asserts());
            return AdvancedTab.this;
        }

        public class Asserts {

            public Asserts assertOrderType(OrderMailType orderMailType) {
                switch (orderMailType) {
                    case OTHER:
                        Assert.assertTrue(useOtherEmailRadio.isSelected());
                        break;
                    case SCALEPOINT_HANDLES:
                        Assert.assertTrue(scalepointHandlesEvouchersRadio.isSelected());
                        break;
                    case SUPPLIER:
                        Assert.assertTrue(useSupplierOrderEmailRadio.isSelected());
                        break;
                }
                return this;
            }

            public Asserts assertOtherEmail(String email) {
                Assert.assertEquals(otherEmailInput.getText(), email);
                return this;
            }

            public Asserts assertDeliveryType(String deliveryType) {
                Assert.assertEquals(deliveryTypeInput.getText(), deliveryType);
                return this;
            }

            public Asserts assertPopularity(Integer popularity) {
                Assert.assertEquals(popularityInput.getText(), popularity.toString());
                return this;
            }

            public Asserts assertUsedAsEVoucher(EVoucherOptions... eVoucherOptions) {
                Assert.assertTrue(useElectronicVoucherCheckbox.isSelected());
                for (EVoucherOptions eVoucherOption : eVoucherOptions) {
                    switch (eVoucherOption) {
                        case EMAIL_REQUIRED:
                            Assert.assertTrue(eVoucherEmailRequired.isSelected());
                            break;
                        case PERSONAL_CODE_REQUIRED:
                            Assert.assertTrue(eVoucherPersonalCodeRequired.isSelected());
                            break;
                        case PHONE_REQUIRED:
                            Assert.assertTrue(eVoucherPhoneRequired.isSelected());
                            break;
                        case USE_PORTAL_REQUIRED:
                            Assert.assertTrue(useEVoucherPortal.isSelected());
                            break;
                    }
                }
                return this;
            }

        }

    }

    public static class LegalTab extends BaseDialog implements VoucherAgreementTabs {

        @FindBy(xpath = ".//textarea[contains(@name, 'conditions')]")
        private WebElement conditionsInput;
        @FindBy(xpath = ".//textarea[contains(@name, 'limitations')]")
        private WebElement limitationsInput;

        @Override
        protected BaseDialog ensureWeAreAt() {
            Wait.waitForVisible(conditionsInput);
            return this;
        }

        public LegalTab setConditions(String conditionsText) {
            conditionsInput.sendKeys(conditionsText);
            return this;
        }

        public LegalTab setLimitations(String limitationsText) {
            limitationsInput.sendKeys(limitationsText);
            return this;
        }

        public LegalTab doAssert(Consumer<Asserts> assertFunc) {
            assertFunc.accept(new Asserts());
            return LegalTab.this;
        }

        public class Asserts {
            public LegalTab.Asserts assertConditions(String conditions) {
                Assert.assertEquals(conditionsInput.getText(), conditions);
                return this;
            }

            public LegalTab.Asserts assertLimitations(String limitaions) {
                Assert.assertEquals(limitationsInput.getText(), limitaions);
                return this;
            }
        }
    }

    public static class CoverageTab extends BaseDialog implements VoucherAgreementTabs {

        @FindBy(name = "brands")
        private WebElement brands;
        @FindBy(name = "tags")
        private WebElement tags;

        @Override
        protected BaseDialog ensureWeAreAt() {
            Wait.waitForVisible(brands);
            return this;
        }

        public CoverageTab setBrands(String brandsText) {
            brands.sendKeys(brandsText);
            return this;
        }

        public CoverageTab setTags(String tagsText) {
            tags.sendKeys(tagsText);
            return this;
        }
    }

    public static class InfoTab extends BaseDialog implements VoucherAgreementTabs {

        @FindBy(xpath = ".//textarea[contains(@name, 'informations')]")
        private WebElement information;
        @FindBy(xpath = ".//textarea[contains(@name, 'terms')]")
        private WebElement terms;
        @FindBy(xpath = ".//textarea[contains(@name, 'deliveryInformations')]")
        private WebElement deliveryInformation;
        @FindBy(xpath = ".//textarea[contains(@name, 'issuedTexts')]")
        private WebElement issued;

        @Override
        protected BaseDialog ensureWeAreAt() {
            Wait.waitForVisible(information);
            return this;
        }

        public InfoTab setInformation(String informationText) {
            information.sendKeys(informationText);
            return this;
        }

        public InfoTab setTags(String termsText) {
            terms.sendKeys(termsText);
            return this;
        }

        public InfoTab setDeliveryInformation(String deliveryInformationText) {
            deliveryInformation.sendKeys(deliveryInformationText);
            return this;
        }

        public InfoTab setIssuedText(String issuedText) {
            issued.sendKeys(issuedText);
            return this;
        }
    }

    public static class CategoriesTab extends BaseDialog implements VoucherAgreementTabs {
        @FindBy(xpath = "id('categoriesVoucherTabId')//tr")
        private List<WebElement> mappedCategories;

        @FindBy(className = "supplier-voucher-edit-mappings-btn")
        private WebElement editMappingsBtn;

        @Override
        protected BaseDialog ensureWeAreAt() {
            Wait.waitForVisible(editMappingsBtn);
            return this;
        }

        public CategoriesTab doAssert(Consumer<Asserts> assertFunc) {
            assertFunc.accept(new Asserts());
            return CategoriesTab.this;
        }

        public class Asserts {

            public Asserts assertCategoryMapped(String categoryGroup, String category) {
                Assert.assertTrue(isCategoryMapped(categoryGroup, category));
                return this;
            }


            public Asserts assertCategoryNotMapped(String categoryGroup, String category) {
                Assert.assertFalse(isCategoryMapped(categoryGroup, category));
                return this;
            }

            private boolean isCategoryMapped(String categoryGroup, String category) {
                String expectedCategory = EditCategoryMappingsDialog.formatCategoryOption(categoryGroup, category);
                return $(By.xpath("//div[@id='categoriesVoucherTabId']//div[text()='" + expectedCategory + "']")).isDisplayed();
            }

        }

        public EditCategoryMappingsDialog openEditMappingsDialog() {
            clickUsingJsIfSeleniumClickReturnError(editMappingsBtn);
            return BaseDialog.at(EditCategoryMappingsDialog.class);
        }

        public CategoriesTab mapToCategory(String categoryGroup, String categoryName) {
            return openEditMappingsDialog()
                    .mapCategory(categoryGroup, categoryName);
        }

        public CategoriesTab removeMapping(String categoryGroup, String categoryName) {
            return openEditMappingsDialog()
                    .removeMapping(categoryGroup, categoryName);
        }
    }

    public static class DiscountDistributionTab extends BaseDialog implements VoucherAgreementTabs {

        @FindBy(name = "discountToIC")
        private ExtInput discountToICInput;

        @FindBy(name = "discountToClaimant")
        private ExtInput discountToClaimantInput;

        public DiscountDistributionTab setDiscountToIc(Integer discount) {
            discountToICInput.clear();
            discountToICInput.sendKeys(discount.toString());
            return this;
        }

        public DiscountDistributionTab setDiscountToClaimant(Integer discount) {
            discountToClaimantInput.clear();
            discountToClaimantInput.sendKeys(discount.toString());
            return this;
        }

        @Override
        protected BaseDialog ensureWeAreAt() {
            return this;
        }

        public DiscountDistributionTab doAssert(Consumer<Asserts> assertFunc) {
            assertFunc.accept(new Asserts());
            return DiscountDistributionTab.this;
        }

        public class Asserts {

            public Asserts assertDiscountToIc(Integer discount) {
                Assert.assertEquals(discountToICInput.getText(), discount.toString());
                return this;
            }

            public Asserts assertDiscountToClaimant(Integer discount) {
                Assert.assertEquals(discountToClaimantInput.getText(), discount.toString());
                return this;
            }
        }
    }
}
