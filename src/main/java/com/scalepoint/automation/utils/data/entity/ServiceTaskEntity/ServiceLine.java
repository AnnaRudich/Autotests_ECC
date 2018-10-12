package com.scalepoint.automation.utils.data.entity.ServiceTaskEntity;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class ServiceLine
{
    private Category category;

    @XmlElement
    private String claimLineId;

    private Item item;

    private Valuations valuations;

    @XmlElement
    private String uniqueId;

    public Category getCategory ()
    {
        return category;
    }

    public void setCategory (Category category)
    {
        this.category = category;
    }

    public String getClaimLineId ()
    {
        return claimLineId;
    }

    public void setClaimLineId (String claimLineId)
    {
        this.claimLineId = claimLineId;
    }

    public Item getItem ()
    {
        return item;
    }

    public void setItem (Item item)
    {
        this.item = item;
    }

    public Valuations getValuations ()
    {
        return valuations;
    }

    public void setValuations (Valuations valuations)
    {
        this.valuations = valuations;
    }

    public String getUniqueId ()
    {
        return uniqueId;
    }

    public void setUniqueId (String uniqueId)
    {
        this.uniqueId = uniqueId;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [category = "+category+", claimLineId = "+claimLineId+", item = "+item+", valuations = "+valuations+", uniqueId = "+uniqueId+"]";
    }
}

