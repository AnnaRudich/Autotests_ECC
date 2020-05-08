package com.scalepoint.automation.utils.data.entity.order;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

@Builder
@Setter
@Getter
@ToString
@XmlType(name = "Deposits")
public class Deposits {

    Double depositsTotal;
    List<Deposit> deposit = new ArrayList<>();
}
