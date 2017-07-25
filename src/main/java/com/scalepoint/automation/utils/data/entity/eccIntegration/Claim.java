package com.scalepoint.automation.utils.data.entity.eccIntegration;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.UUID;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Claime")
public class Claim {

    @XmlAttribute
    private String claimNumber = UUID.randomUUID().toString();
    @XmlAttribute
    private String allowAutoClose;

    @XmlElement(name = "ClaimedItems")
    private ClaimedItems claimedItems;

    public ClaimedItems getClaimedItems() {
        return claimedItems;
    }

    public Claim setClaimedItems(ClaimedItems claimedItems) {
        this.claimedItems = claimedItems;
        return this;
    }

    public String getClaimNumber() {
        return claimNumber;
    }

    public Claim setClaimNumber(String claimNumber) {
        this.claimNumber = claimNumber;
        return this;
    }

    public String getAllowAutoClose() {
        return allowAutoClose;
    }

    public Claim setAllowAutoClose(String allowAutoClose) {
        this.allowAutoClose = allowAutoClose;
        return this;
    }

    @Override
    public String toString() {
        return "Claim{" +
                "claimNumber='" + claimNumber + '\'' +
                ", allowAutoClose='" + allowAutoClose + '\'' +
                ", claimedItems=" + claimedItems +
                '}';
    }
}
