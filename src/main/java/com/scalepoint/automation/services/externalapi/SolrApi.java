package com.scalepoint.automation.services.externalapi;

import com.scalepoint.automation.shared.ProductInfo;
import com.scalepoint.automation.utils.Configuration;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.beans.DocumentObjectBinder;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SolrApi {

    private static Logger logger = LoggerFactory.getLogger(SolrApi.class);

    public static ProductInfo findProduct(String productId) {
        try {
            SolrClient solr = new HttpSolrClient.Builder(Configuration.getSolrProductsUrl()).build();
            SolrDocument product = solr.getById(productId);
            DocumentObjectBinder binder = new DocumentObjectBinder();
            return binder.getBean(ProductInfo.class, product);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new IllegalArgumentException("no products found", e);
        }
    }

    public static ProductInfo findProductInvoiceHigherMarket() {
        String filterQuery = "{!frange l=0 incl=false}sub(price_invoice_1,market_price)";
        ProductInfo product = findOrderableProduct(filterQuery, "ProductInvoiceHigherMarket");
        assert product.getInvoicePrice() > product.getMarketPrice();
        return product;
    }

    public static ProductInfo findProductInvoiceLowerMarket() {
        String filterQuery = "{!frange l=1 incl=false}sub(market_price, price_invoice_1)";
        ProductInfo product = findOrderableProduct(filterQuery, "ProductInvoiceLowerMarket");
        assert product.getInvoicePrice() < product.getMarketPrice();
        return product;
    }

    public static ProductInfo findProductInvoiceEqualMarket() {
        String filterQuery = "{!frange l=0 u=0}sub(price_invoice_1,market_price)";
        ProductInfo product = findOrderableProduct(filterQuery, "ProductInvoiceEqualMarket");
        assert product.getInvoicePrice() == product.getMarketPrice();
        return product;
    }

    private static ProductInfo findOrderableProduct(String filterQuery, String message) {
        try {
            SolrClient solr = new HttpSolrClient.Builder(Configuration.getSolrProductsUrl()).build();
            SolrQuery query = new SolrQuery();
            query.setQuery("orderable:true AND -product_as_voucher_agreement_id_1:[* TO *]");
            query.setFilterQueries(filterQuery);
            query.setRows(1);
            QueryResponse response = solr.query(query);
            ProductInfo productInfo = response.getBeans(ProductInfo.class).get(0);
            logger.info(message + ": {}", productInfo);
            return productInfo;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new IllegalStateException("no products found", e);
        }
    }

    public static ProductInfo findProductAsVoucher() {
        try {
            SolrClient solr = new HttpSolrClient.Builder(Configuration.getSolrProductsUrl()).build();
            SolrQuery query = new SolrQuery();
            query.setQuery("orderable:true AND price_voucher_only_in_shop_1:true");
            query.setRows(1);
            QueryResponse response = solr.query(query);
            ProductInfo productInfo = response.getBeans(ProductInfo.class).get(0);
            logger.info("FindBaOProduct: {}", productInfo);
            return productInfo;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new IllegalStateException("no products found", e);
        }
    }
}
