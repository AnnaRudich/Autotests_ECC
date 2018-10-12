package com.scalepoint.automation.utils.data.entity.ServiceTaskEntity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Invoice
{
    @XmlElement
    private String invoiceDate;

    @XmlElement
    private String paymentDueDate;

    @XmlElement
    private String invoiceType;

    @XmlElement
    private String vat;

    @XmlElement
    private String totalAmount;

    @XmlElement
    private String invoiceNumber;

    private InvoiceLines invoiceLines;

    @XmlElement
    private String netAmount;

    public String getInvoiceDate ()
    {
        return invoiceDate;
    }

    public void setInvoiceDate (String invoiceDate)
    {
        this.invoiceDate = invoiceDate;
    }

    public String getPaymentDueDate ()
    {
        return paymentDueDate;
    }

    public void setPaymentDueDate (String paymentDueDate)
    {
        this.paymentDueDate = paymentDueDate;
    }

    public String getInvoiceType ()
    {
        return invoiceType;
    }

    public void setInvoiceType (String invoiceType)
    {
        this.invoiceType = invoiceType;
    }

    public String getVat ()
    {
        return vat;
    }

    public void setVat (String vat)
    {
        this.vat = vat;
    }

    public String getTotalAmount ()
    {
        return totalAmount;
    }

    public void setTotalAmount (String totalAmount)
    {
        this.totalAmount = totalAmount;
    }

    public String getInvoiceNumber ()
    {
        return invoiceNumber;
    }

    public void setInvoiceNumber (String invoiceNumber)
    {
        this.invoiceNumber = invoiceNumber;
    }

    public InvoiceLines getInvoiceLines ()
    {
        return invoiceLines;
    }

    public void setInvoiceLines (InvoiceLines invoiceLines)
    {
        this.invoiceLines = invoiceLines;
    }

    public String getNetAmount ()
    {
        return netAmount;
    }

    public void setNetAmount (String netAmount)
    {
        this.netAmount = netAmount;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [invoiceDate = "+invoiceDate+", paymentDueDate = "+paymentDueDate+", invoiceType = "+invoiceType+", vat = "+vat+", totalAmount = "+totalAmount+", invoiceNumber = "+invoiceNumber+", invoiceLines = "+invoiceLines+", netAmount = "+netAmount+"]";
    }
}


