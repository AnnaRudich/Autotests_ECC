package com.scalepoint.automation.utils.data.entity.serviceTaskEntity.taskData;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "serviceLine")
@XmlAccessorType(XmlAccessType.FIELD)
public class ServiceLine {

    @XmlElement
    private Category category;

    @XmlAttribute
    private String claimLineId;

    @XmlAttribute
    private String taskType;

    @XmlElement
    private Item item;

    @XmlAttribute
    private String valuations;

    @XmlAttribute
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

    public String getTaskType ()
    {
        return taskType;
    }

    public void setTaskType (String taskType)
    {
        this.taskType = taskType;
    }

    public Item getItem ()
    {
        return item;
    }

    public void setItem (Item item)
    {
        this.item = item;
    }

    public String getValuations ()
    {
        return valuations;
    }

    public void setValuations (String valuations)
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
        return "ClassPojo [category = "+category+", claimLineId = "+claimLineId+", taskType = "+taskType+", item = "+item+", valuations = "+valuations+", uniqueId = "+uniqueId+"]";
    }
}


