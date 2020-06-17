package com.scalepoint.automation.utils.data.entity.eventsApiEntity.changed;

import lombok.Data;

@Data
public class Product {

    private String brand;
    private String catalogCategory;
    private String code;
    private double retailPrice;
    private double scalepointPrice;

}
