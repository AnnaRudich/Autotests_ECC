package com.scalepoint.automation.utils.data.entity.serviceTaskEntity.copy;


import javax.xml.bind.annotation.XmlAttribute;
import java.math.BigDecimal;

public class ValuationsExport extends Valuations{


    private BigDecimal productPrice;
    private BigDecimal retailPrice;

    @XmlAttribute
    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

    @XmlAttribute
    public BigDecimal getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(BigDecimal retailPrice) {
        this.retailPrice = retailPrice;
    }
}
