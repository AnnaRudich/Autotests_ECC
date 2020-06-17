package com.scalepoint.automation.utils.data.entity.rnv.serviceTask;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.List;


@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class InvoiceImport extends Invoice {

    @XmlElementWrapper(name = "invoiceLines")
    @XmlElement(name = "invoiceLine")
    private List<InvoiceLine> invoiceLines;

}
