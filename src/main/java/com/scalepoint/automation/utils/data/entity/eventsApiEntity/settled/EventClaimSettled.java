
package com.scalepoint.automation.utils.data.entity.eventsApiEntity.settled;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.scalepoint.automation.utils.data.entity.eventsApiEntity.EventClaim;
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
    "expenses",
    "payments",
    "obligations"
})
public class EventClaimSettled extends EventClaim {

    @JsonProperty(value = "eventType", required = true)
    private String eventType;

    @JsonProperty(value = "payloadVersion", required = true)
    private String payloadVersion;

    @JsonProperty(value = "correlationId", required = true)
    private String correlationId;

    @JsonProperty(value = "timestamp", required = true)
    private String timestamp;

    @JsonProperty(value = "case", required = true)
    private Case _case;

    @JsonProperty(value = "settlement", required = true)
    private Settlement settlement;

    @JsonProperty(value = "parties", required = true)
    private List<Party> parties = null;

    @JsonProperty(value = "expenses", required = true)
    private List<Expense> expenses = null;

    @JsonProperty(value = "payments", required = true)
    private List<Payment> payments = null;

    @JsonProperty("obligations")
    private List<Obligation> obligations = null;

    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getPayloadVersion() {
        return payloadVersion;
    }

    public void setPayloadVersion(String payloadVersion) {
        this.payloadVersion = payloadVersion;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Case getCase() {
        return _case;
    }

    public void setCase(Case _case) {
        this._case = _case;
    }

    public Settlement getSettlement() {
        return settlement;
    }

    public void setSettlement(Settlement settlement) {
        this.settlement = settlement;
    }

    public List<Party> getParties() {
        return parties;
    }

    public void setParties(List<Party> parties) {
        this.parties = parties;
    }

    public List<Expense> getExpenses() {
        return expenses;
    }

    public void setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
    }

    public List<Payment> getPayments() { return payments; }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    public List<Obligation> getObligations() { return obligations; }

    public void setObligations(List<Obligation> obligations) {
        this.obligations = obligations;
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
