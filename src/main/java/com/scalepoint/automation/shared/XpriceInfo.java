package com.scalepoint.automation.shared;

import org.apache.solr.client.solrj.beans.Field;

/**
 * Created by aru on 2018-06-27.
 */
public class XpriceInfo {

    private String productKey;

    private int productId;

    private double marketPrice;

    private double invoicePrice;

    private String supplierName;

    private double supplierShopPrice;

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

    public void setMarketPrice(double marketPrice) {
        this.marketPrice = marketPrice;
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

    public double getMarketPrice() {
        return marketPrice;
    }

    public double getInvoicePrice() {
        return invoicePrice;
    }

    @Override
    public String toString(){
        return "XPriceInfo{" +
                "productKey=" + productKey +
                ", productId=" + productId +
                ", marketPrice='" + marketPrice + '\'' +
                ", invoicePrice='" + invoicePrice + '\'' +
                '}';
    }
}
