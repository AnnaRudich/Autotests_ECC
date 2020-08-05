package com.scalepoint.automation.utils.data.entity.rnv.webService;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Claimant {

    private String name;
    private String mobilePhone;
    private String phone;
    private String address1;
    private String address2;
    private String postalCode;
    private String city;
    private String email;
    private String claimGUID;
    private String zipCodeValidationPattern;
}
