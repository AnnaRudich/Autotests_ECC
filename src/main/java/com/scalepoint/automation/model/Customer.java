package com.scalepoint.automation.model;

import lombok.Data;

@Data
public class Customer {
    String name;
    String phone;
    String mobile;
    String email;
    Address address;
}
