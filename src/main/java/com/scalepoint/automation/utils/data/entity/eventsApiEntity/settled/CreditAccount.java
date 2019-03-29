
package com.scalepoint.automation.utils.data.entity.eventsApiEntity.settled;

import com.fasterxml.jackson.annotation.*;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "accountId"
})
public class CreditAccount {

    /**
     * AccountID is used to identify the account ID on payment forms with the paymentId FIK71,FIK73,FIK75. The accountId is always 8 numeric characters.
     * (Required)
     */
    @JsonProperty(value = "accountId", required = true)
    private String accountId;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
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
