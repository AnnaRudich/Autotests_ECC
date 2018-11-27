package com.scalepoint.automation.utils.data.entity.serviceTaskEntity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public class Bank
{
    @XmlAttribute
    private String accountNumber;

    @XmlAttribute
    private String regNumber;

    @XmlAttribute
    private String bankName;

    @XmlAttribute
    private String IBAN;

    @XmlAttribute
    private String fikNumber;

    @XmlAttribute
    private String fikType;

    @XmlAttribute
    private String fikCreditorCode;


    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getRegNumber() {
        return regNumber;
    }

    public void setRegNumber(String regNumber) {
        this.regNumber = regNumber;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getIBAN() {
        return IBAN;
    }

    public void setIBAN(String IBAN) {
        this.IBAN = IBAN;
    }

    public String getFikNumber ()
    {
        return fikNumber;
    }

    public void setFikNumber (String fikNumber)
    {
        this.fikNumber = fikNumber;
    }

    public String getFikType ()
    {
        return fikType;
    }

    public void setFikType (String fikType)
    {
        this.fikType = fikType;
    }

    public String getFikCreditorCode ()
    {
        return fikCreditorCode;
    }

    public void setFikCreditorCode (String fikCreditorCode)
    {
        this.fikCreditorCode = fikCreditorCode;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [fikNumber = "+fikNumber+", fikType = "+fikType+", fikCreditorCode = "+fikCreditorCode+"]";
    }
}

