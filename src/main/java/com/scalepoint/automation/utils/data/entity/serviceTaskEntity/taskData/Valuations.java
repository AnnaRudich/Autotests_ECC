package com.scalepoint.automation.utils.data.entity.serviceTaskEntity.taskData;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public class Valuations {
    @XmlAttribute
    private String customerDemand;

    @XmlAttribute
    private String newPrice;

    public String getCustomerDemand ()
    {
        return customerDemand;
    }

    public void setCustomerDemand (String customerDemand)
    {
        this.customerDemand = customerDemand;
    }

    public String getNewPrice ()
    {
        return newPrice;
    }

    public void setNewPrice (String newPrice)
    {
        this.newPrice = newPrice;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [customerDemand = "+customerDemand+", newPrice = "+newPrice+"]";
    }
}

