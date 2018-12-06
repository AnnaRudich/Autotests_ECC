package com.scalepoint.automation.utils.data.entity.serviceTaskEntity.copy;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "serviceLine")
public class ServiceLine {

    private Integer lineIndex;
    private String lineGUID;
    private Category category;
    private String taskType;

    @XmlAttribute(name = "claimLineId")
    public Integer getLineIndex() {
        return lineIndex;
    }

    public void setLineIndex(Integer lineIndex) {
        this.lineIndex = lineIndex;
    }

    @XmlAttribute(name = "uniqueId")
    public String getLineGUID() {
        return lineGUID;
    }

    public void setLineGUID(String lineGUID) {
        this.lineGUID = lineGUID;
    }

    @XmlElement
    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @XmlAttribute(name = "taskType")
    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }
}
