package com.scalepoint.automation.utils.data.entity.serviceTaskEntity.copy;

import com.scalepoint.automation.utils.Constants;

import java.math.BigDecimal;

public class ServiceTaskImportBuilder {
    ServiceTaskImport serviceTaskImport;

    public ServiceTaskImportBuilder setDefault(ServiceTasksExport export){

        serviceTaskImport = new ServiceTaskImport();
        serviceTaskImport.setServiceLines(new ConvertImportExport().convertServiceLines(export.getServiceTasks().get(0).getServiceLines()));
       // serviceTaskImport.setServicePartner(export.getServiceTasks().get(0).getServicePartner());
        serviceTaskImport.setInvoice(new InvoiceBuilder().setDefault().build());
        serviceTaskImport.setTakenSelfRisk(BigDecimal.valueOf(Constants.PRICE_10));
        serviceTaskImport.setGUID(export.getServiceTasks().get(0).getGUID());
        serviceTaskImport.setCreatedDate(export.getServiceTasks().get(0).getCreatedDate());
        return this;
    }

    public  ServiceTaskImport build(){
        return this.serviceTaskImport;
    }
}
