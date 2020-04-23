package com.scalepoint.automation.utils.data.entity.rnv.sendToRepairAndValuation;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class SendToRepairAndValuation{
    List<String> selectedGroups;
    List<String> selectedLines;
    List<SelectedLinesAndCategories> selectedLinesAndCategories;
}

