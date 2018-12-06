package com.scalepoint.automation.utils.data.entity.serviceTaskEntity.copy;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "serviceTasks")
public class ServiceTasksExport {

    private List<ServiceTaskExport> serviceTasks;

    @XmlElement(name="serviceTask")
    public List<ServiceTaskExport> getServiceTasks() {
        return serviceTasks;
    }

    public void setServiceTasks(List<ServiceTaskExport> serviceTasks) {
        this.serviceTasks = serviceTasks;
    }
}
