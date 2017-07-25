package com.scalepoint.automation.utils.data.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by bza on 6/22/2017.
 */

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Assignment {

    @XmlElement
    private String company;
    @XmlElement
    private String pseudoCategory;
    @XmlElement
    private String policy;

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPseudoCategory() {
        return pseudoCategory;
    }

    public void setPseudoCategory(String pseudoCategory) {
        this.pseudoCategory = pseudoCategory;
    }

    public String getPolicy() {
        return policy;
    }

    public void setPolicy(String policy) {
        this.policy = policy;
    }
}
