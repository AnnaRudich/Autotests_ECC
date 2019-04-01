package com.scalepoint.automation.utils.data.entity.translations;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class TextSearch {
    @XmlElement
    private String modelGalaxyS7;
    @XmlElement
    private String groupChildren;
    @XmlElement
    private String subgroupChildrenEquipment;
    @XmlElement
    private String subgroupCameraLenses;
    @XmlElement
    private String subgroupVideocamera;
    @XmlElement
    private String brokenQuery1;
    @XmlElement
    private String brokenQueryWithSpecialSymbols1;
    @XmlElement
    private String brandSamsung;
}
