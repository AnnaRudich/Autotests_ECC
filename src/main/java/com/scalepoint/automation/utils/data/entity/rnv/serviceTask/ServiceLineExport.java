package com.scalepoint.automation.utils.data.entity.rnv.serviceTask;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.List;


@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class ServiceLineExport extends ServiceLine {

    @XmlElement
    private ItemExport item;
    @XmlElement
    private ValuationsExport valuations;
    @XmlElementWrapper(name = "attachments")
    @XmlElement(name = "attachment")
    private List<AttachmentExport> attachments;

}
