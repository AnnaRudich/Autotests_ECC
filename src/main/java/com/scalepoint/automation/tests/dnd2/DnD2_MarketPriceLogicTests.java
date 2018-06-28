package com.scalepoint.automation.tests.dnd2;

import com.scalepoint.automation.services.externalapi.SolrApi;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.shared.ProductInfo;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.utils.annotations.Jira;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.apache.solr.client.solrj.SolrQuery;
import org.testng.annotations.Test;

import static com.scalepoint.automation.pageobjects.dialogs.SettlementDialog.Valuation.CATALOG_PRICE;
import static com.scalepoint.automation.pageobjects.dialogs.SettlementDialog.Valuation.MARKET_PRICE;
import static com.scalepoint.automation.services.externalapi.DatabaseApi.PriceConditions.*;

@Jira("https://jira.scalepoint.com/browse/CHARLIE-526")
public class DnD2_MarketPriceLogicTests extends BaseTest {

    @RequiredSetting(type = FTSetting.SHOW_MARKET_PRICE)
    @Test(dataProvider = "testDataProvider", description = "Check if product and market price are visible")
    public void charlie526_checkIsProductPriceVisibleForCatalogPriceLowerMarketPrice(User user, Claim claim){

        ProductInfo productInfo = SolrApi.findProduct(getXpricesForConditions(ORDERALBLE,PRODUCT_AS_VOUCHER_ONLY_FALSE,INVOICE_PRICE_LOWER_THAN_MARKET_PRICE));

        loginAndCreateClaim(user, claim)
                .toTextSearchPage()
                .searchBySku(productInfo.getSku())
                .sortOrderableFirst()
                .openSidForFirstProduct()
                .doAssert(asserts -> {
                    asserts.assertMarketPriceVisible();
                    asserts.assertCatalogPriceVisible();
                });
    }

    @RequiredSetting(type = FTSetting.SHOW_MARKET_PRICE)
    @Test(dataProvider = "testDataProvider", description = "Check if product and market price are visible and when product price are equal or higher than market " +
            "then valuation should be them same in both")
    public void charlie526_checkIfProductPriceVisibleForCatalogPriceHigherMarketPrice(User user, Claim claim){

        ProductInfo productInfo = SolrApi.findProduct(getXpricesForConditions(ORDERALBLE, PRODUCT_AS_VOUCHER_ONLY_FALSE, INVOICE_PRICE_HIGHER_THAN_MARKET_PRICE));

        loginAndCreateClaim(user, claim)
                .toTextSearchPage()
                .searchBySku(productInfo.getSku())
                .sortOrderableFirst()
                .openSidForFirstProduct()
                .doAssert(asserts -> {
                    asserts.assertMarketPriceVisible();
                    asserts.assertCatalogPriceVisible();
                    asserts.assertTotalPriceIsSameInRows(CATALOG_PRICE, MARKET_PRICE);
                });
    }

    @RequiredSetting(type = FTSetting.SHOW_MARKET_PRICE)
    @Test(dataProvider = "testDataProvider", description = "Add product with ProductPrice < Market price")
    public void charlie526_addProductWhenProductPriceLowerThanMarketPrice(User user, Claim claim){
        ProductInfo productInfo = SolrApi.findProduct(getXpricesForConditions(ORDERALBLE,PRODUCT_AS_VOUCHER_ONLY_FALSE,INVOICE_PRICE_LOWER_THAN_MARKET_PRICE));

        loginAndCreateClaim(user, claim)
                .toTextSearchPage()
                .searchBySku(productInfo.getSku())
                .sortOrderableFirst()
                .openSidForFirstProduct()

                .doAssert(asserts -> {
                    asserts.assertMarketPriceVisible();
                    asserts.assertCatalogPriceVisible();
                    asserts.assertIsLowestPriceValuationSelected(CATALOG_PRICE);
                });
    }

    @RequiredSetting(type = FTSetting.SHOW_MARKET_PRICE)
    @Test(dataProvider = "testDataProvider", description = "Add product with ProductPrice = Market price")
    public void charlie526_addProductWhenProductPriceEqualsMarketPrice(User user, Claim claim){
        ProductInfo productInfo = SolrApi.findProduct(getXpricesForConditions(ORDERALBLE, PRODUCT_AS_VOUCHER_ONLY_FALSE,MARKET_PRICE_EQUAL_INVOICE_PRICE));

        loginAndCreateClaim(user, claim)
                .toTextSearchPage()
                .searchBySku(productInfo.getSku())
                .sortOrderableFirst()
                .openSidForFirstProduct()

                .doAssert(asserts -> {
                    asserts.assertMarketPriceVisible();
                    asserts.assertCatalogPriceVisible();
                    asserts.assertTotalPriceIsSameInRows(CATALOG_PRICE, MARKET_PRICE);
                    asserts.assertIsLowestPriceValuationSelected(CATALOG_PRICE);
                });
    }

    @RequiredSetting(type = FTSetting.SHOW_MARKET_PRICE)
    @Test(dataProvider = "testDataProvider", description = "Add product with ProductPrice > Market price")
    public void charlie526_addProductWhenProductPriceHigherThanMarketPrice(User user, Claim claim){
        ProductInfo productInfo = SolrApi.findProduct(getXpricesForConditions(ORDERALBLE, PRODUCT_AS_VOUCHER_ONLY_FALSE, INVOICE_PRICE_HIGHER_THAN_MARKET_PRICE));

        loginAndCreateClaim(user, claim)
                .toTextSearchPage()
                .searchBySku(productInfo.getSku())
                .sortOrderableFirst()
                .openSidForFirstProduct()

                .doAssert(asserts -> {
                    asserts.assertMarketPriceVisible();
                    asserts.assertCatalogPriceVisible();
                    asserts.assertTotalPriceIsSameInRows(CATALOG_PRICE, MARKET_PRICE);
                    asserts.assertIsLowestPriceValuationSelected(CATALOG_PRICE);
                });
    }
}


