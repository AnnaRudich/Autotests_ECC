package com.scalepoint.automation.utils.data.entity.serviceTaskEntity.copy;

import com.scalepoint.automation.utils.Constants;

import java.math.BigDecimal;
import java.util.List;

import static com.scalepoint.automation.utils.data.entity.serviceTaskEntity.copy.ConvertServiceLine.convertServiceLines;
import static com.scalepoint.automation.utils.data.entity.serviceTaskEntity.copy.ConvertServicePartner.convertServicePartner;

public class ServiceTaskImportBuilder {
    ServiceTaskImport serviceTaskImport;

    public ServiceTaskImportBuilder setDefault(ServiceTasksExport export){

        List<ServiceTaskExport> serviceTaskList = export.getServiceTasks();

        ServiceTaskExport serviceTask = serviceTaskList.get(serviceTaskList.size()-1);

        serviceTaskImport = new ServiceTaskImport();
        serviceTaskImport.setServiceLines(convertServiceLines(serviceTask.getServiceLines()));
        serviceTaskImport.setServicePartner(convertServicePartner(serviceTask.getServicePartner()));
        serviceTaskImport.setInvoice(new InvoiceBuilder().setDefault().build());
        serviceTaskImport.setTakenSelfRisk(BigDecimal.valueOf(Constants.PRICE_10));
        serviceTaskImport.setGUID(serviceTask.getGUID());
        serviceTaskImport.setCreatedDate(serviceTask.getCreatedDate());
        return this;
    }

    public  ServiceTaskImport build(){
        return this.serviceTaskImport;
    }
}
