package com.scalepoint.automation.utils.data.entity.rnv.sendToRepairAndValuation;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SelectedLinesAndCategories{

    private String itemId;
    private int claimLineId;
    private int pseudocatId;
    private String description;
    private boolean isIsRvRepairTaskSentOrApproved;

}
