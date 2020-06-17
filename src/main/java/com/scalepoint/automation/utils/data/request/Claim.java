package com.scalepoint.automation.utils.data.request;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * Created by bza on 6/28/2017.
 */
@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Claim")
public class Claim {

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

    @Override
    public String toString() {
        return "ClassPojo [category = " + category + ", newItem = " + newItem + ", description = " + description + ", quantity = " + quantity + ", claimToken = " + claimToken + ", originalDescription = " + originalDescription + ", room = " + room + "]";
    }

}
