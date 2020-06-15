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
@XmlType(name = "Price")
public class Price {

    @XmlAttribute
    private String amount;
    @XmlAttribute
    private String name;
    @XmlAttribute
    private String netAmount;

    @Override
    public String toString() {
        return "ClassPojo [amount = " + amount + ", name = " + name + ", netAmount = " + netAmount + "]";
    }

}
