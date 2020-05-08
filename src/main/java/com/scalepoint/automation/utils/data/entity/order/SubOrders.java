package com.scalepoint.automation.utils.data.entity.order;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

@Builder
@Setter
@Getter
@ToString
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Suborders")
public class SubOrders {
    List<SubOrder> suborder = new ArrayList<>();
}
