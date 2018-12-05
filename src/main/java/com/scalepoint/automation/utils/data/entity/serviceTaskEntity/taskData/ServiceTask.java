package com.scalepoint.automation.utils.data.entity.serviceTaskEntity.taskData;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "serviceTask")
@XmlAccessorType(XmlAccessType.FIELD)
public class ServiceTask extends com.scalepoint.automation.utils.data.entity.serviceTaskEntity.ServiceTask{
    @XmlAttribute
    private String replyAddress;

    @XmlElement
    private Attachments attachments;

    @XmlElement
    private Claim claim;

    @XmlAttribute
    private String createdDate;

    @XmlElement
    private ServiceLines serviceLines;

    @XmlElement
    private Claimant claimant;

    @XmlElement
    private ServicePartner servicePartner;

    @XmlElement
    private String noteToServicePartner;

    @XmlAttribute
    private String uniqueId;

    public String getReplyAddress ()
    {
        return replyAddress;
    }

    public void setReplyAddress (String replyAddress)
    {
        this.replyAddress = replyAddress;
    }



    public Attachments getAttachments ()
    {
        return attachments;
    }

    public void setAttachments (Attachments attachments)
    {
        this.attachments = attachments;
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

    public ServicePartner getServicePartner ()
    {
        return servicePartner;
    }

    public void setServicePartner (ServicePartner servicePartner)
    {
        this.servicePartner = servicePartner;
    }

    public String getNoteToServicePartner ()
    {
        return noteToServicePartner;
    }

    public void setNoteToServicePartner (String noteToServicePartner)
    {
        this.noteToServicePartner = noteToServicePartner;
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
        return "ClassPojo [replyAddress = "+replyAddress+", attachments = "+attachments+", claim = "+claim+", createdDate = "+createdDate+", serviceLines = "+serviceLines+", claimant = "+claimant+", servicePartner = "+servicePartner+", noteToServicePartner = "+noteToServicePartner+", uniqueId = "+uniqueId+"]";
    }
}
