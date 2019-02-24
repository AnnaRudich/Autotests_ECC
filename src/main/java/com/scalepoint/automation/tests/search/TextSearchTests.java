package com.scalepoint.automation.tests.search;

import com.scalepoint.automation.pageobjects.modules.textSearch.Attributes;
import com.scalepoint.automation.pageobjects.pages.TextSearchPage;
import com.scalepoint.automation.services.externalapi.SolrApi;
import com.scalepoint.automation.shared.ProductInfo;
import com.scalepoint.automation.shared.SortOrder;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.ClaimItem;
import com.scalepoint.automation.utils.data.entity.TextSearch;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.testng.annotations.Test;

import static com.scalepoint.automation.pageobjects.modules.textSearch.Attributes.DUAL_KAMERA_NEJ;
import static com.scalepoint.automation.pageobjects.modules.textSearch.Attributes.NFC_NEJ;
import static com.scalepoint.automation.services.externalapi.DatabaseApi.PriceConditions.*;

public class TextSearchTests extends BaseTest {

    @Test(dataProvider = "testDataProvider", description = "Check if search results match to the search target")
    public void charlie510_checkIfSearchResultsMathTarget(User user, Claim claim) {
        ProductInfo productInfo = SolrApi.findProduct(getXpricesForConditions(ORDERABLE, PRODUCT_AS_VOUCHER_ONLY_FALSE, INVOICE_PRICE_EQUALS_MARKET_PRICE));

        loginAndCreateClaim(user, claim)
                .toTextSearchPage()
                .searchBySku(productInfo.getSku())
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
        textSearchPage
                .waitForResultsLoad()
                .doAssert(
                        asserts -> asserts.assertSearchQueryContainsBrandAndModel(searchText)
                );
    }

    @Test(dataProvider = "testDataProvider", description = "Check if search results match to the selected brand and model")
    public void charlie510_selectBrandAndModel(User user, Claim claim, TextSearch textSearch) {
        loginAndCreateClaim(user, claim)
                .toTextSearchPage()
                .searchByProductName("samsung galaxy s7")
                .selectBrand(textSearch.getBrandSamsung())
                .selectModel(textSearch.getModel1())
                .doAssert(
                        asserts -> {
                            asserts.assertSearchResultsContainsSearchModel(textSearch.getModel1());
                            asserts.assertSearchResultsContainsSearchBrand(textSearch.getBrandSamsung());
                        });
    }

    @Test(dataProvider = "testDataProvider", description = "Check if search results match to the selected attributes")
    public void charlie510_selectAttributes(User user, Claim claim, TextSearch textSearch) {
        int index = 0;
        Attributes[] attributes = {DUAL_KAMERA_NEJ, NFC_NEJ};

        loginAndCreateClaim(user, claim)
                .toTextSearchPage()
                .searchByProductName("samsung galaxy s7")
                .selectBrand(textSearch.getBrandSamsung())
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

    @Test(dataProvider = "testDataProvider", description = "Check if search results match to the selected group")
    public void charlie510_createClaimManuallyFromSearch(User user, Claim claim, ClaimItem claimItem) {
        loginAndCreateClaim(user, claim)
                .toTextSearchPage()
                .openSid()
                .setBaseData(claimItem)
                .closeSidWithOk()
                .doAssert(
                        asserts -> asserts.assertItemIsPresent(claimItem.getTextFieldSP())
                );
    }

    @Test(dataProvider = "testDataProvider", description = "Check is sorting by popularity works")
    public void charlie516_checkSortingByPopularity(User user, Claim claim, TextSearch textSearch) {
        String brand = "samsung galaxy s7";
        TextSearchPage tsp = loginAndCreateClaim(user, claim)
                .toTextSearchPage()
                .searchByProductName(brand);

        tsp.sortPopularityDescending()
                .waitForResultsLoad()
                .doAssert(
                        asserts -> asserts.assertPopularityInCorrectOrder(SortOrder.DESCENDING));

        tsp.sortPopularityAscending()
                .waitForResultsLoad()
                .doAssert(
                        asserts -> asserts.assertPopularityInCorrectOrder(SortOrder.ASCENDING));

        /* popularity sort should be left the same with enabling brand filter*/
        tsp.selectBrand(textSearch.getBrandSamsung())
                .waitForResultsLoad()
                .doAssert(
                        asserts -> asserts.assertPopularityInCorrectOrder(SortOrder.ASCENDING));

        /* selecting model we have only one result shown so we can check only icon presence*/
        tsp.selectModel(textSearch.getModel1())
                .waitForResultsLoad()
                .doAssert(TextSearchPage.Asserts::assertAscendingPopularityChosen);

        /* new search should reset popularity sort so no icons will be present */
        tsp.searchByProductName(brand)
                .waitForResultsLoad()
                .doAssert(
                        TextSearchPage.Asserts::assertNoPopularitySortChosen);
    }

    @Test(dataProvider = "testDataProvider", description = "Check if search by sku works")
    public void charlie510_checkSearchBySku(User user, Claim claim) {
        ProductInfo productInfo = SolrApi.findProduct(getXpricesForConditions(ORDERABLE, PRODUCT_AS_VOUCHER_ONLY_FALSE, INVOICE_PRICE_EQUALS_MARKET_PRICE));

        loginAndCreateClaim(user, claim)
                .toTextSearchPage()
                .searchBySku(productInfo.getSku())
                .doAssert(
                        asserts -> {
                            asserts.assertSearchResultsContainsSearchModel(productInfo.getModel());
                            asserts.assertSearchResultsContainsSearchBrand(productInfo.getBrand());
                        });
    }

    @Test(dataProvider = "testDataProvider", description = "Check if search by sku works")
    public void charlie510_checkDidYouMean(User user, Claim claim, TextSearch textSearch) {
        loginAndCreateClaim(user, claim)
                .toTextSearchPage()
                .searchByProductName(textSearch.getBrokenQuery1())
                .doAssert(
                        TextSearchPage.Asserts::assertIsDidYouMeanDisplayed
                )
                .clickOnDidYouMean()
                .waitForResultsLoad()
                .doAssert(
                        asserts -> {
                            asserts.assertSearchResultsContainsSearchBrand(textSearch.getBrandSamsung());
                        });
    }

    @Test(dataProvider = "testDataProvider", description = "Check if search by sku works")
    public void charlie510_checkDidYouMeanWithSpecialCharacters(User user, Claim claim, TextSearch textSearch) {
        loginAndCreateClaim(user, claim)
                .toTextSearchPage()
                .searchByProductName(textSearch.getBrokenQueryWithSpecialSymbols1())
                .doAssert(
                        asserts -> {
                            asserts.assertIsDidYouMeanDisplayed();
                            asserts.assertQueryContainsDidYouMeanText(textSearch.getBrokenQueryWithSpecialSymbols1());
                        }
                )
                .clickOnDidYouMean()
                .waitForResultsLoad()
                .doAssert(
                        asserts -> {
                            asserts.assertSearchResultsContainsSearchBrand(textSearch.getBrandSamsung());
                        });
    }

    @Test(dataProvider = "testDataProvider", description = "Check category selection")
    public void charlie520_checkIfCorrectCategoryWasSelected(User user, Claim claim, TextSearch textSearch) {
        loginAndCreateClaim(user, claim)
                .toTextSearchPage()
                .searchByProductName(textSearch.getSubgroup3())
                .waitForResultsLoad()
                .doAssert(
                        asserts -> asserts.assertSearchResultsContainsSearchCategory(textSearch.getSubgroup3())
                ).searchByProductName(textSearch.getSubgroup2())
                .waitForResultsLoad()
                .doAssert(
                        asserts -> asserts.assertSearchResultsContainsSearchCategory(textSearch.getSubgroup2())
                );

    }

    @Test(dataProvider = "testDataProvider", description = "Check manual category selection")
    public void charlie520_checkIfManuallySelectedCategoryIsNotDiscardedAfterQuery(User user, Claim claim, TextSearch textSearch) {
        loginAndCreateClaim(user, claim)
                .toTextSearchPage()
                .chooseCategory(textSearch.getGroup1())
                .chooseCategory(textSearch.getSubgroup1())
                .waitForResultsLoad()
                .doAssert(
                        asserts -> asserts.assertSearchResultsContainsSearchCategory(textSearch.getSubgroup1())
                ).searchByProductName(textSearch.getSubgroup2())
                .waitForResultsLoad()
                .doAssert(
                        TextSearchPage.Asserts::assertSearchResultsCategoryIsEmpty
                ).snapCategory()
                .waitForResultsLoad()
                .doAssert(
                        asserts -> asserts.assertSearchResultsContainsSearchCategory(textSearch.getSubgroup2())
                );
    }
}
