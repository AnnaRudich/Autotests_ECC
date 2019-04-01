package com.scalepoint.automation.utils.data.entity;

import com.scalepoint.automation.utils.RandomUtils;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * NewSystemUser: kke
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class Category implements Cloneable {
    private String groupName = RandomUtils.randomName("Group");
    private String categoryName = RandomUtils.randomName("Category");
    private String modelName = RandomUtils.randomName("Model");

    public Category clone() throws CloneNotSupportedException {
        return (Category) super.clone();
    }
}
