package com.scalepoint.automation.utils.data.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MailContent {

    @JsonProperty("ReplyTo")
    private String replyTo;
    @JsonProperty("From")
    private String from;
    @JsonProperty("CaseId")
    private String caseId;
    @JsonProperty("Company")
    private String company;
    @JsonProperty("Country")
    private String country;
    @JsonProperty("EventId")
    private String eventId;
    @JsonProperty("Output")
    private String output;
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
    @JsonProperty("Addresses")
    private List<Address> addresses;

    @Data
    public static class Address {
        @JsonProperty("Address")
        private String address;
        @JsonProperty("Field")
        private String field;
        @JsonProperty("Role")
        private String role;
    }
}
