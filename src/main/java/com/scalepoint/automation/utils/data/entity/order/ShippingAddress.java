package com.scalepoint.automation.utils.data.entity.order;

import com.scalepoint.automation.shared.Locale;
import lombok.Builder;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@Builder
@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class ShippingAddress {

    @XmlAttribute
    String firstName;
    @XmlAttribute
    String lastName;
    @XmlAttribute
    @Builder.Default
    String addressLine1 = "";
    @XmlAttribute
    @Builder.Default
    String addressLine2 = "";
    @XmlAttribute
    @Builder.Default
    String zipCode = "";
    @XmlAttribute
    @Builder.Default
    String city = "Copenhagen";
    @XmlAttribute
    @Builder.Default
    String state = "";
    @XmlAttribute
    @Builder.Default
    String country = Locale.DK.getValue();
    @XmlAttribute
    @Builder.Default
    String mobilePhone = "";
    @XmlAttribute
    @Builder.Default
    String phone = "";

}
