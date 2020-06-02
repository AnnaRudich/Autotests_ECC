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
public class OrderLine {
    @XmlAttribute
    int quantity;
    @XmlAttribute
    String description;

    @XmlElement(name = "OrderedItem")
    OrderedItem orderedItem;
    @XmlElement(name = "BasePurchasePrice")
    BasePurchasePrice basePurchasePrice;
    @XmlElement(name = "TotalPurchasePrice")
    TotalPurchasePrice totalPurchasePrice;
    @XmlElement(name = "TotalInvoicePrice")
    TotalInvoicePrice totalInvoicePrice;
    @XmlElement(name = "Freightprice")
    Freightprice freightprice;
}
