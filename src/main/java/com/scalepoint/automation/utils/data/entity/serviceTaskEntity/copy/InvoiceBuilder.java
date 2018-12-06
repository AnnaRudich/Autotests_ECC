package com.scalepoint.automation.utils.data.entity.serviceTaskEntity.copy;


import com.scalepoint.automation.utils.Constants;
import com.scalepoint.automation.utils.DateUtils;
import com.scalepoint.automation.utils.RandomUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class InvoiceBuilder {
    private InvoiceImport invoice;

    public InvoiceBuilder(){
        this.invoice = new InvoiceImport();
    }

    public InvoiceBuilder setDefault() {

        List<InvoiceLineImport> invoiceLineImports = new ArrayList<>();
        invoiceLineImports.add(new InvoiceLineImportBuilder().build());

        invoice.setInvoiceLines(invoiceLineImports);
        invoice.setInvoiceType("INVOICE");
        invoice.setInvoiceNumber(Integer.toString(RandomUtils.randomInt()));
        invoice.setInvoiceDate(DateUtils.localDateToString(LocalDate.now(), "yyyy-MM-dd"));
        invoice.setPaymentDueDate(DateUtils.localDateToString(LocalDate.now(), "yyyy-MM-dd"));
        invoice.setVat(BigDecimal.valueOf(Constants.VAT_AMOUNT_200));
        invoice.setTotalAmount(BigDecimal.valueOf(Constants.PRICE_500));
        invoice.setNetAmount(BigDecimal.valueOf(Constants.PRICE_500 - Constants.VAT_AMOUNT_200));
        return this;
    }

    public InvoiceBuilder withInvoiceType(String invoiceType){
        this.invoice.setInvoiceType(invoiceType);
        return this;
    }


    public InvoiceImport build(){
        return invoice;
    }

//    public void aaa(){
//        Invoice test = new InvoiceBuilder().withInvoiceType("aaa")..withLibuild();
//    }
}
