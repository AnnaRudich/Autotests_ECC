package com.scalepoint.automation.utils.data.request;

import lombok.Data;

import javax.xml.bind.annotation.*;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ProductMatch")
public class ProductMatch {

    @XmlAttribute
    private String variant;
    @XmlElement(name = "Price")
    private Price[] Price;
}
