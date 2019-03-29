package com.scalepoint.automation.utils.data.entity.eventsApiEntity.settled;

import com.fasterxml.jackson.annotation.*;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "$id",
        "uuid",
        "dueDate",
        "issueDate",
        "total",
        "currencyCode",
        "administrative",
        "payerParty",
        "payeeParty",
        "expenseType"
})

public class Expense {

    @JsonProperty(value = "$id", required = true)
    private String $id;
    @JsonProperty("uuid")
    private String uuid;
    @JsonProperty("dueDate")
    private String dueDate;
    @JsonProperty("issueDate")
    private String issueDate;

    @JsonProperty(value = "total", required = true)
    private Double total;
    /**
     * ISO 4217 alpha3. (https://www.iso.org/iso-4217-currency-codes.html)
     */
    @JsonProperty(value = "currencyCode", required = true)
    private String currencyCode;
    @JsonProperty("administrative")
    private Boolean administrative;

    @JsonProperty(value = "payerParty", required = true)
    private PartyRef payerParty;

    @JsonProperty(value = "payeeParty", required = true)
    private PartyRef payeeParty;

    @JsonProperty(value = "expenseType", required = true)
    private String expenseType;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();

    public String get$id() {
        return $id;
    }

    public void set$id(String $id) {
        this.$id = $id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
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

    public Boolean getAdministrative() {
        return administrative;
    }

    public void setAdministrative(Boolean administrative) {
        this.administrative = administrative;
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

    public String getExpenseType() {
        return expenseType;
    }

    public void setExpenseType(String expenseType) {
        this.expenseType = expenseType;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProprties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }


}