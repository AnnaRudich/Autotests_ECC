package com.scalepoint.automation.utils.data.entity.ServiceTaskEntity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Valuations
{

    @XmlElement
    private String newPrice;

    @XmlElement
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
