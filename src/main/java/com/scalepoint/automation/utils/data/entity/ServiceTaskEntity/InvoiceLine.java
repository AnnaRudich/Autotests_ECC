package com.scalepoint.automation.utils.data.entity.serviceTaskEntity;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "invoiceLine")
@XmlAccessorType(XmlAccessType.FIELD)
public class InvoiceLine {
    @XmlElement
    private String lineTotal;

    @XmlElement
    private String description;

    @XmlElement
    private String unitNetAmount;

    @XmlElement
    private String quantity;

    @XmlElement
    private String unitPrice;

    @XmlElement
    private String unitVatAmount;

    @XmlElement
    private String units;

    public String getLineTotal ()
    {
        return lineTotal;
    }

    public void setLineTotal (String lineTotal)
    {
        this.lineTotal = lineTotal;
    }

    public String getDescription ()
    {
        return description;
    }

    public void setDescription (String description)
    {
        this.description = description;
    }

    public String getUnitNetAmount ()
    {
        return unitNetAmount;
    }

    public void setUnitNetAmount (String unitNetAmount)
    {
        this.unitNetAmount = unitNetAmount;
    }

    public String getQuantity ()
    {
        return quantity;
    }

    public void setQuantity (String quantity)
    {
        this.quantity = quantity;
    }

    public String getUnitPrice ()
    {
        return unitPrice;
    }

    public void setUnitPrice (String unitPrice)
    {
        this.unitPrice = unitPrice;
    }

    public String getUnitVatAmount ()
    {
        return unitVatAmount;
    }

    public void setUnitVatAmount (String unitVatAmount)
    {
        this.unitVatAmount = unitVatAmount;
    }

    public String getUnits ()
    {
        return units;
    }

    public void setUnits (String units)
    {
        this.units = units;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [lineTotal = "+lineTotal+", description = "+description+", unitNetAmount = "+unitNetAmount+", quantity = "+quantity+", unitPrice = "+unitPrice+", unitVatAmount = "+unitVatAmount+", units = "+units+"]";
    }
}


