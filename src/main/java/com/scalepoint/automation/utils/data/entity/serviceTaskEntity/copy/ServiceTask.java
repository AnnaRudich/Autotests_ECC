package com.scalepoint.automation.utils.data.entity.serviceTaskEntity.copy;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

@XmlSeeAlso({ServiceTaskExport.class, ServiceTaskImport.class})
@XmlRootElement(name = "serviceTask")
public class ServiceTask {

    private String GUID;
    private String createdDate;


    @XmlAttribute(name = "uniqueId", required = true)
    public String getGUID() {
        return GUID;
    }

    public void setGUID(String GUID) {
        this.GUID = GUID;
    }

    @XmlAttribute(required = true)
    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }
}
