package com.scalepoint.automation.utils.data.entity.serviceTaskEntity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Bank
{
    @XmlElement
    private String fikNumber;

    @XmlElement
    private String fikType;

    @XmlElement
    private String fikCreditorCode;


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

