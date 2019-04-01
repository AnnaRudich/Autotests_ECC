package com.scalepoint.automation.utils.data.entity.translations;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class RRLinesFields {

    @XmlElement
    private String description;
    @XmlElement
    private String ageFrom;
    @XmlElement
    private String ageTo;
    @XmlElement
    private String newItem;
    @XmlElement
    private String priceFrom;
    @XmlElement
    private String priceTo;
    @XmlElement
    private String documentation;
    @XmlElement
    private String claimantRating;
    @XmlElement
    private String claimReduction;
    @XmlElement
    private String cashReduction;
}
