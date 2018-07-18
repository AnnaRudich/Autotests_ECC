package com.scalepoint.automation.utils.data.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Objects;


@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class AttachmentFiles {

    @XmlElement
    private String jpgFile2;

    public String getJpgFile2Loc() {
        return Objects.requireNonNull(ClassLoader.getSystemClassLoader().getResource(jpgFile2)).getPath();
    }


}
