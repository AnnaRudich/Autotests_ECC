package com.scalepoint.automation.utils.data.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SelfServiceInitData {

    String success;
    @JsonProperty(value = "data")
    SelfServiceInitDataData data;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SelfServiceInitDataData{

        @JsonProperty(value = "loss")
        SelfServiceLoss selfServiceLoss;
    }
}
