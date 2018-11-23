package com.scalepoint.automation.utils.data.entity.serviceTaskEntity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public class Valuations
{

    @XmlAttribute
    private String newPrice;

    @XmlAttribute
    private String repairPrice;


    public String getNewPrice ()
    {
        return newPrice;
    }

    public void setNewPrice (String newPrice)
    {
        this.newPrice = newPrice;
    }

    public String getRepairPrice ()
    {
        return repairPrice;
    }

    public void setRepairPrice (String repairPrice)
    {
        this.repairPrice = repairPrice;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [newPrice = "+newPrice+", repairPrice = "+repairPrice+"]";
    }
}
