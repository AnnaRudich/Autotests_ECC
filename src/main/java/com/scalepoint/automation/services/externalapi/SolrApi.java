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

import static com.scalepoint.automation.services.externalapi.DatabaseApi.PriceConditions.INVOICE_PRICE_EQUALS_MARKET_PRICE;
import static com.scalepoint.automation.services.externalapi.DatabaseApi.PriceConditions.INVOICE_PRICE_HIGHER_THAN_MARKET_PRICE;
import static com.scalepoint.automation.services.externalapi.DatabaseApi.PriceConditions.ORDERABLE;
import static com.scalepoint.automation.services.externalapi.DatabaseApi.PriceConditions.PRODUCT_AS_VOUCHER_ONLY;

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
}
