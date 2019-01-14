package com.scalepoint.automation.utils.data.entity.rnv.serviceTask.dataBuilders;

import com.scalepoint.automation.utils.data.entity.rnv.serviceTask.ServiceLineExport;
import com.scalepoint.automation.utils.data.entity.rnv.serviceTask.ServiceLineImport;
import com.scalepoint.automation.utils.data.entity.rnv.serviceTask.ValuationsImport;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.scalepoint.automation.utils.data.entity.rnv.serviceTask.dataBuilders.ConvertItem.convertItem;


public class ConvertServiceLine {
    public static List<ServiceLineImport> convertServiceLines(List<ServiceLineExport> serviceLinesExport){
        List<ServiceLineImport> serviceLinesImport = new ArrayList<>();

        serviceLinesImport.addAll(serviceLinesExport.stream().map(ConvertServiceLine::converter).collect(Collectors.toList()));

       return serviceLinesImport;
    }

    public static ServiceLineImport converter(ServiceLineExport serviceLineExport){
        ServiceLineImport serviceLineImport = new ServiceLineImport();
        serviceLineImport.setLineGUID(serviceLineExport.getLineGUID());
        serviceLineImport.setLineIndex(serviceLineExport.getLineIndex());
        serviceLineImport.setTaskType(serviceLineExport.getTaskType());
        serviceLineImport.setCategory(serviceLineExport.getCategory());
        serviceLineImport.setItem(convertItem(serviceLineExport.getItem()));
        serviceLineImport.setValuations(new ValuationsImport(serviceLineExport));
        return serviceLineImport;
    }
}
