package com.scalepoint.automation.utils.data.entity.eventsApiEntity.changed;

import lombok.Data;

@Data
public class Case {
    String token;
    String tenant;
    String country;
    String caseType;
    String caseNumber;
    Object policy;
    Customer customer;
    Loss loss;
}
