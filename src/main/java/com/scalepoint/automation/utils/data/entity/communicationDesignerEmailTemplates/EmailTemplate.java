package com.scalepoint.automation.utils.data.entity.communicationDesignerEmailTemplates;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public  class EmailTemplate{

    @XmlElement
    private int templateId;
    @XmlElement
    private String templateName;
    @XmlElement
    private String title;
}

