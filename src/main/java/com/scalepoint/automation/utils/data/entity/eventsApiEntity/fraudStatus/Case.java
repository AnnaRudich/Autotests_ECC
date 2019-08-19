package com.scalepoint.automation.utils.data.entity.eventsApiEntity.fraudStatus;

import lombok.Data;

@Data
public class Case {
    String uuid;
    String number;
    String token;
    String caseType;
}
