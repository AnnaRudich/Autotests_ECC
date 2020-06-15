package com.scalepoint.automation.utils.data.entity;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "simpleSupplier", propOrder = {
        "name"
})
public class SimpleSupplier {

    @XmlAttribute(name = "name")
    private String name;
    @XmlAttribute(name = "agreement")
    private String agreement;
    @XmlAttribute(name = "inactiveAgreement")
    private String inactiveAgreement;
    @XmlAttribute(name = "scalepointAgreement")
    private String scalepointAgreement;
    @XmlAttribute(name = "insuranceCompany")
    private String insuranceCompany;
    @XmlAttribute(name = "shopName")
    private String shopName;
    @XmlAttribute(name = "withVouchers")
    private boolean withVouchers;

}
