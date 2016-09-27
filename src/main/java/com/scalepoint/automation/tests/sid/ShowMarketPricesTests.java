package com.scalepoint.automation.tests.sid;

import com.scalepoint.automation.BaseTest;
import com.scalepoint.automation.pageobjects.dialogs.ProductDetailsPage;
import com.scalepoint.automation.pageobjects.dialogs.SettlementDialog;
import com.scalepoint.automation.pageobjects.dialogs.SettlementDialog.Valuation;
import com.scalepoint.automation.pageobjects.pages.BestFitPage;
import com.scalepoint.automation.pageobjects.pages.TextSearchPage;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.utils.annotations.Bug;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.ClaimItem;
import com.scalepoint.automation.utils.data.entity.TextSearch;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.listeners.InvokedMethodListener;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class ShowMarketPricesTests extends BaseTest {
    /**
     * GIVEN: FT "Show Market Price" OFF
     * WHEN: ClaimHandler(CH) created claim
     * AND: CH search for Product from the Catalog
     * THEN: Market Price column not displays on Test Search page
     * THEN: Market Price value not displays on Product Details page
     * THEN: Market Price is unavailable on the Product's Best Fit page
     * THEN: Market Price supplier not displays on Product Details page
     */
    @Test(dataProvider = "testDataProvider", description = "CHARLIE-588 Show Market Price (off), search for Product in Catalog, verify Best Fit Page")
    @RequiredSetting(type = FTSetting.SHOW_MARKET_PRICE, enabled = false)
    public void charlie_588_1_showMarketPriceDisabled(User user, Claim claim, ClaimItem claimItem) {

        TextSearchPage textSearchPage = loginAndCreateClaim(user, claim).
                findInCatalogue().
                chooseCategory(claimItem.getExistingCat1_Born());

        assertFalse(textSearchPage.isMarketPriceVisible(), "Market Price is Visible");

        BestFitPage bestFitPage = textSearchPage.
                sortSearchResults().
                sortSearchResults().
                bestFit();

        assertFalse(bestFitPage.isMarketPriceVisible(), "Market Price is not visible");
    }

    /**
     * GIVEN: FT "Show Market Price" OFF
     * AND: ClaimHandler(CH) created claim
     * WHEN: Find product in Catalog
     * AND: CH search for Product from the Catalog
     * THEN: Market Price column not displays on Test Search page
     * THEN: Market Price value not displays on Product Details page
     * THEN: Market Price supplier not displays on Product Details page
     */

    @Test(dataProvider = "testDataProvider", description = "CHARLIE-588 Show Market Price (off), search for Product in Catalog, verify Product Details Page")
    @RequiredSetting(type = FTSetting.SHOW_MARKET_PRICE, enabled = false)
    public void charlie_588_2_showMarketPriceDisabled(User user, Claim claim, ClaimItem claimItem) {
        TextSearchPage textSearchPage = loginAndCreateClaim(user, claim).
                findInCatalogue().
                chooseCategory(claimItem.getExistingCat1_Born());

        assertFalse(textSearchPage.isMarketPriceVisible(), "Market Price is Visible");

        ProductDetailsPage productDetailsPage = textSearchPage.
                sortSearchResults().
                sortSearchResults().
                productDetails();

        assertFalse(productDetailsPage.isMarketPriceVisible(), "Market price is not visible");
        assertFalse(productDetailsPage.isMarketPriceSupplierVisible(), "Supplier is visible");

        productDetailsPage.closeWindow();
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
    @RequiredSetting(type = FTSetting.ENABLE_NEW_SETTLEMENT_ITEM_DIALOG)
    public void charlie_588_3_showMarketPriceDisabled(User user, Claim claim, ClaimItem claimItem) {
        SettlementDialog settlementDialog = loginAndCreateClaim(user, claim).
                findInCatalogue().
                chooseCategory(claimItem.getExistingCat3_Telefoni()).
                sortSearchResults().
                matchFirst().
                selectValuation(Valuation.MARKET_PRICE);

        assertTrue(settlementDialog.isMarketPriceVisible(), "Market price is not visible");
        assertFalse(settlementDialog.isMarketPriceSupplierVisible(), "Supplier is visible");
    }

    /**
     * GIVEN: FT "Show Market Price" OFF
     * AND: ClaimHandler(CH) created claim
     * WHEN: Find product in Catalog
     * AND: CH add Product from the Catalog
     * THEN: Market Price column not displays on Test Search page
     * THEN: Market Price value not displays on Product Details page
     * THEN: Market Price supplier not displays on Product Details page
     */
    @Test(dataProvider = "testDataProvider", description = "CHARLIE-588 Show Market Price (off), add Product in Catalog, verify Product Info Page")
    @RequiredSetting(type = FTSetting.SHOW_MARKET_PRICE, enabled = false)
    public void charlie_588_4_showMarketPriceDisabled(User user, Claim claim, ClaimItem claimItem) {
        TextSearchPage textSearchPage = loginAndCreateClaim(user, claim).
                findInCatalogue().
                chooseCategory(claimItem.getExistingCat3_Telefoni()).
                sortSearchResults();
        assertFalse(textSearchPage.isMarketPriceVisible(), "Market Price is not visible");

        ProductDetailsPage productDetailsPage = textSearchPage.productDetails();
        assertFalse(productDetailsPage.isMarketPriceVisible(), "Market price is visible");
        assertTrue(productDetailsPage.isMarketPriceSupplierVisible(), "Supplier is not visible");
        productDetailsPage.closeWindow();
    }

    /**
     * GIVEN: FT "Show Market Price" ON
     * AND: ClaimHandler(CH) created/opened claim
     * WHEN: Find product in Catalog
     * AND: Market Price selected as valuation
     * AND: CH search Product from the Catalog
     * THEN: Market Price column displays on Test Search page
     * THEN: It's possible to sort by Market Price column
     * THEN: Market Price value displays on Product Details page
     * THEN: Market Price supplier displays on Product Details page
     */
    @Bug(bug = "CHARLIE-1033")
    @Test(dataProvider = "testDataProvider", description = "CHARLIE-588 Show Market Price (on), search for Product in catalog, verify Product Details Page")
    @RequiredSetting(type = FTSetting.SHOW_MARKET_PRICE)
    public void charlie_588_6_showMarketPriceEnabled(User user, Claim claim, ClaimItem claimItem) {
        TextSearchPage textSearchPage = loginAndCreateClaim(user, claim).
                findInCatalogue().
                chooseCategory(claimItem.getExistingCat3_Telefoni()).
                sortSearchResults().sortMarketPrices();
        assertTrue(textSearchPage.isSortingMarketPriceAscendant(), "Ascendant sorting of Market Price does not work");

        textSearchPage.sortMarketPrices();
        assertTrue(textSearchPage.isSortingMarketPriceDescendant(), "Descendant sorting of Market Price does not work");

        ProductDetailsPage productDetailsPage = textSearchPage.productDetails();
        assertTrue(productDetailsPage.isMarketPriceVisible(), "Market price is not visible");
        assertTrue(productDetailsPage.isMarketPriceSupplierVisible(), "Market price is not visible");
        productDetailsPage.closeWindow();
    }

    /**
     * GIVEN: FT "Show Market Price" ON
     * AND: ClaimHandler(CH) created/opened claim
     * WHEN: Find product in catalog,
     * AND: CH add Product from the Catalog
     * AND: Market Price selected as valuation
     * THEN: Market Price valuation exists in the SID
     * THEN: Market Price Supplier name displays in the SID
     */
    @Test(dataProvider = "testDataProvider", description = "CHARLIE-588 Show Market Price (on), add Product from the catalog, verify SID")
    @RequiredSetting(type = FTSetting.SHOW_MARKET_PRICE)
    public void charlie_588_7_showMarketPriceEnabled(User user, Claim claim, ClaimItem claimItem, TextSearch textSearch) {
        SettlementDialog settlementDialog = loginAndCreateClaim(user, claim).
                findInCatalogue().
                chooseCategory(claimItem.getExistingCat3_Telefoni()).
                sortSearchResults().
                matchFirst().
                selectValuation(Valuation.MARKET_PRICE);

        assertTrue(settlementDialog.isMarketPriceVisible(), "Market price is not visible");
    }

    /**
     * GIVEN: FT "Show Market Price" ON
     * AND: ClaimHandler(CH) created/opened claim
     * WHEN: Find product in catalog,
     * AND: CH add Product from the Catalog
     * AND: Market Price selected as valuation
     * THEN: Market Price value displays on Product Info page
     * THEN: Market Price Supplier displays on Product Info page
     */
    @Bug(bug = "CHARLIE-1033")
    @Test(dataProvider = "testDataProvider", description = "CHARLIE-588 Show Market Price (on), add Product from catalog, verify Product info page")
    @RequiredSetting(type = FTSetting.SHOW_MARKET_PRICE)
    public void charlie_588_8_showMarketPriceEnabled(User user, Claim claim, ClaimItem claimItem) {
        ProductDetailsPage productDetailsPage = loginAndCreateClaim(user, claim).
                findInCatalogue().
                chooseCategory(claimItem.getExistingCat3_Telefoni()).
                sortSearchResults().
                productDetails();

        assertTrue(productDetailsPage.isMarketPriceVisible(), "Market price is not visible");
        assertTrue(productDetailsPage.isMarketPriceSupplierVisible(), "Supplier is not visible");
        productDetailsPage.closeWindow();
    }

}
