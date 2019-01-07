package com.scalepoint.automation.utils.data.entity.rnv.serviceTask;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.List;

public class InvoiceImport extends Invoice{

    private List<InvoiceLine> invoiceLines;
    @XmlElementWrapper(name="invoiceLines")
    @XmlElement(name="invoiceLine")
    public List<InvoiceLine> getInvoiceLines() {
        return invoiceLines;
    }

    void setInvoiceLines(List<InvoiceLine> invoiceLines) {
        this.invoiceLines = invoiceLines;
    }
}
