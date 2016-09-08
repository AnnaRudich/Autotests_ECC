package com.scalepoint.automation.utils.data.entity;

import com.scalepoint.automation.utils.RandomUtils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class GenericItem {

    private String itemDescription = RandomUtils.randomName("GenericItem");
    @XmlElement
    private String group1;
    @XmlElement
    private String category1;
    @XmlElement
    private String companySP;


    public String getItemDescription() {
        return itemDescription;
    }

    public String getCompanySP() {
        return companySP;
    }

    public String getCategory1() {
        return category1;
    }

    public String getGroup1() {
        return group1;
    }
}
