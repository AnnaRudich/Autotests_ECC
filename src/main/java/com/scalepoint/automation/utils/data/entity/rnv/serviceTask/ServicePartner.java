package com.scalepoint.automation.utils.data.entity.rnv.serviceTask;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "servicePartner")
public class ServicePartner {

    @XmlAttribute
    private String name;
    @XmlAttribute
    private String address1;
    @XmlAttribute
    private String address2;
    @XmlAttribute
    private String phone;
    @XmlAttribute
    private String email;
    @XmlAttribute
    private String postalCode;
    @XmlAttribute
    private String cvrNumber;
    @XmlAttribute
    private String city;

}
