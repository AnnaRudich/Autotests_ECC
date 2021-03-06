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
public class ClaimItem {

    private String textFieldSP = RandomUtils.randomName("ClaimItem ÆæØøÅåß");
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
    private String excelPathWithoutCatAuto;
    @XmlElement
    private String excelPathWithoutCatNoAuto;
    @XmlElement
    private String excelPathVoucherPrediction;
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

    @Override
    public String toString() {
        return "ClaimItem{" +
                "ssDescriptionSP='" + ssDescriptionSP + '\'' +
                '}';
    }

}
