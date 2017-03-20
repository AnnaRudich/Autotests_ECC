package com.scalepoint.automation.tests;

import com.scalepoint.automation.pageobjects.dialogs.SettlementDialog;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.pageobjects.pages.TextSearchPage;
import com.scalepoint.automation.pageobjects.pages.oldshop.ShopWelcomePage;
import com.scalepoint.automation.services.externalapi.SolrApi;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.shared.ProductInfo;
import com.scalepoint.automation.utils.annotations.Jira;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.testng.annotations.Test;

import java.util.function.Supplier;

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
     *
     * ecc3278_productPricesInShopWelcome
     */
    @Test(dataProvider = "testDataProvider",
            description = "CHARLIE-587 Only product prices are displayed in Product catalog in Shop")
    public void ecc3278_productPricesInShopCatalog(User user, Claim claim) {

        TextSearchPage textSearchPage = loginAndCreateClaim(user, claim).toTextSearchPage();

        ProductCashValue productInvoiceGtMarketCash = findProductAdnAddToClaim(SolrApi::findProductInvoiceHigherMarket, textSearchPage, null);
        ProductCashValue productInvoiceEqualMarketCash = findProductAdnAddToClaim(SolrApi::findProductInvoiceEqualMarket, textSearchPage, null);
        ProductCashValue productInvoiceLtMarketCash = findProductAdnAddToClaim(SolrApi::findProductInvoiceLowerMarket, textSearchPage, SettlementDialog.Valuation.MARKET_PRICE);

        ShopWelcomePage shopWelcomePage = textSearchPage.toSettlementPage()
                .toCompleteClaimPage()
                .fillClaimFormWithPassword(claim)
                .completeWithEmailAndLoginToShop();

        shopWelcomePage.doAssert(welcomePage->{
            welcomePage.assertItemWithPricePresent(productInvoiceGtMarketCash.name, productInvoiceGtMarketCash.lowestPrice);
            welcomePage.assertItemWithPricePresent(productInvoiceEqualMarketCash.name, productInvoiceEqualMarketCash.lowestPrice);
            welcomePage.assertItemWithPricePresent(productInvoiceLtMarketCash.name, productInvoiceLtMarketCash.lowestPrice);
        });

        shopWelcomePage.toProductSearchPage()
                .searchForProduct(productInvoiceGtMarketCash.name)
                .doAssert(searchPage -> searchPage.assertRequiredPriceIsDisplayed(productInvoiceGtMarketCash.lowestPrice))
                .searchForProduct(productInvoiceEqualMarketCash.name)
                .doAssert(searchPage -> searchPage.assertRequiredPriceIsDisplayed(productInvoiceEqualMarketCash.lowestPrice))
                .searchForProduct(productInvoiceLtMarketCash.name)
                .doAssert(searchPage -> searchPage.assertRequiredPriceIsDisplayed(productInvoiceLtMarketCash.lowestPrice));
    }

    /**
     * GIVEN: Existing CH user U1, product P1 where Retail Price RP1 > product price PP1
     * GIVEN: product P2 where Retail Price RP2 == product price PP2
     * product P3 where Retail Price RP3 > product price PP3 and Retail price is elected as valuation in SID
     * WHEN: U1 completes claim, navigates to Shop, observes Catalog page
     * THEN: Search returns P1 with PP1 is located
     * THEN: Search returns P2 with RP2 is located
     * THEN: Search returns P3 with PP3 is located
     */
    @Test(dataProvider = "testDataProvider",
            description = "ECC-3278 Only product prices are displayed in Product catalog in Shop, we don't add products to the claim")
    public void ecc3278_productPricesInShopCatalogNotAdding(User user, Claim claim) {

        SettlementPage settlementPage = loginAndCreateClaim(user, claim);

        ProductInfo productInvoiceHigherMarket = SolrApi.findProductInvoiceHigherMarket();
        ProductInfo productInvoiceEqualMarket = SolrApi.findProductInvoiceEqualMarket();
        ProductInfo productInvoiceLowerMarket = SolrApi.findProductInvoiceLowerMarket();

        ShopWelcomePage shopWelcomePage = settlementPage
                .toCompleteClaimPage()
                .fillClaimFormWithPassword(claim)
                .completeWithEmailAndLoginToShop();

        shopWelcomePage.doAssert(welcomePage->{
            welcomePage.assertItemNotPresent(productInvoiceHigherMarket.getModel());
            welcomePage.assertItemNotPresent(productInvoiceEqualMarket.getModel());
            welcomePage.assertItemNotPresent(productInvoiceLowerMarket.getModel());
        });

        shopWelcomePage.toProductSearchPage()
                .searchForProduct(productInvoiceHigherMarket.getModel())
                .doAssert(searchPage -> searchPage.assertRequiredPriceIsDisplayed(productInvoiceHigherMarket.getInvoicePrice()))
                .searchForProduct(productInvoiceEqualMarket.getModel())
                .doAssert(searchPage -> searchPage.assertRequiredPriceIsDisplayed(productInvoiceEqualMarket.getInvoicePrice()))
                .searchForProduct(productInvoiceLowerMarket.getModel())
                .doAssert(searchPage -> searchPage.assertRequiredPriceIsDisplayed(productInvoiceLowerMarket.getInvoicePrice()));
    }

    private ProductCashValue findProductAdnAddToClaim(Supplier<ProductInfo> searchStrategy, TextSearchPage textSearchPage, SettlementDialog.Valuation valuation) {
        ProductInfo productInfo = searchStrategy.get();
        SettlementDialog settlementDialog = textSearchPage
                .searchByProductName(productInfo.getModel())
                .matchStrict(productInfo.getModel());

        Double cashCompensationFieldValue = settlementDialog.getCashCompensationValue();
        Double lowestPrice = productInfo.getLowestPrice();
        String name = settlementDialog.getDescriptionText();
        settlementDialog.selectOtherCategoryIfNotChosen();
        if (valuation !=null) {
            settlementDialog.setValuation(valuation);
        }
        settlementDialog.closeSidWithAdd();
        return new ProductCashValue(cashCompensationFieldValue, lowestPrice, name);
    }


    private class ProductCashValue {
        private Double invoicePrice;
        private Double lowestPrice;
        private String name;

        ProductCashValue(Double invoicePrice, Double lowestPrice, String name) {
            this.invoicePrice = invoicePrice;
            this.lowestPrice = lowestPrice;
            this.name = name;
        }
    }
}
