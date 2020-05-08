package com.scalepoint.automation.utils.data.entity.order;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlType;

@Builder
@Setter
@Getter
@ToString
@XmlType(name = "OrderedItem")
public class OrderedItem {
    Product product;
}
