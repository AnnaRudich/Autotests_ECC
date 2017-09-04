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
@RequiredSetting(type = FTSetting.ENABLE_NEW_SETTLEMENT_ITEM_DIALOG)
public class CatalogTests extends BaseTest{
    @RequiredSetting(type = FTSetting.SHOW_MARKET_PRICE)
    @RequiredSetting(type = FTSetting.COMPARISON_OF_DISCOUNT_DEPRECATION)
    @Test(dataProvider = "testDataProvider", description = "Add BnO product from catalog")
    public void charlie543_addFromCatalogOrderableProductsWithBnO(User user, Claim claim){
        ProductInfo productInfo = SolrApi.findProductAsVoucherWithProductPriceLowerThanMarketPrice();//run me on upToDate Solr!

        loginAndCreateClaim(user, claim)
                .toTextSearchPage()
                .searchByProductName(productInfo.getModel())
                .openSidForFirstProduct()

                .doAssert(asserts -> {
                    asserts.assertMarketPriceVisible();
                    asserts.assertCatalogPriceInvisible();
                    asserts.assertIsLowestPriceValuationSelected(VOUCHER);
                });
    }
}
//does it make sense to have a Test class with one test?
