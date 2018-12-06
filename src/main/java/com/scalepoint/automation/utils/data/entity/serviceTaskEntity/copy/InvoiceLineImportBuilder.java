package com.scalepoint.automation.utils.data.entity.serviceTaskEntity.copy;


import com.scalepoint.automation.utils.Constants;
import com.scalepoint.automation.utils.RandomUtils;

import java.math.BigDecimal;

public class InvoiceLineImportBuilder {

    private InvoiceLineImport invoiceLine;

    public InvoiceLineImportBuilder() {
        invoiceLine.setDescription("InvoiceLine" + RandomUtils.randomInt());
        invoiceLine.setQuantity(BigDecimal.valueOf(1));
        invoiceLine.setUnitVatAmount(BigDecimal.valueOf(Constants.PRICE_10));
        invoiceLine.setUnitPrice(BigDecimal.valueOf(Constants.PRICE_50));
        invoiceLine.setUnitNetAmount(BigDecimal.valueOf(40));
        invoiceLine.setLineTotal(BigDecimal.valueOf(40*1));

    }

    public InvoiceLineImportBuilder withUnits(String units){
        this.invoiceLine.setUnits(units);
        return this;
    }

    public InvoiceLineImport build(){
        return invoiceLine;
    }
}
