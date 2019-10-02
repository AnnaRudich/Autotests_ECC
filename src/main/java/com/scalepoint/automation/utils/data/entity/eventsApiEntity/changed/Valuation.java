package com.scalepoint.automation.utils.data.entity.eventsApiEntity.changed;

import lombok.Data;

@Data
public class Valuation {
    Double price;
    Boolean active;
    String type;
}
