package com.scalepoint.automation.utils.data.entity.eventsApiEntity.changed;

import lombok.Data;

import java.util.List;

@Data
public class Item {

    private String category;
    private String subCategory;
    private String subCategoryToken;
    private String description;
    private String itemId;
    private Long uiItemId;
    private Long depreciationPercentage;
    private Double depreciationAmount;
    private String createType;
    private Boolean draft;
    private Boolean rejected;
    private Long quantity;
    private Double replacementAmount;
    private Long ageMonths;
    private Double basePrice;
    private List<Valuation> valuations;
    private Voucher voucher;
    private Product product;

    public Valuation getValuationByType(String type){
        return valuations
                .stream()
                .filter(valuation -> valuation.getType()
                        .equals(type))
                .findFirst()
                .get();
    }

}
