package com.scalepoint.automation.utils.data.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SelfServiceLoss {

    String customerComment;
    @JsonProperty(value = "isAccepted")
    boolean isAccepted;
    @JsonProperty(value = "isLocked")
    boolean isLocked;
    @JsonProperty(value = "items")
    List<SelfServiceLossItems> selfServiceLossItemsList;
}
