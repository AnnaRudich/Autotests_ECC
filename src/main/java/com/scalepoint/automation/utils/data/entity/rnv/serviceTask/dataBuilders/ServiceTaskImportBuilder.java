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

import static com.scalepoint.automation.utils.data.entity.rnv.serviceTask.dataBuilders.ServiceLineBuilder.convertServiceLines;
import static com.scalepoint.automation.utils.data.entity.rnv.serviceTask.dataBuilders.ConvertServicePartner.convertServicePartner;
import static com.scalepoint.automation.utils.data.entity.rnv.serviceTask.dataBuilders.ServiceLineBuilder.convertServiceLinesWithRepairPrice;

public class ServiceTaskImportBuilder {

        private ServiceTaskImport serviceTaskImport;
        private ServiceTaskExport serviceTaskExport;
        public Claim claim;
        private ServiceTasksExport export;

        public ServiceTaskImportBuilder(Claim claim, ServiceTasksExport export){
            this.claim = claim;
            this.export = export;
            this.serviceTaskExport = getFirstServiceTaskExportByClaimNumber();
        }

        private ServiceTaskExport getFirstServiceTaskExportByClaimNumber(){
            List<ServiceTaskExport> serviceTaskList = export.getServiceTasks();

            Predicate<ServiceTaskExport> byClaimNumber = serviceTaskExport -> serviceTaskExport.getClaim().getClaimNumber().equals(claim.getClaimNumber());
            List<ServiceTaskExport> result = serviceTaskList.stream().filter(byClaimNumber).collect(Collectors.toList());
            return result.get(0);
        }


        public ServiceTaskImport setDefault() {
            serviceTaskImport = new ServiceTaskImport();
            serviceTaskImport.setServiceLines(convertServiceLines(serviceTaskExport.getServiceLines()));
            serviceTaskImport.setServicePartner(convertServicePartner(serviceTaskExport.getServicePartner()));
            serviceTaskImport.setInvoice(new InvoiceBuilder().setDefault().build());
            serviceTaskImport.setTakenSelfRisk(BigDecimal.valueOf(Constants.PRICE_10));
            serviceTaskImport.setGUID(serviceTaskExport.getGUID());
            serviceTaskImport.setCreatedDate(serviceTaskExport.getCreatedDate());
            return serviceTaskImport;
        }

        public ServiceTaskImport withRepairPrice(BigDecimal repairPrice){
            setDefault().setServiceLines(convertServiceLinesWithRepairPrice(repairPrice, serviceTaskExport.getServiceLines()));
            return serviceTaskImport;
        }
    }

