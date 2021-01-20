package com.scalepoint.automation.utils.data.entity.rnv.serviceTask.dataBuilders;

import com.scalepoint.automation.services.restService.RnvInvoiceType;
import com.scalepoint.automation.utils.Constants;
import com.scalepoint.automation.utils.data.entity.input.Claim;
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


    public ServiceTaskImport buildDefaultWithInvoice() {
        buildDefaultWithoutInvoice();
        this.serviceTaskImport.setInvoice(new InvoiceBuilder().build());
        this.serviceTaskImport.setTakenSelfRisk(BigDecimal.valueOf(Constants.PRICE_10));
        return this.serviceTaskImport;
    }

    public ServiceTaskImport buildDefaultWithCreditNote(String invoiceNumber) {
        buildDefaultWithoutInvoice();
        this.serviceTaskImport.setInvoice(new InvoiceBuilder()
                .withInvoiceType(String.valueOf(RnvInvoiceType.CREDIT_NOTE))
                .withInvoiceNumber(invoiceNumber)
                .build());
        this.serviceTaskImport.setTakenSelfRisk(BigDecimal.valueOf(Constants.PRICE_10));
        return this.serviceTaskImport;
    }

    public ServiceTaskImport buildDefaultWithoutInvoice(){
        this.serviceTaskImport = new ServiceTaskImport();
        this.serviceTaskImport.setServiceLines(convertServiceLines(this.serviceTaskExport.getServiceLines()));
        this.serviceTaskImport.setServicePartner(convertServicePartner(this.serviceTaskExport.getServicePartner()));
        this.serviceTaskImport.setGUID(this.serviceTaskExport.getGUID());
        this.serviceTaskImport.setCreatedDate(this.serviceTaskExport.getCreatedDate());
        return this.serviceTaskImport;
    }

    public ServiceTaskImport buildDefaultWithoutInvoiceWithRepairPrice(BigDecimal repairPrice){
        buildDefaultWithoutInvoice();
        this.serviceTaskImport.setServiceLines(convertServiceLinesWithRepairPrice(repairPrice, this.serviceTaskExport.getServiceLines()));
        return this.serviceTaskImport;
    }

    public ServiceTaskImport buildDefaultWithInvoiceWithRepairPrice(BigDecimal repairPrice) {
        buildDefaultWithInvoice();
        this.serviceTaskImport.setServiceLines(convertServiceLinesWithRepairPrice(repairPrice, this.serviceTaskExport.getServiceLines()));
        return this.serviceTaskImport;
    }

    public ServiceTaskImport buildDefaultWithCreditNoteWithRepairPrice(BigDecimal repairPrice, String invoiceNumber) {
        buildDefaultWithCreditNote(invoiceNumber);
        this.serviceTaskImport.setServiceLines(convertServiceLinesWithRepairPrice(repairPrice, this.serviceTaskExport.getServiceLines()));
        return this.serviceTaskImport;
    }
}

