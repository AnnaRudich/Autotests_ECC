package com.scalepoint.automation.utils.data.entity.rnv.sendToRepairAndValuation;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SelectedLinesAndCategories{

    String itemId;
    int claimLineId;
    int pseudocatId;
    String description;
    boolean isIsRvRepairTaskSentOrApproved;
}
