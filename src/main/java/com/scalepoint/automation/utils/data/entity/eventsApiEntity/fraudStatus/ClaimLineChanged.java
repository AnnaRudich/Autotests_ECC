package com.scalepoint.automation.utils.data.entity.eventsApiEntity.fraudStatus;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.scalepoint.automation.utils.data.entity.eventsApiEntity.EventClaim;
import lombok.Data;

@Data
public class ClaimLineChanged extends EventClaim {
    String eventId;
    String eventType;
    String payloadVersion;
    String correlationId;
    String timestamp;
    @JsonProperty("case")
    CaseClaimLineChanged caseClaimLineChanged;
}
