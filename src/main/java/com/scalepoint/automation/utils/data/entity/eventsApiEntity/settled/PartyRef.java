
package com.scalepoint.automation.utils.data.entity.eventsApiEntity.settled;

import com.fasterxml.jackson.annotation.*;
import com.scalepoint.automation.tests.api.unifiedpayments.v3.BaseUnifiedPaymentsApiTest;
import lombok.Data;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "$ref"
})

public class PartyRef {

    /**
     * Reference id to object. Defined in object by $id.
     */
    @JsonProperty("$ref")
    private String ref;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();

    @Override
    public String toString() {
        return BaseUnifiedPaymentsApiTest.PartyReference.getByValue(Integer.parseInt(ref.replaceAll("\\D+", ""))).name();
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
