package com.scalepoint.automation.utils.data.entity.serviceTaskEntity;


public class InvoiceLines
{
    private InvoiceLine[] invoiceLine;

    public InvoiceLine[] getInvoiceLine ()
    {
        return invoiceLine;
    }

    public void setInvoiceLine (InvoiceLine[] invoiceLine)
    {
        this.invoiceLine = invoiceLine;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [invoiceLine = "+invoiceLine+"]";
    }
}
