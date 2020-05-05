package com.scalepoint.automation.utils.data.entity.order;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Setter
@Getter
@ToString
public class Order {
    OrderTotalPurchasePrice orderTotalPurchasePrice;
    OrderTotalInvoicePrice orderTotalInvoicePrice;
    Payments payments;
    Suborders suborders;
    ShippingAddress shippingAddress;
}
