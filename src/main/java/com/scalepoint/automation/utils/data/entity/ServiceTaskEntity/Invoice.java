package com.scalepoint.automation.utils.data.entity.serviceTaskEntity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "invoice")
@XmlAccessorType(XmlAccessType.FIELD)
public class Invoice {

    @XmlElement
    private InvoiceLines invoiceLines;

    @XmlAttribute
    private String dateOfValuation;

    @XmlAttribute
    private String nameOfValuationResponsible;

    @XmlAttribute
    private String creditNoteNumber;

    @XmlAttribute
    private String invoiceDate;

    @XmlAttribute
    private String paymentDueDate;

    @XmlAttribute
    private String invoiceType;

    @XmlAttribute
    private String vat;

    @XmlAttribute
    private String totalAmount;

    @XmlAttribute
    private String invoiceNumber;

    @XmlAttribute
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

    public String getDateOfValuation() {
        return dateOfValuation;
    }

    public void setDateOfValuation(String dateOfValuation) {
        this.dateOfValuation = dateOfValuation;
    }

    public String getNameOfValuationResponsible() {
        return nameOfValuationResponsible;
    }

    public void setNameOfValuationResponsible(String nameOfValuationResponsible) {
        this.nameOfValuationResponsible = nameOfValuationResponsible;
    }

    public String getCreditNoteNumber() {
        return creditNoteNumber;
    }

    public void setCreditNoteNumber(String creditNoteNumber) {
        this.creditNoteNumber = creditNoteNumber;
    }


    @Override
    public String toString()
    {
        return "ClassPojo [invoiceDate = "+invoiceDate+", paymentDueDate = "+paymentDueDate+", invoiceType = "+invoiceType+", vat = "+vat+", totalAmount = "+totalAmount+", invoiceNumber = "+invoiceNumber+", invoiceLines = "+invoiceLines+", netAmount = "+netAmount+"]";
    }
}


