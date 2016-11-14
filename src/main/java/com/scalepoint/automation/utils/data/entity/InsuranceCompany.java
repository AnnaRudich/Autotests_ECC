package com.scalepoint.automation.utils.data.entity;

import com.scalepoint.automation.utils.RandomUtils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * NewSystemUser: kke
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class InsuranceCompany {

    private String icID = Integer.toString(RandomUtils.randomInt());
    private String icName = RandomUtils.randomName("Autotest-IC");
    private String icCode = icName.toUpperCase();
    private String address = RandomUtils.randomName("addr");
    @XmlElement
    private String zipCode;
    private String icCity = RandomUtils.randomName("City");
    @XmlElement
    private String companyCommonMail;
    @XmlElement
    private String functionTemplate;
    @XmlElement
    private String icCulture;
    @XmlElement
    private String claimNotificationMail;
    @XmlElement
    private String guiTemplate;
    @XmlElement
    private String contactNumber;
    @XmlElement
    private String officeHours;
    @XmlElement
    private String spFTName;
    private String nameFT = RandomUtils.randomName("FT");
    @XmlElement
    private String ftIC;
    @XmlElement
    private String ftTryg;
    @XmlElement
    private String ftTrygHolding;
    @XmlElement
    private String dkCompanyDepartment;
    @XmlElement
    private String ftAlka;
    @XmlElement
    private String ftLB;

    public String getFtTrygHolding(){
        return ftTrygHolding;
    }

    public String getDkCompanyDepartment() {
        return dkCompanyDepartment;
    }

    public void setDkCompanyDepartment(String dkCompanyDepartment) {
        this.dkCompanyDepartment = dkCompanyDepartment;
    }

    public String getIcID() {
        return icID;
    }

    public String getIcCode() {
        return icCode;
    }

    public String getIcName() {
        return icName;
    }

    public void setIcName(String icName) {
        this.icName = icName;
    }

    public String getAddress() {
        return address;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getIcCity() {
        return icCity;
    }

    public String getCompanyCommonMail() {
        return companyCommonMail;
    }

    public String getFunctionTemplate() {
        return functionTemplate;
    }


    public void setFunctionTemplate(String ft) {
        this.functionTemplate = ft;
    }

    public String getIcCulture() {
        return icCulture;
    }

    public String getClaimNotificationMail() {
        return claimNotificationMail;
    }

    public String getGuiTemplate() {
        return guiTemplate;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public String getOfficeHours() {
        return officeHours;
    }

    public String getScalepointFuncTemplateName() {
        return spFTName;
    }

    public String getNameFT() {
        return nameFT;
    }

    public String getIcFt() {
        return ftIC;
    }

    public String getFtTryg() {
        return ftTryg;
    }

    public String getFtAlka() {
        return ftAlka;
    }

    public String getFtLB() {
        return ftLB;
    }
}
