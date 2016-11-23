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
    @XmlElement
    private String discretionaryReason3;
    @XmlElement
    private String discretionaryReason4;
    @XmlElement
    private String discretionaryReason5;
    @XmlElement
    private String discretionaryReason6;
    @XmlElement
    private String discretionaryReason7;
    @XmlElement
    private String discretionaryReason71;
    @XmlElement
    private String discretionaryReason70;

    public String getDiscretionaryReason71(){
        return discretionaryReason71;
    }

    public String getDiscretionaryReason70(){
        return discretionaryReason70;
    }

    public String getDiscretionaryReason1(){
        return discretionaryReason1;
    }

    public String getDiscretionaryReason2(){
        return discretionaryReason2;
    }

    public String getDiscretionaryReason3(){
        return discretionaryReason3;
    }

    public String getDiscretionaryReason4(){
        return discretionaryReason4;
    }

    public String getDiscretionaryReason5(){
        return discretionaryReason5;
    }

    public String getDiscretionaryReason6() {
        return discretionaryReason6;
    }

    public String getDiscretionaryReason7() {
        return discretionaryReason7;
    }

}
