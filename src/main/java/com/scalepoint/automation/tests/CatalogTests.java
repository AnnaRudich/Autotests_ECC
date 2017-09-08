package com.scalepoint.automation.tests;

import com.scalepoint.automation.services.externalapi.SolrApi;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.shared.ProductInfo;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.testng.annotations.Test;
import static com.scalepoint.automation.pageobjects.dialogs.SettlementDialog.Valuation.VOUCHER;


@SuppressWarnings("AccessStaticViaInstance")
public class CatalogTests extends BaseTest{
    @RequiredSetting(type = FTSetting.SHOW_MARKET_PRICE)
    @Test(enabled=false, dataProvider = "testDataProvider", description = "Add BnO product with ProductPrice lower than Market price")
    public void charlie543_addFromCatalogOrderableProductsWithBnoProductPriceLowerThanMarketPrice(User user, Claim claim){
        ProductInfo productInfo = SolrApi.findProductAsVoucherWithProductPriceLowerThanMarketPrice();

        loginAndCreateClaim(user, claim)
                .toTextSearchPage()
                .searchByProductName(productInfo.getModel())
                .sortOrderableFirst()
                .openSidForFirstProduct()

                .doAssert(asserts -> {
                    asserts.assertMarketPriceVisible();
                    asserts.assertCatalogPriceVisible();
                    asserts.assertIsLowestPriceValuationSelected(VOUCHER);
                });
    }
        @RequiredSetting(type = FTSetting.SHOW_MARKET_PRICE)
        @Test(enabled = false, dataProvider = "testDataProvider", description = "Add BnO product with ProductPrice equals Market price")
        public void charlie543_addFromCatalogOrderableProductsWithBnoProductPriceEqualsMarketPrice(User user, Claim claim){
            ProductInfo productInfo = SolrApi.findProductAsVoucherWithProductPriceEqualsMarketPrice();

            loginAndCreateClaim(user, claim)
                    .toTextSearchPage()
                    .searchByProductName(productInfo.getModel())
                    .sortOrderableFirst()
                    .openSidForFirstProduct()

                    .doAssert(asserts -> {
                        asserts.assertMarketPriceVisible();
                        asserts.assertCatalogPriceInvisible();
                        asserts.assertIsLowestPriceValuationSelected(VOUCHER);
                    });
        }
}
