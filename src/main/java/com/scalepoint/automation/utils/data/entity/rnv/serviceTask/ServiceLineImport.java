package com.scalepoint.automation.utils.data.entity.rnv.serviceTask;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;


@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class ServiceLineImport extends ServiceLine {

    @XmlElement
    private ItemImport item;
    @XmlElement
    private Valuations valuations;

}
