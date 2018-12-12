package com.scalepoint.automation.utils.data.entity.serviceTaskEntity.copy;

import javax.xml.bind.annotation.XmlAttribute;

public class BankImport extends Bank{

    private String bankName;
    private String fikNumber;

    public BankImport(){
        BankExport bankExport = new BankExport();
        setRegNumber(bankExport.getRegNumber());
        setAccountNumber(bankExport.getAccountNumber());
        setFikCreditorCode(bankExport.getFikCreditorCode());
        setFikType(bankExport.getFikType());
        setIBAN(bankExport.getIBAN());
        this.bankName = "bankName";
        this.fikNumber = "fikName";
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
