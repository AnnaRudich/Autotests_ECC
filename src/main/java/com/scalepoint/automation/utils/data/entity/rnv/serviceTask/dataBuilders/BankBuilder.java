package com.scalepoint.automation.utils.data.entity.rnv.serviceTask.dataBuilders;

import com.scalepoint.automation.utils.data.TestData;
import com.scalepoint.automation.utils.data.entity.Supplier;
import com.scalepoint.automation.utils.data.entity.rnv.serviceTask.BankImport;

public class BankBuilder {
    private BankImport bankImport;

    public  BankImport setDefault(){
        Supplier supplierData = TestData.getSupplier();
        bankImport = new BankImport();
        bankImport.setFikType(supplierData.getBankFikType());
        bankImport.setFikCreditorCode(supplierData.getBankFikCreditorCode());
        bankImport.setFikNumber(supplierData.getBankFikNumber());
        bankImport.setAccountNumber(supplierData.getBankAccNumber());
        bankImport.setRegNumber(supplierData.getBankRegNumber());
        bankImport.setBankName(supplierData.getBankName());
        return bankImport;
}
}
