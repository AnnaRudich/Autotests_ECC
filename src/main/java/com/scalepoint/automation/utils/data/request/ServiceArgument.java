package com.scalepoint.automation.utils.data.request;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ServiceArgument {

    private int selectedCategory;
    private int startcat;
    private int brand;
    private int model;
    private String price;
    private String difference;
    private String searchText;
    private int start;
    private int count;
    private String sort;
    private String first;
    private List<Object> selectedAttributes;
    private boolean refreshAllComponents;
    private int matchId;
    private boolean matchWithSuggestion;
    private int selectedModelId;
    private boolean didYouMeanMatch;
    private boolean manuallySelectedCategory;
}
