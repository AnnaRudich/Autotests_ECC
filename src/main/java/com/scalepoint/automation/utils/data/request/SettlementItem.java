package com.scalepoint.automation.utils.data.request;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Created by bza on 6/28/2017.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SettlementItem")
public class SettlementItem
{
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

    public String getAutomaticDepreciation ()
    {
        return automaticDepreciation;
    }

    public void setAutomaticDepreciation (String automaticDepreciation)
    {
        this.automaticDepreciation = automaticDepreciation;
    }

    public String getReductionRuleLineId ()
{
    return reductionRuleLineId;
}

    public void setReductionRuleLineId (String reductionRuleLineId)
    {
        this.reductionRuleLineId = reductionRuleLineId;
    }

    public String getTaxRate ()
    {
        return taxRate;
    }

    public void setTaxRate (String taxRate)
    {
        this.taxRate = taxRate;
    }

    public Claim getClaim ()
    {
        return claim;
    }

    public void setClaim (Claim Claim)
    {
        this.claim = Claim;
    }

    public String getDepreciationChanged ()
    {
        return depreciationChanged;
    }

    public void setDepreciationChanged (String depreciationChanged)
    {
        this.depreciationChanged = depreciationChanged;
    }

    public String getIncludeTax ()
    {
        return includeTax;
    }

    public void setIncludeTax (String includeTax)
    {
        this.includeTax = includeTax;
    }

    public String getDepreciateDiscounted ()
    {
        return depreciateDiscounted;
    }

    public void setDepreciateDiscounted (String depreciateDiscounted)
    {
        this.depreciateDiscounted = depreciateDiscounted;
    }

    public String getDepreciationType ()
    {
        return depreciationType;
    }

    public void setDepreciationType (String depreciationType)
    {
        this.depreciationType = depreciationType;
    }

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getReviewed ()
    {
        return reviewed;
    }

    public void setReviewed (String reviewed)
    {
        this.reviewed = reviewed;
    }

    public String getDiscretionaryRuleDepreciation ()
    {
        return discretionaryRuleDepreciation;
    }

    public void setDiscretionaryRuleDepreciation (String discretionaryRuleDepreciation)
    {
        this.discretionaryRuleDepreciation = discretionaryRuleDepreciation;
    }

    public String getCashReduction ()
    {
        return cashReduction;
    }

    public void setCashReduction (String cashReduction)
    {
        this.cashReduction = cashReduction;
    }

    public String getPostDepreciation ()
    {
        return postDepreciation;
    }

    public void setPostDepreciation (String postDepreciation)
    {
        this.postDepreciation = postDepreciation;
    }

    public String getRejected ()
    {
        return rejected;
    }

    public void setRejected (String rejected)
    {
        this.rejected = rejected;
    }

    public String getActive ()
    {
        return active;
    }

    public void setActive (String active)
    {
        this.active = active;
    }

    public String getPolicyRuleDepreciation ()
    {
        return policyRuleDepreciation;
    }

    public void setPolicyRuleDepreciation (String policyRuleDepreciation)
    {
        this.policyRuleDepreciation = policyRuleDepreciation;
    }

    public String getDocumentationOk ()
    {
        return documentationOk;
    }

    public void setDocumentationOk (String documentationOk)
    {
        this.documentationOk = documentationOk;
    }

    public String getPreDepreciation ()
    {
        return preDepreciation;
    }

    public void setPreDepreciation (String preDepreciation)
    {
        this.preDepreciation = preDepreciation;
    }

    public Valuations getValuations ()
    {
        return valuations;
    }

    public void setValuations (Valuations Valuations)
    {
        this.valuations = Valuations;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [automaticDepreciation = "+automaticDepreciation+", reductionRuleLineId = "+reductionRuleLineId+", taxRate = "+taxRate+", claim = "+ claim +", depreciationChanged = "+depreciationChanged+", includeTax = "+includeTax+", depreciateDiscounted = "+depreciateDiscounted+", depreciationType = "+depreciationType+", id = "+id+", reviewed = "+reviewed+", discretionaryRuleDepreciation = "+discretionaryRuleDepreciation+", cashReduction = "+cashReduction+", postDepreciation = "+postDepreciation+", rejected = "+rejected+", active = "+active+", policyRuleDepreciation = "+policyRuleDepreciation+", documentationOk = "+documentationOk+", preDepreciation = "+preDepreciation+", Valuations = "+valuations+"]";
    }
}
