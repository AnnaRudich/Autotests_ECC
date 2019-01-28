
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
    "id",
    "fiBranch"
})
public class PayerFinancialAccount {

    /**
     * The identifier for the Financial Account; the Bank Account Number.
     * <p>
     * 
     * 
     */
    @JsonProperty("id")
    private String id;
    @JsonProperty("fiBranch")
    private FiBranch fiBranch;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();

    /**
     * The identifier for the Financial Account; the Bank Account Number.
     * <p>
     * 
     * 
     */
    public String getId() {
        return id;
    }

    /**
     * The identifier for the Financial Account; the Bank Account Number.
     * <p>
     * 
     * 
     */
    public void setId(String id) {
        this.id = id;
    }

    public FiBranch getFiBranch() {
        return fiBranch;
    }

    public void setFiBranch(FiBranch fiBranch) {
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
