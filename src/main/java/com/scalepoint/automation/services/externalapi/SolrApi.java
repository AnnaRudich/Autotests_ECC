package com.scalepoint.automation.services.externalapi;

import com.google.common.base.Function;
import com.scalepoint.automation.shared.ClaimStatus;
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

    private static final int HARD_COMMIT_TIME = 60; //seconds

    private static final String PRODUCTS_COLLECTION = "da_DK";
    private static final String CLAIMS_COLLECTION = "claim_DK";
    private static final int POLL_MS = 1000;

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

    private static SolrClaim findClaimById(String claimId) {
        SolrQuery solrQuery = new SolrQuery("id:" + claimId);
        return findOneClaim(solrQuery);
    }

    private static SolrClaim findClaimByClaimNumber(String claimNumber) {
        SolrQuery solrQuery = new SolrQuery("claim_number:" + claimNumber);
        return findOneClaim(solrQuery);
    }

    private static SolrClaim findOneClaim(SolrQuery solrQuery) {
        try {
            SolrClient solr = new HttpSolrClient.Builder(Configuration.getSolrBaseUrl()).build();

            SolrDocumentList list = solr.query(CLAIMS_COLLECTION, solrQuery).getResults();
            if (list.isEmpty()) {
                logger.info("Claim is not found: {}", solrQuery.toQueryString());
                return null;
            }
            DocumentObjectBinder binder = new DocumentObjectBinder();
            SolrClaim solrClaim = binder.getBean(SolrClaim.class, list.get(0));
            logger.info("Claim is found: {}", solrClaim);
            return solrClaim;
        } catch (Exception e) {
            logger.info("Claim is not found: {} {}", solrQuery.toQueryString(), e.getMessage());
            return null;
        }
    }

    public static void waitForClaimStatusChangedTo(Claim claim, ClaimStatus claimState) {
        Wait.forCondition((Function<WebDriver, Object>) webDriver -> {
            SolrClaim solrClaim = SolrApi.findClaimById(claim.getClaimId());
            if (solrClaim != null) {
                return solrClaim.getClaimStatus().equalsIgnoreCase(claimState.getStatus());
            }
            return null;
        }, SolrApi.HARD_COMMIT_TIME, POLL_MS);
    }

    public static void waitForClaimAppearedInIndexById(Claim claim) {
        Wait.forCondition((Function<WebDriver, Object>) webDriver -> SolrApi.findClaimById(claim.getClaimId()), SolrApi.HARD_COMMIT_TIME, POLL_MS);
    }

    public static void waitForClaimAppearedInIndexByClaimNumber(Claim claim) {
        Wait.forCondition((Function<WebDriver, Object>) webDriver -> SolrApi.findClaimByClaimNumber(claim.getClaimNumber()), SolrApi.HARD_COMMIT_TIME, POLL_MS);
        SolrClaim claimByClaimNumber = findClaimByClaimNumber(claim.getClaimNumber());
        claim.setClaimId(Long.toString(claimByClaimNumber.getId()));
    }
}