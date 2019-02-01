package com.scalepoint.automation.utils.data.entity.eventsApiEntity.updated;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.scalepoint.automation.utils.data.entity.eventsApiEntity.EventClaim;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     *
     * (Required)
     *
     */
    @JsonProperty("eventType")
    private String eventType;
    /**
     *
     * (Required)
     *
     */
    @JsonProperty("payloadVersion")
    private String payloadVersion;
    /**
     *
     * (Required)
     *
     */
    @JsonProperty("correlationId")
    private String correlationId;
    /**
     *
     * (Required)
     *
     */
    @JsonProperty("timestamp")
    private String timestamp;
    /**
     *
     * (Required)
     *
     */
    @JsonProperty("case")
    private Case _case;
    @JsonProperty("changedBy")
    private ChangedBy changedBy;
    /**
     *
     * (Required)
     *
     */
    @JsonProperty("changes")
    private List<Changes> changes = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * (Required)
     *
     */
    @JsonProperty("eventType")
    public String getEventType() {
        return eventType;
    }

    /**
     *
     * (Required)
     *
     */
    @JsonProperty("eventType")
    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    /**
     *
     * (Required)
     *
     */
    @JsonProperty("payloadVersion")
    public String getPayloadVersion() {
        return payloadVersion;
    }

    /**
     *
     * (Required)
     *
     */
    @JsonProperty("payloadVersion")
    public void setPayloadVersion(String payloadVersion) {
        this.payloadVersion = payloadVersion;
    }

    /**
     *
     * (Required)
     *
     */
    @JsonProperty("correlationId")
    public String getCorrelationId() {
        return correlationId;
    }

    /**
     *
     * (Required)
     *
     */
    @JsonProperty("correlationId")
    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    /**
     *
     * (Required)
     *
     */
    @JsonProperty("timestamp")
    public String getTimestamp() {
        return timestamp;
    }

    /**
     *
     * (Required)
     *
     */
    @JsonProperty("timestamp")
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    /**
     *
     * (Required)
     *
     */
    @JsonProperty("case")
    public Case getCase() {
        return _case;
    }

    /**
     *
     * (Required)
     *
     */
    @JsonProperty("case")
    public void setCase(Case _case) {
        this._case = _case;
    }

    @JsonProperty("changedBy")
    public ChangedBy getChangedBy() {
        return changedBy;
    }

    @JsonProperty("changedBy")
    public void setChangedBy(ChangedBy changedBy) {
        this.changedBy = changedBy;
    }

    /**
     *
     * (Required)
     *
     */
    @JsonProperty("changes")
    public List<Changes> getChanges() {
        return changes;
    }

    /**
     *
     * (Required)
     *
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
        return new HashCodeBuilder().append(eventType).append(payloadVersion).append(correlationId).append(timestamp).append(_case).append(changedBy).append(changes).append(additionalProperties).toHashCode();
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
        return new EqualsBuilder().append(eventType, rhs.eventType).append(payloadVersion, rhs.payloadVersion).append(correlationId, rhs.correlationId).append(timestamp, rhs.timestamp).append(_case, rhs._case).append(changedBy, rhs.changedBy).append(changes, rhs.changes).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
