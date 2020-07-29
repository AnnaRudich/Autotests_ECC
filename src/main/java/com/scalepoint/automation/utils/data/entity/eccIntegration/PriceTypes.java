package com.scalepoint.automation.utils.data.entity.eccIntegration;

import java.util.Arrays;
import java.util.NoSuchElementException;

public enum PriceTypes {

    PRICE("price"),
    RECOMMENDED_RETAIL_PRICE("recommendedRetailPrice"),
    FREIGHT_PRICE("freightPrice");

    private String name;

    PriceTypes(String name) {

        this.name = name;
    }

    public static PriceTypes findPrice(String name){

        return Arrays.stream(PriceTypes.values())
                .filter(test ->  test.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(name));
    }

    public String getName(){

        return name;
    }
}
