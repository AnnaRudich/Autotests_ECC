package com.scalepoint.automation.utils.data.entity.order;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@Builder
@Setter
@Getter
@ToString
@XmlAccessorType(XmlAccessType.FIELD)
public class OrderLine {
    @XmlAttribute
    int quantity;
    @XmlAttribute
    String description;

    OrderedItem orderedItem;

    BasePurchasePrice basePurchasePrice;
    TotalPurchasePrice totalPurchasePrice;
    TotalInvoicePrice totalInvoicePrice;
    Freightprice freightprice;
}
