package com.scalepoint.automation.utils.data.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ClaimState {

    @JsonProperty("state")
    private Character state;
    @JsonProperty("name")
    private String name;

    public char getState() {
        return state;
    }

    public String getName() {
        return name;
    }
}
