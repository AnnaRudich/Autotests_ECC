package com.scalepoint.automation.utils.data.entity.translations;


import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Data
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

}
