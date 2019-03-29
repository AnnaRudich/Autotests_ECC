package com.scalepoint.automation.utils.data.entity.eventsApiEntity.settled;

import com.fasterxml.jackson.annotation.*;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.HashMap;
import java.util.Map;

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

    public String get$id() {
        return $id;
    }

    public void set$id(String $id) {
        this.$id = $id;
    }

    public String getObligationType() {
        return obligationType;
    }

    public void setObligationType(String obligationType) {
        this.obligationType = obligationType;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public PartyRef getPayerParty() {
        return payerParty;
    }

    public void setPayerParty(PartyRef payerParty) {
        this.payerParty = payerParty;
    }

    public PartyRef getPayeeParty() {
        return payeeParty;
    }

    public void setPayeeParty(PartyRef payeeParty) {
        this.payeeParty = payeeParty;
    }

    public Expense getExpense() {
        return expense;
    }

    public void setExpense(Expense expense) {
        this.expense = expense;
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

