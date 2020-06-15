package com.scalepoint.automation.utils.data.entity.order;

import lombok.Builder;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@Builder
@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class Payments {

    @XmlElement(name = "Deposits")
    Deposits deposits;

}
