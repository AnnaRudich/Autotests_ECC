package com.scalepoint.automation.utils.data.entity.rnv.serviceTask;

import javax.xml.bind.annotation.XmlAttribute;

public class BankImport extends Bank {

    private String bankName;
    private String fikNumber;

    public BankImport() {
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
