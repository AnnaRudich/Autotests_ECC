package com.scalepoint.automation.services.restService.common;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AttachmentsMapPayload {

    String matchIdToMap;
    List<AttachmentsPayload> attachments;
}
