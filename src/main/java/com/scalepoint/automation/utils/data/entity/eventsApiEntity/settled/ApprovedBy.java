
package com.scalepoint.automation.utils.data.entity.eventsApiEntity.settled;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.HashMap;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "email",
        "name",
        "username",
        "type"
})
public class ApprovedBy {

    @JsonProperty("email")
    private String email;
    /**
     * (Required)
     */
    @JsonProperty(value = "name", required = true)
    private String name;

    @JsonProperty("username")
    private String username;

    /**
     * 'user', 'automatic' etc.
     * (Required)
     */
    @JsonProperty(value = "type", required = true)
    private String type;
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
