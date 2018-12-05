package com.scalepoint.automation.utils.data.entity.serviceTaskEntity.taskData;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "attachments")
@XmlAccessorType(XmlAccessType.FIELD)
public class Attachments {

    @XmlElement(name = "attachmentList")
    private List<Attachment> attachmentList;

    public List<Attachment> getAttachmentList() {
        return attachmentList;
    }

    public void setAttachmentList(List<Attachment> attachmentList) {
        this.attachmentList = attachmentList;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [attachmentList = "+ attachmentList +"]";
    }
}
