package com.scalepoint.automation.utils.data.entity.serviceTaskEntity;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class ServiceTask   {private String replyAddress;

    @XmlElement
    private String invoicePaidBy;
@XmlAttribute
    private String damageType;
@XmlElement
    private Claim claim;
@XmlAttribute
    private String createdDate;
@XmlElement
    private ServiceLines serviceLines;

    private Claimant claimant;

    private String noteToServicePartner;

    private ServicePartner servicePartner;

    private String uniqueId;

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
