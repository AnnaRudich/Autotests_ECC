package com.scalepoint.automation.utils.data.entity.rnv.serviceTask;

import com.scalepoint.automation.utils.Constants;
import com.scalepoint.automation.utils.data.entity.Claim;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.scalepoint.automation.utils.data.entity.rnv.serviceTask.ConvertServiceLine.convertServiceLines;
import static com.scalepoint.automation.utils.data.entity.rnv.serviceTask.ConvertServicePartner.convertServicePartner;

public class ServiceTaskImportBuilder {
    private ServiceTaskImport serviceTaskImport;

    public ServiceTaskImportBuilder setDefault(ServiceTasksExport export, Claim claim) {

        List<ServiceTaskExport> serviceTaskList = export.getServiceTasks();

        Predicate<ServiceTaskExport> byClaimNumber = serviceTaskExport -> serviceTaskExport.getClaim().getClaimNumber().equals(claim.getClaimNumber());
        List<ServiceTaskExport> result = serviceTaskList.stream().filter(byClaimNumber).collect(Collectors.toList());


        ServiceTaskExport serviceTask = result.get(0);

        serviceTaskImport = new ServiceTaskImport();
        serviceTaskImport.setServiceLines(convertServiceLines(serviceTask.getServiceLines()));
        serviceTaskImport.setServicePartner(convertServicePartner(serviceTask.getServicePartner()));
        serviceTaskImport.setInvoice(new InvoiceBuilder().setDefault().build());
        serviceTaskImport.setTakenSelfRisk(BigDecimal.valueOf(Constants.PRICE_10));
        serviceTaskImport.setGUID(serviceTask.getGUID());
        serviceTaskImport.setCreatedDate(serviceTask.getCreatedDate());
        return this;
    }

    public ServiceTaskImport build() {
        return this.serviceTaskImport;
    }
}
