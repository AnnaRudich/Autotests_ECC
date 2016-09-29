package com.scalepoint.automation.utils.data.entity;

import com.scalepoint.automation.utils.RandomUtils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class GenericItem {

    private String name = RandomUtils.randomName("GenericItem");
    @XmlElement
    private String group;
    @XmlElement
    private String category;

    private String price = "100";

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getGroup() {
        return group;
    }

    public String getPrice() {
        return price;
    }

    public void setName(String name) {
        this.name = name;
    }
}
