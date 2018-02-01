
package com.scalepoint.automation.utils.data.entity.eventsApiEntity.settled;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
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
    "settlement",
    "parties",
    "payments"
})
public class EventClaimSettled {

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
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("settlement")
    private Settlement settlement;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("parties")
    private List<Party> parties = null;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("payments")
    private List<Payment> payments = null;
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

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("settlement")
    public Settlement getSettlement() {
        return settlement;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("settlement")
    public void setSettlement(Settlement settlement) {
        this.settlement = settlement;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("parties")
    public List<Party> getParties() {
        return parties;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("parties")
    public void setParties(List<Party> parties) {
        this.parties = parties;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("payments")
    public List<Payment> getPayments() {
        return payments;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("payments")
    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public Double getTotal(){
        return getPayments().get(0).getTotal();
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
