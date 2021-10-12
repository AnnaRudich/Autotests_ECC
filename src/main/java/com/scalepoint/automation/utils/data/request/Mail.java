package com.scalepoint.automation.utils.data.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Mail {

    @JsonProperty("Source")
    private String source;
    @JsonProperty("Transformation")
    private String transformation;
    @JsonProperty("CountryCode")
    private String countryCode;
    @JsonProperty("CompanyCode")
    private String companyCode;
    @JsonProperty("From")
    private String from;
    @JsonProperty("To")
    private String to;
    @JsonProperty("Bcc")
    private String bcc;
    @JsonProperty("ReplyTo")
    private String replyTo;
    @JsonProperty("Subject")
    private String subject;
    @JsonProperty("EventType")
    private String eventType;
    @JsonProperty("EventId")
    private String eventId;
    @JsonProperty("MailType")
    private String mailType;
    @JsonProperty("CaseId")
    private String caseId;
    @JsonProperty("DebugText")
    private String debugText;
    @JsonProperty("Department")
    private String department;
    @JsonProperty("ClaimNumber")
    private String claimNumber;
    @JsonProperty("Attachments")
    private List<AttachmentInfo> attachments;
}
