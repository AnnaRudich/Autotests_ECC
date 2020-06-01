package com.scalepoint.automation.shared;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
@Getter
@Setter
@ToString
public class XpriceInfo {

    private String productKey;

    private String productId;

    private double invoicePrice;

    private String supplierName;

    private double supplierShopPrice;

    private String priceModelID;

    private String priceModelType;

    private String discountFromDate;

    private String discountToDate;

    private Double discountValue;

    private String priceSourceType;

    private String priceSourceSupplierID;

    private String originalProductID;

    private String supplierId;

    private String agreementId;
}
