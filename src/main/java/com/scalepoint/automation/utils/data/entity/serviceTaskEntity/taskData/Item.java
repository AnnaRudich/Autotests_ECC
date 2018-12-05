package com.scalepoint.automation.utils.data.entity.serviceTaskEntity.taskData;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public class Item {
    @XmlAttribute
    private String customerDescription;

    @XmlAttribute
    private String depreciation;

    @XmlAttribute
    private String quantity;

    @XmlAttribute
    private String productMatchDescription;

    public String getCustomerDescription ()
    {
        return customerDescription;
    }

    public void setCustomerDescription (String customerDescription)
    {
        this.customerDescription = customerDescription;
    }

    public String getDepreciation ()
    {
        return depreciation;
    }

    public void setDepreciation (String depreciation)
    {
        this.depreciation = depreciation;
    }

    public String getQuantity ()
    {
        return quantity;
    }

    public void setQuantity (String quantity)
    {
        this.quantity = quantity;
    }

    public String getProductMatchDescription ()
    {
        return productMatchDescription;
    }

    public void setProductMatchDescription (String productMatchDescription)
    {
        this.productMatchDescription = productMatchDescription;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [customerDescription = "+customerDescription+", depreciation = "+depreciation+", quantity = "+quantity+", productMatchDescription = "+productMatchDescription+"]";
    }
}


