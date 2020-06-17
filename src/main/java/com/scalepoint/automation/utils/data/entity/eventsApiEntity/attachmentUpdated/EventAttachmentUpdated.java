
package com.scalepoint.automation.utils.data.entity.eventsApiEntity.attachmentUpdated;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.scalepoint.automation.utils.data.entity.eventsApiEntity.EventClaim;
import com.scalepoint.automation.utils.data.entity.eventsApiEntity.settled.Case;
import lombok.Data;

import java.util.List;

@Data
public class EventAttachmentUpdated extends EventClaim {

    private String eventId;
    private List<Change> changes;
    private String eventType;
    private String payloadVersion;
    private String correlationId;
    private String timestamp;
    @JsonProperty("case")
    private Case aCase;

}
