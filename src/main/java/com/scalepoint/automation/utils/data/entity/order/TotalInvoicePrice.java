package com.scalepoint.automation.utils.data.entity.order;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Setter
@Getter
@ToString
public class TotalInvoicePrice {
    Double amount;
    Double amountNet;
}
