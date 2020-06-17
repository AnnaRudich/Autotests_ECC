package com.scalepoint.automation.utils.data.request;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.HashMap;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "number"
})
public class Policy {

    @JsonProperty("number")
    private String number;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

}
