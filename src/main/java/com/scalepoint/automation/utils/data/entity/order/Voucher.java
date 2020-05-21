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

   // CustomerEmail="{{email}}" CustomerPhone="44222222" CustomerPersonalCode=""
    // voucherID="DK2405254" purchaseDiscount="0.1000" voucherType="EVOUCHER"/>

    @XmlAttribute(name="CustomerEmail")
    String customerEmail;

    @XmlAttribute(name="CustomerPhone")
    String customerPhone;

    @XmlAttribute(name ="CustomerPersonalCode")
    String customerPersonalCode;

    @XmlAttribute
    String voucherID;

    @XmlAttribute
    String purchaseDiscount;

    @XmlAttribute
    String voucherType;
}
