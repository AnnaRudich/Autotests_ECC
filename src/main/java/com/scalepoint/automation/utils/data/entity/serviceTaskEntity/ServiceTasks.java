package com.scalepoint.automation.utils.data.entity.serviceTaskEntity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "serviceTasks")
@XmlAccessorType(XmlAccessType.FIELD)
public class ServiceTasks {

    @XmlElement(name = "serviceTask")
    private List<ServiceTask> serviceTasks=null;

    public List<ServiceTask> getServiceTasks() {
        return serviceTasks;
    }

    public void setServiceTasks(List<ServiceTask> serviceTasks) {
        this.serviceTasks = serviceTasks;
    }


    @Override
    public String toString(){
        return "ClassPojo [serviceTasks = "+ serviceTasks +"]";
    }
}
