package com.scalepoint.automation.utils.data.entity.eccIntegration;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Damage")
public class Damage {

    @XmlAttribute
    private String damageDate;
    @XmlAttribute
    private String damageDescription;

    public String getDamageDate() {
        return damageDate;
    }

    public Damage setDamageDate(String damageDate) {
        this.damageDate = damageDate;
        return this;
    }

    public String getDamageDescription() {
        return damageDescription;
    }

    public Damage setDamageDescription(String damageDescription) {
        this.damageDescription = damageDescription;
        return this;
    }

    @Override
    public String toString() {
        return "Damage{" +
                "damageDate='" + damageDate + '\'' +
                ", damageDescription='" + damageDescription + '\'' +
                '}';
    }
}
