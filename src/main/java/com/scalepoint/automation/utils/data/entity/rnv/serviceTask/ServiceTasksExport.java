package com.scalepoint.automation.utils.data.entity.rnv.serviceTask;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "serviceTasks")
public class ServiceTasksExport {

    private List<ServiceTaskExport> serviceTasks;

    @XmlElement(name="serviceTask")
    List<ServiceTaskExport> getServiceTasks() {
        return serviceTasks;
    }

    public void setServiceTasks(List<ServiceTaskExport> serviceTasks) {
        this.serviceTasks = serviceTasks;
    }
}
