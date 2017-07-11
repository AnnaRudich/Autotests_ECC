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
@XmlType(name = "Valuation")
public class Valuation
{
    @XmlAttribute
    private String id;
    @XmlAttribute
    private String hidden;
    @XmlAttribute
    private String baseValuation;
    @XmlElement(name = "Price")
    private Price[] Price;
    @XmlAttribute
    private String dirty;
    @XmlAttribute
    private String valuationType;
    @XmlAttribute
    private String active;
    @XmlAttribute
    private String priceAfterAllDeductions;
    @XmlElement(name = "VoucherReplacement")
    private VoucherReplacement VoucherReplacement;
    @XmlAttribute
    private String quantity;
    @XmlAttribute
    private String preDepreciation;

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getHidden ()
    {
        return hidden;
    }

    public void setHidden (String hidden)
    {
        this.hidden = hidden;
    }

    public String getBaseValuation ()
    {
        return baseValuation;
    }

    public void setBaseValuation (String baseValuation)
    {
        this.baseValuation = baseValuation;
    }

    public Price[] getPrice ()
    {
        return Price;
    }

    public void setPrice (Price[] Price)
    {
        this.Price = Price;
    }

    public String getDirty ()
    {
        return dirty;
    }

    public void setDirty (String dirty)
    {
        this.dirty = dirty;
    }

    public String getValuationType ()
    {
        return valuationType;
    }

    public void setValuationType (String valuationType)
    {
        this.valuationType = valuationType;
    }

    public String getActive ()
    {
        return active;
    }

    public void setActive (String active)
    {
        this.active = active;
    }

    public String getPriceAfterAllDeductions ()
    {
        return priceAfterAllDeductions;
    }

    public void setPriceAfterAllDeductions (String priceAfterAllDeductions)
    {
        this.priceAfterAllDeductions = priceAfterAllDeductions;
    }

    public VoucherReplacement getVoucherReplacement ()
    {
        return VoucherReplacement;
    }

    public void setVoucherReplacement (VoucherReplacement VoucherReplacement)
    {
        this.VoucherReplacement = VoucherReplacement;
    }

    public String getQuantity ()
    {
        return quantity;
    }

    public void setQuantity (String quantity)
    {
        this.quantity = quantity;
    }

    public String getPreDepreciation ()
    {
        return preDepreciation;
    }

    public void setPreDepreciation (String preDepreciation)
    {
        this.preDepreciation = preDepreciation;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [id = "+id+", hidden = "+hidden+", baseValuation = "+baseValuation+", Price = "+Price+", dirty = "+dirty+", valuationType = "+valuationType+", active = "+active+", priceAfterAllDeductions = "+priceAfterAllDeductions+", VoucherReplacement = "+VoucherReplacement+", quantity = "+quantity+", preDepreciation = "+preDepreciation+"]";
    }
}
