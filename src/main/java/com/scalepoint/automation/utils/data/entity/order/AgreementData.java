package com.scalepoint.automation.utils.data.entity.order;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Setter
@Getter
@ToString
public class AgreementData {
    String priceModelID = "DK1";
    int priceModelType = 1;
    String agreementID = "DK248";
    String discountCategoryID= "DK1";
    String discountStartDate = "2010-01-01T00:00:00";
    String discountEndDate = "2025-01-01T00:00:00";
    Double discountValue = 0.0000;
    int priceSourceType = 1;
    String priceSourceSupplierID = "DK13";
    String originalProductID = "DK4066212";

    RecommendedPrice recommendedPrice;
    MarketPrice marketPrice;
    SupplierShopPrice supplierShopPrice;
}
