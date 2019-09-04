package com.scalepoint.automation.utils.data.request;

import lombok.Data;

import java.util.List;

@Data
public class SelfServiceLossItems {

    String savedCategoryId;
    boolean hasDocumentation;
    boolean clearAttachments;
    double purchasePrice;
    double newPrice;
    int customerDemand;
    boolean loadAttachments;
    int quantity;
    int progress;
    Repair repair;
    String acquired;
    String lineId;
    boolean unableToUpload;
    boolean uploadAttachments;
    String customerNote;
    List<String> attachments;
    String productMatch;
    List<String> deletedAttachments;
    int groupCategoryId;
    boolean deleted;
    boolean noDocumentation;
    int categoryId;
    int id;
    boolean selected;
    String description;
    String lossType;
    int purchaseYear;
    String itemState;
    int age;
    int purchaseMonth;
}
