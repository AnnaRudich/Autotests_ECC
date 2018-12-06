package com.scalepoint.automation.utils.data.entity.serviceTaskEntity.copy;

public class InvoiceLineImport extends InvoiceLine{

    public InvoiceLineImport(){
        InvoiceLineImport invoiceLineImport = new InvoiceLineImportBuilder().build();
        this.description = invoiceLineImport.getDescription();
        this.units = invoiceLineImport.getUnits();
        this.unitNetAmount = invoiceLineImport.getUnitNetAmount();
        this.unitVatAmount = invoiceLineImport.getUnitVatAmount();
        this.unitPrice = invoiceLineImport.getUnitVatAmount();
        this.quantity = invoiceLineImport.getQuantity();
        this.lineTotal = invoiceLineImport.getLineTotal();
    }

//    public InvoiceLineImport(String description, String units, BigDecimal unitNetAmount, BigDecimal unitVatAmount, BigDecimal unitPrice, BigDecimal quantity, BigDecimal lineTotal) {
//        this.description = description;
//        this.units = units;
//        this.unitNetAmount = unitNetAmount;
//        this.unitVatAmount = unitVatAmount;
//        this.unitPrice = unitPrice;
//        this.quantity = quantity;
//        this.lineTotal = lineTotal;
//    }
}
