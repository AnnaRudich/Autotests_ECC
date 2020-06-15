package com.scalepoint.automation.utils.data.entity.order;

import com.scalepoint.automation.utils.RandomUtils;
import lombok.Builder;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@Builder
@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class SubOrder {

    @XmlAttribute
    @Builder.Default
    String subOrderID = String.valueOf(RandomUtils.randomInt());
    @XmlElement(name ="OrderLines")
    OrderLines orderLines;
    @XmlElement(name = "SubTotalPurchasePrice")
    SubTotalPurchasePrice subTotalPurchasePrice;
    @XmlElement(name = "SubTotalInvoicePrice")
    SubTotalInvoicePrice subTotalInvoicePrice;
    @XmlElement(name = "Supplier")
    Supplier supplier;

}
