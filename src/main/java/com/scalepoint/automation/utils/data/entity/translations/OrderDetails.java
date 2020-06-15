package com.scalepoint.automation.utils.data.entity.translations;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class OrderDetails {

    @XmlElement
    private String indemnityText;
    @XmlElement
    private String orderedItemsText;
    @XmlElement
    private String withdrawallsText;
    @XmlElement
    private String depositsText;
    @XmlElement
    private String remainingCompensationText;
    @XmlElement
    private String totalText;

    public String getIndemnity(String companyName) {
        return substituteParams(indemnityText, companyName);
    }

    private String substituteParams(String message, String companyName) {
        return message.replace("{companyName}", companyName);
    }
}
