package com.scalepoint.automation.utils.data.entity.serviceTaskEntity.copy;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.List;

public class ServiceLineExport extends ServiceLine {

    private ItemExport item;
    private ValuationsExport valuations;
    private List<AttachmentExport> attachments;

    @XmlElement
    public ItemExport getItem() {
        return item;
    }

    public void setItem(ItemExport item) {
        this.item = item;
    }

    @XmlElement
    public ValuationsExport getValuations() {
        return valuations;
    }

    public void setValuations(ValuationsExport valuations) {
        this.valuations = valuations;
    }

    @XmlElementWrapper(name = "attachments")
    @XmlElement(name = "attachment")
    public List<AttachmentExport> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<AttachmentExport> attachments) {
        this.attachments = attachments;
    }
}
