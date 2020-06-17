package com.scalepoint.automation.utils.data.request;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by bza on 6/28/2017.
 */
@Data
@XmlRootElement(name = "InsertSettlementItemRequest")
@XmlAccessorType(XmlAccessType.NONE)
public class InsertSettlementItemRequest {

    @XmlElement(name = "InsertSettlementItem")
    private InsertSettlementItem insertSettlementItem;

    @Override
    public String toString() {
        return "ClassPojo [insertSettlementItem = " + insertSettlementItem + "]";
    }

}
