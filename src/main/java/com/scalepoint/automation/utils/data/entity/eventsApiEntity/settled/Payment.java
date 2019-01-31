
package com.scalepoint.automation.utils.data.entity.eventsApiEntity.settled;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "uuid",
    "dueDate",
    "total",
    "currencyCode",
    "payerParty",
    "payeeParty",
    "paymentMeans"
})
public class Payment {

    @JsonProperty(value = "uuid", required = true)
    private String uuid;

    @JsonProperty(value = "dueDate", required = true)
    private String dueDate;

    @JsonProperty(value = "total", required = true)
    private Double total;
    /**
     * ISO 4217 alpha3. (https://www.iso.org/iso-4217-currency-codes.html)
     * 
     */
    @JsonProperty(value = "currencyCode", required = true)
    private String currencyCode;

    @JsonProperty(value = "payerParty", required = true)
    private PartyRef payerParty;

    @JsonProperty(value = "payeeParty", required = true)
    private PartyRef payeeParty;
    @JsonProperty("paymentMeans")
    private PaymentMeans paymentMeans;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();

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

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    /**
     * ISO 4217 alpha3. (https://www.iso.org/iso-4217-currency-codes.html)
     * (Required)
     * 
     */
    public String getCurrencyCode() {
        return currencyCode;
    }

    /**
     * ISO 4217 alpha3. (https://www.iso.org/iso-4217-currency-codes.html)
     * (Required)
     * 
     */
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

    public PaymentMeans getPaymentMeans() {
        return paymentMeans;
    }

    public void setPaymentMeans(PaymentMeans paymentMeans) {
        this.paymentMeans = paymentMeans;
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
