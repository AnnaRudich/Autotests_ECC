package com.scalepoint.automation.utils.data.entity.eventsApiEntity.updated;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.HashMap;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "number",
        "token",
        "caseType"
})
public class Case {

    @JsonProperty("number")
    private String number;
    @JsonProperty("token")
    private String token;
    @JsonProperty("caseType")
    private String caseType;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

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

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(number).append(token).append(caseType).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Case) == false) {
            return false;
        }
        Case rhs = ((Case) other);
        return new EqualsBuilder().append(number, rhs.number).append(token, rhs.token).append(caseType, rhs.caseType).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
