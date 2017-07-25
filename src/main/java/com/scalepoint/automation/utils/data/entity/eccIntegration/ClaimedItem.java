package com.scalepoint.automation.utils.data.entity.eccIntegration;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ClaimedItem")
public class ClaimedItem {

    @XmlAttribute
    private String description;
    @XmlAttribute
    private String quantity;
    @XmlAttribute
    private String depreciation;
    @XmlAttribute
    private String ageMonths;
    @XmlAttribute
    private String itemId;
    @XmlAttribute
    private String brand;
    @XmlAttribute
    private String model;
    @XmlAttribute
    private String status;
    @XmlAttribute
    private ValuationTypes activeValuation;
    @XmlElement(name = "Valuations")
    private Valuations valuations;
    @XmlElement(name = "CompanySpecificItemData")
    private CompanySpecificItemData companySpecificItemData;


    public String getDescription() {
        return description;
    }

    public ClaimedItem setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getQuantity() {
        return quantity;
    }

    public ClaimedItem setQuantity(String quantity) {
        this.quantity = quantity;
        return this;
    }

    public String getDepreciation() {
        return depreciation;
    }

    public ClaimedItem setDepreciation(String depreciation) {
        this.depreciation = depreciation;
        return this;
    }

    public String getAgeMonths() {
        return ageMonths;
    }

    public ClaimedItem setAgeMonths(String ageMonths) {
        this.ageMonths = ageMonths;
        return this;
    }

    public String getItemId() {
        return itemId;
    }

    public ClaimedItem setItemId(String itemId) {
        this.itemId = itemId;
        return this;
    }

    public String getBrand() {
        return brand;
    }

    public ClaimedItem setBrand(String brand) {
        this.brand = brand;
        return this;
    }

    public String getModel() {
        return model;
    }

    public ClaimedItem setModel(String model) {
        this.model = model;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public ClaimedItem setStatus(String status) {
        this.status = status;
        return this;
    }

    public ValuationTypes getActiveValuation() {
        return activeValuation;
    }

    public ClaimedItem setActiveValuation(ValuationTypes activeValuation) {
        this.activeValuation = activeValuation;
        return this;
    }

    public ClaimedItem setActiveValuation(String activeValuation) {
        this.activeValuation = ValuationTypes.valueOf(activeValuation);
        return this;
    }

    public Valuations getValuations() {
        return valuations;
    }

    public ClaimedItem setValuations(Valuations valuations) {
        this.valuations = valuations;
        return this;
    }

    public CompanySpecificItemData getCompanySpecificItemData() {
        return companySpecificItemData;
    }

    public ClaimedItem setCompanySpecificItemData(CompanySpecificItemData companySpecificItemData) {
        this.companySpecificItemData = companySpecificItemData;
        return this;
    }

    @Override
    public String toString() {
        return "ClaimedItem{" +
                "description='" + description + '\'' +
                ", quantity='" + quantity + '\'' +
                ", depreciation='" + depreciation + '\'' +
                ", ageMonths='" + ageMonths + '\'' +
                ", itemId='" + itemId + '\'' +
                ", brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", status='" + status + '\'' +
                ", activeValuation=" + activeValuation +
                ", valuations=" + valuations +
                ", companySpecificItemData=" + companySpecificItemData +
                '}';
    }
}
