package com.scalepoint.automation.utils.data.entity.eventsApiEntity.settled;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.HashMap;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "fiBranch",
        "paymentNote",
        "paymentDescription"
})
public class FinancialAccount {

    /**
     * The identifier for the Financial Account; the Bank Account Number
     */
    @JsonProperty("id")
    private String id;
    @JsonProperty("fiBranch")
    private FiBranch fiBranch;
    @JsonProperty("paymentNote")
    private String paymentNote;
    @JsonProperty("paymentDescription")
    private String paymentDescription;
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