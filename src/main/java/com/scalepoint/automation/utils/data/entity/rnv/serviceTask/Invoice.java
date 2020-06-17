package com.scalepoint.automation.utils.data.entity.rnv.serviceTask;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "invoice")
public class Invoice {

    @XmlAttribute
    private String invoiceType;
    @XmlAttribute
    private String invoiceNumber;
    @XmlAttribute
    private String invoiceDate;
    @XmlAttribute
    private String paymentDueDate;
    @XmlAttribute
    private String creditNoteNumber;
    @XmlAttribute
    private String nameOfValuationResponsible;
    @XmlAttribute
    private String dateOfValuation;
    @XmlAttribute
    private BigDecimal netAmount;
    @XmlAttribute
    private BigDecimal vat;
    @XmlAttribute
    private BigDecimal totalAmount;

}
