package com.scalepoint.automation.utils.data.request;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Created by bza on 6/28/2017.
 */
@XmlRootElement(name = "InsertSettlementItem")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InsertSettlementItem")
public class InsertSettlementItem
{
    @XmlAttribute
    private String caseId;

    @XmlElement
    private SettlementItem SettlementItem;

    public String getCaseId ()
    {
        return caseId;
    }

    public void setCaseId (String caseId)
    {
        this.caseId = caseId;
    }

    public SettlementItem getSettlementItem ()
    {
        return SettlementItem;
    }

    public void setSettlementItem (SettlementItem SettlementItem)
    {
        this.SettlementItem = SettlementItem;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [caseId = "+caseId+", SettlementItem = "+SettlementItem+"]";
    }
}
