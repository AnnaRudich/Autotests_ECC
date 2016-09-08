package com.scalepoint.automation.utils.data.entity;

import com.scalepoint.automation.utils.RandomUtils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ClaimItem {

    private String textFieldSP = RandomUtils.randomName("ClaimItem");
    @XmlElement
    private String newPriceSP;
    @XmlElement
    private String purchasePriceSP;
    @XmlElement
    private String ssDescriptionSP;
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
    private String xlsDescr1;
    @XmlElement
    private String depTypePolicy;
    @XmlElement
    private String depTypeDisc;
    @XmlElement
    private String depAmount1;
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
    private String customerDemand;
    @XmlElement
    private String usedPrice;
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
    private String lowerPrice;
    @XmlElement
    private String valuationType1;
    @XmlElement
    private String valuationType2;
    @XmlElement
    private String valuationType3;
    @XmlElement
    private String valuationType4;
    @XmlElement
    private String valuationType5;
    @XmlElement
    private String ageStatus;
    @XmlElement
    private String bigCustomDemandPrice;
    @XmlElement
    private String existingCat3;
    @XmlElement
    private String existingSubCat3;
    @XmlElement
    private String reductionRule;
    @XmlElement
    private String alkaCategory;
    @XmlElement
    private String alkaSubCategory;
    @XmlElement
    private String alkaUserReductionRule;
    @XmlElement
    private String alkaCategoryUnpublishedPolicy;
    @XmlElement
    private String alkaSubCategoryUnpublishedPolicy;
    @XmlElement
    private String alkaUserReductionRule40;
    @XmlElement
    private String existingVoucher3;
    @XmlElement
    private String existingVoucher4;
    @XmlElement
    private String brandLinkVoucher4;


    public String getTextFieldSP() {
        return textFieldSP;
    }

    public String getNewPriceSP() {
        return newPriceSP;
    }

    public String getPurchasePriceSP() {
        return purchasePriceSP;
    }

    public String getSSDescriptionSP() {
        return ssDescriptionSP;
    }

    public String getSsDescriptionSP() {
        return ssDescriptionSP;
    }

    public String getExistingCat1() {
        return existingCat1;
    }

    public String getExistingCat2() {
        return existingCat2;
    }

    public String getExistingSubCat1() {
        return existingSubCat1;
    }

    public String getExistingSubCat2() {
        return existingSubCat2;
    }

    public String getExistingVoucher1() {
        return existingVoucher1;
    }

    public String getExcelPath1() {
        return excelPath1;
    }

    public String getXlsDescr1() {
        return xlsDescr1;
    }

    public String getDepTypePolicy() {
        return depTypePolicy;
    }

    public String getDepTypeDisc() {
        return depTypeDisc;
    }

    public String getDepAmount1() {
        return depAmount1;
    }

    public String getDepAmount2() {
        return depAmount2;
    }

    public String getPolicyExcess1() {
        return policyExcess1;
    }

    public String getPolicyExcess2() {
        return policyExcess2;
    }

    public String getFileLoc() {
        return fileLoc;
    }

    public String getFile2Loc() {
        return file2Loc;
    }

    public String getFileName(String fileLocation) {
        String[] splitedString = fileLocation.split("\\\\");
        String fileName = splitedString[splitedString.length - 1];
        return fileName;
    }

    public String getExistingVoucher2() {
        return existingVoucher2;
    }

    public String getZeroDistance() {
        return zeroDistance;
    }

    public String getValidDistance1() {
        return validDistance1;
    }

    public String getSetDialogTextMatch() {
        return setDialogTextMatch;
    }

    public String getProcuraLimitPrice() {
        return procuraLimitPrice;

    }

    public String getCustomerDemand() {
        return customerDemand;
    }

    public String getUsedPrice() {
        return usedPrice;
    }

    public String getMonths() {
        return month;
    }

    public String getIntegrationCategory() {
        return integrationCategory;
    }


    public String getYesOption() {
        return yes;
    }

    public String getNoOption() {
        return no;
    }

    public String getBlueColor() {
        return blueColor;
    }

    public String getPinkColor() {
        return pinkColor;
    }

    public String getLowerPrice() {
        return lowerPrice;
    }

    public String getValuationType1() {
        return valuationType1;
    }

    public String getValuationType2() {
        return valuationType2;
    }

    public String getValuationType3() {
        return valuationType3;
    }

    public String getValuationType4() {
        return valuationType4;
    }

    public String getValuationType5() {
        return valuationType5;
    }

    public String getAgeStatus() {
        return ageStatus;
    }

    public String getBigCustomDemandPrice() {
        return bigCustomDemandPrice;
    }

    public String getExistingCat3() {
        return existingCat3;
    }

    public String getExistingSubCat3() {
        return existingSubCat3;
    }

    public String getReductionRule() {
        return reductionRule;
    }

    public String getAlkaCategory() {
        return alkaCategory;
    }

    public String getAlkaSubCategory() {
        return alkaSubCategory;
    }

    public String getAlkaUserReductionRule() {
        return alkaUserReductionRule;
    }

    public String getAlkaCategoryUnpublishedPolicy() {
        return alkaCategoryUnpublishedPolicy;
    }

    public String getAlkaSubCategoryUnpublishedPolicy() {
        return alkaSubCategoryUnpublishedPolicy;
    }

    public String getAlkaUserReductionRule40() {
        return alkaUserReductionRule40;
    }

    public String getExistingVoucher3() {
        return existingVoucher3;
    }

    public String getExistingVoucher4() {
        return existingVoucher4;
    }

    public String getBrandLinkVoucher4() {
        return brandLinkVoucher4;
    }
}
