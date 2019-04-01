package com.scalepoint.automation.utils.data.entity;

import com.scalepoint.automation.utils.RandomUtils;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class Shop {
    private String shopName = RandomUtils.randomName("Shop");
    private String shopAddress1 = RandomUtils.randomName("ShopAddr1");
    private String shopAddress2 = RandomUtils.randomName("ShopAddr2");
    private String shopCity = RandomUtils.randomName("ShopCity");

    @XmlElement
    private String evoucherEmail;
    @XmlElement
    private String postalCode;
    @XmlElement
    private String phone;
}
