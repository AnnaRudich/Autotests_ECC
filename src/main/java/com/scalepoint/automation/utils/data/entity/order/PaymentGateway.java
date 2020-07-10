package com.scalepoint.automation.utils.data.entity.order;

import lombok.Builder;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@Builder
@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class PaymentGateway {
    @XmlAttribute
    @Builder.Default
    String transasctionID="2793920490";

    @XmlAttribute
    @Builder.Default
    String paymentGateway="DIBS";

    @XmlAttribute
    @Builder.Default
    String orderReferenceID="63729729610097617529629";
}
