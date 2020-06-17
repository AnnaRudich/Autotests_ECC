package com.scalepoint.automation.utils.data.entity.rnv.serviceTask;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "serviceTask")
public class ServiceTask {

    @XmlAttribute(name = "uniqueId", required = true)
    private String GUID;
    @XmlAttribute(required = true)
    private String createdDate;

}
