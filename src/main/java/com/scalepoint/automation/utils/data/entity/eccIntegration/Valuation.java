package com.scalepoint.automation.utils.data.entity.eccIntegration;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Valuation")
public class Valuation {

    @XmlAttribute
    private String price;
    @XmlAttribute
    private ValuationTypes type;

    public String getPrice() {
        return price;
    }

    public Valuation setPrice(String price) {
        this.price = price;
        return this;
    }

    public ValuationTypes getType() {
        return type;
    }

    public Valuation setType(ValuationTypes type) {
        this.type = type;
        return this;
    }

    public Valuation setType(String type) {
        this.type = ValuationTypes.valueOf(type);
        return this;
    }

    @Override
    public String toString() {
        return "Valuation{" +
                "price='" + price + '\'' +
                ", type=" + type +
                '}';
    }
}
