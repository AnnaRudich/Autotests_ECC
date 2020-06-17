package com.scalepoint.automation.utils.data.entity.order;

import lombok.Builder;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@Builder
@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class AgreementData {

    @XmlAttribute
    String priceModelID;
    @XmlAttribute
    String priceModelType;
    @XmlAttribute
    String agreementID;
    @XmlAttribute
    Double discountValue;
    @XmlAttribute
    String priceSourceType;
    @XmlAttribute
    String priceSourceSupplierID;
    @XmlElement(name="RecommendedPrice")
    RecommendedPrice recommendedPrice;
    @XmlElement(name="MarketPrice")
    MarketPrice marketPrice;
    @XmlElement(name="SupplierShopPrice")
    SupplierShopPrice supplierShopPrice;

}
