package com.scalepoint.automation.utils.data.entity.rnv.serviceTask;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.*;
import java.math.BigDecimal;


@Data
@XmlSeeAlso(ClaimExport.class)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "claim")
public class Claim {

    @XmlAttribute
    private Integer insuranceCompanyId;
    @XmlAttribute
    private String insuranceCompanyName;
    @XmlAttribute
    private String claimHandlerName;
    @XmlAttribute
    private String claimHandlerFullName;
    @XmlAttribute
    private String claimHandlerEmail;
    @XmlAttribute
    private Integer claimReferenceNumber;
    @XmlAttribute
    private String claimNumber;
    @XmlAttribute
    private BigDecimal selfRisk;
    @XmlAttribute
    private BigDecimal takenSelfRisk;
    @XmlAttribute
    private BigDecimal updateClaimSelfRisk;
    @XmlAttribute
    private String updateSelfRiskReason;
    @XmlAttribute
    private String claimDate;
    @XmlAttribute
    private String customerNoteToClaim;
    @XmlAttribute
    private String policyNumber;
    @XmlAttribute
    private String policyType;
    @XmlAttribute
    private String cultureId;

}
