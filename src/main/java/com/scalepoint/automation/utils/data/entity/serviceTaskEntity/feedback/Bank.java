package com.scalepoint.automation.utils.data.entity.serviceTaskEntity.feedback;

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
    private String fikType;

    @XmlAttribute
    private String fikCreditorCode;

    public Bank(){
        com.scalepoint.automation.utils.data.entity.serviceTaskEntity.taskData.Bank bank
                = new com.scalepoint.automation.utils.data.entity.serviceTaskEntity.taskData.Bank();
        this.accountNumber = bank.getAccountNumber();
        this.regNumber = bank.getRegNumber();
        this.fikType = bank.getFikType();
        this.fikCreditorCode = bank.getFikCreditorCode();
    }


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
        return "ClassPojo [accountNumber = "+accountNumber+", regNumber = "+regNumber+", fikType = "+fikType+", fikCreditorCode = "+fikCreditorCode+"]";
    }
}

