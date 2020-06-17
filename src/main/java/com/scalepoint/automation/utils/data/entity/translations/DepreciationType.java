package com.scalepoint.automation.utils.data.entity.translations;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class DepreciationType {

    @XmlElement
    private String policyType;
    @XmlElement
    private String discretionaryType;

}
