package com.scalepoint.automation.utils.data.entity.rnv.serviceTask;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "bank")
public class Bank {

    @XmlAttribute
    private String regNumber;
    @XmlAttribute
    private String accountNumber;
    @XmlAttribute
    private String IBAN;
    @XmlAttribute
    private String fikType;
    @XmlAttribute
    private String fikCreditorCode;

}
