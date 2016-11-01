package com.scalepoint.automation.utils.data.entity;

import com.scalepoint.automation.utils.RandomUtils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Shop {
    private String shopName = RandomUtils.randomName("Shop");
    private String shopAddress1 = RandomUtils.randomName("ShopAddr1");
    private String shopAddress2 = RandomUtils.randomName("ShopAddr2");

    @XmlElement
    private String eVoucherEmail;
    @XmlElement
    private String postalCode;
    @XmlElement
    private String postalCode2;
    private String shopCity = RandomUtils.randomName("ShopCity");
    @XmlElement
    private String phone;

    public String getShopName() {
        return shopName;
    }

    public String getShopAddress1() {
        return shopAddress1;
    }

    public String getShopAddress2() {
        return shopAddress2;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getPostalCode2() {
        return postalCode2;
    }

    public String getShopCity() {
        return shopCity;
    }

    public String getPhone() {
        return phone;
    }

    public String geteVoucherEmail() {
        return eVoucherEmail;
    }
}
