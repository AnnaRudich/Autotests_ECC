package com.scalepoint.automation.utils.data.entity.serviceTaskEntity.copy;

import javax.xml.bind.annotation.XmlAttribute;

public class Bank {

    private String regNumber;
    private String accountNumber;
    private String IBAN;
    private String fikType;
    private String fikCreditorCode;

    @XmlAttribute
    public String getRegNumber() {
        return regNumber;
    }

    public void setRegNumber(String regNumber) {
        this.regNumber = regNumber;
    }

    @XmlAttribute
    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    @XmlAttribute
    public String getIBAN() {
        return IBAN;
    }

    public void setIBAN(String IBAN) {
        this.IBAN = IBAN;
    }

    @XmlAttribute
    public String getFikType() {
        return fikType;
    }

    public void setFikType(String fikType) {
        this.fikType = fikType;
    }

    @XmlAttribute
    public String getFikCreditorCode() {
        return fikCreditorCode;
    }

    public void setFikCreditorCode(String fikCreditorCode) {
        this.fikCreditorCode = fikCreditorCode;
    }
}
