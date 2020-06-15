package com.scalepoint.automation.utils.data.entity.rnv.serviceTask;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "category")
public class Category {

    @XmlAttribute(required = true)
    private String name;
    @XmlAttribute(name = "uniqueId", required = true)
    private String token;
    @XmlAttribute(required = true)
    private String parentCategory;

}
