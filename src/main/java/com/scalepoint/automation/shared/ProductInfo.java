package com.scalepoint.automation.shared;

import org.apache.solr.client.solrj.beans.Field;

public class ProductInfo {

    @Field("id")
    private long id;

    @Field("parent_id")
    private long parentId;

    @Field("brand")
    private String brand;

    @Field("model")
    private String model;

    @Field("category")
    private String category;

    @Field("price")
    private double price;

    @Field("market_price")
    private double marketPrice;

    @Field("orderable")
    private boolean orderable;

    @Field("product_key")
    private String sku;

    @Field("price_lowest_1")
    private double lowestPrice;

    @Field("price_invoice_1")
    private double invoicePrice;

    @Field("price_supplier_shop_1")
    private Double supplierShopPrice;

    @Field("price_supplier_name_1")
    private String supplierName;

    @Field("price_voucher_only_in_shop_1")
    private boolean voucherOnlyInShop;

    public long getId() {
        return id;
    }

    public long getParentId() {
        return parentId;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public String getCategory() {
        return category;
    }

    public double getPrice() {
        return price;
    }

    public double getMarketPrice() {
        return marketPrice;
    }

    public boolean isOrderable() {
        return orderable;
    }

    public String getSku() {
        return sku;
    }

    public double getLowestPrice() {
        return lowestPrice;
    }

    public double getInvoicePrice() {
        return invoicePrice;
    }

    public Double getSupplierShopPrice() {
        return supplierShopPrice;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public boolean isVoucherOnlyInShop() {
        return voucherOnlyInShop;
    }

    public String getModelAndCategory(){
        return model + " " + category;
    }

    @Override
    public String toString() {
        return "ProductInfo{" +
                "id=" + id +
                ", parentId=" + parentId +
                ", brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", category='" + category + '\'' +
                ", price=" + price +
                ", marketPrice=" + marketPrice +
                ", orderable=" + orderable +
                ", sku='" + sku + '\'' +
                ", lowestPrice=" + lowestPrice +
                ", invoicePrice=" + invoicePrice +
                ", supplierShopPrice=" + supplierShopPrice +
                ", supplierName='" + supplierName + '\'' +
                ", voucherOnlyInShop=" + voucherOnlyInShop +
                '}';
    }
}
