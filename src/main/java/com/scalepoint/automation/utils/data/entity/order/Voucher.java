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
public class Voucher {

    @XmlAttribute(name="CustomerEmail")
    String customerEmail;

    @XmlAttribute(name="CustomerPhone")
    String customerPhone;

    @XmlAttribute(name ="CustomerPersonalCode")
    String customerPersonalCode;

    @XmlAttribute
    String voucherID;

    @XmlAttribute
    Double purchaseDiscount;

    @XmlAttribute
    String voucherType;
}
