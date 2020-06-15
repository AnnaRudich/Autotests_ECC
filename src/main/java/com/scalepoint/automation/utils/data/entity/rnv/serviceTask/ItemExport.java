package com.scalepoint.automation.utils.data.entity.rnv.serviceTask;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import java.math.BigDecimal;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class ItemExport extends Item {

    @XmlAttribute
    private String manufacturer;
    @XmlAttribute
    private String model;
    @XmlAttribute
    private String serialNumber;
    @XmlAttribute
    private String damageDescription;
    @XmlAttribute
    private BigDecimal repairLimit;
    @XmlAttribute
    private Integer depreciation;
    @XmlAttribute
    private String customerNotesToClaimLine;
    @XmlAttribute(name = "EAN")
    private String EAN;
    @XmlAttribute
    private String damageType;

}
