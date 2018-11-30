package com.scalepoint.automation.utils.data.entity.serviceTaskEntity;

import java.util.List;

public class InvoiceLinesBuilder {
    private List<InvoiceLine> invoiceLinesList;

    public InvoiceLinesBuilder setInvoiceLinesList() {
        this.invoiceLinesList.add(new InvoiceLineBuilder().build());
        return this;
    }

    public InvoiceLines createInvoiceLines() {
        return new InvoiceLines();
    }
}