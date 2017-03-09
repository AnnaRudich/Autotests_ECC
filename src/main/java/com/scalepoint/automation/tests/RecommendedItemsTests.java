package com.scalepoint.automation.tests;

import com.scalepoint.automation.pageobjects.dialogs.SettlementDialog;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.pageobjects.pages.TextSearchPage;
import com.scalepoint.automation.pageobjects.pages.oldshop.ShopProductSearchPage;
import com.scalepoint.automation.services.externalapi.SolrApi;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.shared.ProductInfo;
import com.scalepoint.automation.utils.annotations.Jira;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.testng.annotations.Test;

@Jira("https://jira.scalepoint.com/browse/CHARLIE-587")
@RequiredSetting(type = FTSetting.USE_UCOMMERCE_SHOP, enabled = false)
@RequiredSetting(type = FTSetting.ENABLE_NEW_SETTLEMENT_ITEM_DIALOG)
@RequiredSetting(type = FTSetting.SHOW_NOT_CHEAPEST_CHOICE_POPUP, enabled = false)
public class RecommendedItemsTests extends BaseTest {

    /**
     * GIVEN: Existing CH user U1, product P1 where Retail Price RP1 > product price PP1
     * GIVEN: product P2 where Retail Price RP2 == product price PP2
     * GIVEN: product P3 where Retail Price RP3 > product price PP3
     * WHEN: U1 adds P1 to the claim
     * WHEN: U1 adds P2 to the claim
     * WHEN: U1 adds P3 to the claim and selects RP3 as valuation type
     * WHEN: U1 completes claim, navigates to Shop, observes Catalog page
     * THEN: Search returns P1 with PP1 is located
     * THEN: Search returns P2 with RP2 is located
     * THEN: Search returns P3 with PP3 is located
     */
    @Test(dataProvider = "testDataProvider",
            description = "CHARLIE-587 Only product prices are displayed in Product catalog in Shop")
    public void charlie587_productPricesInShopCatalog(User user, Claim claim) {

        TextSearchPage textSearchPage = loginAndCreateClaim(user, claim).toTextSearchPage();

        ProductCashValue productInvoiceGtMarketCash = new ProductCashValue(findProductAndOpenSid(SolrApi.findProductInvoiceHigherMarket(), textSearchPage), true);

        ProductInfo productWithEqualPrices = SolrApi.findProductInvoiceEqualMarket();
        ProductCashValue productInvoiceEqualMarketCash = new ProductCashValue(findProductAndOpenSid(productWithEqualPrices, textSearchPage), true);

        ProductInfo productWithLtPrices = SolrApi.findProductInvoiceLowerMarket();
        SettlementDialog sid = findProductAndOpenSid(productWithLtPrices, textSearchPage);
        ProductCashValue productInvoiceLtMarketCash = new ProductCashValue(sid, false);

        SettlementPage settlementPage = sid.setValuation(SettlementDialog.Valuation.MARKET_PRICE).closeSidWithOk();
        ShopProductSearchPage shopProductSearchPage = settlementPage.toCompleteClaimPage()
                .fillClaimFormWithPassword(claim)
                .completeWithEmailAndLoginToShop()
                .toProductSearchPage();

        shopProductSearchPage
                .searchForProduct(productInvoiceGtMarketCash.name)
                .doAssert(searchPage -> searchPage.assertRequiredPriceIsDisplayed(productInvoiceGtMarketCash.cashCompensationFieldValue))
                .searchForProduct(productInvoiceEqualMarketCash.name)
                .doAssert(searchPage -> searchPage.assertRequiredPriceIsDisplayed(productInvoiceEqualMarketCash.cashCompensationFieldValue))
                .searchForProduct(productInvoiceLtMarketCash.name)
                .doAssert(searchPage -> searchPage.assertRequiredPriceIsDisplayed(productInvoiceLtMarketCash.cashCompensationFieldValue));
    }

    private SettlementDialog findProductAndOpenSid(ProductInfo productInfo, TextSearchPage textSearchPage) {
        return textSearchPage.searchByProductName(productInfo.getModel()).matchStrict(productInfo.getModel());
    }

    private class ProductCashValue {
        private Double cashCompensationFieldValue;
        private String name;

        ProductCashValue(SettlementDialog settlementDialog, boolean addToClaim) {
            cashCompensationFieldValue = settlementDialog.getCashCompensationValue();
            name = settlementDialog.getDescriptionText();
            if (addToClaim) {
                settlementDialog.selectOtherCategoryIfNotChosen().closeSidWithAdd();
            }
        }
    }
}
