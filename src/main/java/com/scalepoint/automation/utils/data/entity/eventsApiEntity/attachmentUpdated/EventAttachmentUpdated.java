
package com.scalepoint.automation.utils.data.entity.eventsApiEntity.attachmentUpdated;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.scalepoint.automation.utils.data.entity.eventsApiEntity.EventClaim;
import com.scalepoint.automation.utils.data.entity.eventsApiEntity.settled.Case;
import lombok.Data;

import java.util.List;

@Data
public class EventAttachmentUpdated extends EventClaim {

    String eventId;
    List<Change> changes;
    String eventType;
    String payloadVersion;
    String correlationId;
    String timestamp;
    @JsonProperty("case")
    Case aCase;
}
