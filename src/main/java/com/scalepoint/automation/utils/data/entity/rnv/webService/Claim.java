package com.scalepoint.automation.utils.data.entity.rnv.webService;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Claim {

    private Integer insuranceCompanyId;
    private String insuranceCompanyName;
    private String claimHandlerName;
    private String claimHandlerFullName;
    private String claimHandlerEmail;
    private Integer claimReferenceNumber;
    private String claimNumber;
    private BigDecimal selfRisk;
    private BigDecimal takenSelfRisk;
    private BigDecimal updateClaimSelfRisk;
    private String updateSelfRiskReason;
    private String claimDate;
    private String customerNoteToClaim;
    private String policyNumber;
    private String policyType;
    private String cultureId;
}
