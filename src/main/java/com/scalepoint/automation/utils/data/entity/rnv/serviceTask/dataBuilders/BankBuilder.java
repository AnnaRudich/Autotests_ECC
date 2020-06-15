package com.scalepoint.automation.utils.data.entity.rnv.serviceTask.dataBuilders;

import com.scalepoint.automation.utils.data.TestData;
import com.scalepoint.automation.utils.data.entity.Supplier;
import com.scalepoint.automation.utils.data.entity.rnv.serviceTask.BankImport;

public class BankBuilder {

    private BankImport bankImport;

    public BankBuilder() {
        Supplier supplierData = TestData.getSupplier();
        this.bankImport = new BankImport();
        this.bankImport.setFikType(supplierData.getBankFikType());
        this.bankImport.setFikCreditorCode(supplierData.getBankFikCreditorCode());
        this.bankImport.setFikNumber(supplierData.getBankFikNumber());
        this.bankImport.setAccountNumber(supplierData.getBankAccNumber());
        this.bankImport.setRegNumber(supplierData.getBankRegNumber());
        this.bankImport.setBankName(supplierData.getBankName());
    }

    public BankImport build() {
        return this.bankImport;
    }
}
