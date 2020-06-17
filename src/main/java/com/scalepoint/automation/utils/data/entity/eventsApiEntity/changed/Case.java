package com.scalepoint.automation.utils.data.entity.eventsApiEntity.changed;

import lombok.Data;

@Data
public class Case {

    private String token;
    private String tenant;
    private String country;
    private String caseType;
    private String caseNumber;
    private Object policy;
    private Customer customer;
    private Loss loss;

}
