package com.scalepoint.automation.utils.data.entity;

import com.scalepoint.automation.utils.RandomUtils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Supplier {

    private String supplierID = Integer.toString(RandomUtils.randomInt());
    private String supplierName = RandomUtils.randomName("AutotestSupplier-");
    private String supplierCVR = Integer.toString(RandomUtils.randomInt() + 10000000);
    private String address1 = RandomUtils.randomName("addr1");
    private String address2 = RandomUtils.randomName("addr2");
    private String city = RandomUtils.randomName("City");
    @XmlElement
    private String supplierPhone;
    @XmlElement
    private String supplierEmail;
    @XmlElement
    private String supplierURL;
    @XmlElement
    private String logoPath;
    @XmlElement
    private String defaultDeliveryTime;
    private String vendorAccountNumber = Integer.toString(RandomUtils.randomInt());
    @XmlElement
    private String culture;
    @XmlElement
    private String order_notification_method;
    @XmlElement
    private String order_mail_culture;
    private String promotionText = RandomUtils.randomName("Promotion");
    private String commercialText = RandomUtils.randomName("Commercial");
    private String shopTitleText = RandomUtils.randomName("ShopTitle");
    @XmlElement
    private String workflow_supplier;
    @XmlElement
    private String postCode;
    @XmlElement
    private String bannerGerman;
    @XmlElement
    private String bannerFrench;
    @XmlElement
    private String bannerItalian;
    @XmlElement
    private String bannerEnglish;
    @XmlElement
    private String supplierImage;
    @XmlElement
    private String supplierNameHandling1;
    @XmlElement
    private String supplierNameHandling2;
    @XmlElement
    private String supplierNameHandling3;
    @XmlElement
    private String bankRegNumber;
    @XmlElement
    private String bankAccNumber;
    @XmlElement
    private String bankFikType;
    @XmlElement
    private String bankFikCreditorCode;
    @XmlElement
    private String bankFikNumber;
    @XmlElement
    private String bankName;

    public String getSupplierID() {
        return supplierID;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public String getAddress1() {
        return address1;
    }

    public String getAddress2() {
        return address2;
    }

    public String getSupplierPhone() {
        return supplierPhone;
    }

    public String getSupplierEmail() {
        return supplierEmail;
    }

    public String getSupplierURL() {
        return supplierURL;
    }

    public String getLogoPath() {
        return logoPath;
    }

    public String getVendorAccountNumber() {
        return vendorAccountNumber;
    }

    public String getCulture() {
        return culture;
    }

    public String getOrder_notification_method() {
        return order_notification_method;
    }


    public String getDefaultDeliveryTime() {
        return defaultDeliveryTime;
    }

    public String getOrder_mail_culture() {
        return order_mail_culture;
    }

    public String getPromotionText() {
        return promotionText;
    }

    public String getCommercialText() {
        return commercialText;
    }

    public String getShopTitleText() {
        return shopTitleText;
    }

    public String getWorkflow_supplier() {
        return workflow_supplier;
    }

    public String getSupplierCVR() {
        return supplierCVR;
    }

    public String getPostCode() {
        return postCode;
    }

    public String getCity() {
        return city;
    }

    public String getBannerGerman() {
        return bannerGerman;
    }

    public String getBannerFrench() {
        return bannerFrench;
    }

    public String getBannerItalian() {
        return bannerItalian;
    }

    public String getBannerEnglish() {
        return bannerEnglish;
    }

    public String getSupplierImage() {
        return supplierImage;
    }

    public String getSupplierNameHandling1() {
        return supplierNameHandling1;
    }

    public String getSupplierNameHandling2() {
        return supplierNameHandling2;
    }

    public String getSupplierNameHandling3() {
        return supplierNameHandling3;
    }

    public String getBankRegNumber() {
        return bankRegNumber;
    }

    public String getBankAccNumber() {
        return bankAccNumber;
    }

    public String setSupplierCVR(String supplierCVR) {
        return this.supplierCVR = supplierCVR;
    }

    public String setSupplierName(String supplierName) {
        return this.supplierName = supplierName;
    }



    public void setBankRegNumber(String bankRegNumber) {
        this.bankRegNumber = bankRegNumber;
    }

    public void setBankAccNumber(String bankAccNumber) {
        this.bankAccNumber = bankAccNumber;
    }

    public String getBankFikType() {
        return bankFikType;
    }

    public void setBankFikType(String bankFikType) {
        this.bankFikType = bankFikType;
    }

    public String getBankFikCreditorCode() {
        return bankFikCreditorCode;
    }

    public void setBankFikCreditorCode(String bankFikCreditorCode) {
        this.bankFikCreditorCode = bankFikCreditorCode;
    }

    public String getBankFikNumber() {
        return bankFikNumber;
    }

    public void setBankFikNumber(String bankFikNumber) {
        this.bankFikNumber = bankFikNumber;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
}
