package com.scalepoint.automation.utils.data.entity.serviceTaskEntity.copy;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class ServicePartnerExport extends ServicePartner {

    private String serviceAgreementName;
    private BankExport bank;
    private Location location;


    @XmlAttribute
    public String getServiceAgreementName() {
        return serviceAgreementName;
    }

    public void setServiceAgreementName(String serviceAgreementName) {
        this.serviceAgreementName = serviceAgreementName;
    }

    @XmlElement(nillable = true)
    public BankExport getBank() {
        return bank;
    }

    public void setBank(BankExport bank) {
        this.bank = bank;
    }

    @XmlElement(nillable = true)
    public Location getLocation() {
        return location;
    }

    public void setLocation(LocationExport location) {
        this.location = location;
    }
}
