package com.scalepoint.automation.utils.data.entity.order;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@Builder
@Setter
@Getter
@ToString

@XmlAccessorType(XmlAccessType.FIELD)
public class AgreementData {
    @XmlAttribute
    @Builder.Default
    String priceModelID = "DK1";

    @XmlAttribute
    @Builder.Default
    int priceModelType = 1;

    @XmlAttribute
    @Builder.Default
    String agreementID = "DK248";

    @XmlAttribute
    @Builder.Default
    String discountCategoryID= "DK1";

    @XmlAttribute
    @Builder.Default
    String discountStartDate = "2010-01-01T00:00:00";

    @XmlAttribute
    @Builder.Default
    String discountEndDate = "2025-01-01T00:00:00";

    @XmlAttribute
    @Builder.Default
    Double discountValue = 0.0000;

    @XmlAttribute
    @Builder.Default
    int priceSourceType = 1;

    @XmlAttribute
    @Builder.Default
    String priceSourceSupplierID = "DK13";

    @XmlAttribute
    @Builder.Default
    String originalProductID = "DK4066212";


    @Builder.Default
    @XmlElement(name="RecommendedPrice")
    RecommendedPrice recommendedPrice;

    @Builder.Default
    @XmlElement(name="MarketPrice")
    MarketPrice marketPrice;

    @Builder.Default
    @XmlElement(name="SupplierShopPrice")
    SupplierShopPrice supplierShopPrice;
}
