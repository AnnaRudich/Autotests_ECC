package com.scalepoint.automation.utils.data.entity.serviceTaskEntity;


import com.scalepoint.automation.utils.Constants;
import com.scalepoint.automation.utils.DateUtils;
import com.scalepoint.automation.utils.RandomUtils;

import java.time.LocalDate;

public class InvoiceBuilder {
    private Invoice invoice;

    public InvoiceBuilder(){
        this.invoice = new Invoice();
    }

    public InvoiceBuilder setDefault() {
        //invoice.setInvoiceLines();
        invoice.setInvoiceType("INVOICE");
        invoice.setInvoiceNumber(Integer.toString(RandomUtils.randomInt()));
        invoice.setInvoiceDate(DateUtils.localDateToString(LocalDate.now(), "yyyy-MM-dd"));
        invoice.setPaymentDueDate(DateUtils.localDateToString(LocalDate.now(), "yyyy-MM-dd"));
        invoice.setVat(Double.toString(Constants.VAT_AMOUNT_200));
        invoice.setTotalAmount(Double.toString(Constants.PRICE_500));
        invoice.setNetAmount(String.valueOf(Double.valueOf(invoice.getTotalAmount())-Double.valueOf(invoice.getVat())));
        return this;
    }

    public InvoiceBuilder withInvoiceType(String invoiceType){
        this.invoice.setInvoiceType(invoiceType);
        return this;
    }

    public InvoiceBuilder withCreditNoteNumber(String creditNoteNumber){
        this.invoice.setCreditNoteNumber(creditNoteNumber);
        return this;
    }

    public InvoiceBuilder withNameOfValuationResponsible(String nameOfValuationResponsible){
        this.invoice.setNameOfValuationResponsible(nameOfValuationResponsible);
        return this;
    }

    public InvoiceBuilder withDateOfValuation(String dateOfValuation){
        this.invoice.setDateOfValuation(dateOfValuation);
        return this;
    }

    public Invoice build(){
        return invoice;
    }

//    public void aaa(){
//        Invoice test = new InvoiceBuilder().withInvoiceType("aaa")..withLibuild();
//    }
}
