package com.scalepoint.automation.utils.data.entity;

import com.scalepoint.automation.utils.RandomUtils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * NewSystemUser: kke
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Category implements Cloneable {
    private String groupName = RandomUtils.randomName("Group");
    private String categoryName = RandomUtils.randomName("Category");
    private String modelName = RandomUtils.randomName("Model");
    @XmlElement
    private String spModel;
    @XmlElement
    private String allCategories;
    @XmlElement
    private String allCategoriesDk;

    public String getAllCategories() {
        return allCategories;
    }

    public void setAllCategories(String allCategories) {
        this.allCategories = allCategories;
    }

    public String getAllCategoriesDk() {
        return allCategoriesDk;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getModelName() {
        return modelName;
    }

    public String getSpModel() {
        return spModel;
    }

    public void setSpModel(String spModel) {
        this.spModel = spModel;
    }

    public Category clone() throws CloneNotSupportedException {
        return (Category) super.clone();
    }
}
