package com.scalepoint.automation.utils.data.entity.rnv.serviceTask;

import javax.xml.bind.annotation.XmlAttribute;

public class AttachmentExport {


    private String name;
    private String link;

    public AttachmentExport(String name, String link) {
        this.name = name;
        this.link = link;
    }

    @XmlAttribute(required = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlAttribute(required = true)
    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
