package com.scalepoint.automation.utils.data.entity.rnv.serviceTask;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "valuations")
public class Valuations {

    @XmlAttribute
    private BigDecimal purchasePrice;
    @XmlAttribute
    private BigDecimal newPrice;
    @XmlAttribute
    private BigDecimal customerDemand;
    @XmlAttribute
    private BigDecimal discretionaryValuation;
    @XmlAttribute
    private BigDecimal usedPrice;
    @XmlAttribute
    private BigDecimal repairEstimate;
    @XmlAttribute
    private BigDecimal repairPrice;

}
