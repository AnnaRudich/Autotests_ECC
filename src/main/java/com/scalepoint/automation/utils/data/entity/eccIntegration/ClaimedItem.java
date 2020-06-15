package com.scalepoint.automation.utils.data.entity.eccIntegration;

import lombok.Data;

import javax.xml.bind.annotation.*;

@Data
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
