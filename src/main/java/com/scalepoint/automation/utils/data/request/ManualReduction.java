package com.scalepoint.automation.utils.data.request;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@JsonInclude(NON_NULL)
@RequiredArgsConstructor
@NoArgsConstructor
public class ManualReduction {

    @NonNull
    private long caseId;
    @NonNull
    private String manualReductionAmount;
    private boolean manualReductionFlag = true;
    private String manualReductionText = "Manual Reduction";

}
