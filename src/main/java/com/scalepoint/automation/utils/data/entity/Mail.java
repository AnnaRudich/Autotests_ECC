package com.scalepoint.automation.utils.data.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Mail {

    @XmlElement
    private String reminderSubj;
    @XmlElement
    private String accessClosedSubj;
    @XmlElement
    private String onlineReplacementSubj;
    @XmlElement
    private String customerMailSSConfirmation;
    @XmlElement
    private String icMailSSConfirmation2CH;
    @XmlElement
    private String ssAccess;
    @XmlElement
    private String settlementNotificationCH;
    @XmlElement
    private String orderConfirmationCustomer;

    public String getReminderSubj() {
        return reminderSubj;
    }

    public String getAccessClosedSubj() {
        return accessClosedSubj;
    }

    public String getOnlineReplacementSubj() {
        return onlineReplacementSubj;
    }

    public String getCustomerMailSSConfirmation() {
        return customerMailSSConfirmation;
    }

    public String getIcMailSSConfirmation2CH() {
        return icMailSSConfirmation2CH;
    }

    public String getSsAccess() {
        return ssAccess;
    }

    public String getSettlementNotificationCH() {
        return settlementNotificationCH;
    }

    public String getOrderConfirmationCustomer() {
        return orderConfirmationCustomer;
    }
}
