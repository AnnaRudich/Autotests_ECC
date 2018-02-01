
package com.scalepoint.automation.utils.data.entity.eventsApiEntity.settled;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "manualReduction",
    "shareOfVat",
    "deductible",
    "depreciation"
})
public class Summary {

    @JsonProperty("manualReduction")
    private Double manualReduction;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("shareOfVat")
    private Double shareOfVat;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("deductible")
    private Double deductible;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("depreciation")
    private Double depreciation;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("manualReduction")
    public Double getManualReduction() {
        return manualReduction;
    }

    @JsonProperty("manualReduction")
    public void setManualReduction(Double manualReduction) {
        this.manualReduction = manualReduction;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("shareOfVat")
    public Double getShareOfVat() {
        return shareOfVat;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("shareOfVat")
    public void setShareOfVat(Double shareOfVat) {
        this.shareOfVat = shareOfVat;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("deductible")
    public Double getDeductible() {
        return deductible;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("deductible")
    public void setDeductible(Double deductible) {
        this.deductible = deductible;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("depreciation")
    public Double getDepreciation() {
        return depreciation;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("depreciation")
    public void setDepreciation(Double depreciation) {
        this.depreciation = depreciation;
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
