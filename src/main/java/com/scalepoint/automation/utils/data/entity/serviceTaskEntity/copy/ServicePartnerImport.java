package com.scalepoint.automation.utils.data.entity.serviceTaskEntity.copy;

import javax.xml.bind.annotation.XmlElement;

public class ServicePartnerImport extends ServicePartner{

    BankImport bank;

    @XmlElement(nillable = true)
    public BankImport getBank() {
        return bank;
    }

    public ServicePartnerImport(){}

    public ServicePartnerImport(BankImport bankImport){
        this.bank = bankImport;
    }

    public void setBank(BankImport bank) {
        this.bank = bank;
    }
}
