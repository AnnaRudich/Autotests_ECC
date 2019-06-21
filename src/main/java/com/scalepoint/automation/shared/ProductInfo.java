package com.scalepoint.automation.shared;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import org.apache.solr.client.solrj.beans.Field;

@Data
public class ProductInfo {

    @Field("id")
    private long id;

    @Field("parent_id")
    private long parentId;

    @Field("popularity_rating")
    private int popularityRating;

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

    @Getter(AccessLevel.NONE)
    @Field("price_product_sku_1")
    private String sku;

    @Field("price_lowest_1")
    private double lowestPrice;

    @Field("price_voucher_only_in_shop_1")
    private boolean voucherOnlyInShop;


    private double invoicePrice;
    private Double supplierShopPrice;
    private String supplierName;

    public String getSku(){
        return sku.trim();
    }

    public void setXprices(XpriceInfo xpriceInfo) {
        setSupplierName(xpriceInfo.getSupplierName());
        setInvoicePrice(xpriceInfo.getInvoicePrice());
        setSupplierShopPrice(xpriceInfo.getSupplierShopPrice());
    }
}
