package com.scalepoint.automation.tests.search;

import com.scalepoint.automation.pageobjects.modules.textSearch.Attributes;
import com.scalepoint.automation.pageobjects.pages.TextSearchPage;
import com.scalepoint.automation.services.externalapi.SolrApi;
import com.scalepoint.automation.shared.ProductInfo;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.TextSearch;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.testng.annotations.Test;

import static com.scalepoint.automation.pageobjects.modules.textSearch.Attributes.GPS_NEJ;
import static com.scalepoint.automation.pageobjects.modules.textSearch.Attributes.SMARTPHONE_NEJ;

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

    @Test(dataProvider = "testDataProvider", description = "Check if results are matching selected suggestion")
    public void charlie510_useSuggestionsToFindProduct(User user, Claim claim) {
        TextSearchPage textSearchPage = loginAndCreateClaim(user, claim)
                .toTextSearchPage()
                .searchProductAndSelectFirstSuggestion("samsung");
        String searchText = textSearchPage.getSearchInputText();
        textSearchPage.doAssert(
                asserts -> asserts.assertSearchQueryContainsBrandAndModel(searchText)
        );
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

    @Test(dataProvider = "testDataProvider", description = "Check if search results match to the selected attributes")
    public void charlie510_selectAttributes(User user, Claim claim, TextSearch textSearch) {
        int index = 0;
        Attributes[] attributes = {GPS_NEJ, SMARTPHONE_NEJ};

        loginAndCreateClaim(user, claim)
                .toTextSearchPage()
                .searchByProductName("phone")
                .selectBrand(textSearch.getBrand1())
                .openAttributesMenu()
                .selectAttribute(attributes)
                .searchAttributes()
                .openSpecificationForItem(index)
                .doAssert(
                        asserts -> asserts.assertAttributeResultContains(index, attributes)
                );
    }

    @Test(dataProvider = "testDataProvider", description = "Check if search results match to the selected group")
    public void charlie510_selectCategory(User user, Claim claim, TextSearch textSearch) {
        loginAndCreateClaim(user, claim)
                .toTextSearchPage()
                .chooseCategory(textSearch.getGroup1())
                .chooseCategory(textSearch.getSubgroup1())
                .doAssert(
                        asserts -> asserts.assertSearchResultsContainsSearchCategory(textSearch.getSubgroup1())
                );
    }
}
