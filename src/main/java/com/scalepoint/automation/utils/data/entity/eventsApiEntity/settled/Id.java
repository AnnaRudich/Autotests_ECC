
package com.scalepoint.automation.utils.data.entity.eventsApiEntity.settled;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.HashMap;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "schemeId",
        "value"
})
public class Id {

    @JsonProperty(value = "schemeId", required = true)
    private Id.SchemeId schemeId;

    @JsonProperty(value = "value", required = true)
    private String value;
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

    public enum SchemeId {

        DK_CVR("DK:CVR"),
        DK_CPR("DK:CPR");
        private final String value;
        private final static Map<String, Id.SchemeId> CONSTANTS = new HashMap<String, Id.SchemeId>();

        static {
            for (Id.SchemeId c : values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private SchemeId(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        @JsonValue
        public String value() {
            return this.value;
        }

        @JsonCreator
        public static Id.SchemeId fromValue(String value) {
            Id.SchemeId constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
