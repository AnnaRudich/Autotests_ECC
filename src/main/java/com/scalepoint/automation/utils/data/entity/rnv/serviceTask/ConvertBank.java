package com.scalepoint.automation.utils.data.entity.rnv.serviceTask;


class ConvertBank {
    static BankImport convertBank(BankExport bankExport){
    BankImport bankImport = new BankImport();
        bankImport.setIBAN(bankExport.getIBAN());
        bankImport.setFikType(bankExport.getFikType());
        bankImport.setFikCreditorCode(bankExport.getFikCreditorCode());
        bankImport.setAccountNumber("1223344556677889");
        bankImport.setRegNumber(bankExport.getRegNumber());
        bankImport.setBankName("bankName");
        bankImport.setFikNumber("2352534");
        bankImport.setRegNumber("989767345");
        return bankImport;
    }
}