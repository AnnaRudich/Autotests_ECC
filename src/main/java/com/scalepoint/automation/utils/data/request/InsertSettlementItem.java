package com.scalepoint.automation.utils.data.request;

import lombok.Data;

import javax.xml.bind.annotation.*;

/**
 * Created by bza on 6/28/2017.
 */
@Data
@XmlRootElement(name = "InsertSettlementItem")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InsertSettlementItem")
public class InsertSettlementItem {

    @XmlAttribute
    private String caseId;
    @XmlElement
    private SettlementItem SettlementItem;
    @XmlTransient
    public Integer eccItemId;

    @Override
    public String toString() {
        return "ClassPojo [caseId = " + caseId + ", SettlementItem = " + SettlementItem + "]";
    }

}
