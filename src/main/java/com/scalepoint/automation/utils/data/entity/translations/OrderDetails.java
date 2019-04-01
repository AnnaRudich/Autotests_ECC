package com.scalepoint.automation.utils.data.entity.translations;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class OrderDetails {
    @XmlElement
    private String indemnity;
    @XmlElement
    private String orderedItems;
    @XmlElement
    private String withdrawalls;
    @XmlElement
    private String deposits;
    @XmlElement
    private String remainingIdemnity;
    @XmlElement
    private String totalText;

    public String getIndemnity(String companyName) {
        return substituteParams(indemnity, companyName);
    }

    private String substituteParams(String message, String companyName) {
        return message.replace("{companyName}", companyName);
    }
}
