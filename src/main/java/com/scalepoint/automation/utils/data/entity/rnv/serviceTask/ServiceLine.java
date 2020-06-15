package com.scalepoint.automation.utils.data.entity.rnv.serviceTask;

import lombok.Data;

import javax.xml.bind.annotation.*;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "serviceLine")
public class ServiceLine {

    @XmlAttribute(name = "claimLineId")
    private Integer lineIndex;
    @XmlAttribute(name = "uniqueId")
    private String lineGUID;
    @XmlElement
    private Category category;
    @XmlAttribute(name = "taskType")
    private String taskType;

}
