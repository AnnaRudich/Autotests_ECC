package com.scalepoint.automation.utils.data.entity.eventsApiEntity.changed;

import lombok.Data;

@Data
public class Product {
    String brand;
    String catalogCategory;
    String code;
    double retailPrice;
    double scalepointPrice;
}
