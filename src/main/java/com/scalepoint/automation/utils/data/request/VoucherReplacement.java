package com.scalepoint.automation.utils.data.request;

import lombok.Data;

import javax.xml.bind.annotation.*;

/**
 * Created by bza on 6/28/2017.
 */
@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "VoucherReplacement")
public class VoucherReplacement {

    @XmlElement(name = "Price")
    private Price[] Price;
    @XmlAttribute
    private String voucher;

    @Override
    public String toString() {
        return "ClassPojo [Price = " + Price + ", voucher = " + voucher + "]";
    }

}
