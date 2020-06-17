package com.scalepoint.automation.utils.data.entity.eventsApiEntity.changed;

import lombok.Data;

@Data
public class Customer {

    private String name;
    private String phone;
    private String mobile;
    private String email;
    private Address address;

}
