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
    String priceModelType;

    @XmlAttribute
    String agreementID;

    @XmlAttribute
    @Builder.Default
    Double discountValue = 0.0000;

    @XmlAttribute
    String priceSourceType;

    @XmlAttribute
    @Builder.Default
    String priceSourceSupplierID = "DK13";

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
