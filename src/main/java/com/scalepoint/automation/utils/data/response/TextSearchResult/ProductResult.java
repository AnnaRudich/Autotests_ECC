package com.scalepoint.automation.utils.data.response.TextSearchResult;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductResult {

    @JsonProperty("Id")
    private int id;
    @JsonProperty("ProductId")
    private int productId;
    @JsonProperty("Description")
    private String description;
    @JsonProperty("InvoicePrice")
    private String invoicePrice;
    @JsonProperty("MarketPrice")
    private String marketPrice;
    @JsonProperty("FreightPrice")
    private String freightPrice;
    @JsonProperty("ProductDesc")
    private String productDesc;
}
