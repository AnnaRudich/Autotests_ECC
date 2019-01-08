package com.scalepoint.automation.utils.data.entity.rnv.serviceTask;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "serviceTask")
class ServiceTask {

    private String GUID;
    private String createdDate;


    @XmlAttribute(name = "uniqueId", required = true)
    String getGUID() {
        return GUID;
    }

    void setGUID(String GUID) {
        this.GUID = GUID;
    }

    @XmlAttribute(required = true)
    String getCreatedDate() {
        return createdDate;
    }

    void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }
}
