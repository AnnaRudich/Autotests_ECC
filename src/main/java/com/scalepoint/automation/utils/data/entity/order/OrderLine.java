package com.scalepoint.automation.utils.data.entity.order;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Setter
@Getter
@ToString
public class OrderLine {
    int quantity;
    String description;

    OrderedItem orderedItem;

    BasePurchasePrice basePurchasePrice;
    TotalPurchasePrice totalPurchasePrice;
    TotalInvoicePrice totalInvoicePrice;
    Freightprice freightprice;
}
