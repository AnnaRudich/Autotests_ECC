package com.scalepoint.automation.utils.data.entity.rnv.serviceTask;

import javax.xml.bind.annotation.XmlElement;

public class ServicePartnerImport extends ServicePartner {

    private BankImport bank;

    @XmlElement(nillable = true)
    public BankImport getBank() {
        return bank;
    }

    public ServicePartnerImport() {
    }

    public void setBank(BankImport bank) {
        this.bank = bank;
    }
}
