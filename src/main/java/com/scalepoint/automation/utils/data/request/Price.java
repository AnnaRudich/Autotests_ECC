package com.scalepoint.automation.utils.data.request;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * Created by bza on 6/28/2017.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Price")
public class Price
{
    @XmlAttribute
    private String amount;
    @XmlAttribute
    private String name;
    @XmlAttribute
    private String netAmount;

    public String getAmount ()
    {
        return amount;
    }

    public void setAmount (String amount)
    {
        this.amount = amount;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getNetAmount ()
    {
        return netAmount;
    }

    public void setNetAmount (String netAmount)
    {
        this.netAmount = netAmount;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [amount = "+amount+", name = "+name+", netAmount = "+netAmount+"]";
    }
}
