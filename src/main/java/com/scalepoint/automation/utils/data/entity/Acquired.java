package com.scalepoint.automation.utils.data.entity;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "acquiredType")
@XmlAccessorType(XmlAccessType.FIELD)
public class Acquired {

    @XmlElement(name = "new")
    private String acquiredNew;

    @XmlElement
    private String used;

    @XmlElement
    private String heritage;

    @XmlElement
    private String gift;


    public String getAcquiredNew() {
        return acquiredNew;
    }

    public String getUsed() {
        return used;
    }

    public String getHeritage() {
        return heritage;
    }

    public String getGift() {
        return gift;
    }
}
