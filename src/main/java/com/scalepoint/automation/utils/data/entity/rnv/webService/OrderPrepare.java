package com.scalepoint.automation.utils.data.entity.rnv.webService;

import com.scalepoint.automation.utils.data.entity.rnv.serviceTask.Claim;
import com.scalepoint.automation.utils.data.entity.rnv.serviceTask.Claimant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderPrepare {

    private Claim claim;
    private Claimant claimant;
    private List<ServiceLine> serviceLines;
}
