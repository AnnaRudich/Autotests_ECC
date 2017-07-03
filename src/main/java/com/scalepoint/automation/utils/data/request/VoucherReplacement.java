package com.scalepoint.automation.utils.data.request;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Created by bza on 6/28/2017.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "VoucherReplacement")
public class VoucherReplacement
{
    @XmlElement(name = "Price")
    private Price[] Price;
    @XmlAttribute
    private String voucher;

    public Price[] getPrice ()
    {
        return Price;
    }

    public void setPrice (Price[] Price)
    {
        this.Price = Price;
    }

    public String getVoucher ()
    {
        return voucher;
    }

    public void setVoucher (String voucher)
    {
        this.voucher = voucher;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [Price = "+Price+", voucher = "+voucher+"]";
    }
}
