package com.scalepoint.automation.utils.data.entity;

import com.scalepoint.automation.utils.RandomUtils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Voucher {

    private String voucherNameSP = RandomUtils.randomName("SPVoucher");
    @XmlElement
    private String discount;
    @XmlElement
    private String discount2;
    @XmlElement
    private String claimantDiscountDD;
    @XmlElement
    private String existing_supplier;
    @XmlElement
    private String voucherURL;
    @XmlElement
    private String discountUpdate;
    @XmlElement
    private String logoPath;
    @XmlElement
    private String minFV;
    @XmlElement
    private String stepFV;
    @XmlElement
    private String culture;
    private String text = RandomUtils.randomName("Text");
    @XmlElement
    private String invoice_rebate;
    @XmlElement
    private String customer_rebate;
    @XmlElement
    private String discount_mode;
    private String conditions = RandomUtils.randomName("Conditions");
    private String limitations = RandomUtils.randomName("Limitations");
    @XmlElement
    private String workflow_voucher;
    @XmlElement
    private String large_customer_rebate;
    @XmlElement
    private String custom_min_amount;
    @XmlElement
    private String custom_step_amount;
    @XmlElement
    private String discountModeGain;
    @XmlElement
    private String voucherListDebateDescription;
    @XmlElement
    private String voucherListGainDescription;
    @XmlElement
    private String customCustomerRebate;
    @XmlElement
    private String customValueOfVoucher;
    @XmlElement
    private String customInvoicePrice;
    @XmlElement
    private String customGainPercentage;
    @XmlElement
    private String customInvoicePriceGain;
    @XmlElement
    private String customValueOfVoucherGain;
    @XmlElement
    private String customLogo;
    @XmlElement
    private String simpleAgreementDiscount;
    @XmlElement
    private String simpleCustomerDiscount;
    @XmlElement
    private String agreementEnabled;
    @XmlElement
    private String agreementDisabled;
    @XmlElement
    private String otherOrderEmail;
    @XmlElement
    private String deliveryCost;
    @XmlElement
    private String legalGermanConditions;
    @XmlElement
    private String legalGermanLimitations;
    @XmlElement
    private String legalFrenchConditions;
    @XmlElement
    private String legalFrenchLimitations;
    @XmlElement
    private String legalItalianConditions;
    @XmlElement
    private String legalItalianLimitations;
    @XmlElement
    private String legalEnglishConditions;
    @XmlElement
    private String legalEnglishLimitations;
    @XmlElement
    private String brands;
    @XmlElement
    private String tags;
    @XmlElement
    private String existingBrand;
    @XmlElement
    private String existingTags;
    @XmlElement
    private String brandLink;
    @XmlElement
    private String existingVoucher;

    @XmlElement
    private String voucherTerm;

    public String getVoucherNameSP() {
        return voucherNameSP;
    }

    public String getDiscount() {
        return discount;
    }

    public String getDiscount2() {
        return discount2;
    }

    public void setDiscount2(String discount2) {
        this.discount2 = discount2;
    }

    public String getDiscountUpdate() {
        return discountUpdate;
    }

    public String getClaimantDiscountDD() {
        return claimantDiscountDD;
    }

    public String getExisting_supplier() {
        return existing_supplier;
    }

    public String getVoucherURL() {
        return voucherURL;
    }

    public String getLogoPath() {
        return logoPath;
    }

    public String getMinFV() {
        return minFV;
    }

    public String getStepFV() {
        return stepFV;
    }

    public String getCulture() {
        return culture;
    }

    public String getText() {
        return text;
    }

    public String getInvoice_rebate() {
        return invoice_rebate;
    }

    public String getCustomer_rebate() {
        return customer_rebate;
    }

    public String getDiscount_mode() {
        return discount_mode;
    }

    public String getConditions() {
        return conditions;
    }

    public String getLimitations() {
        return limitations;
    }

    public String getWorkflow_voucher() {
        return workflow_voucher;
    }

    public String getLarge_customer_rebate() {
        return large_customer_rebate;
    }

    public String getCustom_min_amount() {
        return custom_min_amount;
    }

    public String getCustom_step_amount() {
        return custom_step_amount;
    }

    public String getDiscountModeGain() {
        return discountModeGain;
    }

    public String getVoucherListDebateDescription() {
        return voucherListDebateDescription;
    }

    public String getVoucherListGainDescription() {
        return voucherListGainDescription;
    }

    public String getCustomCustomerRebate() {
        return customCustomerRebate;
    }

    public String getCustomValueOfVoucher() {
        return customValueOfVoucher;
    }

    public String getCustomInvoicePrice() {
        return customInvoicePrice;
    }

    public String getCustomGainPercentage() {
        return customGainPercentage;
    }

    public String getCustomInvoicePriceGain() {
        return customInvoicePriceGain;
    }

    public String getCustomValueOfVoucherGain() {
        return customValueOfVoucherGain;
    }

    public String getCustomLogo() {
        return customLogo;
    }

    public String getSimpleAgreementDiscount() {
        return simpleAgreementDiscount;
    }

    public String getSimpleCustomerDiscount() {
        return simpleCustomerDiscount;
    }

    public String getAgreementEnabled() {
        return agreementEnabled;
    }

    public String getAgreementDisabled() {
        return agreementDisabled;
    }

    public String getOtherOrderEmail() {
        return otherOrderEmail;
    }

    public String getDeliveryCost() {
        return deliveryCost;
    }

    public String getLegalGermanConditions() {
        return legalGermanConditions;
    }

    public String getLegalGermanLimitations() {
        return legalGermanLimitations;
    }

    public String getLegalFrenchConditions() {
        return legalFrenchConditions;
    }

    public String getLegalFrenchLimitations() {
        return legalFrenchLimitations;
    }

    public String getLegalItalianConditions() {
        return legalItalianConditions;
    }

    public String getLegalItalianLimitations() {
        return legalItalianLimitations;
    }

    public String getLegalEnglishConditions() {
        return legalEnglishConditions;
    }

    public String getLegalEnglishLimitations() {
        return legalEnglishLimitations;
    }

    public String getBrandsText() {
        return brands;
    }

    public String getTagsText() {
        return tags;
    }

    public String getVoucherTerm() {
        return voucherTerm;
    }

    public String getExistingBrand() {
        return existingBrand;
    }

    public String getExistingTags() {
        return existingTags;
    }

    public String getBrandLink() {
        return brandLink;
    }

    public String getExistingVoucher() {
        return existingVoucher;
    }

    @Override
    public String toString() {
        return "Voucher{" +
                "name='" + voucherNameSP + '\'' +
                ", discount='" + discount + '\'' +
                '}';
    }
}


