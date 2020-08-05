package com.scalepoint.automation.utils.data.entity.rnv.webService;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceLine {

    private int matchId;
    private int claimLineId;
    private String description;
    private String originalDescription;
    private String pseudoCategoryToken;
    private String categoryName;
    private String subCategoryName;
    private int quantity;
    private int taskTypeId;
    private String taskType;
    private String possibleTaskTypeIds;
    private String servicePartner;
    private String serviceAgreementId;
    private String serviceAgreementName;
    private String serviceAgreementInformation;
    private String age;
    private int depreciation;
    private String purchasePrice;
    private String customerDemand;
    private int newPrice;
    private String usedPrice;
    private String productPrice;
    private String retailPrice;
    private String discretionaryValuation;
    private String repairEstimate;
    private String repairPrice;
    private String claimLineGUID;
    private String productThatWasMatched;
    private String[] attachmentInfoModels;
    private int locationId;
    private String locationEmail;
    private String locationAddress;
    private String locationAddress2;
    private String locationCity;
    private String locationZipCode;
    private String locationPhone;
    private String locationName;
    private String locationDisplayName;
    private String customerNotesToClaimLine;
    private String serialNumber;
    private String manufacturer;
    private String damageTypesString;
    private String damageType;
    private boolean showDamageType;
}
