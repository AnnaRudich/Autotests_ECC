package com.scalepoint.automation.tests.search;

import com.scalepoint.automation.pageobjects.modules.textSearch.Attributes;
import com.scalepoint.automation.pageobjects.pages.TextSearchPage;
import com.scalepoint.automation.services.externalapi.SolrApi;
import com.scalepoint.automation.shared.ProductInfo;
import com.scalepoint.automation.shared.SortOrder;
import com.scalepoint.automation.testGroups.TestGroups;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.input.Claim;
import com.scalepoint.automation.utils.data.entity.input.ClaimItem;
import com.scalepoint.automation.utils.data.entity.input.Translations;
import com.scalepoint.automation.utils.data.entity.translations.TextSearch;
import org.testng.annotations.Test;

import static com.scalepoint.automation.pageobjects.modules.textSearch.Attributes.NFC_NEJ;
import static com.scalepoint.automation.pageobjects.modules.textSearch.Attributes.TOUCH_SCREEN_NEJ;
import static com.scalepoint.automation.services.externalapi.DatabaseApi.PriceConditions.*;

public class TextSearchTests extends BaseTest {

    private static final String SAMSUNG_GALAXY_S_7 = "samsung galaxy s7";

    @Test(groups = {TestGroups.SEARCH, TestGroups.TEXT_SEARCH}, dataProvider = "testDataProvider",
            description = "Check if search results match to the search target")
    public void charlie510_checkIfSearchResultsMathTarget(User user, Claim claim) {
        ProductInfo productInfo = SolrApi.findProduct(getXpricesForConditions(ORDERABLE, PRODUCT_AS_VOUCHER_ONLY_FALSE, INVOICE_PRICE_LOWER_THAN_MARKET_PRICE));

        loginFlow.loginAndCreateClaim(user, claim)
                .toTextSearchPage()
                .searchBySku(productInfo.getSku())
                .doAssert(
                        asserts -> {
                            asserts.assertSearchResultsContainsSearchModel(productInfo.getModel());
                            asserts.assertSearchResultsContainsSearchBrand(productInfo.getBrand());
                        });
    }

    @Test(groups = {TestGroups.SEARCH, TestGroups.TEXT_SEARCH}, dataProvider = "testDataProvider",
            description = "Check if results are matching selected suggestion")
    public void charlie510_useSuggestionsToFindProduct(User user, Claim claim) {
        TextSearchPage textSearchPage = loginFlow.loginAndCreateClaim(user, claim)
                .toTextSearchPage()
                .searchProductAndSelectFirstSuggestion("samsung");
        String searchText = textSearchPage.getSearchInputText();
        textSearchPage
                .waitForResultsLoad()
                .doAssert(
                        asserts -> asserts.assertSearchQueryContainsBrandAndModel(searchText)
                );
    }

    @Test(groups = {TestGroups.SEARCH, TestGroups.TEXT_SEARCH}, dataProvider = "testDataProvider",
            description = "Check if search results match to the selected brand and model")
    public void charlie510_selectBrandAndModel(User user, Claim claim, ClaimItem claimItem, Translations translations) {
        TextSearch textSearch = translations.getTextSearch();

        loginFlow.loginAndCreateClaim(user, claim)
                .toTextSearchPage()
                .searchByProductName(SAMSUNG_GALAXY_S_7)
                .chooseCategory(claimItem.getCategoryMobilePhones())
                .selectBrand(textSearch.getBrandSamsung())
                .selectModel(textSearch.getModelGalaxyS7())
                .doAssert(
                        asserts -> {
                            asserts.assertSearchResultsContainsSearchModel(textSearch.getModelGalaxyS7());
                            asserts.assertSearchResultsContainsSearchBrand(textSearch.getBrandSamsung());
                        });
    }

    @Test(groups = {TestGroups.SEARCH, TestGroups.TEXT_SEARCH}, dataProvider = "testDataProvider",
            description = "Check if search results match to the selected attributes")
    public void charlie510_selectAttributes(User user, Claim claim, ClaimItem claimItem, Translations translations) {
        int index = 0;
        Attributes[] attributes = {TOUCH_SCREEN_NEJ, NFC_NEJ};
        TextSearch textSearch = translations.getTextSearch();

        loginFlow.loginAndCreateClaim(user, claim)
                .toTextSearchPage()
                .searchByProductName(SAMSUNG_GALAXY_S_7)
                .chooseCategory(claimItem.getCategoryMobilePhones())
                .selectBrand(textSearch.getBrandSamsung())
                .openAttributesMenu()
                .selectAttribute(attributes)
                .searchAttributes()
                .openSpecificationForItem(index)
                .doAssert(
                        asserts -> asserts.assertAttributeResultContains(index, attributes)
                );
    }

    @Test(groups = {TestGroups.SEARCH, TestGroups.TEXT_SEARCH}, dataProvider = "testDataProvider",
            description = "Check if search results match to the selected group")
    public void charlie510_selectCategory(User user, Claim claim, Translations translations) {
        TextSearch textSearch = translations.getTextSearch();
        loginFlow.loginAndCreateClaim(user, claim)
                .toTextSearchPage()
                .chooseCategory(textSearch.getGroupChildren())
                .chooseCategory(textSearch.getSubgroupChildrenEquipment())
                .doAssert(
                        asserts -> asserts.assertSearchResultsContainsSearchCategory(textSearch.getSubgroupChildrenEquipment())
                );
    }

    @Test(groups = {TestGroups.SEARCH, TestGroups.TEXT_SEARCH}, dataProvider = "testDataProvider",
            description = "Check if search results match to the selected group")
    public void charlie510_createClaimManuallyFromSearch(User user, Claim claim, ClaimItem claimItem) {
        loginFlow.loginAndCreateClaim(user, claim)
                .toTextSearchPage()
                .openSid()
                .setBaseData(claimItem)
                .closeSidWithOk()
                .doAssert(
                        asserts -> asserts.assertItemIsPresent(claimItem.getTextFieldSP())
                );
    }

    @Test(groups = {TestGroups.SEARCH, TestGroups.TEXT_SEARCH}, dataProvider = "testDataProvider",
            description = "Check is sorting by popularity works")
    public void charlie516_checkSortingByPopularity(User user, Claim claim, ClaimItem claimItem, Translations translations) {
        TextSearch textSearch = translations.getTextSearch();
        String product = SAMSUNG_GALAXY_S_7;
        TextSearchPage tsp = loginFlow.loginAndCreateClaim(user, claim)
                .toTextSearchPage()
                .searchByProductName(product)
                .chooseCategory(claimItem.getCategoryMobilePhones());

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
        tsp.selectModel(textSearch.getModelGalaxyS7())
                .waitForResultsLoad()
                .doAssert(asserts -> asserts.assertAscendingPopularityChosen());

        /* new search should reset popularity sort so no icons will be present */
        tsp.searchByProductName(product)
                .waitForResultsLoad()
                .doAssert(
                        asserts -> asserts.assertNoPopularitySortChosen());
    }

    @Test(groups = {TestGroups.SEARCH, TestGroups.TEXT_SEARCH}, dataProvider = "testDataProvider",
            description = "Check if search by sku works")
    public void charlie510_checkSearchBySku(User user, Claim claim) {
        ProductInfo productInfo = SolrApi.findProduct(getXpricesForConditions(ORDERABLE, PRODUCT_AS_VOUCHER_ONLY_FALSE, INVOICE_PRICE_LOWER_THAN_MARKET_PRICE));

        loginFlow.loginAndCreateClaim(user, claim)
                .toTextSearchPage()
                .searchBySku(productInfo.getSku())
                .doAssert(
                        asserts -> {
                            asserts.assertSearchResultsContainsSearchModel(productInfo.getModel());
                            asserts.assertSearchResultsContainsSearchBrand(productInfo.getBrand());
                        });
    }

    @Test(groups = {TestGroups.SEARCH, TestGroups.TEXT_SEARCH}, dataProvider = "testDataProvider",
            description = "Check if Did you mean appears. Misspelling")
    public void charlie510_checkDidYouMean(User user, Claim claim, Translations translations) {
        TextSearch textSearch = translations.getTextSearch();
        loginFlow.loginAndCreateClaim(user, claim)
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

    @Test(groups = {TestGroups.SEARCH, TestGroups.TEXT_SEARCH}, dataProvider = "testDataProvider",
            description = "Check if Did you mean appears. Special characters")
    public void charlie510_checkDidYouMeanWithSpecialCharacters(User user, Claim claim, Translations translations) {
        TextSearch textSearch = translations.getTextSearch();
        loginFlow.loginAndCreateClaim(user, claim)
                .toTextSearchPage()
                .searchByProductName(textSearch.getBrokenQueryWithSpecialSymbols1())
                .doAssert(
                        asserts -> {
                            asserts.assertIsDidYouMeanDisplayed();
                        }
                )
                .clickOnDidYouMean()
                .waitForResultsLoad()
                .doAssert(
                        asserts -> {
                            asserts.assertSearchResultsContainsSearchBrand(textSearch.getBrandSamsung());
                        });
    }

    @Test(groups = {TestGroups.SEARCH, TestGroups.TEXT_SEARCH}, dataProvider = "testDataProvider",
            description = "Check category selection")
    public void charlie520_checkIfCorrectCategoryWasSelected(User user, Claim claim, Translations translations) {
        TextSearch textSearch = translations.getTextSearch();
        loginFlow.loginAndCreateClaim(user, claim)
                .toTextSearchPage()
                .searchByProductName(textSearch.getSubgroupVideocamera())
                .waitForResultsLoad()
                .doAssert(
                        asserts -> asserts.assertSearchResultsContainsSearchCategory(textSearch.getSubgroupVideocamera())
                ).searchByProductName(textSearch.getSubgroupCameraLenses())
                .waitForResultsLoad()
                .doAssert(
                        asserts -> asserts.assertSearchResultsContainsSearchCategory(textSearch.getSubgroupCameraLenses())
                );

    }

    @Test(groups = {TestGroups.SEARCH, TestGroups.TEXT_SEARCH}, dataProvider = "testDataProvider",
            description = "Check manual category selection")
    public void charlie520_checkIfManuallySelectedCategoryIsNotDiscardedAfterQuery(User user, Claim claim, Translations translations) {
        TextSearch textSearch = translations.getTextSearch();
        loginFlow.loginAndCreateClaim(user, claim)
                .toTextSearchPage()
                .chooseCategory(textSearch.getGroupChildren())
                .chooseCategory(textSearch.getSubgroupChildrenEquipment())
                .waitForResultsLoad()
                .doAssert(
                        asserts -> asserts.assertSearchResultsContainsSearchCategory(textSearch.getSubgroupChildrenEquipment())
                ).searchByProductName(textSearch.getSubgroupCameraLenses())
                .waitForResultsLoad()
                .doAssert(
                        TextSearchPage.Asserts::assertSearchResultsCategoryIsEmpty
                ).snapCategory()
                .waitForResultsLoad()
                .doAssert(
                        asserts -> asserts.assertSearchResultsContainsSearchCategory(textSearch.getSubgroupCameraLenses())
                );
    }
}
