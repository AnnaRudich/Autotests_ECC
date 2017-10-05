package com.scalepoint.automation.tests;

import com.scalepoint.automation.services.externalapi.SolrApi;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.shared.ProductInfo;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.testng.annotations.Test;

import static com.scalepoint.automation.pageobjects.dialogs.SettlementDialog.Valuation.CATALOG_PRICE;
import static com.scalepoint.automation.pageobjects.dialogs.SettlementDialog.Valuation.VOUCHER;

/**
 * this class verifies catalog pricing when no DnD FTs are enabled
 */

@SuppressWarnings("AccessStaticViaInstance")
public class CatalogPricesTests extends BaseTest{

    /**
     * this section covers ProductsAsVouchers "B&O products"
     */
    @RequiredSetting(type = FTSetting.SHOW_MARKET_PRICE)
    @Test(dataProvider = "testDataProvider", description = "Add BnO product with ProductInvoicePrice < Market price")
    public void charlie543_addOrderableProductsWithBnoWhenProductPriceLowerThanMarketPrice(User user, Claim claim){
        ProductInfo productInfo = SolrApi.findProductAsVoucherWithProductInvoiceLowerThanMarket();

        loginAndCreateClaim(user, claim)
                .toTextSearchPage()
                .searchBySku(productInfo.getSku())
                .sortOrderableFirst()
                .openSidForFirstProduct()

                .doAssert(asserts -> {
                    asserts.assertMarketPriceVisible();
                    asserts.assertValuationIsDisabled(CATALOG_PRICE);
                    asserts.assertIsLowestPriceValuationSelected(VOUCHER);
                    asserts.assertVoucherFaceValueIs(productInfo.getMarketPrice());//voucher is based on Market Price
                    //Voucher based on MarketPrice price exists, all discount given to the IC - ADD!
                });
    }
//Do not have test data at the moment
        @RequiredSetting(type = FTSetting.SHOW_MARKET_PRICE)
        @Test(enabled = false, dataProvider = "testDataProvider", description = "Add BnO product with ProductPrice = Market price")
        public void charlie543_addOrderableProductsWithBnoProductWhenProductInvoicePriceEqualsMarketPrice(User user, Claim claim){
            ProductInfo productInfo = SolrApi.findProductAsVoucherWithProductInvoiceEqualsMarketPrice();

            loginAndCreateClaim(user, claim)
                    .toTextSearchPage()
                    .searchByProductName(productInfo.getModel())
                    .sortOrderableFirst()
                    .openSidForFirstProduct()

                    .doAssert(asserts -> {
                        asserts.assertMarketPriceVisible();
                        asserts.assertCatalogPriceInvisible();
                        asserts.assertIsLowestPriceValuationSelected(VOUCHER);
                        asserts.assertVoucherFaceValueIs(productInfo.getMarketPrice());//voucher is based on Market Price
                    });
        }

    //Do not have test data at the moment
    @RequiredSetting(type = FTSetting.SHOW_MARKET_PRICE)
    @Test(enabled = false, dataProvider = "testDataProvider", description = "Add BnO product with Product Invoice Price > than Market price")
    public void charlie543_addOrderableProductsWithBnoProductWhenProductInvoicePriceHigherThanMarketPrice(User user, Claim claim){
        ProductInfo productInfo = SolrApi.findProductAsVoucherWithProductInvoiceHigherThanMarketPrice();

        loginAndCreateClaim(user, claim)
                .toTextSearchPage()
                .searchByProductName(productInfo.getModel())
                .sortOrderableFirst()
                .openSidForFirstProduct()

                .doAssert(asserts -> {
                    asserts.assertMarketPriceVisible();
                    asserts.assertCatalogPriceInvisible();
                    asserts.assertIsLowestPriceValuationSelected(VOUCHER);
                    asserts.assertVoucherFaceValueIs(productInfo.getMarketPrice());//voucher is based on Market Price
                });
    }
}
