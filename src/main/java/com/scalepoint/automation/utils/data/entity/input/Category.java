package com.scalepoint.automation.utils.data.entity.input;

import com.scalepoint.automation.utils.RandomUtils;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * NewSystemUser: kke
 */

@Data
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Category implements Cloneable {

    private String groupName = RandomUtils.randomName("Group");
    private String categoryName = RandomUtils.randomName("Category");
    private String modelName = RandomUtils.randomName("Model");

    public Category clone() throws CloneNotSupportedException {
        return (Category) super.clone();
    }

}
