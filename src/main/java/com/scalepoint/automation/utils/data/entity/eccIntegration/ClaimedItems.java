package com.scalepoint.automation.utils.data.entity.eccIntegration;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ClaimedItems")
public class ClaimedItems {

    @XmlElement(name = "ClaimedItem")
    private ClaimedItem[] claimedItems;

    public ClaimedItem[] getClaimedItems() {
        return claimedItems;
    }

    public ClaimedItems setClaimedItems(ClaimedItem[] claimedItems) {
        this.claimedItems = claimedItems;
        return this;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [ClaimedItem = "+claimedItems+"]";
    }
}
