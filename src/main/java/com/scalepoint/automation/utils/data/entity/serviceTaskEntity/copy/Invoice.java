package com.scalepoint.automation.utils.data.entity.serviceTaskEntity.copy;

import javax.xml.bind.annotation.XmlAttribute;
import java.math.BigDecimal;

public class Invoice {
    String invoiceType;
    String invoiceNumber;
    String invoiceDate;
    String paymentDueDate;
    String creditNoteNumber;
    String nameOfValuationResponsible;
    String dateOfValuation;

    BigDecimal netAmount;
    BigDecimal vat;
    BigDecimal totalAmount;

    public Invoice(){}



    @XmlAttribute(required = true)
    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

    @XmlAttribute(required = true)
    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    @XmlAttribute(required = true)
    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    @XmlAttribute(required = true)
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

    @XmlAttribute(required = true)
    public BigDecimal getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(BigDecimal netAmount) {
        this.netAmount = netAmount;
    }

    @XmlAttribute(required = true)
    public BigDecimal getVat() {
        return vat;
    }

    public void setVat(BigDecimal vat) {
        this.vat = vat;
    }

    @XmlAttribute(required = true)
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
}
