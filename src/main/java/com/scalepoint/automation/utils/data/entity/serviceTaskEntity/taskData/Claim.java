package com.scalepoint.automation.utils.data.entity.serviceTaskEntity.taskData;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public class Claim {
    @XmlAttribute
    private String claimDate;

    @XmlAttribute
    private String claimHandlerName;

    @XmlAttribute
    private String claimHandlerEmail;

    @XmlAttribute
    private String insuranceCompanyId;

    @XmlAttribute
    private String claimHandlerFullName;

    @XmlAttribute
    private String insuranceCompanyName;

    @XmlAttribute
    private String selfRisk;

    @XmlAttribute
    private String policyNumber;

    @XmlAttribute
    private String claimNumber;

    @XmlAttribute
    private String claimReferenceNumber;

    @XmlAttribute
    private String customerNoteToClaim;

    public String getClaimDate ()
    {
        return claimDate;
    }

    public void setClaimDate (String claimDate)
    {
        this.claimDate = claimDate;
    }

    public String getClaimHandlerName ()
    {
        return claimHandlerName;
    }

    public void setClaimHandlerName (String claimHandlerName)
    {
        this.claimHandlerName = claimHandlerName;
    }

    public String getClaimHandlerEmail ()
    {
        return claimHandlerEmail;
    }

    public void setClaimHandlerEmail (String claimHandlerEmail)
    {
        this.claimHandlerEmail = claimHandlerEmail;
    }

    public String getInsuranceCompanyId ()
    {
        return insuranceCompanyId;
    }

    public void setInsuranceCompanyId (String insuranceCompanyId)
    {
        this.insuranceCompanyId = insuranceCompanyId;
    }

    public String getClaimHandlerFullName ()
    {
        return claimHandlerFullName;
    }

    public void setClaimHandlerFullName (String claimHandlerFullName)
    {
        this.claimHandlerFullName = claimHandlerFullName;
    }

    public String getInsuranceCompanyName ()
    {
        return insuranceCompanyName;
    }

    public void setInsuranceCompanyName (String insuranceCompanyName)
    {
        this.insuranceCompanyName = insuranceCompanyName;
    }

    public String getSelfRisk ()
    {
        return selfRisk;
    }

    public void setSelfRisk (String selfRisk)
    {
        this.selfRisk = selfRisk;
    }

    public String getPolicyNumber ()
    {
        return policyNumber;
    }

    public void setPolicyNumber (String policyNumber)
    {
        this.policyNumber = policyNumber;
    }

    public String getClaimNumber ()
    {
        return claimNumber;
    }

    public void setClaimNumber (String claimNumber)
    {
        this.claimNumber = claimNumber;
    }

    public String getClaimReferenceNumber ()
    {
        return claimReferenceNumber;
    }

    public void setClaimReferenceNumber (String claimReferenceNumber)
    {
        this.claimReferenceNumber = claimReferenceNumber;
    }

    public String getCustomerNoteToClaim ()
    {
        return customerNoteToClaim;
    }

    public void setCustomerNoteToClaim (String customerNoteToClaim)
    {
        this.customerNoteToClaim = customerNoteToClaim;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [claimDate = "+claimDate+", claimHandlerName = "+claimHandlerName+", claimHandlerEmail = "+claimHandlerEmail+", insuranceCompanyId = "+insuranceCompanyId+", claimHandlerFullName = "+claimHandlerFullName+", insuranceCompanyName = "+insuranceCompanyName+", selfRisk = "+selfRisk+", policyNumber = "+policyNumber+", claimNumber = "+claimNumber+", claimReferenceNumber = "+claimReferenceNumber+", customerNoteToClaim = "+customerNoteToClaim+"]";
    }
}


