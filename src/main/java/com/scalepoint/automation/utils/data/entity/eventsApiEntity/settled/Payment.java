
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

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("uuid")
    private String uuid;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("dueDate")
    private String dueDate;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("total")
    private Double total;
    /**
     * ISO 4217 alpha3. (https://www.iso.org/iso-4217-currency-codes.html)
     * (Required)
     * 
     */
    @JsonProperty("currencyCode")
    private String currencyCode;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("payerParty")
    private PayerParty payerParty;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("payeeParty")
    private PayeeParty payeeParty;
    @JsonProperty("paymentMeans")
    private PaymentMeans paymentMeans;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("uuid")
    public String getUuid() {
        return uuid;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("uuid")
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("dueDate")
    public String getDueDate() {
        return dueDate;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("dueDate")
    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("total")
    public Double getTotal() {
        return total;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("total")
    public void setTotal(Double total) {
        this.total = total;
    }

    /**
     * ISO 4217 alpha3. (https://www.iso.org/iso-4217-currency-codes.html)
     * (Required)
     * 
     */
    @JsonProperty("currencyCode")
    public String getCurrencyCode() {
        return currencyCode;
    }

    /**
     * ISO 4217 alpha3. (https://www.iso.org/iso-4217-currency-codes.html)
     * (Required)
     * 
     */
    @JsonProperty("currencyCode")
    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("payerParty")
    public PayerParty getPayerParty() {
        return payerParty;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("payerParty")
    public void setPayerParty(PayerParty payerParty) {
        this.payerParty = payerParty;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("payeeParty")
    public PayeeParty getPayeeParty() {
        return payeeParty;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("payeeParty")
    public void setPayeeParty(PayeeParty payeeParty) {
        this.payeeParty = payeeParty;
    }

    @JsonProperty("paymentMeans")
    public PaymentMeans getPaymentMeans() {
        return paymentMeans;
    }

    @JsonProperty("paymentMeans")
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
