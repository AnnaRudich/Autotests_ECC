package com.scalepoint.automation.utils.data.entity.rnv.serviceTask.dataBuilders;

import com.scalepoint.automation.utils.data.entity.rnv.serviceTask.ServiceLineExport;
import com.scalepoint.automation.utils.data.entity.rnv.serviceTask.ServiceLineImport;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.scalepoint.automation.utils.data.entity.rnv.serviceTask.dataBuilders.ConvertItem.convertItem;


public class ServiceLineBuilder {

    public static ServiceLineImport serviceLineImport;

    public static List<ServiceLineImport> convertServiceLines(List<ServiceLineExport> serviceLinesExport){
        List<ServiceLineImport> serviceLinesImport = new ArrayList<>();

        serviceLinesImport.addAll(serviceLinesExport.stream().map(ServiceLineBuilder::setDefault).collect(Collectors.toList()));

       return serviceLinesImport;
    }

    public static List<ServiceLineImport> convertServiceLinesWithRepairPrice(BigDecimal repairPrice, List<ServiceLineExport> serviceLinesExport){
        List<ServiceLineImport> serviceLinesImport = new ArrayList<>();

        serviceLinesImport.addAll(serviceLinesExport.stream().map((serviceLineExport) -> withRepairPrice(repairPrice, serviceLineExport)).collect(Collectors.toList()));

        return serviceLinesImport;
    }

    public static ServiceLineImport setDefault(ServiceLineExport serviceLineExport){
        serviceLineImport = new ServiceLineImport();
        serviceLineImport.setLineGUID(serviceLineExport.getLineGUID());
        serviceLineImport.setLineIndex(serviceLineExport.getLineIndex());
        serviceLineImport.setTaskType(serviceLineExport.getTaskType());
        serviceLineImport.setCategory(serviceLineExport.getCategory());
        serviceLineImport.setItem(convertItem(serviceLineExport.getItem()));
        serviceLineImport.setValuations(new ValuationsBuilder().setDefault());
        return serviceLineImport;
    }

    public static ServiceLineImport withRepairPrice(BigDecimal repairPrice, ServiceLineExport serviceLineExport){
        ServiceLineImport serviceLineImport = setDefault(serviceLineExport);
        serviceLineImport.setValuations(new ValuationsBuilder().withRepairPrice(repairPrice));
        return serviceLineImport;
    }
}
