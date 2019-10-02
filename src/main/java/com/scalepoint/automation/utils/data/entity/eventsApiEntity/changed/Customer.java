package com.scalepoint.automation.utils.data.entity.eventsApiEntity.changed;

import lombok.Data;

@Data
public class Customer {
    String name;
    String phone;
    String mobile;
    String email;
    Address address;
}
