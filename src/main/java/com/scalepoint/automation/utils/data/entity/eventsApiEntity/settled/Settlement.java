
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
    "approvedBy",
    "summary",
    "revisionId"
})
public class Settlement {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("approvedBy")
    private ApprovedBy approvedBy;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("summary")
    private Summary summary;
    @JsonProperty("revisionId")
    private Integer revisionId;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("approvedBy")
    public ApprovedBy getApprovedBy() {
        return approvedBy;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("approvedBy")
    public void setApprovedBy(ApprovedBy approvedBy) {
        this.approvedBy = approvedBy;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("summary")
    public Summary getSummary() {
        return summary;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("summary")
    public void setSummary(Summary summary) {
        this.summary = summary;
    }

    @JsonProperty("revisionId")
    public Integer getRevisionId() {
        return revisionId;
    }

    @JsonProperty("revisionId")
    public void setRevisionId(Integer revisionId) {
        this.revisionId = revisionId;
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
