package com.scalepoint.automation.utils.data.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by asa on 10/27/2016.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)

public class DiscretionaryReason {
    @XmlElement
    private String discretionaryReason1;
    @XmlElement
    private String discretionaryReason2;

    public String getDiscretionaryReason1(){
        return discretionaryReason1;
    }

    public String getDiscretionaryReason2(){
        return discretionaryReason2;
    }
}
