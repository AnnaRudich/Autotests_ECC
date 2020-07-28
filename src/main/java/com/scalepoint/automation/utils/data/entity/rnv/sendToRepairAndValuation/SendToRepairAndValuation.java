package com.scalepoint.automation.utils.data.entity.rnv.sendToRepairAndValuation;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class SendToRepairAndValuation{

    private List<String> selectedGroups;
    private List<String> selectedLines;
    private List<SelectedLinesAndCategories> selectedLinesAndCategories;
}

