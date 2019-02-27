package com.scalepoint.automation.utils.data.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ClaimStatuses {

    @JsonProperty("claimStatuses")
    private List<ClaimStatus> claimStatuses;

    public List<ClaimStatus> getClaimStatuses() {
        return claimStatuses;
    }
}
