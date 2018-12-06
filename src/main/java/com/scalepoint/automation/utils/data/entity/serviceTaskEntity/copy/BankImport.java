package com.scalepoint.automation.utils.data.entity.serviceTaskEntity.copy;

import javax.xml.bind.annotation.XmlAttribute;

public class BankImport {

    private String bankName;
    private String fikNumber;

    public BankImport(){}

    public BankImport(String bankName, String fikName){
        this.bankName = bankName;
        this.fikNumber = fikName;
    }

    @XmlAttribute
    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    @XmlAttribute
    public String getFikNumber() {
        return fikNumber;
    }

    public void setFikNumber(String fikNumber) {
        this.fikNumber = fikNumber;
    }
}
