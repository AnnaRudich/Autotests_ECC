package com.scalepoint.automation.utils.data.entity;

import com.scalepoint.automation.utils.data.entity.translations.*;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement(name = "translations")
@XmlAccessorType(XmlAccessType.FIELD)
public class Translations {

    @XmlElement(name = "acquiredType")
    private Acquired acquired;

    @XmlElement(name = "depreciationType")
    private DepreciationType depreciationType;

    @XmlElement(name = "discretionaryReason")
    private DiscretionaryReason discretionaryReason;

    @XmlElement(name = "rrLinesFields")
    private RRLinesFields rrLinesFields;

    @XmlElement(name = "orderDetails")
    private OrderDetails orderDetails;

    @XmlElement(name = "textSearch")
    private TextSearch textSearch;

    @XmlElement(name = "rnvtasktype")
    private RnvTaskType rnvTaskType;

    @XmlElement(name = "roles")
    private Roles roles;

}
