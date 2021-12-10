package com.scalepoint.automation.utils.data.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MailListItem {
    @JsonProperty("Token")
    private String token;
    @JsonProperty("Subject")
    private String subject;
    @JsonProperty("Type")
    private String type;
    @JsonProperty("EventType")
    private String eventType;
    @JsonProperty("Status")
    private int status;
    @JsonProperty("Date")
    private String date;
}
