package com.scalepoint.automation.utils.data.entity.eventsApiEntity.fraudStatus;

import lombok.Data;

@Data
public class CaseFraudStatus {

    private String uuid;
    private String number;
    private String token;
    private String caseType;

}
