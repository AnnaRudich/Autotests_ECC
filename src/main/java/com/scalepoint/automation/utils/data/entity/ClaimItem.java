package com.scalepoint.automation.utils.data.entity;

import com.scalepoint.automation.utils.RandomUtils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Objects;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ClaimItem {

    private String textFieldSP = RandomUtils.randomName("ClaimItem");
    @XmlElement
    private Double newPriceSP;
    @XmlElement
    private Integer purchasePriceSP;
    @XmlElement
    private String ssDescriptionSP;
    @XmlElement
    private String existingCatWithoutVoucherAndSubCategory;
    @XmlElement
    private String existingGroupWithPolicyDepreciationTypeAndReductionRule;
    @XmlElement
    private String existingGroupWithDiscretionaryDepreciationTypeAndReductionRule;
    @XmlElement
    private String existingSubCategoryForVideoGroupWithReductionRuleAndDepreciationPolicy;
    @XmlElement
    private String existingSubCategoryForVideoGroupWithReductionRuleAndDiscretionaryType;
    @XmlElement
    private String existingGroupFotoAndVideo;
    @XmlElement
    private String existingCat1;
    @XmlElement
    private String existingCat2;
    @XmlElement
    private String existingSubCat1;
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
    private String existingSubCat2;
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
    private String existingCat3;
    @XmlElement
    private String existingSubCat3;
    @XmlElement
    private Integer reductionRule;
    @XmlElement
    private String alkaCategory;
    @XmlElement
    private String alkaSubCategory;
    @XmlElement
    private Integer alkaUserReductionRule;
    @XmlElement
    private String alkaCategoryUnpublishedPolicy;
    @XmlElement
    private String alkaSubCategoryUnpublishedPolicy;
    @XmlElement
    private Integer alkaUserReductionRule40;
    @XmlElement
    private String existingVoucher3;
    @XmlElement
    private String existingVoucher4;
    @XmlElement
    private String brandLinkVoucher4;
    @XmlElement
    private String trygCategory;
    @XmlElement
    private String trygSubCategory;
    @XmlElement
    private Double trygNewPrice;
    @XmlElement
    private String trygCat1;
    @XmlElement
    private String trygSubCat1;
    @XmlElement
    private String trygCategory1;
    @XmlElement
    private String existingCat4;
    @XmlElement
    private String existingSubCat4;
    @XmlElement
    private String matchedText;


    public String getExistingCat4(){
        return existingCat4;
    }

    public String getExistingSubCat4(){
        return existingSubCat4;
    }

    public String getTrygCategory1(){
        return trygCategory1;
    }

    public String getTrygCat1(){
        return trygCat1;
    }

    public String getTrygSubCat1(){
        return trygSubCat1;
    }

    public String getTrygCategory(){
        return trygCategory;
    }

    public String getTrygSubCategory(){
        return trygSubCategory;
    }

    public Double getTrygNewPrice(){
        return trygNewPrice;
    }

    public String getTextFieldSP() {
        return textFieldSP;
    }

    public String getCategoryGroupBorn() {
        return existingCat1;
    }

    public String getExistingCatWithoutVoucherAndSubCategory() {
        return existingCatWithoutVoucherAndSubCategory;
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

    public String getExistingGroupFotoAndVideo() {
        return existingGroupFotoAndVideo;
    }

    public String getCategoryBornBabyudstyr() {
        return existingSubCat1;
    }

    public String getExistingSubCat2() {
        return existingSubCat2;
    }

    public String getExistingVoucher_10() {
        return existingVoucher1;
    }

    public String getExcelPath1() {
        return Objects.requireNonNull(ClassLoader.getSystemClassLoader().getResource(excelPath1)).getPath();
    }

    public String getXlsDescr1() {
        return xlsDescr1;
    }

    public String getFileLoc() {
        return Objects.requireNonNull(ClassLoader.getSystemClassLoader().getResource(fileLoc)).getPath();
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

    public String getExistingCat3_Telefoni() {
        return existingCat3;
    }

    public String getExistingSubCat3_Mobiltelefoner() {
        return existingSubCat3;
    }

    public Integer getReductionRule_30() {
        return reductionRule;
    }

    public String getAlkaCategory() {
        return alkaCategory;
    }

    public String getAlkaSubCategory() {
        return alkaSubCategory;
    }

    public Integer getAlkaUserReductionRule_25() {
        return alkaUserReductionRule;
    }

    public String getAlkaCategoryUnpublishedPolicy() {
        return alkaCategoryUnpublishedPolicy;
    }

    public String getAlkaSubCategoryUnpublishedPolicy() {
        return alkaSubCategoryUnpublishedPolicy;
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

    public void setNewPriceSP(Double newPriceSP) {
        this.newPriceSP = newPriceSP;
    }

    public void setCustomerDemand(Double customerDemand) {
        this.customerDemand = customerDemand;
    }

    @Override
    public String toString() {
        return "ClaimItem{" +
                "ssDescriptionSP='" + ssDescriptionSP + '\'' +
                '}';
    }
}
