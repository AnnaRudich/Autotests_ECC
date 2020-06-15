package com.scalepoint.automation.utils.data.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ClaimStatus {

    @JsonProperty("status")
    private String status;
    @JsonProperty("name")
    private String name;

}
