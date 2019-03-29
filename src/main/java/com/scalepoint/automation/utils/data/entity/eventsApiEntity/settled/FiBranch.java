
package com.scalepoint.automation.utils.data.entity.eventsApiEntity.settled;

import com.fasterxml.jackson.annotation.*;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id"
})
public class FiBranch {

    /**
     * For domestic bank transfer it is 4 digits bank branch number.
     */
    @JsonProperty("id")
    private String id;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();

    /**
     * For domestic bank transfer it is 4 digits bank branch number.
     */
    public String getId() {
        return id;
    }

    /**
     * For domestic bank transfer it is 4 digits bank branch number.
     */
    public void setId(String id) {
        this.id = id;
    }

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
