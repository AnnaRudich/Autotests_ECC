package com.scalepoint.automation.shared;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
@Getter
@Setter
@ToString
public class VoucherInfo {
    String voucherId;
    String voucherSupplier;
    Double purchaseDiscount;
}
