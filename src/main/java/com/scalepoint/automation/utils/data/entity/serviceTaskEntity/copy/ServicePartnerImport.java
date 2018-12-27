package com.scalepoint.automation.utils.data.entity.serviceTaskEntity.copy;

import javax.xml.bind.annotation.XmlElement;

import static com.scalepoint.automation.utils.data.entity.serviceTaskEntity.copy.ConvertBank.convertBank;

public class ServicePartnerImport extends ServicePartner{

    BankImport bank;

    @XmlElement(nillable = true)
    public BankImport getBank() {
        return bank;
    }

    public ServicePartnerImport(){}

    public ServicePartnerImport(ServiceTaskExport serviceTaskExport){

        ServicePartnerExport servicePartnerExport = serviceTaskExport.getServicePartner();

       ServicePartnerImport servicePartnerImport = new ServicePartnerImport();
       servicePartnerImport.setBank(convertBank(servicePartnerExport.getBank()));
        servicePartnerImport.setAddress1(servicePartnerExport.getAddress1());
        servicePartnerImport.setAddress2(servicePartnerExport.getAddress2());
        servicePartnerImport.setCity(servicePartnerExport.getCity());
        servicePartnerImport.setCvrNumber(servicePartnerExport.getCvrNumber());
        servicePartnerImport.setEmail(servicePartnerExport.getEmail());
        servicePartnerImport.setName(servicePartnerExport.getName());
        servicePartnerImport.setPhone(servicePartnerExport.getPhone());
        servicePartnerImport.setPostalCode(servicePartnerExport.getPostalCode());
    }

    public void setBank(BankImport bank) {
        this.bank = bank;
    }
}
