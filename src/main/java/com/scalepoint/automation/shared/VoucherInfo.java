package com.scalepoint.automation.shared;

import lombok.Data;

@Data
public class VoucherInfo {
    String voucherId;
    String voucherSupplierId;
    Double purchaseDiscount;
}
