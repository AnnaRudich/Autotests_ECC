package com.scalepoint.automation.utils.data.entity;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by bza on 6/22/2017.
 */

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class Assignment {

    @XmlElement
    private String company;
    @XmlElement
    private String pseudoCategory;
    @XmlElement
    private String policy;

}
