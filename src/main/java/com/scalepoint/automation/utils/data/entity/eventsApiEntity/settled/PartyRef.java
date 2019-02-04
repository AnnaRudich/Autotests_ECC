
package com.scalepoint.automation.utils.data.entity.eventsApiEntity.settled;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.ToString;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.HashMap;
import java.util.Map;

@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "$ref"
})
public class PartyRef {

    /**
     * Reference id to object. Defined in object by $id.
     * 
     */
    @JsonProperty("$ref")
    private String $ref;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();

    /**
     * Reference id to object. Defined in object by $id.
     * 
     */
    public String get$ref() {
        return $ref;
    }

    /**
     * Reference id to object. Defined in object by $id.
     * 
     */
    public void set$ref(String $ref) {
        this.$ref = $ref;
    }

    @Override
    public String toString() {
        return "PartyRef{" +
                "$ref='" + $ref + '\'' +
                '}';
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
