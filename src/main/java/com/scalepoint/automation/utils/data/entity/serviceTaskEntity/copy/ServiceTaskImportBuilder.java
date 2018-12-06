package com.scalepoint.automation.utils.data.entity.serviceTaskEntity.copy;

public class ServiceTaskImportBuilder {
    ServiceTaskImport serviceTaskImport;

    public ServiceTaskImportBuilder(){
        this.serviceTaskImport = new ServiceTaskImport();
    }

    public ServiceTaskImportBuilder setDefault(){

        ServiceTask serviceTaskExport = new ServiceTasksExport().getServiceTasks().get(0);
        serviceTaskImport.setServicePartner(new ServicePartnerImport());
        return this;
    }
}
