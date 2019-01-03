package com.scalepoint.automation.utils.data.entity.rnv.serviceTask;


import com.scalepoint.automation.utils.Constants;
import com.scalepoint.automation.utils.RandomUtils;

import java.math.BigDecimal;

class InvoiceLineBuilder {

    private InvoiceLine invoiceLine;

    InvoiceLineBuilder() {
        invoiceLine = new InvoiceLine();
        invoiceLine.setDescription("InvoiceLine" + RandomUtils.randomInt());
        invoiceLine.setQuantity(BigDecimal.valueOf(1));
        invoiceLine.setUnitVatAmount(BigDecimal.valueOf(Constants.PRICE_10));
        invoiceLine.setUnitPrice(BigDecimal.valueOf(Constants.PRICE_50));
        invoiceLine.setUnitNetAmount(BigDecimal.valueOf(40));
        invoiceLine.setLineTotal(BigDecimal.valueOf(40));
    }

    public InvoiceLineBuilder withUnits(String units){
        this.invoiceLine.setUnits(units);
        return this;
    }

    public InvoiceLine build(){
        return invoiceLine;
    }
}
