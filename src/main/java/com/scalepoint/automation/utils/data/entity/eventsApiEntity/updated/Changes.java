package com.scalepoint.automation.utils.data.entity.eventsApiEntity.updated;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.HashMap;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "property",
        "shouldBeClosedInOwnSystem"
})
public class Changes {

    @JsonProperty("property")
    private Property property;

    @JsonProperty("shouldBeClosedInOwnSystem")
    private boolean shouldBeClosedInOwnSystem;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public enum Property {

        CASE_CLOSED("CaseClosed"),
        CASE_REOPENED("CaseReopened");

        private final String value;
        private final static Map<String, Changes.Property> CONSTANTS = new HashMap<>();

        static {
            for (Changes.Property c : values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private Property(String value) {
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
        public static Changes.Property fromValue(String value) {
            Changes.Property constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }
    }
}