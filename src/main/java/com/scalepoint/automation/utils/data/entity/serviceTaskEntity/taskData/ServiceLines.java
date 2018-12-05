package com.scalepoint.automation.utils.data.entity.serviceTaskEntity.taskData;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "serviceLines")
@XmlAccessorType(XmlAccessType.FIELD)
public class ServiceLines {

    @XmlElement(name = "serviceLine")
    private List<ServiceLine> serviceLineList;

    public List<ServiceLine> getServiceLineList() {
        return serviceLineList;
    }

    public void setServiceLineList(List<ServiceLine> serviceLineList) {
        this.serviceLineList = serviceLineList;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [serviceLine = "+serviceLineList+"]";
    }
}
