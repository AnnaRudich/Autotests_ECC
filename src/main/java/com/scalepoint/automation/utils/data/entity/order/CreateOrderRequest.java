package com.scalepoint.automation.utils.data.entity.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "CreateOrderRequest")
@XmlAccessorType(XmlAccessType.FIELD)

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequest {

    @XmlElement(name="Order")
    Order order;
    @XmlElement(name="Account")
    Account account;

}
