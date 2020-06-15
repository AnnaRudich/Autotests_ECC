package com.scalepoint.automation.utils.data.entity.order;


import lombok.Builder;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@Builder
@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class MarketPrice {

    @XmlAttribute
    @Builder.Default
    Double amount=0.00;
    @XmlAttribute
    @Builder.Default
    Double amountNet=0.00;

}
