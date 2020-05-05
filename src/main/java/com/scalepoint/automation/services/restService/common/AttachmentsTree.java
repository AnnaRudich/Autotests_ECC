package com.scalepoint.automation.services.restService.common;

import lombok.Data;

import java.util.List;

@Data
public class AttachmentsTree {

    Integer id;
    String text;
    List<AttachmentsTree> children;
    boolean hasClaimLineNotes;
    boolean hasServicePartnerNotes;
    boolean hasCustomerNotes;
    boolean leaf;
    Integer claimLineId;
    boolean expanded;
    boolean hasAttachments;
    boolean hasGroups;
}
