package com.scalepoint.automation.utils.data.request;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * Created by bza on 6/28/2017.
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Claim")
public class Claim
{
    @XmlAttribute
    private String category;
    @XmlAttribute
    private String newItem;
    @XmlAttribute
    private String description;
    @XmlAttribute
    private String quantity;
    @XmlAttribute
    private String claimToken;
    @XmlAttribute
    private String originalDescription;
    @XmlAttribute
    private String room;

    public String getCategory ()
    {
        return category;
    }

    public void setCategory (String category)
    {
        this.category = category;
    }

    public String getNewItem ()
    {
        return newItem;
    }

    public void setNewItem (String newItem)
    {
        this.newItem = newItem;
    }

    public String getDescription ()
    {
        return description;
    }

    public void setDescription (String description)
    {
        this.description = description;
    }

    public String getQuantity ()
    {
        return quantity;
    }

    public void setQuantity (String quantity)
    {
        this.quantity = quantity;
    }

    public String getClaimToken ()
    {
        return claimToken;
    }

    public void setClaimToken (String claimToken)
    {
        this.claimToken = claimToken;
    }

    public String getOriginalDescription ()
    {
        return originalDescription;
    }

    public void setOriginalDescription (String originalDescription)
    {
        this.originalDescription = originalDescription;
    }

    public String getRoom ()
    {
        return room;
    }

    public void setRoom (String room)
    {
        this.room = room;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [category = "+category+", newItem = "+newItem+", description = "+description+", quantity = "+quantity+", claimToken = "+claimToken+", originalDescription = "+originalDescription+", room = "+room+"]";
    }
}
