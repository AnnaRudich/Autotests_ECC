package com.scalepoint.automation.utils.data.entity.rnv.serviceTask.dataBuilders;

import com.scalepoint.automation.utils.data.entity.rnv.serviceTask.ServiceLineExport;
import com.scalepoint.automation.utils.data.entity.rnv.serviceTask.ServiceLineImport;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.scalepoint.automation.utils.data.entity.rnv.serviceTask.dataBuilders.ConvertItem.convertItem;


public class ServiceLineBuilder {
    private ServiceLineBuilder() {
    }

    public static ServiceLineImport serviceLineImport;

    public static List<ServiceLineImport> convertServiceLines(List<ServiceLineExport> serviceLinesExport) {
        return convert(serviceLinesExport, ServiceLineBuilder::setDefault);
    }

    public static List<ServiceLineImport> convertServiceLinesWithRepairPrice(BigDecimal repairPrice, List<ServiceLineExport> serviceLinesExport) {
        return convert(serviceLinesExport, (serviceLineExport) -> withRepairPrice(repairPrice, serviceLineExport));
    }

    public static List<ServiceLineImport> convert(List<ServiceLineExport> serviceLinesExport, Function<ServiceLineExport, ServiceLineImport> function) {
        List<ServiceLineImport> importLines = new ArrayList<>();
        importLines.addAll(serviceLinesExport.stream().map(function).collect(Collectors.toList()));
        return importLines;
    }

    public static ServiceLineImport setDefault(ServiceLineExport serviceLineExport) {
        serviceLineImport = new ServiceLineImport();
        serviceLineImport.setLineGUID(serviceLineExport.getLineGUID());
        serviceLineImport.setLineIndex(serviceLineExport.getLineIndex());
        serviceLineImport.setTaskType(serviceLineExport.getTaskType());
        serviceLineImport.setCategory(serviceLineExport.getCategory());
        serviceLineImport.setItem(convertItem(serviceLineExport.getItem()));
        serviceLineImport.setValuations(new ValuationsBuilder().build());
        return serviceLineImport;
    }

    public static ServiceLineImport withRepairPrice(BigDecimal repairPrice, ServiceLineExport serviceLineExport) {
        ServiceLineImport serviceLineImport = setDefault(serviceLineExport);
        serviceLineImport.setValuations(new ValuationsBuilder().withRepairPrice(repairPrice).build());
        return serviceLineImport;
    }
}
