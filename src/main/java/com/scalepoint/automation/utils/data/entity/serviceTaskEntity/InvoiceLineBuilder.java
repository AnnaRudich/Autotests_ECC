package com.scalepoint.automation.utils.data.entity.serviceTaskEntity;


import com.scalepoint.automation.utils.Constants;
import com.scalepoint.automation.utils.RandomUtils;

public class InvoiceLineBuilder {

    private InvoiceLine invoiceLine;

    public InvoiceLineBuilder() {
        this.invoiceLine = new InvoiceLine();
        invoiceLine.setDescription("InvoiceLine" + RandomUtils.randomInt());
        invoiceLine.setQuantity("1");
        invoiceLine.setUnitVatAmount(Double.toString(Constants.PRICE_10));
        invoiceLine.setUnitPrice(Double.toString(Constants.PRICE_50));
        invoiceLine.setUnitNetAmount(String.valueOf(Double.valueOf(invoiceLine.getUnitPrice())-Double.valueOf(invoiceLine.getUnitVatAmount())));
        invoiceLine.setLineTotal(String.valueOf(Double.valueOf(invoiceLine.getUnitNetAmount())*Double.valueOf(invoiceLine.getQuantity())));

    }

    public InvoiceLineBuilder withUnits(String units){
        this.invoiceLine.setUnits(units);
        return this;
    }
}
