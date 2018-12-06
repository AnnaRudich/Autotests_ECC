package com.scalepoint.automation.utils.data.entity.serviceTaskEntity.copy;

import com.scalepoint.automation.utils.Constants;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ServiceTaskImportBuilder {
    ServiceTaskImport serviceTaskImport;

    public ServiceTaskImportBuilder setDefault(){

        List<ServiceLineImport> serviceLineImportList = new ArrayList<>();
        serviceLineImportList.add(new ServiceLineImport(new ItemImport(false), new ValuationsImport()));

        serviceTaskImport.setServiceLines(serviceLineImportList);
        serviceTaskImport.setServicePartner(new ServicePartnerImport(new BankImport("bankName", "fikName")));
        serviceTaskImport.setInvoice(new InvoiceBuilder().setDefault().build());
        serviceTaskImport.setTakenSelfRisk(BigDecimal.valueOf(Constants.PRICE_10));
        return this;
    }

    public  ServiceTaskImport build(){
        return this.serviceTaskImport;
    }
}
