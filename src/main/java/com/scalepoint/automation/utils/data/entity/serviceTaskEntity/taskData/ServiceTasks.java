package com.scalepoint.automation.utils.data.entity.serviceTaskEntity.taskData;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "serviceTasks")
@XmlAccessorType(XmlAccessType.FIELD)

public class ServiceTasks {

    @XmlElement(name = "serviceTask")
    private List<ServiceTask> serviceTaskList;

    public List<ServiceTask> getServiceTaskList() {
        return serviceTaskList;
    }

    public void setServiceTaskList(List<ServiceTask> serviceTaskList) {
        this.serviceTaskList = serviceTaskList;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [serviceTask = "+serviceTaskList+"]";
    }
}
