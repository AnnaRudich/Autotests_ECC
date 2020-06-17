package com.scalepoint.automation.utils.data.entity.eventsApiEntity.settled;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.HashMap;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "$id",
        "obligationType",
        "total",
        "currencyCode",
        "payerParty",
        "payeeParty",
        "expense"
})
public class Obligation {

    /**
     * JSON object reference id
     */
    @JsonProperty(value = "$id", required = true)
    private String $id;

    @JsonProperty(value = "obligationType", required = true)
    private String obligationType;

    @JsonProperty(value = "total", required = true)
    private Double total;
    /**
     * ISO 4217 alpha3. (https://www.iso.org/iso-4217-currency-codes.html)
     */
    @JsonProperty(value = "currencyCode", required = true)
    private String currencyCode;

    @JsonProperty(value = "payerParty", required = true)
    private PartyRef payerParty;

    @JsonProperty(value = "payeeParty", required = true)
    private PartyRef payeeParty;
    @JsonProperty("expense")
    private Expense expense;

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

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }


}

