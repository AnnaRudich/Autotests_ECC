package com.scalepoint.automation.utils.data.entity.order;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import java.util.UUID;

@Setter
@Getter
@ToString
@Builder
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="Order")
public class Order {
    @XmlAttribute
    @Builder.Default
    String orderID = UUID.randomUUID().toString();

    OrderTotalPurchasePrice orderTotalPurchasePrice;
    OrderTotalInvoicePrice orderTotalInvoicePrice;
    Payments payments;
    SubOrders suborders;
    ShippingAddress shippingAddress;
}
