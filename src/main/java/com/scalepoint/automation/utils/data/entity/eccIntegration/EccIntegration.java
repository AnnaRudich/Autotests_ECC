package com.scalepoint.automation.utils.data.entity.eccIntegration;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "ECCIntegration")
@XmlAccessorType(XmlAccessType.FIELD)
public class EccIntegration {

    @XmlAttribute
    private String updateAction;
    @XmlElement(name = "Claim")
    private Claim claim;
    @XmlElement(name = "Claimant")
    private Claimant claimant;

    public Claim getClaim() {
        return claim;
    }

    public EccIntegration setClaim(Claim claim) {
        this.claim = claim;
        return this;
    }

    public Claimant getClaimant() {
        return claimant;
    }

    public EccIntegration setClaimant(Claimant claimant) {
        this.claimant = claimant;
        return this;
    }

    public String getUpdateAction() {
        return updateAction;
    }

    public EccIntegration setUpdateAction(String updateAction) {
        this.updateAction = updateAction;
        return this;
    }

    @Override
    public String toString() {
        return "EccIntegration{" +
                "updateAction='" + updateAction + '\'' +
                ", claim=" + claim +
                ", claimant=" + claimant +
                '}';
    }

}
