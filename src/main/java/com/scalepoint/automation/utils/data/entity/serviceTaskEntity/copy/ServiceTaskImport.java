package com.scalepoint.automation.utils.data.entity.serviceTaskEntity.copy;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;
import java.util.List;
@XmlRootElement(name = "serviceTask")
public class ServiceTaskImport extends ServiceTask{


    private ServicePartnerImport servicePartner;
    private List<ServiceLineImport> serviceLines;
    private InvoiceImport invoice;

    private BigDecimal takenSelfRisk;
    private BigDecimal updateClaimSelfRisk;
    private String updateClaimSelfRiskReason;

    public ServiceTaskImport(){}

    @XmlElement(nillable = true)
    public ServicePartnerImport getServicePartner() {
        return servicePartner;
    }

    public void setServicePartner(ServicePartnerImport servicePartner) {
        this.servicePartner = servicePartner;
    }

    @XmlElementWrapper(name="serviceLines")
    @XmlElement(name="serviceLine")
    public List<ServiceLineImport> getServiceLines() {
        return serviceLines;
    }

    public void setServiceLines(List<ServiceLineImport> serviceLines) {
        this.serviceLines = serviceLines;
    }


    @XmlElement(nillable = true)
    public InvoiceImport getInvoice() {
        return invoice;
    }

    public void setInvoice(InvoiceImport invoice) {
        this.invoice = invoice;
    }

    @XmlAttribute(required = false)
    public BigDecimal getTakenSelfRisk() {
        return takenSelfRisk;
    }

    public void setTakenSelfRisk(BigDecimal takenSelfRisk) {
        this.takenSelfRisk = takenSelfRisk;
    }

    @XmlAttribute(required = false)
    public BigDecimal getUpdateClaimSelfRisk() {
        return updateClaimSelfRisk;
    }

    public void setUpdateClaimSelfRisk(BigDecimal updateClaimSelfRisk) {
        this.updateClaimSelfRisk = updateClaimSelfRisk;
    }

    @XmlAttribute(required = false)
    public String getUpdateClaimSelfRiskReason() {
        return updateClaimSelfRiskReason;
    }

    public void setUpdateClaimSelfRiskReason(String updateClaimSelfRiskReason) {
        this.updateClaimSelfRiskReason = updateClaimSelfRiskReason;
    }
}
