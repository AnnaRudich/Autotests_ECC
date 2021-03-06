package com.scalepoint.automation.utils.data.entity.input;

import com.scalepoint.automation.utils.RandomUtils;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * NewSystemUser: kke
 */
@Data
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ReductionRule implements Cloneable {

    private String rrName = RandomUtils.randomName("RR ÆæØøÅåß");
    private String policyName1 = "Pola ÆæØøÅåß" + RandomUtils.randomInt();
    private String policyName2 = "Polb ÆæØøÅåß" + RandomUtils.randomInt();
    private String description1 = "Desc1 ÆæØøÅåß" + RandomUtils.randomInt();
    @XmlElement
    private String ageFrom1;
    @XmlElement
    private String ageTo1;
    @XmlElement
    private String ageFrom2;
    @XmlElement
    private String ageTo2;
    @XmlElement
    private String claimReduction1;
    @XmlElement
    private String priceRangeFrom1;
    @XmlElement
    private String priceRangeTo1;
    @XmlElement
    private String priceRangeFrom2;
    @XmlElement
    private String priceRangeTo2;
    @XmlElement
    private String maxDepreciation;

    public ReductionRule clone() throws CloneNotSupportedException {
        return (ReductionRule) super.clone();
    }

}
