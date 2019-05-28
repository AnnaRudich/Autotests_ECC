package com.scalepoint.automation.utils.data.entity;

import com.scalepoint.automation.utils.RandomUtils;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import static com.scalepoint.automation.utils.SystemUtils.getResourcePath;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class ClaimItem {

    private String textFieldSP = RandomUtils.randomName("ClaimItem");
    @XmlElement
    private Double newPriceSP;
    @XmlElement
    private Integer purchasePriceSP;
    @XmlElement
    private String ssDescriptionSP;
    @XmlElement
    private String existingGroupWithPolicyDepreciationTypeAndReductionRule;
    @XmlElement
    private String existingGroupWithDiscretionaryDepreciationTypeAndReductionRule;
    @XmlElement
    private String existingSubCategoryForVideoGroupWithReductionRuleAndDepreciationPolicy;
    @XmlElement
    private String existingSubCategoryForVideoGroupWithReductionRuleAndDiscretionaryType;

    @XmlElement
    private String existingVoucher1;
    @XmlElement
    private String excelPath1;
    @XmlElement
    private String excelWithGroupingPath;
    @XmlElement
    private String xlsDescr1;
    @XmlElement
    private String depTypePolicy;
    @XmlElement
    private String depTypeDisc;
    @XmlElement
    private Integer depAmount1;
    @XmlElement
    private String policyExcess1;
    @XmlElement
    private String policyExcess2;
    @XmlElement
    private String fileLoc;
    @XmlElement
    private String file2Loc;
    @XmlElement
    private String existingVoucher2;
    @XmlElement
    private String zeroDistance;
    @XmlElement
    private String validDistance1;
    @XmlElement
    private String setDialogTextMatch;
    @XmlElement
    private String procuraLimitPrice;
    @XmlElement
    private Double customerDemand;
    @XmlElement
    private Double usedPrice;
    @XmlElement
    private String depAmount2;
    @XmlElement
    private String month;
    @XmlElement
    private String integrationCategory;
    @XmlElement
    private String yes;
    @XmlElement
    private String no;
    @XmlElement
    private String blueColor;
    @XmlElement
    private String pinkColor;
    @XmlElement
    private Double lowerPrice;
    @XmlElement
    private String valuationTypeNewPrice;
    @XmlElement
    private String valuationTypeCustomerDemand;
    @XmlElement
    private String valuationTypeUsedPrice;
    @XmlElement
    private String valuationTypeDiscretionary;
    @XmlElement
    private String valuationTypeRepair;
    @XmlElement
    private String ageStatus;
    @XmlElement
    private Double bigCustomDemandPrice;
    @XmlElement
    private Integer reductionRule;
    @XmlElement
    private Integer alkaUserReductionRule;
    @XmlElement
    private Integer alkaUserReductionRule40;
    @XmlElement
    private String existingVoucher3;
    @XmlElement
    private String existingVoucher4;
    @XmlElement
    private String brandLinkVoucher4;
    @XmlElement
    private Double trygNewPrice;
    @XmlElement
    private String matchedText;
    @XmlElement
    private String damageTypeValidationError;

    @XmlElement(name = "categoryBabyItems")
    private PseudoCategory categoryBabyItems;

    @XmlElement(name = "categoryVideoCamera")
    private PseudoCategory categoryVideoCamera;

    @XmlElement(name = "categoryMobilePhones")
    private PseudoCategory categoryMobilePhones;

    @XmlElement(name = "categoryShoes")
    private PseudoCategory categoryShoes;

    @XmlElement(name = "categoryJewelry")
    private PseudoCategory categoryJewelry;

    @XmlElement(name = "categoryPersonalMedicine")
    private PseudoCategory categoryPersonalMedicine;

    @XmlElement(name = "categoryBicycles")
    private PseudoCategory categoryBicycles;

    @XmlElement(name = "categoryMusic")
    private PseudoCategory categoryMusic;

    @XmlElement(name = "categoryOther")
    private PseudoCategory categoryOther;

    @XmlElement(name = "categoryPurses")
    private PseudoCategory categoryPurses;

    @XmlElement(name = "categoryLuxuryWatches")
    private PseudoCategory categoryLuxuryWatches;

    @XmlElement(name = "categoryHearingAids")
    private PseudoCategory categoryHearingAids;

    public Double getTrygNewPrice() {
        return trygNewPrice;
    }

    public String getTextFieldSP() {
        return textFieldSP;
    }

    public String getExistingGroupWithPolicyDepreciationTypeAndReductionRule() {
        return existingGroupWithPolicyDepreciationTypeAndReductionRule;
    }

    public String getExistingGroupWithDiscretionaryDepreciationTypeAndReductionRule() {
        return existingGroupWithDiscretionaryDepreciationTypeAndReductionRule;
    }


    public String getExistingSubCategoryForVideoGroupWithReductionRuleAndDepreciationPolicy() {
        return existingSubCategoryForVideoGroupWithReductionRuleAndDepreciationPolicy;
    }

    public String getExistingSubCategoryForVideoGroupWithReductionRuleAndDiscretionaryType() {
        return existingSubCategoryForVideoGroupWithReductionRuleAndDiscretionaryType;
    }

    public String getExistingVoucher_10() {
        return existingVoucher1;
    }

    public String getExcelPath1() {
        return getResourcePath(excelPath1);
    }

    public String getXlsDescr1() {
        return xlsDescr1;
    }

    public String getFileLoc() {
        return getResourcePath(fileLoc);
    }

    public String getExistingVoucher2() {
        return existingVoucher2;
    }

    public String getSetDialogTextMatch() {
        return setDialogTextMatch;
    }

    public Double getCustomerDemand() {
        return customerDemand;
    }

    public Double getUsedPrice() {
        return usedPrice;
    }

    public String getBlueColor() {
        return blueColor;
    }

    public String getPinkColor() {
        return pinkColor;
    }

    public Double getLowerPrice() {
        return lowerPrice;
    }

    public String getValuationTypeNewPrice() {
        return valuationTypeNewPrice;
    }

    public String getValuationTypeUsedPrice() {
        return valuationTypeUsedPrice;
    }

    public String getValuationTypeDiscretionary() {
        return valuationTypeDiscretionary;
    }

    public String getValuationTypeRepair() {
        return valuationTypeRepair;
    }

    public Integer getReductionRule_30() {
        return reductionRule;
    }

    public Integer getAlkaUserReductionRule_25() {
        return alkaUserReductionRule;
    }

    public Integer getAlkaUserReductionRule40() {
        return alkaUserReductionRule40;
    }

    public String getExistingVoucher4() {
        return existingVoucher4;
    }

    public String getBrandLinkVoucher4() {
        return brandLinkVoucher4;
    }

    public String getMatchedText() {
        return matchedText;
    }

    public Double getNewPriceSP() {
        return newPriceSP;
    }

    @Override
    public String toString() {
        return "ClaimItem{" +
                "ssDescriptionSP='" + ssDescriptionSP + '\'' +
                '}';
    }
}
