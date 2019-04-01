package com.scalepoint.automation.utils.data.entity.translations;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class DiscretionaryReason {
    @XmlElement
    private String estimatedCompensationDueToAgeAndUseFairValue;
    @XmlElement
    private String maxCoverage;
    @XmlElement
    private String estimatedCompensationDueToLackOfDocumentation;
    @XmlElement
    private String estimatedCompensationDueToConsumption;
    @XmlElement
    private String fairValue;
}
