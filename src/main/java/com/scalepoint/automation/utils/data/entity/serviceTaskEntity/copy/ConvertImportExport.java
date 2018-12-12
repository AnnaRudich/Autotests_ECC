package com.scalepoint.automation.utils.data.entity.serviceTaskEntity.copy;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class ConvertImportExport {
    public static List<ServiceLineImport> convertServiceLines(List<ServiceLineExport> serviceLinesExport){
        List<ServiceLineImport> serviceLinesImport = new ArrayList<>();

        serviceLinesImport.addAll(serviceLinesExport.stream().map(ConvertImportExport::converter).collect(Collectors.toList()));

       return serviceLinesImport;
    }

    public static ServiceLineImport converter(ServiceLineExport serviceLineExport){
        ServiceLineImport serviceLineImport = new ServiceLineImport();
        serviceLineImport.setLineGUID(serviceLineExport.getLineGUID());
        serviceLineImport.setLineIndex(serviceLineExport.getLineIndex());
        serviceLineImport.setTaskType(serviceLineExport.getTaskType());
        serviceLineImport.setCategory(serviceLineExport.getCategory());
        serviceLineImport.setItem(new ItemImport(serviceLineExport));
        serviceLineImport.setValuations(new ValuationsImport(serviceLineExport));
        return serviceLineImport;
    }
}
