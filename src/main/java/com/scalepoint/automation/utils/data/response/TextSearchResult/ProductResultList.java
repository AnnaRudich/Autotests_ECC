package com.scalepoint.automation.utils.data.response.TextSearchResult;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductResultList {

    @JsonProperty("TotalCount")
    private int totalCount;
    @JsonProperty("ProductResults")
    private List<ProductResult> productResults;
}
