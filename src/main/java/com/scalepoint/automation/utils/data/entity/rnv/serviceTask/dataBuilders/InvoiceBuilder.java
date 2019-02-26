package com.scalepoint.automation.utils.data.entity.rnv.serviceTask.dataBuilders;


import com.scalepoint.automation.services.restService.RnvInvoiceType;
import com.scalepoint.automation.utils.Constants;
import com.scalepoint.automation.utils.DateUtils;
import com.scalepoint.automation.utils.RandomUtils;
import com.scalepoint.automation.utils.data.entity.rnv.serviceTask.InvoiceImport;
import com.scalepoint.automation.utils.data.entity.rnv.serviceTask.InvoiceLine;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class InvoiceBuilder {
    private InvoiceImport invoice;

    public InvoiceBuilder() {
        List<InvoiceLine> invoiceLineImports = new ArrayList<>();
        invoiceLineImports.add(new InvoiceLineBuilder().build());

        this.invoice = new InvoiceImport();

        this.invoice.setInvoiceLines(invoiceLineImports);
        this.invoice.setInvoiceType(String.valueOf(RnvInvoiceType.INVOICE));
        this.invoice.setInvoiceNumber(String.valueOf(RandomUtils.randomInt()));
        this.invoice.setInvoiceDate(DateUtils.format(LocalDate.now(), "yyyy-MM-dd"));
        this.invoice.setPaymentDueDate(DateUtils.format(LocalDate.now(), "yyyy-MM-dd"));
        this.invoice.setVat(BigDecimal.valueOf(Constants.VAT_AMOUNT_200));
        this.invoice.setTotalAmount(BigDecimal.valueOf(Constants.PRICE_500));
        this.invoice.setNetAmount(BigDecimal.valueOf(Constants.PRICE_500 - Constants.VAT_AMOUNT_200));
    }

    public InvoiceBuilder withInvoiceType(String invoiceType) {
        this.invoice.setInvoiceType(invoiceType);
        return this;
    }

    public InvoiceImport build() {
        return invoice;
    }
}
