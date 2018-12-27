package com.scalepoint.automation.utils.data.entity.serviceTaskEntity.copy;

import static com.scalepoint.automation.utils.data.entity.serviceTaskEntity.copy.ConvertBank.convertBank;

public class ConvertServicePartner {


    public static ServicePartnerImport convertServicePartner(ServicePartnerExport servicePartnerExport){
        ServicePartnerImport servicePartnerImport = new ServicePartnerImport();

        servicePartnerImport.setBank(convertBank(servicePartnerExport.getBank()));
        servicePartnerImport.setAddress1(servicePartnerExport.getAddress1());
        servicePartnerImport.setAddress1(servicePartnerExport.getAddress1());
        servicePartnerImport.setCity(servicePartnerExport.getCity());
        servicePartnerImport.setCvrNumber(servicePartnerExport.getCvrNumber());
        servicePartnerImport.setEmail(servicePartnerExport.getEmail());
        servicePartnerImport.setName(servicePartnerExport.getName());
        servicePartnerImport.setPhone(servicePartnerExport.getPhone());
        servicePartnerImport.setPostalCode(servicePartnerExport.getPostalCode());

        return servicePartnerImport;
    }
}
