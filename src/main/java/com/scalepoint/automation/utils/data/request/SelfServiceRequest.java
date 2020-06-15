package com.scalepoint.automation.utils.data.request;

import lombok.Data;

@Data
public class SelfServiceRequest {

    private String claimsNo;
    private String closeAutomatically;
    private String culture;
    private String customerNotes;
    private String email;
    private String firstName;
    private String lastName;
    private String mobileNumber;
    private String password;
    private String previousNotes;
    private String selfServiceType;
    private String sendPasswordSMS;

}
