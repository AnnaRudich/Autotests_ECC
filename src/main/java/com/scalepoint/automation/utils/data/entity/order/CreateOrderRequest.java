package com.scalepoint.automation.utils.data.entity.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "CreateOrderRequest")
@XmlAccessorType(XmlAccessType.FIELD)

@Builder
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequest {
    @XmlElement(name="Order")
    Order order;
    @XmlElement(name="Account")
    Account account;
}
