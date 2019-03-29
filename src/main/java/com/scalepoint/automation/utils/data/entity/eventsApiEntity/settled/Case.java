
package com.scalepoint.automation.utils.data.entity.eventsApiEntity.settled;

import com.fasterxml.jackson.annotation.*;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "uuid",
        "token",
        "number"
})
public class Case {

    @JsonProperty(value = "uuid", required = true)
    private String uuid;

    @JsonProperty(value = "token", required = true)
    private String token;

    @JsonProperty(value = "number", required = true)
    private String number;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
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
