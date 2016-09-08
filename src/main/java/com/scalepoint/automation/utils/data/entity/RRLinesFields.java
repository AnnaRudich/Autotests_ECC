package com.scalepoint.automation.utils.data.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by sro on 2/5/14.
 */

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
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

    public String getDescription() {
        return description;
    }

    public String getAgeFrom() {
        return ageFrom;
    }

    public String getAgeTo() {
        return ageTo;
    }

    public String getNewItem() {
        return newItem;
    }

    public String getPriceFrom() {
        return priceFrom;
    }

    public String getPriceTo() {
        return priceTo;
    }

    public String getDocumentation() {
        return documentation;
    }

    public String getClaimantRating() {
        return claimantRating;
    }

    public String getCashReduction() {
        return cashReduction;
    }

    public String getClaimReduction() {
        return claimReduction;
    }
}
