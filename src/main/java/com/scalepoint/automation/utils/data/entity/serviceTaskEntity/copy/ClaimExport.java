package com.scalepoint.automation.utils.data.entity.serviceTaskEntity.copy;

import javax.xml.bind.annotation.XmlAttribute;

public class ClaimExport extends Claim{
    private String policyType;

    @XmlAttribute
    public String getPolicyType() {
        return policyType;
    }

    public void setPolicyType(String policyType) {
        this.policyType = policyType;
    }
}
