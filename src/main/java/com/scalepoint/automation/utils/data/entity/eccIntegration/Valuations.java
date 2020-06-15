package com.scalepoint.automation.utils.data.entity.eccIntegration;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Valuations")
public class Valuations {

    @XmlElement(name = "Valuation")
    private Valuation[] Valuation;

    @Override
    public String toString() {
        return "ClassPojo [Valuation = " + Valuation + "]";
    }
}
