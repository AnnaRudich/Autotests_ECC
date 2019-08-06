package com.scalepoint.automation.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
public class Item {
    String category;
    String subCategory;
    String description;
    String itemId;
    Long uiItemId;
    Long depreciationPercentage;
    Double depreciationAmount;
    String createType;
    Boolean draft;
    Boolean rejected;
    Long quantity;
    Double replacementAmount;
    Long ageMonths;
    Double basePrice;
    List<Valuation> valuations;
    Voucher voucher;

    public Valuation getValuationByType(String type){
        return valuations
                .stream()
                .filter(valuation -> valuation.getType()
                        .equals(type))
                .findFirst()
                .get();
    }
}
