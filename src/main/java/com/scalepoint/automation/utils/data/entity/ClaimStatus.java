package com.scalepoint.automation.utils.data.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ClaimStatus {

    @JsonProperty("status")
    private String status;
    @JsonProperty("name")
    private String name;

    public String getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }
}
