package com.scalepoint.automation.utils.data.entity.rnv.serviceTask;

import javax.xml.bind.annotation.XmlAttribute;
import java.math.BigDecimal;

public class InvoiceLine {
    private String description;
    private String units;
    private BigDecimal unitNetAmount;
    private BigDecimal unitVatAmount;
    private BigDecimal unitPrice;
    private BigDecimal quantity;
    private BigDecimal lineTotal;

    @XmlAttribute
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @XmlAttribute
    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    @XmlAttribute
    public BigDecimal getUnitNetAmount() {
        return unitNetAmount;
    }

    public void setUnitNetAmount(BigDecimal unitNetAmount) {
        this.unitNetAmount = unitNetAmount;
    }

    @XmlAttribute
    public BigDecimal getUnitVatAmount() {
        return unitVatAmount;
    }

    public void setUnitVatAmount(BigDecimal unitVatAmount) {
        this.unitVatAmount = unitVatAmount;
    }

    @XmlAttribute
    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    @XmlAttribute
    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    @XmlAttribute
    public BigDecimal getLineTotal() {
        return lineTotal;
    }

    public void setLineTotal(BigDecimal lineTotal) {
        this.lineTotal = lineTotal;
    }
}
