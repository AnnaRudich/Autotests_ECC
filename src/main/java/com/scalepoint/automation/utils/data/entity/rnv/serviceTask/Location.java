package com.scalepoint.automation.utils.data.entity.rnv.serviceTask;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "location")
public class Location {

    @XmlAttribute
    private Integer id;
    @XmlAttribute
    protected String name;
    @XmlAttribute
    private String address1;
    @XmlAttribute
    private String address2;
    @XmlAttribute
    private String postalCode;
    @XmlAttribute
    private String city;
    @XmlAttribute
    private String phone;
    @XmlAttribute
    private String email;
    @XmlAttribute
    private String information;

}
