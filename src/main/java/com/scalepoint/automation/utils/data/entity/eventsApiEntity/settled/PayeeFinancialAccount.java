
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
    "id",
    "fiBranch"
})
public class PayeeFinancialAccount {

    /**
     * The identifier for the Financial Account; the Bank Account Number.
     * <p>
     * 
     * 
     */
    @JsonProperty("id")
    private String id;
    @JsonProperty("fiBranch")
    private FiBranch_ fiBranch;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * The identifier for the Financial Account; the Bank Account Number.
     * <p>
     * 
     * 
     */
    @JsonProperty("id")
    public String getId() {
        return id;
    }

    /**
     * The identifier for the Financial Account; the Bank Account Number.
     * <p>
     * 
     * 
     */
    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("fiBranch")
    public FiBranch_ getFiBranch() {
        return fiBranch;
    }

    @JsonProperty("fiBranch")
    public void setFiBranch(FiBranch_ fiBranch) {
        this.fiBranch = fiBranch;
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
