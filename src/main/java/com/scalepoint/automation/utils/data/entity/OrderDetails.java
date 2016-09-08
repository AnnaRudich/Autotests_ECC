package com.scalepoint.automation.utils.data.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by sro on 10.09.2014.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class OrderDetails {
    @XmlElement
    private String indemnity;
    @XmlElement
    private String orderedItems;


    @XmlElement

    private String withdrawalls;
    @XmlElement
    private String deposits;
    @XmlElement
    private String remainingIdemnity;
    @XmlElement
    private String totalText;


    public String getIndemnity() {
        return indemnity;
    }

    public String getOrderedItems() {
        return orderedItems;
    }

    public String getWithdrawalls() {
        return withdrawalls;
    }

    public String getDeposits() {
        return deposits;
    }

    public String getRemainingIdemnity() {
        return remainingIdemnity;
    }

    public String getTotalText() {
        return totalText;
    }
}
