
package com.scalepoint.automation.utils.data.entity.eventsApiEntity.settled;

import com.fasterxml.jackson.annotation.*;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.HashMap;
import java.util.Map;

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

    @JsonProperty(value = "shareOfVat", required = true)
    private Double shareOfVat;

    @JsonProperty(value = "deductible", required = true)
    private Double deductible;

    @JsonProperty(value = "depreciation", required = true)
    private Double depreciation;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();

    public Double getManualReduction() {
        return manualReduction;
    }

    public void setManualReduction(Double manualReduction) {
        this.manualReduction = manualReduction;
    }

    public Double getShareOfVat() {
        return shareOfVat;
    }

    public void setShareOfVat(Double shareOfVat) {
        this.shareOfVat = shareOfVat;
    }

    public Double getDeductible() {
        return deductible;
    }

    public void setDeductible(Double deductible) {
        this.deductible = deductible;
    }

    public Double getDepreciation() {
        return depreciation;
    }

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
