package com.scalepoint.automation.utils.data.entity.serviceTaskEntity;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement()
public class ServiceTasks {

    private List<ServiceTask> serviceTasks;


    public List<ServiceTask> getServiceTasks() {
        return serviceTasks;
    }

    public void setServiceTasks(List<ServiceTask> serviceTasks) {
        this.serviceTasks = serviceTasks;
    }

    @Override
    public String toString(){
        return "ClassPojo [serviceTasks = "+serviceTasks+"]";
    }
}
