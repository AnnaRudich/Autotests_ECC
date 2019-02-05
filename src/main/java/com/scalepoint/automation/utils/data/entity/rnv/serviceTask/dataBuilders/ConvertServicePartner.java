package com.scalepoint.automation.utils.data.entity.rnv.serviceTask.dataBuilders;

import com.scalepoint.automation.utils.data.entity.rnv.serviceTask.ServicePartnerExport;
import com.scalepoint.automation.utils.data.entity.rnv.serviceTask.ServicePartnerImport;
import com.scalepoint.automation.utils.data.entity.rnv.serviceTask.dataBuilders.BankBuilder;

public class ConvertServicePartner {


    public static ServicePartnerImport convertServicePartner(ServicePartnerExport servicePartnerExport){
        ServicePartnerImport servicePartnerImport = new ServicePartnerImport();

        servicePartnerImport.setBank(new BankBuilder().setDefault());
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
