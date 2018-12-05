package com.scalepoint.automation.utils.data.entity.serviceTaskEntity.taskData;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "attachment")
@XmlAccessorType(XmlAccessType.FIELD)
public class Attachment {

    @XmlAttribute
    private String link;

    @XmlAttribute
    private String name;

    public String getLink ()
    {
        return link;
    }

    public void setLink (String link)
    {
        this.link = link;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [link = "+link+", name = "+name+"]";
    }
}

