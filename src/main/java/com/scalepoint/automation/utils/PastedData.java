package com.scalepoint.automation.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PastedData{

    private String firstName;
    private String lastName;
    private String zipCode;
    private String claimNumber;
    private String city;
    private String address;
    private String policyNumber;
    private String policyType;
    private String damageDate;

    static public PastedData parsePastedData(String pastedData){

        PastedData.PastedDataBuilder dataBuilder = PastedData.builder();

        String[] sections = pastedData.split("\\|");
        String fullNameSection = sections[0];
        String addressSection = sections.length  > 1 ? sections[1] : "";
        String claimDataSection = sections.length > 2 ? sections[2]: "";

        String[] fullName = fullNameSection.trim().split(" ");
        dataBuilder.firstName(fullName[0]);
        dataBuilder.lastName(fullName[fullName.length - 1]);
        dataBuilder.zipCode(extractZipCodeFromAddress(addressSection));
        dataBuilder.city(extractCity(addressSection));
        dataBuilder.address(extractAddress(addressSection));
        dataBuilder.claimNumber(extractClaimNumberFromClaimData(claimDataSection));
        dataBuilder.city(extractCity(addressSection));
        dataBuilder.address(extractAddress(addressSection));
        dataBuilder.policyNumber(extractPolicyNumber(claimDataSection));
        dataBuilder.policyType(extractPolicyType(claimDataSection));
        dataBuilder.damageDate(extractDamageType(claimDataSection));

        return dataBuilder.build();
    }

    static private String extractZipCodeFromAddress(String address){

        if(address.equals("")){
            return "";
        }

        String prefix = "CH-";
        int beginningOfPrefix = address.indexOf(prefix);

        if(beginningOfPrefix < 0){
            return "";
        }

        int zipCodeLength = 4;
        int startOfZipCode = beginningOfPrefix + prefix.length();
        return address.substring(startOfZipCode, startOfZipCode + zipCodeLength);
    }

    static private String extractClaimNumberFromClaimData(String claimData){

        if(claimData.equals("")){
            return "";
        }

        int start = claimData.indexOf("/");
        int end = claimData.indexOf(",");

        if(start < 0 || end < 0){
            return "";
        }

        return claimData.substring(start + 2, end);
    }

    static private String extractCity(String address){

        if(address.equals("")){
            return "";
        }
        return address.trim()
                .split(",")[0]
                .split(" ")[1];
    }

    static private String extractAddress(String address){

        if(address.equals("")){
            return "";
        }
        return address.trim()
                .split(",")[1].trim()
                .split(";")[0];
    }

    static private String extractPolicyNumber(String claimData){

        if(claimData.equals("")){
            return "";
        }

        return claimData
                .split("/")[0].trim();
    }

    static private String extractPolicyType(String claimData){

        if(claimData.equals("")){
            return "";
        }

        return claimData
                .split("/")[1]
                .split(",")[2].trim();
    }

    static private String extractDamageType(String claimData){

        if(claimData.equals("")){
            return "";
        }

        return claimData
                .split("/")[1]
                .split(",")[3].trim();
    }
}
