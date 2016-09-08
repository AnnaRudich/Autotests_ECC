package com.scalepoint.automation.utils.data.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by sro on 2/5/13.
 */

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class DepreciationType {
    @XmlElement
    private String policyType;

    @XmlElement
    private String discretionaryType;

    public String getDiscretionaryType() {
        return discretionaryType;
    }

    public String getPolicyType() {
        return policyType;
    }


}
