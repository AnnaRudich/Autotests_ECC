
package com.scalepoint.automation.utils.data.entity.eventsApiEntity.settled;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.HashMap;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "manualReduction",
        "shareOfVat",
        "deductible",
        "depreciation"
})
public class Summary {

    @JsonProperty("manualReduction")
    private Double manualReduction;

    @JsonProperty(value = "shareOfVat", required = true)
    private Double shareOfVat;

    @JsonProperty(value = "deductible", required = true)
    private Double deductible;

    @JsonProperty(value = "depreciation", required = true)
    private Double depreciation;
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
