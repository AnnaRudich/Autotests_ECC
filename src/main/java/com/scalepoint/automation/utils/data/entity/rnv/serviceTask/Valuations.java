package com.scalepoint.automation.utils.data.entity.rnv.serviceTask;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;

@XmlRootElement(name = "valuations")
public class Valuations {
    private BigDecimal purchasePrice;
    private BigDecimal newPrice;
    private BigDecimal customerDemand;
    private BigDecimal discretionaryValuation;
    private BigDecimal usedPrice;
    private BigDecimal repairEstimate;
    private BigDecimal repairPrice;

    public Valuations() {
    }


    @XmlAttribute
    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    @XmlAttribute
    public BigDecimal getCustomerDemand() {
        return customerDemand;
    }

    public void setCustomerDemand(BigDecimal customerDemand) {
        this.customerDemand = customerDemand;
    }

    @XmlAttribute
    public BigDecimal getNewPrice() {
        return newPrice;
    }

    public void setNewPrice(BigDecimal newPrice) {
        this.newPrice = newPrice;
    }

    @XmlAttribute
    public BigDecimal getDiscretionaryValuation() {
        return discretionaryValuation;
    }

    public void setDiscretionaryValuation(BigDecimal discretionaryValuation) {
        this.discretionaryValuation = discretionaryValuation;
    }

    @XmlAttribute
    public BigDecimal getRepairEstimate() {
        return repairEstimate;
    }

    public void setRepairEstimate(BigDecimal repairEstimate) {
        this.repairEstimate = repairEstimate;
    }

    @XmlAttribute
    public BigDecimal getRepairPrice() {
        return repairPrice;
    }

    public void setRepairPrice(BigDecimal repairPrice) {
        this.repairPrice = repairPrice;
    }

    @XmlAttribute
    BigDecimal getUsedPrice() {
        return usedPrice;
    }

    void setUsedPrice(BigDecimal usedPrice) {
        this.usedPrice = usedPrice;
    }
}
