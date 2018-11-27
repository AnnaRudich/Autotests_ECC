package com.scalepoint.automation.utils.data.entity.serviceTaskEntity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "invoiceLines")
@XmlAccessorType(XmlAccessType.FIELD)
public class InvoiceLines {
    @XmlElement(name = "invoiceLines")
    private List<InvoiceLine> invoiceLines=null;

    public List<InvoiceLine> getInvoiceLines() {
        return invoiceLines;
    }

    public void setInvoiceLines(List<InvoiceLine> invoiceLines) {
        this.invoiceLines = invoiceLines;
    }

    @Override
    public String toString() {
        return "ClassPojo [invoiceLines = " + invoiceLines + "]";
    }
}
