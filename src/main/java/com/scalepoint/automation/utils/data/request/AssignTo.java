package com.scalepoint.automation.utils.data.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.builder.ToStringBuilder;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "role",
        "email"
})
public class AssignTo {

    @JsonProperty("role")
    private String role;
    @JsonProperty("email")
    private String email;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
