package com.scalepoint.automation.services.externalapi;

import com.google.common.base.Function;
import com.scalepoint.automation.shared.ProductInfo;
import com.scalepoint.automation.shared.SolrClaim;
import com.scalepoint.automation.shared.XpriceInfo;
import com.scalepoint.automation.utils.Configuration;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.data.entity.Claim;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.beans.DocumentObjectBinder;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class SolrApi {

    public static final int HARD_COMMIT_TIME = 15; //seconds

    private static final String PRODUCTS_COLLECTION = "da_DK";
    private static final String CLAIMS_COLLECTION = "claim_DK";

    private static Logger logger = LogManager.getLogger(SolrApi.class);

    public static ProductInfo findProduct(XpriceInfo xpriceInfo) {
        ProductInfo productInfo = findProduct(String.valueOf(xpriceInfo.getProductId()));
        productInfo.setXprices(xpriceInfo);
        return productInfo;
    }

    public static ProductInfo findProduct(String productId) {
        try {
            SolrClient solr = new HttpSolrClient.Builder(Configuration.getSolrBaseUrl()).build();
            SolrDocument product = solr.getById(PRODUCTS_COLLECTION, productId);
            DocumentObjectBinder binder = new DocumentObjectBinder();
            return binder.getBean(ProductInfo.class, product);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new IllegalArgumentException("no products found", e);
        }
    }

    public static List<ProductInfo> findProducts(List<String> productIds) {
        return productIds.stream().map(SolrApi::findProduct).collect(Collectors.toCollection(() -> new ArrayList<>(productIds.size())));
    }

    public static SolrClaim findClaim(String claimId) {
        try {
            SolrClient solr = new HttpSolrClient.Builder(Configuration.getSolrBaseUrl()).build();
            SolrQuery solrQuery = new SolrQuery("id:" + claimId);
            SolrDocumentList list = solr.query(CLAIMS_COLLECTION, solrQuery).getResults();
            if (list.isEmpty()) {
                logger.info("Claim is not found: {}", claimId);
                return null;
            }
            DocumentObjectBinder binder = new DocumentObjectBinder();
            SolrClaim solrClaim = binder.getBean(SolrClaim.class, list.get(0));
            logger.info("Claim is found: {}", solrClaim);
            return solrClaim;
        } catch (Exception e) {
            logger.error("Couldn't found claim in solr by id [{}] cause {}", claimId, e.getMessage());
            return null;
        }
    }

    public static void waitForClaimStatusChangedTo(Claim claim, String status) {
        Wait.forCondition((Function<WebDriver, Object>) webDriver -> {
            SolrClaim solrClaim = SolrApi.findClaim(claim.getClaimId());
            if (solrClaim != null) {
                return solrClaim.getClaimStatus().equalsIgnoreCase(status);
            }
            return null;
        }, SolrApi.HARD_COMMIT_TIME, 500);
    }
}