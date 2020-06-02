package com.scalepoint.automation.utils.data.entity.order;


import lombok.Builder;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class Deposits {

    @XmlAttribute
    Double depositsTotal;
    @XmlElement(name = "Deposit")
    List<Deposit> deposit = new ArrayList<>();
}
