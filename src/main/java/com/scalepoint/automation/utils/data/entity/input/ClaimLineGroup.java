package com.scalepoint.automation.utils.data.entity.input;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class ClaimLineGroup {

    @XmlElement
    private String excelWithGroupsFilePath;
    @XmlElement
    private String[] excelLineGroups;

}
