package com.scalepoint.automation.utils.data.entity.rnv.serviceTask;


import com.scalepoint.automation.services.restService.RnvInvoiceType;
import com.scalepoint.automation.utils.Constants;
import com.scalepoint.automation.utils.DateUtils;
import com.scalepoint.automation.utils.RandomUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

class InvoiceBuilder {
    private InvoiceImport invoice;

    InvoiceBuilder() {
        this.invoice = new InvoiceImport();
    }

    InvoiceBuilder setDefault() {
        List<InvoiceLine> invoiceLineImports = new ArrayList<>();
        invoiceLineImports.add(new InvoiceLineBuilder().setDefault().build());

        invoice = new InvoiceImport();

        invoice.setInvoiceLines(invoiceLineImports);
        invoice.setInvoiceType(String.valueOf(RnvInvoiceType.INVOICE));
        invoice.setInvoiceNumber(Integer.toString(RandomUtils.randomInt()));
        invoice.setInvoiceDate(DateUtils.localDateToString(LocalDate.now(), "yyyy-MM-dd"));
        invoice.setPaymentDueDate(DateUtils.localDateToString(LocalDate.now(), "yyyy-MM-dd"));
        invoice.setVat(BigDecimal.valueOf(Constants.VAT_AMOUNT_200));
        invoice.setTotalAmount(BigDecimal.valueOf(Constants.PRICE_500));
        invoice.setNetAmount(BigDecimal.valueOf(Constants.PRICE_500 - Constants.VAT_AMOUNT_200));
        return this;
    }

    public InvoiceBuilder withInvoiceType(String invoiceType) {
        this.invoice.setInvoiceType(invoiceType);
        return this;
    }

    public InvoiceImport build() {
        return invoice;
    }
}
