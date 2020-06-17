package com.scalepoint.automation.utils.data.entity.eventsApiEntity.fraudStatus;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FraudStatus {

    private String eventId;
    private String eventType;
    private String payloadVersion;
    private String timestamp;
    @JsonProperty("case")
    private CaseFraudStatus caseFraudStatus;
    private String status;

}
