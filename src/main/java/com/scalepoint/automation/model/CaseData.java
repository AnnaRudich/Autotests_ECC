package com.scalepoint.automation.model;

import lombok.Data;

@Data
public class CaseData {
    String token;
    String tenant;
    String country;
    String caseType;
    String caseNumber;
    Object policy;
    Customer customer;
    Loss loss;
}
