package com.scalepoint.automation.utils.data.entity.input;

import com.scalepoint.automation.utils.RandomUtils;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Supplier {

    private String supplierID = Integer.toString(RandomUtils.randomInt());
    private String supplierName = RandomUtils.randomName("AutotestSupplier-");
    private String supplierCVR = Integer.toString(RandomUtils.randomInt() + 10000000);
    private String address1 = RandomUtils.randomName("addr1");
    private String address2 = RandomUtils.randomName("addr2");
    private String city = RandomUtils.randomName("City");
    private String promotionText = RandomUtils.randomName("Promotion");
    private String commercialText = RandomUtils.randomName("Commercial");
    private String shopTitleText = RandomUtils.randomName("ShopTitle");
    @XmlElement
    private String supplierPhone;
    @XmlElement
    private String supplierEmail;
    @XmlElement
    private String postCode;
    @XmlElement
    private String bankRegNumber;
    @XmlElement
    private String bankAccNumber;
    @XmlElement
    private String bankFikType;
    @XmlElement
    private String bankFikCreditorCode;
    @XmlElement
    private String bankFikNumber;
    @XmlElement
    private String bankName;

}
