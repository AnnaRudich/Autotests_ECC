package com.scalepoint.automation.tests.dnd2;

import com.scalepoint.automation.services.externalapi.SolrApi;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.shared.ProductInfo;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.utils.annotations.Jira;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.testng.annotations.Test;

import static com.scalepoint.automation.pageobjects.dialogs.SettlementDialog.Valuation.*;

@Jira("https://jira.scalepoint.com/browse/CHARLIE-526")
public class DnD2_MarketPriceLogicTests extends BaseTest {

    @RequiredSetting(type = FTSetting.SHOW_MARKET_PRICE)
    @Test(dataProvider = "testDataProvider", description = "Check if product and market price are visible")
    public void charlie526_checkIsProductPriceVisibleForCatalogPriceLowerMarketPrice(User user, Claim claim){

        ProductInfo productInfo = SolrApi.findProductInvoiceLowerMarket();

        loginAndCreateClaim(user, claim)
                .toTextSearchPage()
                .searchByProductName(productInfo.getModel())
                .openSidForFirstProduct()
                .doAssert(asserts -> {
                    asserts.assertMarketPriceVisible();
                    asserts.assertCatalogPriceVisible();
                });
    }

    @RequiredSetting(type = FTSetting.SHOW_MARKET_PRICE)
    @Test(dataProvider = "testDataProvider", description = "Check if product and market price are visible and when product price are equal or higher than market " +
            "then valuation should be them same in both")
    public void charlie526_checkIsProductPriceVisibleForCatalogPriceHigherMarketPrice(User user, Claim claim){

        ProductInfo productInfo = SolrApi.findProductInvoiceHigherMarket();

        loginAndCreateClaim(user, claim)
                .toTextSearchPage()
                .searchByProductName(productInfo.getModel())
                .openSidForFirstProduct()
                .doAssert(asserts -> {
                    asserts.assertMarketPriceVisible();
                    asserts.assertCatalogPriceVisible();
                    asserts.assertTotalPriceIsSameInRows(CATALOG_PRICE, MARKET_PRICE);
                });
    }
}


