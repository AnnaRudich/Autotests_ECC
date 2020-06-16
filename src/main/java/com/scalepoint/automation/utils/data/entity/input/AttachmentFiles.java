package com.scalepoint.automation.utils.data.entity.input;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import static com.scalepoint.automation.utils.SystemUtils.getResourcePath;

@Data
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class AttachmentFiles {

    @XmlElement
    private String jpgFile2;

    public String getJpgFile2Loc() {
        return getResourcePath(jpgFile2);
    }

}
