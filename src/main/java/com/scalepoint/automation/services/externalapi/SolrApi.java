package com.scalepoint.automation.services.externalapi;

import com.scalepoint.automation.shared.ProductInfo;
import com.scalepoint.automation.shared.XpriceInfo;
import com.scalepoint.automation.utils.Configuration;
import com.scalepoint.automation.utils.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.beans.DocumentObjectBinder;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;

import java.io.IOException;

public class SolrApi {

    private static Logger logger = LogManager.getLogger(SolrApi.class);

    public static ProductInfo findProduct(XpriceInfo xpriceInfo){
        ProductInfo productInfo = findProduct(String.valueOf(xpriceInfo.getProductId()));
        productInfo.setXprices(xpriceInfo);
        return productInfo;
    }

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

    public static ProductInfo findProductInvoiceLowerMarket() {
        String filterQuery = "{!frange l=1 incl=false}sub(market_price, price_invoice_1)";
        ProductInfo product = findOrderableProduct(filterQuery, "ProductInvoiceLowerMarket");
        assert product.getInvoicePrice() < product.getMarketPrice();
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

    /**
     * this method updates product prices directly in Solr Index, to create test data which is missing in DB
     */
    private static void updatePricesInIndex(SolrClient solrClient, QueryResponse response, String priceValue, String... fieldNames) throws SolrServerException, IOException {
        for (String fieldName : fieldNames) {
            response.getResults().get(0).setField(fieldName, priceValue);
        }

        SolrDocument solrDoc = response.getResults().get(0);
        SolrInputDocument solrInputDoc = new SolrInputDocument();
        for (String name : solrDoc.getFieldNames()) {
            solrInputDoc.addField(name, solrDoc.getFieldValue(name));
        }
        solrClient.add(solrInputDoc);
        solrClient.commit();
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

    public static ProductInfo findProductAsVoucherWithProductInvoiceLowerThanMarket(){
        try{
            SolrClient solr = new HttpSolrClient.Builder(Configuration.getSolrProductsUrl()).build();
            SolrQuery query = new SolrQuery();
            query.setQuery("orderable:true AND price_voucher_only_in_shop_1:true")
                    .setFilterQueries("{!frange l=1 incl=false}sub(market_price, price_invoice_1)");
            QueryResponse response = solr.query(query);
            ProductInfo productInfo = response.getBeans(ProductInfo.class).get(0);
            logger.info("FindBaOProduct: {}", productInfo);
            return productInfo;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new IllegalStateException("no products found", e);
        }
    }

    public static ProductInfo findProductAsVoucherWithProductInvoiceEqualsMarketPrice(){
        try{
            SolrClient solr = new HttpSolrClient.Builder(Configuration.getSolrProductsUrl()).build();
            SolrQuery query = new SolrQuery();
            query.setQuery("orderable:true AND price_voucher_only_in_shop_1:true")
                    .setFilterQueries("{!frange l=0 u=0}sub(price_invoice_2,market_price)");
            QueryResponse response;
            response = solr.query(query);
            if(response.getResults().size() == 0){

                updatePricesInIndex(solr, solr.query(new SolrQuery().setQuery("orderable:true AND price_voucher_only_in_shop_1:true")), Constants.PRICE_500.toString(),
                        "market_price", "price_invoice_1","price_invoice_2", "price", "price_supplier_shop_1", "price_lowest_1");
                response = solr.query(query);
            }
            ProductInfo productInfo = response.getBeans(ProductInfo.class).get(0);
            logger.info("FindBaOProduct: {}", productInfo);
            return productInfo;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new IllegalStateException("no products found", e);
        }
    }

    public static ProductInfo findProductAsVoucherWithProductInvoiceHigherThanMarketPrice(){
        try{
            SolrClient solr = new HttpSolrClient.Builder(Configuration.getSolrProductsUrl()).build();
            SolrQuery query = new SolrQuery();
            query.setQuery("orderable:true AND price_voucher_only_in_shop_1:true")
                    .setFilterQueries("{!frange l=0 incl=false}sub(price_invoice_2,market_price)");
            QueryResponse response = solr.query(query);
            if(response.getResults().size() == 0){

                updatePricesInIndex(solr, solr.query(new SolrQuery().setQuery("orderable:true AND price_voucher_only_in_shop_1:true")), Constants.PRICE_10.toString(),
                        "market_price", "price_invoice_1","price_supplier_shop_1", "price_lowest_1");
                response = solr.query(query);
            }
            ProductInfo productInfo = response.getBeans(ProductInfo.class).get(0);
            logger.info("FindBaOProduct: {}", productInfo);
            return productInfo;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new IllegalStateException("no products found", e);
        }
    }
}
