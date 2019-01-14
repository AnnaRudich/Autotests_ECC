package com.scalepoint.automation.utils.data.entity.rnv.serviceTask;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import java.math.BigDecimal;

@XmlSeeAlso(ClaimExport.class)
@XmlRootElement(name = "claim")
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

    @XmlAttribute
    public int getInsuranceCompanyId() {
        return insuranceCompanyId;
    }

    public void setInsuranceCompanyId(int insuranceCompanyId) {
        this.insuranceCompanyId = insuranceCompanyId;
    }

    @XmlAttribute
    public String getInsuranceCompanyName() {
        return insuranceCompanyName;
    }

    public void setInsuranceCompanyName(String insuranceCompanyName) {
        this.insuranceCompanyName = insuranceCompanyName;
    }

    @XmlAttribute
    public String getClaimHandlerName() {
        return claimHandlerName;
    }

    public void setClaimHandlerName(String claimHandlerName) {
        this.claimHandlerName = claimHandlerName;
    }

    @XmlAttribute
    public String getClaimHandlerFullName() {
        return claimHandlerFullName;
    }

    public void setClaimHandlerFullName(String claimHandlerFullName) {
        this.claimHandlerFullName = claimHandlerFullName;
    }

    @XmlAttribute
    public String getClaimHandlerEmail() {
        return claimHandlerEmail;
    }

    public void setClaimHandlerEmail(String claimHandlerEmail) {
        this.claimHandlerEmail = claimHandlerEmail;
    }

    @XmlAttribute
    public int getClaimReferenceNumber() {
        return claimReferenceNumber;
    }

    public void setClaimReferenceNumber(int claimReferenceNumber) {
        this.claimReferenceNumber = claimReferenceNumber;
    }

    @XmlAttribute
    public String getClaimNumber() {
        return claimNumber;
    }

    public void setClaimNumber(String claimNumber) {
        this.claimNumber = claimNumber;
    }

    @XmlAttribute
    public String getUpdateSelfRiskReason() {
        return updateSelfRiskReason;
    }

    public void setUpdateSelfRiskReason(String updateSelfRiskReason) {
        this.updateSelfRiskReason = updateSelfRiskReason;
    }

    @XmlAttribute
    public BigDecimal getUpdateClaimSelfRisk() {
        return updateClaimSelfRisk;
    }

    public void setUpdateClaimSelfRisk(BigDecimal updateClaimSelfRisk) {
        this.updateClaimSelfRisk = updateClaimSelfRisk;
    }

    @XmlAttribute
    public BigDecimal getSelfRisk() {
        return selfRisk;
    }

    public void setSelfRisk(BigDecimal selfRisk) {
        this.selfRisk = selfRisk;
    }

    @XmlAttribute
    public BigDecimal getTakenSelfRisk() {
        return takenSelfRisk;
    }

    public void setTakenSelfRisk(BigDecimal takenSelfRisk) {
        this.takenSelfRisk = takenSelfRisk;
    }

    @XmlAttribute
    public String getClaimDate() {
        return claimDate;
    }

    public void setClaimDate(String claimDate) {
        this.claimDate = claimDate;
    }

    @XmlAttribute
    public String getCustomerNoteToClaim() {
        return customerNoteToClaim;
    }

    public void setCustomerNoteToClaim(String customerNoteToClaim) {
        this.customerNoteToClaim = customerNoteToClaim;
    }

    @XmlAttribute
    public String getPolicyNumber() {
        return policyNumber;
    }

    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }
}
