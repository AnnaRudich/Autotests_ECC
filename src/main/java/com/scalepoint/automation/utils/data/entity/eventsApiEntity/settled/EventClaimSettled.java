
package com.scalepoint.automation.utils.data.entity.eventsApiEntity.settled;

import com.fasterxml.jackson.annotation.*;
import com.scalepoint.automation.utils.data.entity.eventsApiEntity.EventClaim;
import lombok.Data;
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
    private Case aCase;

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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public Double getTotal() {
        return getPayments().get(0).getTotal();
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
