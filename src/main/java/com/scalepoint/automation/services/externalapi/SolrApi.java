package com.scalepoint.automation.services.externalapi;

import com.scalepoint.automation.domain.ProductInfo;
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

    public static ProductInfo findProductInvoiceBiggerMarket() {
        String filterQuery = "{!frange l=0 incl=false}sub(price_invoice_1,market_price)";
        return findOrderableQueryWithFilterQuery(filterQuery);
    }

    public static ProductInfo findProductInvoiceLowerMarket() {
        String filterQuery = "{!frange l=0 incl=false}sub(market_price, price_invoice_1)";
        return findOrderableQueryWithFilterQuery(filterQuery);
    }

    public static ProductInfo findProductInvoiceEqualMarket() {
        String filterQuery = "{!frange =0 incl=false}sub(price_invoice_1,market_price)";
        return findOrderableQueryWithFilterQuery(filterQuery);
    }

    private static ProductInfo findOrderableQueryWithFilterQuery(String filterQuery) {
        try {
            SolrClient solr = new HttpSolrClient.Builder(Configuration.getSolrProductsUrl()).build();
            SolrQuery query = new SolrQuery();
            query.setQuery("orderable:true");
            query.setFilterQueries(filterQuery);
            query.setRows(1);
            QueryResponse response = solr.query(query);
            return response.getBeans(ProductInfo.class).get(0);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new IllegalStateException("no products found", e);
        }
    }
}
