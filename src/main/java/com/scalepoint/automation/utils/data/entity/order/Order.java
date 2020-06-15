package com.scalepoint.automation.utils.data.entity.order;

import com.scalepoint.automation.utils.RandomUtils;
import lombok.Builder;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@Builder
@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class Order {

    @XmlAttribute
    @Builder.Default
    String orderID = String.valueOf(RandomUtils.randomInt());
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
