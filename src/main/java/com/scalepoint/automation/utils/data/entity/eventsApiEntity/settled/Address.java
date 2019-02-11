
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
    "street",
    "street2",
    "city",
    "postalCode",
    "countryCode"
})
public class Address {

    @JsonProperty("street")
    private String street;
    @JsonProperty("street2")
    private String street2;
    @JsonProperty("city")
    private String city;
    @JsonProperty("postalCode")
    private String postalCode;
    /**
     * ISO 3166-1 alpha 2 (https://en.wikipedia.org/wiki/ISO_3166-1_alpha-2)
     * 
     */
    @JsonProperty("countryCode")
    private String countryCode;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreet2() {
        return street2;
    }

    public void setStreet2(String street2) {
        this.street2 = street2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * ISO 3166-1 alpha 2 (https://en.wikipedia.org/wiki/ISO_3166-1_alpha-2)
     * 
     */
    public String getCountryCode() {
        return countryCode;
    }

    /**
     * ISO 3166-1 alpha 2 (https://en.wikipedia.org/wiki/ISO_3166-1_alpha-2)
     * 
     */
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
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
