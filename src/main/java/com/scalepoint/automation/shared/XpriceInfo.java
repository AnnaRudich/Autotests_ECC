package com.scalepoint.automation.shared;

public class XpriceInfo {

    private String productKey;

    private int productId;

    private double invoicePrice;

    private String supplierName;

    private double supplierShopPrice;

    private String priceModelID;

    private String priceModelType;

    private String discountFromDate;

    private String discountToDate;

    private Double discountValue;

    private String priceSourceType;

    private String priceSourceSupplierID;

    private String originalProductID;

    private String supplierId;

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public double getSupplierShopPrice() {
        return supplierShopPrice;
    }

    public void setSupplierShopPrice(double supplierShopPrice) {
        this.supplierShopPrice = supplierShopPrice;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setInvoicePrice(double invoicePrice) {
        this.invoicePrice = invoicePrice;
    }


    public String getProductKey() {
        return productKey;
    }

    public int getProductId() {
        return productId;
    }

    public double getInvoicePrice() {
        return invoicePrice;
    }



    public String getPriceModelID() {
        return priceModelID;
    }

    public void setPriceModelID(String priceModelID) {
        this.priceModelID = priceModelID;
    }

    public String getPriceModelType() {
        return priceModelType;
    }

    public void setPriceModelType(String priceModelType) {
        this.priceModelType = priceModelType;
    }

    public String getDiscountFromDate() {
        return discountFromDate;
    }

    public void setDiscountFromDate(String discountFromDate) {
        this.discountFromDate = discountFromDate;
    }

    public String getDiscountToDate() {
        return discountToDate;
    }

    public void setDiscountToDate(String discountToDate) {
        this.discountToDate = discountToDate;
    }

    public Double getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(Double discountValue) {
        this.discountValue = discountValue;
    }

    public String getPriceSourceType() {
        return priceSourceType;
    }

    public void setPriceSourceType(String priceSourceType) {
        this.priceSourceType = priceSourceType;
    }

    public String getPriceSourceSupplierID() {
        return priceSourceSupplierID;
    }

    public void setPriceSourceSupplierID(String priceSourceSupplierID) {
        this.priceSourceSupplierID = priceSourceSupplierID;
    }

    public String getOriginalProductID() {
        return originalProductID;
    }

    public void setOriginalProductID(String originalProductID) {
        this.originalProductID = originalProductID;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }


    @Override
    public String toString() {
        return "XPriceInfo{" +
                "productKey=" + productKey +
                ", productId=" + productId +
                ", invoicePrice='" + invoicePrice + '\'' +
                '}';
    }
}
