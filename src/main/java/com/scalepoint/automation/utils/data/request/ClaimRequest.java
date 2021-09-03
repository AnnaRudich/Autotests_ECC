package com.scalepoint.automation.utils.data.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "tenant",
        "company",
        "country",
        "language",
        "caseType",
        "caseNumber",
        "checkForFraud",
        "accidentDate",
        "itemizationCaseReference",
        "externalReference",
        "allowAutoClose",
        "policy",
        "customer",
        "extraModifiers"
})
public class ClaimRequest {

    @JsonProperty("tenant")
    private String tenant;
    @JsonProperty("company")
    private String company;
    @JsonProperty("country")
    private String country;
    @JsonProperty("checkForFraud")
    private boolean checkForFraud;
    @JsonProperty("integrationOptions")
    private IntegrationOptions integrationOptions;
    @JsonProperty("language")
    private String language;
    @JsonProperty("caseType")
    private String caseType;
    @JsonProperty("caseNumber")
    private String caseNumber = UUID.randomUUID().toString();
    @JsonProperty("accidentDate")
    private String accidentDate;
    @JsonProperty("itemizationCaseReference")
    private String itemizationCaseReference;
    @JsonProperty("externalReference")
    private String externalReference;
    @JsonProperty("allowAutoClose")
    private Boolean allowAutoClose;
    @JsonProperty("policy")
    private Policy policy;
    @JsonProperty("customer")
    private Customer customer;
    @JsonProperty("extraModifiers")
    private List<ExtraModifier> extraModifiers = null;
    @JsonProperty("assignTo")
    private List<AssignTo> assignTo;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();

}
