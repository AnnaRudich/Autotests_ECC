package com.scalepoint.automation.utils.data.entity.serviceTaskEntity.copy;

import javax.xml.bind.annotation.XmlAttribute;

public class Item {

    private String originalDescription;
    private String description;
    private Integer age;
    private Integer quantity;

    @XmlAttribute(name = "customerDescription")
    public String getOriginalDescription() {
        return originalDescription;
    }

    public void setOriginalDescription(String originalDescription) {
        this.originalDescription = originalDescription;
    }

    @XmlAttribute(name = "productMatchDescription", required = true)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @XmlAttribute
    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @XmlAttribute(required = true)
    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
