package com.scalepoint.automation.utils.data.entity.rnv.serviceTask;

import javax.xml.bind.annotation.XmlAttribute;
import java.math.BigDecimal;

public class ItemExport extends Item {
    private String manufacturer;
    private String model;
    private String serialNumber;
    private String damageDescription;
    private BigDecimal repairLimit;
    private Integer depreciation;
    private String customerNotesToClaimLine;
    private String EAN;
    private String damageType;

    @XmlAttribute
    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    @XmlAttribute
    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    @XmlAttribute
    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    @XmlAttribute
    public String getDamageDescription() {
        return damageDescription;
    }

    public void setDamageDescription(String damageDescription) {
        this.damageDescription = damageDescription;
    }

    @XmlAttribute
    public BigDecimal getRepairLimit() {
        return repairLimit;
    }

    public void setRepairLimit(BigDecimal repairLimit) {
        this.repairLimit = repairLimit;
    }

    @XmlAttribute
    public Integer getDepreciation() {
        return depreciation;
    }

    public void setDepreciation(Integer depreciation) {
        this.depreciation = depreciation;
    }

    @XmlAttribute
    public String getCustomerNotesToClaimLine() {
        return customerNotesToClaimLine;
    }

    public void setCustomerNotesToClaimLine(String customerNotesToClaimLine) {
        this.customerNotesToClaimLine = customerNotesToClaimLine;
    }

    @XmlAttribute(name = "EAN")
    public String getEAN() {
        return EAN;
    }

    public void setEAN(String EAN) {
        this.EAN = EAN;
    }

    @XmlAttribute
    public String getDamageType() {
        return damageType;
    }

    public void setDamageType(String damageType) {
        this.damageType = damageType;
    }
}
