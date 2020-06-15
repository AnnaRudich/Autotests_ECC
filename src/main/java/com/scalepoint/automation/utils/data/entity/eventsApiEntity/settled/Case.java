
package com.scalepoint.automation.utils.data.entity.eventsApiEntity.settled;

import com.fasterxml.jackson.annotation.*;
import com.scalepoint.automation.utils.annotations.RunOn;
import lombok.Data;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.HashMap;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "uuid",
        "token",
        "number"
})
public class Case {

    @JsonProperty(value = "uuid", required = true)
    private String uuid;

    @JsonProperty(value = "token", required = true)
    private String token;

    @JsonProperty(value = "number", required = true)
    private String number;

    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
