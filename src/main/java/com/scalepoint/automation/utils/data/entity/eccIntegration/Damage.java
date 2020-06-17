package com.scalepoint.automation.utils.data.entity.eccIntegration;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Damage")
public class Damage {

    @XmlAttribute
    private String damageDate;
    @XmlAttribute
    private String damageDescription;

    @Override
    public String toString() {
        return "Damage{" +
                "damageDate='" + damageDate + '\'' +
                ", damageDescription='" + damageDescription + '\'' +
                '}';
    }
}
