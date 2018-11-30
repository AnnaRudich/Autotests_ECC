package com.scalepoint.automation.utils.data.entity.serviceTaskEntity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "invoiceLinesList")
@XmlAccessorType(XmlAccessType.FIELD)
public class InvoiceLines {

    @XmlElement(name = "invoiceLinesList")
    private List<InvoiceLine> invoiceLinesList;

    public List<InvoiceLine> getInvoiceLinesList() {
        return invoiceLinesList;
    }

    public void setInvoiceLinesList(List<InvoiceLine> invoiceLinesList) {
        this.invoiceLinesList = invoiceLinesList;
    }

    @Override
    public String toString() {
        return "ClassPojo [invoiceLinesList = " + invoiceLinesList + "]";
    }
}
