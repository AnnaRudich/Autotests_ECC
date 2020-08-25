package com.scalepoint.automation.utils.data.entity.getBalance;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@XmlAccessorType(XmlAccessType.FIELD)

@XmlRootElement(name = "GetBalanceResponse")
public class GetBalanceResponse {

    @XmlAttribute(name = "accountID")
    String accountId;

    @XmlAttribute
    String balance;

    @XmlAttribute
    String deposits;

    @XmlAttribute
    String withdrawals;

    @XmlAttribute
    String completedOrders;

    @XmlAttribute
    String indemnityCashAmount;

    @XmlAttribute
    String tradeUpLimit;

    @XmlAttribute(name = "Status")
    String status;

    @XmlAttribute
    String hasAccess;
}
