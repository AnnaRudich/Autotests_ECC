package com.scalepoint.automation.utils.data.entity.rnv.serviceTask;

import com.scalepoint.automation.utils.data.TestData;
import com.scalepoint.automation.utils.data.entity.Supplier;

public class BankBuilder {
    private BankImport bankImport;

    public  BankBuilder setDefault(){
        Supplier supplierData = TestData.getSupplier();
        bankImport = new BankImport();
        bankImport.setFikType(supplierData.getBankFikType());
        bankImport.setFikCreditorCode(supplierData.getBankFikCreditorCode());
        bankImport.setFikNumber(supplierData.getBankFikNumber());
        bankImport.setAccountNumber(supplierData.getBankAccNumber());
        bankImport.setRegNumber(supplierData.getBankRegNumber());
        bankImport.setBankName(supplierData.getBankName());
        return this;
}

    public BankImport build(){
        return bankImport;
    }

}
