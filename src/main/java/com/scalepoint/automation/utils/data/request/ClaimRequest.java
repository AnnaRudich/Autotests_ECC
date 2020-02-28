package com.scalepoint.automation.utils.data.request;

import com.fasterxml.jackson.annotation.*;
import lombok.Getter;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
    @Getter
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
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("tenant")
    public String getTenant() {
        return tenant;
    }

    @JsonProperty("tenant")
    public void setTenant(String tenant) {
        this.tenant = tenant;
    }

    public ClaimRequest withTenant(String tenant) {
        this.tenant = tenant;
        return this;
    }

    @JsonProperty("company")
    public String getCompany() {
        return company;
    }

    @JsonProperty("company")
    public void setCompany(String company) {
        this.company = company;
    }

    public ClaimRequest withCompany(String company) {
        this.company = company;
        return this;
    }

    @JsonProperty("integrationOptions")
    public IntegrationOptions getIntegrationOptions() {
        return integrationOptions;
    }

    @JsonProperty("integrationOptions")
    public void setIntegrationOptions(IntegrationOptions integrationOptions) {
        this.integrationOptions = integrationOptions;
    }

    public ClaimRequest withCountry(String country) {
        this.country = country;
        return this;
    }

    @JsonProperty("checkForFraud")
    public boolean getCheckForFraud() {
        return checkForFraud;
    }

    @JsonProperty("checkForFraud")
    public void setCheckForFraud(boolean checkForFraud) {
        this.checkForFraud = checkForFraud;
    }

    public ClaimRequest withCheckForFraud(boolean checkForFraud) {
        this.checkForFraud = checkForFraud;
        return this;
    }

    @JsonProperty("language")
    public String getLanguage() {
        return language;
    }

    @JsonProperty("language")
    public void setLanguage(String language) {
        this.language = language;
    }

    public ClaimRequest withLanguage(String language) {
        this.language = language;
        return this;
    }

    @JsonProperty("caseType")
    public String getCaseType() {
        return caseType;
    }

    @JsonProperty("caseType")
    public void setCaseType(String caseType) {
        this.caseType = caseType;
    }

    public ClaimRequest withCaseType(String caseType) {
        this.caseType = caseType;
        return this;
    }

    @JsonProperty("caseNumber")
    public String getCaseNumber() {
        return caseNumber;
    }

    @JsonProperty("caseNumber")
    public void setCaseNumber(String caseNumber) {
        this.caseNumber = caseNumber;
    }

    public ClaimRequest withCaseNumber(String caseNumber) {
        this.caseNumber = caseNumber;
        return this;
    }

    @JsonProperty("accidentDate")
    public String getAccidentDate() {
        return accidentDate;
    }

    @JsonProperty("accidentDate")
    public void setAccidentDate(String accidentDate) {
        this.accidentDate = accidentDate;
    }

    public ClaimRequest withDamageDate(String damageDate) {
        this.accidentDate = damageDate;
        return this;
    }

    @JsonProperty("itemizationCaseReference")
    public String getItemizationCaseReference() {
        return itemizationCaseReference;
    }

    @JsonProperty("itemizationCaseReference")
    public void setItemizationCaseReference(String itemizationCaseReference) {
        this.itemizationCaseReference = itemizationCaseReference;
    }

    public ClaimRequest withItemizationCaseReference(String itemizationCaseReference) {
        this.itemizationCaseReference = itemizationCaseReference;
        return this;
    }

    @JsonProperty("externalReference")
    public String getExternalReference() {
        return externalReference;
    }

    @JsonProperty("externalReference")
    public void setExternalReference(String externalReference) {
        this.externalReference = externalReference;
    }

    public ClaimRequest withExternalReference(String externalReference) {
        this.externalReference = externalReference;
        return this;
    }

    @JsonProperty("allowAutoClose")
    public Boolean getAllowAutoClose() {
        return allowAutoClose;
    }

    @JsonProperty("allowAutoClose")
    public void setAllowAutoClose(Boolean allowAutoClose) {
        this.allowAutoClose = allowAutoClose;
    }

    public ClaimRequest withAllowAutoClose(Boolean allowAutoClose) {
        this.allowAutoClose = allowAutoClose;
        return this;
    }

    @JsonProperty("policy")
    public Policy getPolicy() {
        return policy;
    }

    @JsonProperty("policy")
    public void setPolicy(Policy policy) {
        this.policy = policy;
    }

    public ClaimRequest withPolicy(Policy policy) {
        this.policy = policy;
        return this;
    }

    @JsonProperty("customer")
    public Customer getCustomer() {
        return customer;
    }

    @JsonProperty("customer")
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public ClaimRequest withCustomer(Customer customer) {
        this.customer = customer;
        return this;
    }

    @JsonProperty("extraModifiers")
    public List<ExtraModifier> getExtraModifiers() {
        return extraModifiers;
    }

    @JsonProperty("extraModifiers")
    public void setExtraModifiers(List<ExtraModifier> extraModifiers) {
        this.extraModifiers = extraModifiers;
    }

    public ClaimRequest withExtraModifiers(List<ExtraModifier> extraModifiers) {
        this.extraModifiers = extraModifiers;
        return this;
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

    public ClaimRequest withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

}
