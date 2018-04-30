package com.scalepoint.automation.utils.data.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ClaimStates {

    @JsonProperty("claimStates")
    private List<ClaimState> claimStates;

    public List<ClaimState> getClaimStates() {
        return claimStates;
    }
}
