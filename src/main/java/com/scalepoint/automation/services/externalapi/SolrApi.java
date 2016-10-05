package com.scalepoint.automation.services.externalapi;

import com.scalepoint.automation.domain.ProductInfo;
import com.scalepoint.automation.utils.Configuration;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.beans.DocumentObjectBinder;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrDocument;

public class SolrApi {

    public static ProductInfo findProduct(String productId) throws Exception {
        SolrClient solr = new HttpSolrClient.Builder(Configuration.getSolrProductsUrl()).build();
        SolrDocument product = solr.getById(productId);
        DocumentObjectBinder binder = new DocumentObjectBinder();
        return binder.getBean(ProductInfo.class, product);
    }
}
