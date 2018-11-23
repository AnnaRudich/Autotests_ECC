package com.scalepoint.automation.utils.data.entity.serviceTaskEntity;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Arrays;

@XmlRootElement
public class ServiceTasks {


    private ServiceTask[] serviceTasks;

    public ServiceTask[] getServiceTasks() {
        return serviceTasks;
    }

    @XmlElement
    public void setServiceTasks(ServiceTask[] serviceTasks) {
        this.serviceTasks = serviceTasks;
    }

    @Override
    public String toString(){
        return "ClassPojo [serviceTasks = "+ Arrays.toString(serviceTasks) +"]";
    }
}
