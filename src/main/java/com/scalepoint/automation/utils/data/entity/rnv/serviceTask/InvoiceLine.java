package com.scalepoint.automation.utils.data.entity.rnv.serviceTask;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import java.math.BigDecimal;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class InvoiceLine {

    @XmlAttribute
    private String description;
    @XmlAttribute
    private String units;
    @XmlAttribute
    private BigDecimal unitNetAmount;
    @XmlAttribute
    private BigDecimal unitVatAmount;
    @XmlAttribute
    private BigDecimal unitPrice;
    @XmlAttribute
    private BigDecimal quantity;
    @XmlAttribute
    private BigDecimal lineTotal;

}
