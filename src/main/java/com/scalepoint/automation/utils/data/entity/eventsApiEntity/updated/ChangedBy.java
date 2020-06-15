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
        "fullName",
        "username",
        "userType"
})
public class ChangedBy {

    @JsonProperty("fullName")
    private String fullName;
    @JsonProperty("username")
    private String username;
    @JsonProperty("userType")
    private String userType;
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
        return new HashCodeBuilder().append(fullName).append(username).append(userType).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ChangedBy) == false) {
            return false;
        }
        ChangedBy rhs = ((ChangedBy) other);
        return new EqualsBuilder().append(fullName, rhs.fullName).append(username, rhs.username).append(userType, rhs.userType).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
