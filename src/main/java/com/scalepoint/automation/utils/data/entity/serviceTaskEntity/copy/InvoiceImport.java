package com.scalepoint.automation.utils.data.entity.serviceTaskEntity.copy;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.List;

public class InvoiceImport extends Invoice{

    List<InvoiceLineImport> invoiceLines;
    @XmlElementWrapper(name="invoiceLines")
    @XmlElement(name="invoiceLine")
    public List<InvoiceLineImport> getInvoiceLines() {
        return invoiceLines;
    }

    public void setInvoiceLines(List<InvoiceLineImport> invoiceLines) {
        this.invoiceLines = invoiceLines;
    }
}
