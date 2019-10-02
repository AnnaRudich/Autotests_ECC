package com.scalepoint.automation.utils.data.entity.eventsApiEntity.fraudStatus;

import lombok.Data;

@Data
public class CaseClaimLineChanged {
    String uuid;
    String number;
    String token;
    String caseType;
    String externalClaimId;
    String company;
}
