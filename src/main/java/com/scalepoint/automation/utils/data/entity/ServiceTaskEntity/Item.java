package com.scalepoint.automation.utils.data.entity.ServiceTaskEntity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Item
{
    @XmlElement
    private String customerDescription;

    @XmlElement
    private String age;

    @XmlElement
    private String quantity;

    @XmlElement
    private String productMatchDescription;

    public String getCustomerDescription ()
    {
        return customerDescription;
    }

    public void setCustomerDescription (String customerDescription)
    {
        this.customerDescription = customerDescription;
    }

    public String getAge ()
    {
        return age;
    }

    public void setAge (String age)
    {
        this.age = age;
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
        return "ClassPojo [customerDescription = "+customerDescription+", age = "+age+", quantity = "+quantity+", productMatchDescription = "+productMatchDescription+"]";
    }
}


