package com.scalepoint.automation.utils.data.entity.rnv.serviceTask;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.List;

public class ServiceTaskExport extends ServiceTask{

    private Claimant claimant;
    private ClaimExport claim;
    private ServicePartnerExport servicePartner;
    private List<ServiceLineExport> serviceLines;
    private String noteToServicePartner;
    private String replyAddress;
    private String invoicePaidBy;
    private List<AttachmentExport> attachments;
    private String damageType;

    public ServiceTaskExport() {
    }

    @XmlElement(nillable = true)
    public Claimant getClaimant() {
        return claimant;
    }

    public void setClaimant(Claimant claimant) {
        this.claimant = claimant;
    }

    @XmlElement(nillable = true)
    public ClaimExport getClaim() {
        return claim;
    }

    public void setClaim(ClaimExport claim) {
        this.claim = claim;
    }

    @XmlElement(nillable = true)
    public ServicePartnerExport getServicePartner() {
        return servicePartner;
    }

    public void setServicePartner(ServicePartnerExport servicePartner) {
        this.servicePartner = servicePartner;
    }

    @XmlElementWrapper(name = "serviceLines")
    @XmlElement(name = "serviceLine")
    public List<ServiceLineExport> getServiceLines() {
        return serviceLines;
    }

    private void setServiceLines(List<ServiceLineExport> serviceLines) {
        this.serviceLines = serviceLines;
    }

    @XmlAttribute(required = true)
    public String getReplyAddress() {
        return replyAddress;
    }

    public void setReplyAddress(String replyAddress) {
        this.replyAddress = replyAddress;
    }

    @XmlElement
    public String getNoteToServicePartner() {
        return noteToServicePartner;
    }

    public void setNoteToServicePartner(String noteToServicePartner) {
        this.noteToServicePartner = noteToServicePartner;
    }

    @XmlElement
    public String getInvoicePaidBy() {
        return invoicePaidBy;
    }

    public void setInvoicePaidBy(String invoicePaidBy) {
        this.invoicePaidBy = invoicePaidBy;
    }

    @XmlElementWrapper(name = "attachments")
    @XmlElement(name = "attachment")
    public List<AttachmentExport> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<AttachmentExport> attachments) {
        this.attachments = attachments;
    }

    @XmlAttribute(required = false)
    public String getDamageType() {
        return damageType;
    }

    public void setDamageType(String damageType) {
        this.damageType = damageType;
    }
}
