package com.scalepoint.automation.utils.data.entity.serviceTaskEntity.copy;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "serviceLines")
public class ServiceLinesExport {

    private List<ServiceLineExport> serviceLinesExportList;

    @XmlElement(name = "serviceLine")
    public List<ServiceLineExport> getServiceLinesExportList() {
        return serviceLinesExportList;
    }

    public void setServiceLinesExportList(List<ServiceLineExport> serviceLinesExportList) {
        this.serviceLinesExportList = serviceLinesExportList;
    }
}
