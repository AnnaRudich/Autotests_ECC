package com.scalepoint.automation.utils.data.entity.order;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Builder
@Setter
@Getter
@ToString
public class Suborder {

    String subOrderID = UUID.randomUUID().toString();
    OrderLines orderLines;
    SubTotalPurchasePrice subTotalPurchasePrice;
    SubTotalInvoicePrice subTotalInvoicePrice;
    Supplier supplier;
}
