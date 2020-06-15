package com.scalepoint.automation.utils.data.entity.rnv.serviceTask;

import lombok.Data;

import javax.xml.bind.annotation.*;
import java.util.List;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class ServiceTaskExport extends ServiceTask {

    @XmlElement(nillable = true)
    private Claimant claimant;
    @XmlElement(nillable = true)
    private ClaimExport claim;
    @XmlElement(nillable = true)
    private ServicePartnerExport servicePartner;
    @XmlElementWrapper(name = "serviceLines")
    @XmlElement(name = "serviceLine")
    private List<ServiceLineExport> serviceLines;
    @XmlElement
    private String noteToServicePartner;
    @XmlAttribute(required = true)
    private String replyAddress;
    @XmlElement
    private String invoicePaidBy;
    @XmlElementWrapper(name = "attachments")
    @XmlElement(name = "attachment")
    private List<AttachmentExport> attachments;
    @XmlAttribute
    private String damageType;

}
