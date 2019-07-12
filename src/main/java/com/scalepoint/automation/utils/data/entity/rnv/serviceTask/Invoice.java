package com.scalepoint.automation.utils.data.entity.rnv.serviceTask;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;

@XmlRootElement(name = "invoice")
public class Invoice {
    private String invoiceType;
    private String invoiceNumber;
    private String invoiceDate;
    private String paymentDueDate;
    private String creditNoteNumber;
    private String nameOfValuationResponsible;
    private String dateOfValuation;

    private BigDecimal netAmount;
    private BigDecimal vat;
    private BigDecimal totalAmount;

    public Invoice() {
    }


    @XmlAttribute
    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

    @XmlAttribute
    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    @XmlAttribute
    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    @XmlAttribute
    public String getPaymentDueDate() {
        return paymentDueDate;
    }

    public void setPaymentDueDate(String paymentDueDate) {
        this.paymentDueDate = paymentDueDate;
    }

    @XmlAttribute
    public String getCreditNoteNumber() {
        return creditNoteNumber;
    }

    public void setCreditNoteNumber(String creditNoteNumber) {
        this.creditNoteNumber = creditNoteNumber;
    }

    @XmlAttribute
    public String getNameOfValuationResponsible() {
        return nameOfValuationResponsible;
    }

    public void setNameOfValuationResponsible(String nameOfValuationResponsible) {
        this.nameOfValuationResponsible = nameOfValuationResponsible;
    }

    @XmlAttribute
    public String getDateOfValuation() {
        return dateOfValuation;
    }

    public void setDateOfValuation(String dateOfValuation) {
        this.dateOfValuation = dateOfValuation;
    }

    @XmlAttribute
    public BigDecimal getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(BigDecimal netAmount) {
        this.netAmount = netAmount;
    }

    @XmlAttribute
    public BigDecimal getVat() {
        return vat;
    }

    public void setVat(BigDecimal vat) {
        this.vat = vat;
    }

    @XmlAttribute
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
}
