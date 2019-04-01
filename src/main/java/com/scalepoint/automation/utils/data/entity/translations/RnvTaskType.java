package com.scalepoint.automation.utils.data.entity.translations;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "rnvtasktype")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class RnvTaskType {

    @XmlElement
    private String repair;

}
