package com.scalepoint.automation.utils.data.entity.serviceTaskEntity.feedback;

import com.scalepoint.automation.utils.RandomUtils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public class Item
{
    @XmlAttribute
    private String customerDescription;

    @XmlAttribute
    private String servicePartnerNote;

    @XmlAttribute
    private String quantity;

    @XmlAttribute
    private String productMatchDescription;

    public Item(){
        com.scalepoint.automation.utils.data.entity.serviceTaskEntity.taskData.Item item
                = new com.scalepoint.automation.utils.data.entity.serviceTaskEntity.taskData.Item();
        this.customerDescription = item.getCustomerDescription();
        this.servicePartnerNote = RandomUtils.randomName("note_to_ServicePartner");
        this.quantity = item.getQuantity();
        this.productMatchDescription = item.getProductMatchDescription();
    }

    public String getCustomerDescription ()
    {
        return customerDescription;
    }

    public void setCustomerDescription (String customerDescription)
    {
        this.customerDescription = customerDescription;
    }

    public String getServicePartnerNote ()
    {
        return servicePartnerNote;
    }

    public void setServicePartnerNote (String servicePartnerNote)
    {
        this.servicePartnerNote = servicePartnerNote;
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
        return "ClassPojo [customerDescription = "+customerDescription+", servicePartnerNote = "+servicePartnerNote+", quantity = "+quantity+", productMatchDescription = "+productMatchDescription+"]";
    }
}


