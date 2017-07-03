package com.scalepoint.automation.utils.data.request;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Created by bza on 6/28/2017.
 */
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

    public void setValuation (Valuation[] Valuation)
    {
        this.Valuation = Valuation;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [Valuation = "+Valuation+"]";
    }
}
