package com.scalepoint.automation.utils.data.entity.rnv.serviceTask;

import lombok.Data;

import javax.xml.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "serviceTask")
@XmlType(propOrder = {
        "invoice",
        "serviceLines",
        "servicePartner"})
public class ServiceTaskImport extends ServiceTask {

    @XmlElementWrapper(name = "serviceLines")
    @XmlElement(name = "serviceLine")
    private List<ServiceLineImport> serviceLines;
    @XmlElement(nillable = true)
    private ServicePartnerImport servicePartner;
    @XmlElement
    private InvoiceImport invoice;
    @XmlAttribute
    private BigDecimal takenSelfRisk;
    @XmlAttribute
    private BigDecimal updateClaimSelfRisk;
    @XmlAttribute
    private String updateClaimSelfRiskReason;

}
