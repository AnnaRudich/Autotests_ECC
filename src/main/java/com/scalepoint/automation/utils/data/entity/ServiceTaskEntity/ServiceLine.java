package com.scalepoint.automation.utils.data.entity.serviceTaskEntity;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class ServiceLine
{
    @XmlAttribute
    private String uniqueId;

    @XmlAttribute
    private String claimLineId;

    @XmlAttribute
    private String taskType;

    @XmlElement
    private Category category;

    @XmlElement
    private Item item;

    @XmlElement
    private Valuations valuations;



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


    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
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

