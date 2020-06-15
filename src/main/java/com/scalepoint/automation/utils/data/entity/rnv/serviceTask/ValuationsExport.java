package com.scalepoint.automation.utils.data.entity.rnv.serviceTask;


import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import java.math.BigDecimal;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
class ValuationsExport extends Valuations {

    @XmlAttribute
    private BigDecimal productPrice;
    @XmlAttribute
    private BigDecimal retailPrice;

}
