package com.scalepoint.automation.utils.data.request;

import lombok.Data;

@Data
public class SelfServiceRequest {

    String claimsNo;
    String closeAutomatically;
    String culture;
    String customerNotes;
    String email;
    String firstName;
    String lastName;
    String mobileNumber;
    String password;
    String previousNotes;
    String selfServiceType;
    String sendPasswordSMS;
}
