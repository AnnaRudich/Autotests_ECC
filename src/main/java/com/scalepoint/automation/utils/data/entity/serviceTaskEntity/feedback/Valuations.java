package com.scalepoint.automation.utils.data.entity.serviceTaskEntity.feedback;

import com.scalepoint.automation.utils.RandomUtils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public class Valuations
{
    @XmlAttribute
    private String usedPrice;

    @XmlAttribute
    private String customerDemand;

    @XmlAttribute
    private String newPrice;

    public Valuations(){
        com.scalepoint.automation.utils.data.entity.serviceTaskEntity.taskData.Valuations valuations
                = new com.scalepoint.automation.utils.data.entity.serviceTaskEntity.taskData.Valuations();

        this.usedPrice = String.valueOf(RandomUtils.randomInt(200));
        this.customerDemand = valuations.getCustomerDemand();
        this.newPrice = valuations.getNewPrice();
    }

    public String getUsedPrice ()
    {
        return usedPrice;
    }

    public void setUsedPrice (String usedPrice)
    {
        this.usedPrice = usedPrice;
    }

    public String getCustomerDemand ()
    {
        return customerDemand;
    }

    public void setCustomerDemand (String customerDemand)
    {
        this.customerDemand = customerDemand;
    }

    public String getNewPrice ()
    {
        return newPrice;
    }

    public void setNewPrice (String newPrice)
    {
        this.newPrice = newPrice;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [usedPrice = "+usedPrice+", customerDemand = "+customerDemand+", newPrice = "+newPrice+"]";
    }
}
