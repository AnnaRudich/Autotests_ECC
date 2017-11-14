package com.scalepoint.automation.utils.data.entity;

import com.scalepoint.automation.utils.RandomUtils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ClaimLineGroup {

    private String overviewGroupName = RandomUtils.randomName("o_group");
    private String valuationGroupName = RandomUtils.randomName("v_group");
    private String noteText = RandomUtils.randomName("Group operations note ");

    @XmlElement
    private String defaultGroupName;
    @XmlElement
    private String customerDemand;
    @XmlElement
    private String newPrice;
    @XmlElement
    private String valuation;
    @XmlElement
    private String filePath;
    @XmlElement
    private String[] excelLineGroups;


    public String getOverviewGroupName() {
        return overviewGroupName;
    }

    public String getDefaultGroupName() {
        return defaultGroupName;
    }

    public String getValuationGroupName() {
        return valuationGroupName;
    }

    public String getCustDemand() {
        return customerDemand;
    }

    public String getNewPrice() {
        return newPrice;
    }

    public String getValuation() {
        return valuation;
    }

    public String getNoteText() {
        return noteText;
    }

    public String getExcelWithGroupsFilePath() {
        return filePath;
    }
    public String[] getExcelLineGroups(){
        return excelLineGroups;
    }
}
