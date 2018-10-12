package com.scalepoint.automation.utils.data.entity.ServiceTaskEntity;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class ServiceTask
    {
        @XmlElement
        private String createdDate;

        @XmlElement
        private String uniqueId;

        private Invoice invoice;

        private ServiceLines serviceLines;

        private ServicePartner servicePartner;



    public Invoice getInvoice ()
    {
        return invoice;
    }

    public void setInvoice (Invoice invoice)
    {
        this.invoice = invoice;
    }

    public String getCreatedDate ()
    {
        return createdDate;
    }

    public void setCreatedDate (String createdDate)
    {
        this.createdDate = createdDate;
    }

    public ServiceLines getServiceLines ()
    {
        return serviceLines;
    }

    public void setServiceLines (ServiceLines serviceLines)
    {
        this.serviceLines = serviceLines;
    }

    public ServicePartner getServicePartner ()
    {
        return servicePartner;
    }

    public void setServicePartner (ServicePartner servicePartner)
    {
        this.servicePartner = servicePartner;
    }

    public String getUniqueId ()
    {
        return uniqueId;
    }

    public void setUniqueId (String uniqueId)
    {
        this.uniqueId = uniqueId;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [invoice = "+invoice+", createdDate = "+createdDate+", serviceLines = "+serviceLines+", servicePartner = "+servicePartner+", uniqueId = "+uniqueId+"]";
    }
}
