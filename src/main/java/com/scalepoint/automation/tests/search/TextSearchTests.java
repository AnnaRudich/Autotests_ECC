package com.scalepoint.automation.tests.search;

import com.scalepoint.automation.pageobjects.modules.TextSearchAttributesMenu;
import com.scalepoint.automation.services.externalapi.SolrApi;
import com.scalepoint.automation.shared.ProductInfo;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.utils.annotations.RunOn;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.TextSearch;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.driver.DriverType;
import org.testng.annotations.Test;

import static com.scalepoint.automation.pageobjects.modules.TextSearchAttributesMenu.Attributes.GPS_NEJ;
import static com.scalepoint.automation.pageobjects.modules.TextSearchAttributesMenu.Attributes.SMARTPHONE_NEJ;

public class TextSearchTests extends BaseTest {

    @Test(dataProvider = "testDataProvider", description = "Check if search results match to the search target")
    public void charlie510_checkIfSearchResultsMathTarget(User user, Claim claim) {
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
    public void charlie510_selectBrandAndModel(User user, Claim claim, TextSearch textSearch) {
        loginAndCreateClaim(user, claim)
                .toTextSearchPage()
                .searchByProductName("phone")
                .selectBrand(textSearch.getBrand1())
                .selectModel(textSearch.getModel1())
                .doAssert(
                        asserts -> {
                            asserts.assertSearchResultsContainsSearchModel(textSearch.getModel1());
                            asserts.assertSearchResultsContainsSearchBrand(textSearch.getBrand1());
                        });
    }


    @RunOn(DriverType.IE_REMOTE)
    @Test(dataProvider = "testDataProvider", description = "Check if search results match to the selected brand and model")
    public void charlie510_selectAttributes(User user, Claim claim, TextSearch textSearch) {
        int index = 0;
        TextSearchAttributesMenu.Attributes[] attributes ={GPS_NEJ,SMARTPHONE_NEJ};

        loginAndCreateClaim(user, claim)
                .toTextSearchPage()
                .searchByProductName("phone")
                .selectBrand(textSearch.getBrand1())
                .openAttributesMenu()
                .selectAttribute(attributes)
                .searchAttributes()
                .openSpecificationForItem(index)
                .doAssert(
                        asserts -> asserts.assertAttributeResultsContains(index, attributes)
                );
    }
}
