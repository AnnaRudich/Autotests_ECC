package com.scalepoint.automation.utils.data.entity.rnv.serviceTask;


public class ConvertBank {
    static BankImport convertBank(BankExport bankExport){
    BankImport bankImport = new BankImport();
        bankImport.setFikType("345");
        bankImport.setFikCreditorCode("435443");
        bankImport.setAccountNumber("1223344556677889");
        bankImport.setRegNumber("34324235");
        bankImport.setBankName("bankName");
        bankImport.setFikNumber("2352534");
        bankImport.setRegNumber("989767345");
        return bankImport;
    }
    //all tha data should be added using Supplier.xml
}