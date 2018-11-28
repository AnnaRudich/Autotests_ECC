package com.scalepoint.automation.utils.data.entity.serviceTaskEntity;

import java.util.List;

public class InvoiceLinesBuilder {
    private List<InvoiceLine> invoiceLinesList;

    public InvoiceLinesBuilder setInvoiceLinesList(List<InvoiceLine> invoiceLinesList) {
        this.invoiceLinesList = invoiceLinesList;
        return this;
    }

    public InvoiceLines createInvoiceLines() {
        return new InvoiceLines(invoiceLinesList);
    }
}