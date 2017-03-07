package com.scalepoint.automation.pageobjects.dialogs.eccadmin;

import com.scalepoint.automation.pageobjects.dialogs.BaseDialog;
import com.scalepoint.automation.utils.Wait;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class VoucherAgreementDialog extends BaseDialog implements VoucherAgreementTabs {

    @Override
    protected BaseDialog ensureWeAreAt() {
        Wait.waitForAjaxCompleted();
        return this;
    }

    public static class GeneralTab extends BaseDialog implements VoucherAgreementTabs {

        @FindBy(name = "voucherName")
        private WebElement voucherName;

        @FindBy(name = "agreementDiscount")
        private WebElement agreementDiscount;

        @Override
        protected BaseDialog ensureWeAreAt() {
            Wait.waitForVisible(voucherName);
            return this;
        }
    }

    public static class LegalTab extends BaseDialog implements VoucherAgreementTabs {

        @FindBy(xpath = ".//textarea[contains(@name, 'conditions')]")
        private WebElement conditions;
        @FindBy(xpath = ".//textarea[contains(@name, 'limitations')]")
        private WebElement limitations;

        @Override
        protected BaseDialog ensureWeAreAt() {
            Wait.waitForVisible(conditions);
            return this;
        }

        public LegalTab setConditions(String conditionsText) {
            conditions.sendKeys(conditionsText);
            return this;
        }

        public LegalTab setLimitations(String limitationsText) {
            limitations.sendKeys(limitationsText);
            return this;
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
        @FindBy(className = "supplier-voucher-edit-mappings-btn")
        private WebElement editMappingsBtn;

        @Override
        protected BaseDialog ensureWeAreAt() {
            Wait.waitForVisible(editMappingsBtn);
            return this;
        }

        public EditCategoryMappingsDialog openEditMappingsDialog() {
            editMappingsBtn.click();
            return BaseDialog.at(EditCategoryMappingsDialog.class);
        }
    }
}
