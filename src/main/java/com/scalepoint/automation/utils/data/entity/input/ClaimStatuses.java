package com.scalepoint.automation.utils.data.entity.input;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ClaimStatuses {

    @JsonProperty("claimStatuses")
    private List<ClaimStatus> claimStatuses;

}
