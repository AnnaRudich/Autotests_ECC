package com.scalepoint.automation.utils.data.request;

import lombok.Data;

import java.util.List;

@Data
public class SelfServiceLossItems {

    private String savedCategoryId;
    private boolean hasDocumentation;
    private boolean clearAttachments;
    private double purchasePrice;
    private double newPrice;
    private int customerDemand;
    private boolean loadAttachments;
    private int quantity;
    private int progress;
    private Repair repair;
    private String acquired;
    private String lineId;
    private boolean unableToUpload;
    private boolean uploadAttachments;
    private String customerNote;
    private List<String> attachments;
    private String productMatch;
    private List<String> deletedAttachments;
    private int groupCategoryId;
    private boolean deleted;
    private boolean noDocumentation;
    private int categoryId;
    private int id;
    private boolean selected;
    private String description;
    private String lossType;
    private int purchaseYear;
    private String itemState;
    private int age;
    private int purchaseMonth;

}
