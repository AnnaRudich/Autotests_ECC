package com.scalepoint.automation.utils.data.entity.eccIntegration;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Valuations")
public class Valuations
{
    @XmlElement(name = "Valuation")
    private Valuation[] Valuation;

    public Valuation[] getValuation ()
    {
        return Valuation;
    }

    public Valuations setValuation (Valuation[] Valuation)
    {
        this.Valuation = Valuation;
        return this;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [Valuation = "+Valuation+"]";
    }
}
