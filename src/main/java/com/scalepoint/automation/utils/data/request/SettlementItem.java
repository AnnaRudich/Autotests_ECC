package com.scalepoint.automation.utils.data.request;

import lombok.Data;

import javax.xml.bind.annotation.*;

/**
 * Created by bza on 6/28/2017.
 */
@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SettlementItem")
public class SettlementItem {

    @XmlAttribute
    private String automaticDepreciation;
    @XmlAttribute
    private String reductionRuleLineId;
    @XmlAttribute
    private String taxRate;
    @XmlElement(name = "Claim")
    private Claim claim;
    @XmlAttribute
    private String depreciationChanged;
    @XmlAttribute
    private String includeTax;
    @XmlAttribute
    private String depreciateDiscounted;
    @XmlAttribute
    private String depreciationType;
    @XmlAttribute
    private String id;
    @XmlAttribute
    private String reviewed;
    @XmlAttribute
    private String discretionaryRuleDepreciation;
    @XmlAttribute
    private String cashReduction;
    @XmlAttribute
    private String postDepreciation;
    @XmlAttribute
    private String rejected;
    @XmlAttribute
    private String active;
    @XmlAttribute
    private String policyRuleDepreciation;
    @XmlAttribute
    private String documentationOk;
    @XmlAttribute
    private String preDepreciation;
    @XmlElement(name = "Valuations")
    private Valuations valuations;

    @Override
    public String toString() {
        return "ClassPojo [automaticDepreciation = " + automaticDepreciation + ", reductionRuleLineId = " + reductionRuleLineId + ", taxRate = " + taxRate + ", claim = " + claim + ", depreciationChanged = " + depreciationChanged + ", includeTax = " + includeTax + ", depreciateDiscounted = " + depreciateDiscounted + ", depreciationType = " + depreciationType + ", id = " + id + ", reviewed = " + reviewed + ", discretionaryRuleDepreciation = " + discretionaryRuleDepreciation + ", cashReduction = " + cashReduction + ", postDepreciation = " + postDepreciation + ", rejected = " + rejected + ", active = " + active + ", policyRuleDepreciation = " + policyRuleDepreciation + ", documentationOk = " + documentationOk + ", preDepreciation = " + preDepreciation + ", Valuations = " + valuations + "]";
    }

}
