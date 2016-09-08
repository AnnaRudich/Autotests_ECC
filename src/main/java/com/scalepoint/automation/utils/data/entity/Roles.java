package com.scalepoint.automation.utils.data.entity;

import com.scalepoint.automation.utils.RandomUtils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Roles {

    private String roleName = RandomUtils.randomName("Role");
    @XmlElement
    private String itManager;

    public String getRoleName() {
        return roleName;
    }

    public String getItManager() {
        return itManager;
    }
}
