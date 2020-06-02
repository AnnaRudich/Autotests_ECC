package com.scalepoint.automation.services.restService.common;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AttachmentsPayload {

    String name;
    String type;
    String level;
    String coveredIds;
    Integer matchId;
    String guid;
    String id;
}
