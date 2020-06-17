package com.scalepoint.automation.utils.data.entity.eccIntegration;

import lombok.Data;

import javax.xml.bind.annotation.*;
import java.util.UUID;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Claime")
public class Claim {

    @XmlElement(name = "Damage")
    private Damage damage;
    @XmlAttribute
    private String claimNumber = UUID.randomUUID().toString();
    @XmlAttribute
    private String allowAutoClose;
    @XmlElement(name = "ClaimedItems")
    private ClaimedItems claimedItems;

    @Override
    public String toString() {
        return "Claim{" +
                "damage=" + damage +
                ", claimNumber='" + claimNumber + '\'' +
                ", allowAutoClose='" + allowAutoClose + '\'' +
                ", claimedItems=" + claimedItems +
                '}';
    }
}
