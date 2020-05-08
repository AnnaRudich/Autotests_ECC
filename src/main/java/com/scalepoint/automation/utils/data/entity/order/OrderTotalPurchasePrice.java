package com.scalepoint.automation.utils.data.entity.order;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@Builder
@Setter
@Getter
@ToString
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="OrderTotalPurchasePrice")
public class OrderTotalPurchasePrice {
    @XmlAttribute
    Double amount;
    @XmlAttribute
    Double amountNet;
}
