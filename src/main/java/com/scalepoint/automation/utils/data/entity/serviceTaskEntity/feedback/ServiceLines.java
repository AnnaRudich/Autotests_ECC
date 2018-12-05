package com.scalepoint.automation.utils.data.entity.serviceTaskEntity.feedback;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "serviceLines")
@XmlAccessorType(XmlAccessType.FIELD)
public class ServiceLines {

    @XmlElement(name = "serviceLine")
    private List<ServiceLine> serviceLine;

    public List<ServiceLine> getServiceLine() {
        return serviceLine;
    }

    public void setServiceLine(List<ServiceLine> serviceLine) {
        this.serviceLine = serviceLine;
    }


    @Override
    public String toString()
    {
        return "ClassPojo [serviceLine = "+serviceLine+"]";
    }
}
