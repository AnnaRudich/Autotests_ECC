package com.scalepoint.automation.utils.data.entity.rnv.serviceTask;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "item")
public class Item {

    @XmlAttribute(name = "customerDescription")
    private String originalDescription;
    @XmlAttribute(name = "productMatchDescription", required = true)
    private String description;
    @XmlAttribute
    private Integer age;
    @XmlAttribute(required = true)
    private Integer quantity;

}
