package com.scalepoint.automation.utils.data.request;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.HashMap;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "street1",
        "postalCode",
        "city"
})
public class Address {

    @JsonProperty("street1")
    private String street1;
    @JsonProperty("postalCode")
    private String postalCode;
    @JsonProperty("city")
    private String city;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
