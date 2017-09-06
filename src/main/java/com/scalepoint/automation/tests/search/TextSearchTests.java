package com.scalepoint.automation.tests.search;

import com.scalepoint.automation.services.externalapi.SolrApi;
import com.scalepoint.automation.shared.ProductInfo;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.utils.annotations.RunOn;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.driver.DriverType;
import org.testng.annotations.Test;

public class TextSearchTests extends BaseTest {

    @RunOn(DriverType.CHROME)
    @Test(dataProvider = "testDataProvider", description = "Check if search results match to the search target")
    public void charlie510_checkIfSearchResultsMathTarget(User user, Claim claim){
        ProductInfo productInfo = SolrApi.findProductInvoiceEqualMarket();

        loginAndCreateClaim(user, claim)
                .toTextSearchPage()
                .searchByProductName(productInfo.getModel())
            .doAssert(
                asserts -> {
                    asserts.assertSearchResultsContainsSearchModel(productInfo.getModel());
                    asserts.assertSearchResultsContainsSearchBrand(productInfo.getBrand());
                });
    }

    @Test(dataProvider = "testDataProvider", description = "Check if search results match to the selected brand and model")
    public void charlie510_selectBrandAndModel(User user, Claim claim){
        String brand = null;
        String model = null;

        loginAndCreateClaim(user, claim)
                .toTextSearchPage()
                .selectBrand(brand)
                .selectModel(model)
        .doAssert(
                asserts -> {
                    asserts.assertSearchResultsContainsSearchModel(model);
                    asserts.assertSearchResultsContainsSearchBrand(brand);
                });
    }
}
