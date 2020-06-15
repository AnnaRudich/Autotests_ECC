package com.scalepoint.automation.utils.data.entity.eventsApiEntity.updated;

import com.fasterxml.jackson.annotation.*;
import com.scalepoint.automation.utils.data.entity.eventsApiEntity.EventClaim;
import lombok.Data;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "eventType",
        "payloadVersion",
        "correlationId",
        "timestamp",
        "case",
        "changedBy",
        "changes"
})
public class EventClaimUpdated extends EventClaim {

    /**
     * (Required)
     */
    @JsonProperty("eventType")
    private String eventType;
    /**
     * (Required)
     */
    @JsonProperty("payloadVersion")
    private String payloadVersion;
    /**
     * (Required)
     */
    @JsonProperty("correlationId")
    private String correlationId;
    /**
     * (Required)
     */
    @JsonProperty("timestamp")
    private String timestamp;
    /**
     * (Required)
     */
    @JsonProperty("case")
    private Case aCase;
    @JsonProperty("changedBy")
    private ChangedBy changedBy;
    /**
     * (Required)
     */
    @JsonProperty("changes")
    private List<Changes> changes = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * (Required)
     */
    @JsonProperty("changes")
    public void setChanges(List<Changes> changes) {
        this.changes = changes;
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

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(eventType).append(payloadVersion).append(correlationId).append(timestamp).append(aCase).append(changedBy).append(changes).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof EventClaimUpdated) == false) {
            return false;
        }
        EventClaimUpdated rhs = ((EventClaimUpdated) other);
        return new EqualsBuilder().append(eventType, rhs.eventType).append(payloadVersion, rhs.payloadVersion).append(correlationId, rhs.correlationId).append(timestamp, rhs.timestamp).append(aCase, rhs.aCase).append(changedBy, rhs.changedBy).append(changes, rhs.changes).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
