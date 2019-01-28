
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
    "revisionToken"
})
public class Settlement {

    @JsonProperty(value = "approvedBy", required = true)
    private ApprovedBy approvedBy;

    @JsonProperty(value = "summary", required = true)
    private Summary summary;
    @JsonProperty("revisionToken")
    private String revisionToken;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();

    public ApprovedBy getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(ApprovedBy approvedBy) {
        this.approvedBy = approvedBy;
    }

    public Summary getSummary() {
        return summary;
    }

    public void setSummary(Summary summary) {
        this.summary = summary;
    }

    public String getRevisionToken() { return revisionToken; }

    public void setRevisionToken(String revisionToken) { this.revisionToken = revisionToken; }

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
