package com.scalepoint.automation.utils.data.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Data
@Getter
@Setter
public class ProductToOrderInShop {
    @XmlElement
    private String vat;

    @XmlElement
    private Double orderLineTotalPriceAmount;

    @XmlElement
    private int quantity;
}
