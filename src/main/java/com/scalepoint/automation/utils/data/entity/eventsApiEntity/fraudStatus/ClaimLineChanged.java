package com.scalepoint.automation.utils.data.entity.eventsApiEntity.fraudStatus;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.scalepoint.automation.utils.data.entity.eventsApiEntity.EventClaim;
import lombok.Data;

@Data
public class ClaimLineChanged extends EventClaim {

    private String eventId;
    private String eventType;
    private String payloadVersion;
    private String correlationId;
    private String timestamp;
    @JsonProperty("case")
    private CaseClaimLineChanged caseClaimLineChanged;

}
