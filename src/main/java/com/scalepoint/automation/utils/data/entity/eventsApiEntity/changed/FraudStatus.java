package com.scalepoint.automation.utils.data.entity.eventsApiEntity.changed;

import lombok.Data;

@Data
public class FraudStatus {
    String uuid;
    String eventType;
    String payloadVersion;
    String timestamp;
}
