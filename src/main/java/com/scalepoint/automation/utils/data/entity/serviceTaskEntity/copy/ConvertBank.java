package com.scalepoint.automation.utils.data.entity.serviceTaskEntity.copy;


public class ConvertBank {

    public static BankImport convertBank(BankExport bankExport){
    BankImport bankImport = new BankImport();
        bankImport.setIBAN(bankExport.getIBAN());
        bankImport.setFikType(bankExport.getFikType());
        bankImport.setFikCreditorCode(bankExport.getFikCreditorCode());
        bankImport.setAccountNumber(bankExport.getFikCreditorCode());
        bankImport.setRegNumber(bankExport.getRegNumber());
        bankImport.setBankName("bankName");
        bankImport.setFikNumber("2352534");
        return bankImport;
    }

}
