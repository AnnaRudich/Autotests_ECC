package com.scalepoint.automation.utils.data.entity.eventsApiEntity.fraudStatus;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FraudStatus {

    String eventId;
    String eventType;
    String payloadVersion;
    String timestamp;
    @JsonProperty("case")
    CaseFraudStatus caseFraudStatus;
    String status;
}
