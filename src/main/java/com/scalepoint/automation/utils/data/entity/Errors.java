package com.scalepoint.automation.utils.data.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Errors {

    @XmlElement
    private String cancelledClaim;
    @XmlElement
    private String invalidBestFitNumber;

    public String getCancelledClaim() {
        return cancelledClaim;
    }

    public String getInvalidBestFitNumber() {
        return invalidBestFitNumber;
    }
}
