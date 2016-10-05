package com.scalepoint.automation.domain;

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

    @Field("market_price")
    private double marketPrice;

    @Field("orderable")
    private boolean orderable;

    @Field("product_key")
    private String sku;

    @Field("price_lowest_1")
    private double lowest;

    @Field("price_invoice_1")
    private double invoice;

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

    public double getMarketPrice() {
        return marketPrice;
    }

    public boolean isOrderable() {
        return orderable;
    }

    public String getSku() {
        return sku;
    }

    public double getLowest() {
        return lowest;
    }

    public double getInvoice() {
        return invoice;
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
}
