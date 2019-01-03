package com.scalepoint.automation.utils.data.entity.rnv.serviceTask;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "serviceLines")
public class ServiceLinesImport {

    private List<ServiceLineImport> serviceLinesImportList;

    public ServiceLinesImport(){
    }

    @XmlElement(name="serviceLine")
    public List<ServiceLineImport> getServiceTasks() {
        return serviceLinesImportList;
    }

    public void setServiceLinesImportList(List<ServiceLineImport> serviceLinesImportList) {
        this.serviceLinesImportList = serviceLinesImportList;
    }

}
