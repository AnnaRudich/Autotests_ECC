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
        this.invoiceLine.setUnitVatAmount(BigDecimal.valueOf(Constants.PRICE_10));
        this.invoiceLine.setUnitPrice(BigDecimal.valueOf(Constants.PRICE_50));
        this.invoiceLine.setUnitNetAmount(BigDecimal.valueOf(40));
        this.invoiceLine.setLineTotal(BigDecimal.valueOf(40));
    }

    public InvoiceLineBuilder withUnits(String units) {
        this.invoiceLine.setUnits(units);
        return this;
    }

    public InvoiceLine build() {
        return this.invoiceLine;
    }
}
