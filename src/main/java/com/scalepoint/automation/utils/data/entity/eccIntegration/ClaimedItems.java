package com.scalepoint.automation.utils.data.entity.eccIntegration;


import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ClaimedItems")
public class ClaimedItems {

    @XmlElement(name = "ClaimedItem")
    private ClaimedItem[] claimedItems;

    @Override
    public String toString() {
        return "ClassPojo [ClaimedItem = " + claimedItems + "]";
    }
}
