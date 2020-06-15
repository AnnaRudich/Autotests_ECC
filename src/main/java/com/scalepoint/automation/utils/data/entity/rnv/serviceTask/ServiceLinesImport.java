package com.scalepoint.automation.utils.data.entity.rnv.serviceTask;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "serviceLines")
public class ServiceLinesImport {

    @XmlElement(name = "serviceLine")
    private List<ServiceLineImport> serviceLinesImportList;

}
