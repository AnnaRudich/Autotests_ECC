package com.scalepoint.automation.utils.data.entity.order;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Setter
@Getter
@ToString
public class Product {
    //DK3828512
    String productID;
    //1986156
    String skuNumber;

    AgreementData agreementData;
}
