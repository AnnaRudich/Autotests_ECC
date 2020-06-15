
package com.scalepoint.automation.utils.data.entity.eventsApiEntity.settled;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.HashMap;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "street",
        "street2",
        "city",
        "postalCode",
        "countryCode"
})
public class Address {

    @JsonProperty("street")
    private String street;
    @JsonProperty("street2")
    private String street2;
    @JsonProperty("city")
    private String city;
    @JsonProperty("postalCode")
    private String postalCode;
    /**
     * ISO 3166-1 alpha 2 (https://en.wikipedia.org/wiki/ISO_3166-1_alpha-2)
     */
    @JsonProperty("countryCode")
    private String countryCode;
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
