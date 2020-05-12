package com.scalepoint.automation.utils.data.entity.order;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.util.UUID;

@Setter
@Getter
@ToString
@Builder
@XmlAccessorType(XmlAccessType.FIELD)
public class Order {
    @XmlAttribute
    @Builder.Default
    String orderID = UUID.randomUUID().toString();

    @XmlElement(name = "OrderTotalPurchasePrice")
    OrderTotalPurchasePrice orderTotalPurchasePrice;
    @XmlElement(name = "OrderTotalInvoicePrice")
    OrderTotalInvoicePrice orderTotalInvoicePrice;
    @XmlElement(name = "Payments")
    Payments payments;
    @XmlElement(name = "SubOrders")
    SubOrders suborders;
    @XmlElement(name = "ShippingAddress")
    ShippingAddress shippingAddress;
}
