package com.scalepoint.automation.utils.data.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by sro on 15.10.2014.
 */

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class PriceRule {

    @XmlElement
    private String productCategory1;
    @XmlElement
    private String productCategory2;
    @XmlElement
    private String brandName1;
    @XmlElement
    private String brandName2;
    @XmlElement
    private String discount1;
    @XmlElement
    private String discount2;
    @XmlElement
    private String discount3;
    @XmlElement
    private String priceSource1;
    @XmlElement
    private String priceSource2;
    @XmlElement
    private String startDate;
    @XmlElement
    private String endDate;
    @XmlElement
    private String supplier1;
    @XmlElement
    private String supplier2;
    @XmlElement
    private String subsupplier2;
    @XmlElement
    private String product1;
    @XmlElement
    private String posotivePrices;
    @XmlElement
    private String negativePrices;
    @XmlElement
    private String bikeCategory;
    @XmlElement
    private String bikeBrand;
    @XmlElement
    private String bikeSupplier;
    @XmlElement
    private String bikeProduct;
    @XmlElement
    private String computerProduct;


    public String getProductCategory1() {
        return productCategory1;
    }

    public String getBrandName1() {
        return brandName1;
    }

    public String getBrandName2() {
        return brandName2;
    }

    public String getDiscount1() {
        return discount1;
    }

    public String getDiscount2() {
        return discount2;
    }

    public String getPriceSource1() {
        return priceSource1;
    }

    public String getPriceSource2() {
        return priceSource2;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getSupplier1() {
        return supplier1;
    }

    public String getProductCategory2() {
        return productCategory2;
    }

    public String getExclusiveSupplier2() {
        return supplier2;
    }

    public String getSubsupplier2() {
        return subsupplier2;
    }

    public String getProduct1() {
        return product1;
    }

    public String getPosotivePrices() {
        return posotivePrices;
    }

    public String getNegativePrices() {
        return negativePrices;
    }

    public String getBikeCategory() {
        return bikeCategory;
    }

    public String getBikeBrand() {
        return bikeBrand;
    }

    public String getSharedBikeSupplier() {
        return bikeSupplier;
    }

    public String getBikeProduct() {
        return bikeProduct;
    }

    public String getDiscount3() {
        return discount3;
    }

    public String getComputerProduct() {
        return computerProduct;
    }
}
