package com.scalepoint.automation.utils.data.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class RnvTaskType {

    @XmlElement
    private String valuation;

    @XmlElement
    private String repair;

    @XmlElement
    private String repairEstimate;

    @XmlElement
    private String matchService;


    public String getValuation() {
        return valuation;
    }

    public String getRepair() {
        return repair;
    }

    public String getRepairEstimate() {
        return repairEstimate;
    }

    public String getMatchService() {
        return matchService;    }


}
