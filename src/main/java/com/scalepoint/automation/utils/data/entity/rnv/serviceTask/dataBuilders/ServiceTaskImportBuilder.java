package com.scalepoint.automation.utils.data.entity.rnv.serviceTask.dataBuilders;

import com.scalepoint.automation.utils.Constants;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.rnv.serviceTask.ServiceTaskExport;
import com.scalepoint.automation.utils.data.entity.rnv.serviceTask.ServiceTaskImport;
import com.scalepoint.automation.utils.data.entity.rnv.serviceTask.ServiceTasksExport;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.scalepoint.automation.utils.data.entity.rnv.serviceTask.dataBuilders.ConvertServicePartner.convertServicePartner;
import static com.scalepoint.automation.utils.data.entity.rnv.serviceTask.dataBuilders.ServiceLineBuilder.convertServiceLines;
import static com.scalepoint.automation.utils.data.entity.rnv.serviceTask.dataBuilders.ServiceLineBuilder.convertServiceLinesWithRepairPrice;

public class ServiceTaskImportBuilder {

    private ServiceTaskImport serviceTaskImport;
    private ServiceTaskExport serviceTaskExport;
    private Claim claim;
    private ServiceTasksExport export;

    public ServiceTaskImportBuilder(Claim claim, ServiceTasksExport export) {
        this.claim = claim;
        this.export = export;
        this.serviceTaskExport = getFirstServiceTaskExportByClaimNumber();
    }

    private ServiceTaskExport getFirstServiceTaskExportByClaimNumber() {
        List<ServiceTaskExport> serviceTaskList = export.getServiceTasks();

        Predicate<ServiceTaskExport> byClaimNumber = serviceTaskExport -> serviceTaskExport.getClaim().getClaimNumber().equals(claim.getClaimNumber());
        List<ServiceTaskExport> result = serviceTaskList.stream().filter(byClaimNumber).collect(Collectors.toList());
        return result.get(0);
    }


    public ServiceTaskImport buildDefault() {
        this.serviceTaskImport = new ServiceTaskImport();
        this.serviceTaskImport.setServiceLines(convertServiceLines(this.serviceTaskExport.getServiceLines()));
        this.serviceTaskImport.setServicePartner(convertServicePartner(this.serviceTaskExport.getServicePartner()));
        this.serviceTaskImport.setInvoice(new InvoiceBuilder().build());
        this.serviceTaskImport.setTakenSelfRisk(BigDecimal.valueOf(Constants.PRICE_10));
        this.serviceTaskImport.setGUID(this.serviceTaskExport.getGUID());
        this.serviceTaskImport.setCreatedDate(this.serviceTaskExport.getCreatedDate());
        return this.serviceTaskImport;
    }

    public ServiceTaskImport buildWithRepairPrice(BigDecimal repairPrice) {
        buildDefault();
        this.serviceTaskImport.setServiceLines(convertServiceLinesWithRepairPrice(repairPrice, this.serviceTaskExport.getServiceLines()));
        return this.serviceTaskImport;
    }
}

