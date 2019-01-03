package com.scalepoint.automation.utils.data.entity.rnv.serviceTask;

import javax.xml.bind.annotation.XmlAttribute;
import java.math.BigDecimal;

class InvoiceLine {
    private String description;
    private String units;
    private BigDecimal unitNetAmount;
    private BigDecimal unitVatAmount;
    private BigDecimal unitPrice;
    private BigDecimal quantity;
    private BigDecimal lineTotal;

    @XmlAttribute(required = true)
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

    @XmlAttribute(required = true)
    public BigDecimal getUnitNetAmount() {
        return unitNetAmount;
    }

    void setUnitNetAmount(BigDecimal unitNetAmount) {
        this.unitNetAmount = unitNetAmount;
    }

    @XmlAttribute(required = true)
    public BigDecimal getUnitVatAmount() {
        return unitVatAmount;
    }

    void setUnitVatAmount(BigDecimal unitVatAmount) {
        this.unitVatAmount = unitVatAmount;
    }

    @XmlAttribute(required = true)
    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    @XmlAttribute(required = true)
    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    @XmlAttribute(required = true)
    public BigDecimal getLineTotal() {
        return lineTotal;
    }

    void setLineTotal(BigDecimal lineTotal) {
        this.lineTotal = lineTotal;
    }
}
