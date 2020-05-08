package com.scalepoint.automation.utils.data.entity.order;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import java.util.UUID;

@Builder
@Setter
@Getter
@ToString
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Suborder")
public class SubOrder {
    @XmlAttribute
    @Builder.Default
    String subOrderID = UUID.randomUUID().toString();
    OrderLines orderLines;
    SubTotalPurchasePrice subTotalPurchasePrice;
    SubTotalInvoicePrice subTotalInvoicePrice;
    Supplier supplier;
}
