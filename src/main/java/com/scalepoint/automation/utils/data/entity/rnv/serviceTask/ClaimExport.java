package com.scalepoint.automation.utils.data.entity.rnv.serviceTask;

import javax.xml.bind.annotation.XmlAttribute;

public class ClaimExport extends Claim {
    private String policyType;

    @XmlAttribute
    public String getPolicyType() {
        return policyType;
    }

    public void setPolicyType(String policyType) {
        this.policyType = policyType;
    }
}
