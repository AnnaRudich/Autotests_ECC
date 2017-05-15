package com.scalepoint.automation.utils.data.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "simpleSupplier", propOrder = {
        "name"
})
public class SimpleSupplier {

    @XmlAttribute(name = "name")
    private String name;
    @XmlAttribute(name = "agreement")
    private String agreement;
    @XmlAttribute(name = "inactiveAgreement")
    private String inactiveAgreement;
    @XmlAttribute(name = "scalepointAgreement")
    private String scalepointAgreement;
    @XmlAttribute(name = "insuranceCompany")
    private String insuranceCompany;
    @XmlAttribute(name = "agreementCompany")
    private String agreementCompany;
    @XmlAttribute(name = "shopName")
    private String shopName;
    @XmlAttribute(name = "withVouchers")
    private Boolean withVouchers;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAgreement() {
        return agreement;
    }

    public void setAgreement(String agreement) {
        this.agreement = agreement;
    }

    public String getInactiveAgreement() {
        return inactiveAgreement;
    }

    public void setInactiveAgreement(String inactiveAgreement) {
        this.inactiveAgreement = inactiveAgreement;
    }

    public String getScalepointAgreement() {
        return scalepointAgreement;
    }

    public void setScalepointAgreement(String scalepointAgreement) {
        this.scalepointAgreement = scalepointAgreement;
    }

    public String getInsuranceCompany() {
        return insuranceCompany;
    }

    public void setInsuranceCompany(String insuranceCompany) {
        this.insuranceCompany = insuranceCompany;
    }

    public String getAgreementCompany() {
        return agreementCompany;
    }

    public void setAgreementCompany(String agreementCompany) {
        this.agreementCompany = agreementCompany;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public Boolean isWithVouchers() {
        return withVouchers;
    }

    public void setWithVouchers(Boolean haveVoucher) {
        this.withVouchers = haveVoucher;
    }
}
