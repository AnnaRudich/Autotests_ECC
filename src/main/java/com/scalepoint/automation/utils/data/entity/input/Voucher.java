package com.scalepoint.automation.utils.data.entity.input;

import com.scalepoint.automation.utils.RandomUtils;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@Data
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Voucher {

    private String voucherNameSP = RandomUtils.randomName("Autotest-SpVoucher");
    @XmlElement
    private Double discount;
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
    private String existingVoucherForDistances;
    @XmlElement
    private String trygVoucher;
    @XmlElement
    private String voucherTerm;

    @Override
    public String toString() {
        return "Voucher{" +
                "name='" + voucherNameSP + '\'' +
                ", discount='" + discount + '\'' +
                '}';
    }

}


