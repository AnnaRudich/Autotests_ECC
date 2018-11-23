package com.scalepoint.automation.utils.data.entity.serviceTaskEntity;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="serviceTask")
@XmlAccessorType(XmlAccessType.FIELD)
public class ServiceTask   {

    @XmlAttribute
    private String damageType;

    @XmlAttribute
    private String replyAddress;

    @XmlAttribute
    private String createdDate;

    @XmlAttribute
    private String uniqueId;

    @XmlElement
    private Claim claim;

    @XmlElement
    private Claimant claimant;

    @XmlElement
    private String invoicePaidBy;

    @XmlElement
    private String noteToServicePartner;

    @XmlElement
    private ServiceLines serviceLines;

    @XmlElement
    private ServicePartner servicePartner;

    public String getReplyAddress ()
    {
        return replyAddress;
    }

    public void setReplyAddress (String replyAddress)
    {
        this.replyAddress = replyAddress;
    }

    public String getInvoicePaidBy ()
    {
        return invoicePaidBy;
    }

    public void setInvoicePaidBy (String invoicePaidBy)
    {
        this.invoicePaidBy = invoicePaidBy;
    }

    public String getDamageType ()
    {
        return damageType;
    }

    public void setDamageType (String damageType)
    {
        this.damageType = damageType;
    }

    public Claim getClaim ()
    {
        return claim;
    }

    public void setClaim (Claim claim)
    {
        this.claim = claim;
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

    public Claimant getClaimant ()
    {
        return claimant;
    }

    public void setClaimant (Claimant claimant)
    {
        this.claimant = claimant;
    }

    public String getNoteToServicePartner ()
    {
        return noteToServicePartner;
    }

    public void setNoteToServicePartner (String noteToServicePartner)
    {
        this.noteToServicePartner = noteToServicePartner;
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
        return "ClassPojo [replyAddress = "+replyAddress+", invoicePaidBy = "+invoicePaidBy+", damageType = "+damageType+", claim = "+claim+", createdDate = "+createdDate+", serviceLines = "+serviceLines+", claimant = "+claimant+", noteToServicePartner = "+noteToServicePartner+", servicePartner = "+servicePartner+", uniqueId = "+uniqueId+"]";
    }
}
