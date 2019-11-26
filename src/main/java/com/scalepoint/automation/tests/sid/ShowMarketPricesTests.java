package com.scalepoint.automation.tests.sid;

import com.scalepoint.automation.grid.ValuationGrid;
import com.scalepoint.automation.pageobjects.dialogs.SettlementDialog;
import com.scalepoint.automation.pageobjects.pages.BestFitPage;
import com.scalepoint.automation.pageobjects.pages.TextSearchPage;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.utils.annotations.Jira;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.ClaimItem;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.request.Valuation;
import org.testng.annotations.Test;

import static com.scalepoint.automation.grid.ValuationGrid.Valuation.MARKET_PRICE;

@Jira("https://jira.scalepoint.com/browse/CHARLIE-588")
@RequiredSetting(type = FTSetting.SHOW_MARKET_PRICE)
public class ShowMarketPricesTests extends BaseTest {
    /**
     * GIVEN: FT "Show Market Price" OFF
     * WHEN: We are on text search page
     * THEN: Market Price column not displays on Test Search page
     * THEN: Market Price value not displays on Product Details page
     * THEN: Market Price is unavailable on the Product's Best Fit page
     * THEN: Market Price supplier not displays on Product Details page
     */
    @Test(dataProvider = "testDataProvider", description = "CHARLIE-588 Show Market Price (off), search for Product in Catalog, verify Best Fit Page")
    @RequiredSetting(type = FTSetting.SHOW_MARKET_PRICE, enabled = false)
    public void charlie_588_1_showMarketPriceDisabled(User user, Claim claim, ClaimItem claimItem) {
        loginAndCreateClaim(user, claim)
                .toTextSearchPage()
                .chooseCategory(claimItem.getCategoryBabyItems())
                .doAssert(TextSearchPage.Asserts::assertMarketPriceSortingInvisible)
                .sortOrderableFirst()
                .openProductDetailsOfFirstProduct()
                .doAssert(productDetails -> {
                    productDetails.assetMarketPriceSupplierInvisible();
                    productDetails.assertMarketPriceInvisible();
                })
                .closeProductDetails()
                .sortNonOrderableFirst()
                .toBestFitPage()
                .doAssert(BestFitPage.Asserts::assertMarketPriceInvisible);
    }

    /**
     * GIVEN: FT "Show Market Price" OFF
     * AND: ClaimHandler(CH) created claim
     * WHEN: Find product in Catalog
     * AND: CH add Product from the Catalog
     * THEN: Market Price valuation exists in the SID
     * THEN: Market Price Supplier name not displays in the SID
     */
    @Test(dataProvider = "testDataProvider", description = "CHARLIE-588 Show Market Price (off), add Product in Catalog, verify SID")
    @RequiredSetting(type = FTSetting.SHOW_MARKET_PRICE, enabled = false)
    public void charlie_588_3_showMarketPriceDisabled(User user, Claim claim, ClaimItem claimItem) {
        loginAndCreateClaim(user, claim)
                .toTextSearchPage()
                .chooseCategory(claimItem.getCategoryMobilePhones())
                .sortOrderableFirst()
                .openSidForFirstProduct()
                .setValuation(MARKET_PRICE)
                .toDialog()
                .doAssert(sid -> {
                    sid.assertMarketPriceSupplierInvisible();
                })
                .valuationGrid()
                .parseValuationRow(MARKET_PRICE)
                .back()
                .doAssert(asserts ->{
                    asserts.assertMarketPriceVisible();
                });
    }

    /**
     * GIVEN: FT "Show Market Price" ON
     * WHEN: Find product in Catalog
     * AND: Market Price selected as valuation
     * AND: CH search Product from the Catalog
     * THEN: Market Price column displays on Test Search page
     * THEN: It's possible to sort by Market Price column
     * THEN: Market Price value displays on Product Details page
     * THEN: Market Price supplier displays on Product Details page
     */
    @Test(dataProvider = "testDataProvider", description = "CHARLIE-588 Show Market Price (on), search for Product in catalog, verify Product Details Page")
    public void charlie_588_4_showMarketPriceEnabled(User user, Claim claim, ClaimItem claimItem) {
        loginAndCreateClaim(user, claim)
                .toTextSearchPage()
                .chooseCategory(claimItem.getCategoryMobilePhones())
                .sortMarketPricesAscending()
                .doAssert(TextSearchPage.Asserts::assertSortingMarketPriceAscendant)
                .sortMarketPricesDescending()
                .doAssert(TextSearchPage.Asserts::assertSortingMarketPriceDescendant)
                .sortOrderableFirst()
                .openProductDetailsOfFirstProduct()
                .doAssert(productDetails -> {
                    productDetails.assertMarketPriceVisible();
                    productDetails.assertMarketPriceSupplierVisible();
                })
                .closeProductDetails();
    }

    /**
     * GIVEN: FT "Show Market Price" ON
     * AND: ClaimHandler(CH) created/opened claim
     * WHEN: Find product in catalog,
     * AND: CH add Product from the Catalog
     * AND: Market Price selected as valuation
     * THEN: Market Price valuation exists in the SID
     * THEN: Market Price Supplier name displays in the SID
     * THEN: Market Price value displays on Product Info page
     * THEN: Market Price Supplier displays on Product Info page
     */
    @Test(dataProvider = "testDataProvider", description = "CHARLIE-588 Show Market Price (on), add Product from the catalog, verify SID")
    public void charlie_588_5_showMarketPriceEnabled(User user, Claim claim, ClaimItem claimItem) {
        loginAndCreateClaim(user, claim)
                .toTextSearchPage()
                .chooseCategory(claimItem.getCategoryMobilePhones())
                .sortOrderableFirst()
                .openProductDetailsOfFirstProduct()
                .doAssert(productDetails -> {
                    productDetails.assertMarketPriceVisible();
                    productDetails.assertMarketPriceSupplierVisible();
                })
                .closeProductDetails()
                .openSidForFirstProduct()
                .setValuation(MARKET_PRICE)
                .doAssert(ValuationGrid.Asserts::assertMarketPriceVisible);
    }
}
