package com.scalepoint.automation.utils.data.entity.eccIntegration;

import lombok.Data;

import javax.xml.bind.annotation.*;

@Data
@XmlRootElement(name = "ECCIntegration")
@XmlAccessorType(XmlAccessType.FIELD)
public class EccIntegration {

    @XmlAttribute
    private String updateAction;
    @XmlElement(name = "Claim")
    private Claim claim;
    @XmlElement(name = "Claimant")
    private Claimant claimant;

    @Override
    public String toString() {
        return "EccIntegration{" +
                "updateAction='" + updateAction + '\'' +
                ", claim=" + claim +
                ", claimant=" + claimant +
                '}';
    }

}
