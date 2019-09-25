package com.scalepoint.automation.utils.data.entity.rnv.serviceTask.dataBuilders;


import com.scalepoint.automation.utils.Constants;
import com.scalepoint.automation.utils.RandomUtils;
import com.scalepoint.automation.utils.data.entity.rnv.serviceTask.InvoiceLine;

import java.math.BigDecimal;

public class InvoiceLineBuilder {

    private InvoiceLine invoiceLine;

    public InvoiceLineBuilder() {
        this.invoiceLine = new InvoiceLine();
        this.invoiceLine.setDescription("InvoiceLine" + RandomUtils.randomInt());
        this.invoiceLine.setQuantity(BigDecimal.valueOf(1));
        this.invoiceLine.setUnits("1");
        this.invoiceLine.setUnitVatAmount(BigDecimal.valueOf(Constants.VAT_20));
        this.invoiceLine.setUnitPrice(BigDecimal.valueOf(Constants.PRICE_30));
        this.invoiceLine.setUnitNetAmount(BigDecimal.valueOf(Constants.PRICE_30));
        this.invoiceLine.setLineTotal(BigDecimal.valueOf(Constants.PRICE_30 + Constants.VAT_20));
    }

    public InvoiceLineBuilder withUnits(String units) {
        this.invoiceLine.setUnits(units);
        return this;
    }

    public InvoiceLine build() {
        return this.invoiceLine;
    }
}
