package com.scalepoint.automation.utils.data.entity.rnv.webService;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    private Claimant claimant;
    private Claim claim;
    private List<ServiceLine> serviceLines;
    private String creationType;
    private boolean someAgreementsNotFound;
}
