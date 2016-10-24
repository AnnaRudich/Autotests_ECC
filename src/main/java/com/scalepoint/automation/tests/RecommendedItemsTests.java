package com.scalepoint.automation.tests;

import com.scalepoint.automation.BaseTest;
import com.scalepoint.automation.domain.ProductInfo;
import com.scalepoint.automation.pageobjects.dialogs.SettlementDialog;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.pageobjects.pages.TextSearchPage;
import com.scalepoint.automation.pageobjects.pages.oldshop.ShopCataloguePage;
import com.scalepoint.automation.services.externalapi.SolrApi;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.testng.Assert;
import org.testng.annotations.Test;

@RequiredSetting(type = FTSetting.USE_UCOMMERCE_SHOP, enabled = false)
@RequiredSetting(type = FTSetting.ENABLE_NEW_SETTLEMENT_ITEM_DIALOG)
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
        ProductInfo productInvoiceGtMarket = SolrApi.findProductInvoiceBiggerMarket();
        TextSearchPage textSearchPage = loginAndCreateClaim(user, claim).toTextSearchPage();
        ProductCashValue productInvoiceGtMarketCash = new ProductCashValue(findProductAndOpenSid(productInvoiceGtMarket, textSearchPage), true);

        ProductInfo productWithEqualPrices = SolrApi.findProductInvoiceEqualMarket();
        ProductCashValue productInvoiceEqualMarketCash = new ProductCashValue(findProductAndOpenSid(productWithEqualPrices, textSearchPage), true);

        ProductInfo productWithLtPrices = SolrApi.findProductInvoiceLowerMarket();
        SettlementDialog sid = findProductAndOpenSid(productWithLtPrices, textSearchPage);
        ProductCashValue productInvoiceLtMarketCash = new ProductCashValue(sid, false);

        SettlementPage settlementPage = sid.selectValuation(SettlementDialog.Valuation.MARKET_PRICE).ok();
        ShopCataloguePage shopCataloguePage = settlementPage.toCompleteClaimPage().
                completeWithEmailAndLoginToShop("12341234").
                toCatalogue();

        shopCataloguePage.searchForProduct(productInvoiceGtMarketCash.getName());
        Assert.assertTrue(shopCataloguePage.isRequiredPriceDisplayed(productInvoiceGtMarketCash.cashCompensationFieldValue));

        shopCataloguePage.searchForProduct(productInvoiceEqualMarketCash.getName());
        Assert.assertTrue(shopCataloguePage.isRequiredPriceDisplayed(productInvoiceEqualMarketCash.cashCompensationFieldValue));

        shopCataloguePage.searchForProduct(productInvoiceLtMarketCash.getName());
        Assert.assertTrue(shopCataloguePage.isRequiredPriceDisplayed(productInvoiceLtMarketCash.cashCompensationFieldValue));
    }

    private SettlementDialog findProductAndOpenSid(ProductInfo productInvoiceGtMarket, TextSearchPage textSearchPage) {
        return textSearchPage.searchByProductName(productInvoiceGtMarket.getModel()).matchFirst();
    }

    class ProductCashValue {
        private String cashCompensationFieldValue;
        private String name;

        public ProductCashValue(SettlementDialog settlementDialog, boolean addToClaim) {
            cashCompensationFieldValue = RecommendedItemsTests.this.toString(settlementDialog.cashCompensationValue());
            name = settlementDialog.getDescriptionText();
            if (addToClaim) {
                settlementDialog.add();
            }
        }

        public String getCashCompensationFieldValue() {
            return cashCompensationFieldValue;
        }

        public String getName() {
            return name;
        }
    }
}
