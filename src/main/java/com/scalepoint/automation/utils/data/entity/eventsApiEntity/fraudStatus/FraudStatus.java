package com.scalepoint.automation.utils.data.entity.eventsApiEntity.fraudStatus;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FraudStatus {

    String uuid;
    String eventType;
    String payloadVersion;
    String timestamp;
    @JsonProperty("case")
    Case caseChanged;
    String status;
}
