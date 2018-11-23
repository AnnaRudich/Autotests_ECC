package com.scalepoint.automation.utils.data.entity.serviceTaskEntity;


public class InvoiceLines
{
    private InvoiceLine[] invoiceLines;

    public InvoiceLine[] getInvoiceLines()
    {
        return invoiceLines;
    }

    public void setInvoiceLines(InvoiceLine[] invoiceLines)
    {
        this.invoiceLines = invoiceLines;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [invoiceLines = "+ invoiceLines +"]";
    }
}
