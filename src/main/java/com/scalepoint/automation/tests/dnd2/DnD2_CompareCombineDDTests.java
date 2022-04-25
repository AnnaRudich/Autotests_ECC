package com.scalepoint.automation.tests.dnd2;

import com.scalepoint.automation.pageobjects.dialogs.SettlementDialog;
import com.scalepoint.automation.pageobjects.modules.SettlementSummary;
import com.scalepoint.automation.pageobjects.pages.MailsPage;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.services.externalapi.DatabaseApi;
import com.scalepoint.automation.services.externalapi.SolrApi;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.shared.ProductInfo;
import com.scalepoint.automation.testGroups.TestGroups;
import com.scalepoint.automation.tests.BaseUITest;
import com.scalepoint.automation.utils.Constants;
import com.scalepoint.automation.utils.annotations.Jira;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.TestDataActions;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.input.Claim;
import com.scalepoint.automation.utils.data.entity.input.ClaimItem;
import com.scalepoint.automation.utils.threadlocal.Browser;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.time.Year;
import java.util.Objects;

import static com.scalepoint.automation.grid.ValuationGrid.Valuation.*;
import static com.scalepoint.automation.services.externalapi.DatabaseApi.PriceConditions.*;

/**
 * The class represents smoke tests set for D&D2 functionality
 * run only on DK
 */

@Jira("https://jira.scalepoint.com/browse/CHARLIE-586")
@RequiredSetting(type = FTSetting.COMBINE_DISCOUNT_DEPRECATION, enabled = false)
public class DnD2_CompareCombineDDTests extends BaseUITest {

    private static final String ADD_FROM_CATALOG_WHERE_PRODUCT_PRICE_IS_HIGHER_THAN_MARKET_PRIDE_DATA_PROVIDER = "addFromCatalogWhereProductPriceIsHigherThanMarketPriceTest";
    private static final String DEPRECATION_DATA_PROVIDER = "deprecationDataProvider";

    @DataProvider(name = ADD_FROM_CATALOG_WHERE_PRODUCT_PRICE_IS_HIGHER_THAN_MARKET_PRIDE_DATA_PROVIDER)
    public static Object[][] addFromCatalogWhereProductPriceIsHigherThanMarketPriceTest(Method method) {

        DatabaseApi.PriceConditions[] priceConditions = {ORDERABLE, PRODUCT_AS_VOUCHER_ONLY_FALSE, INVOICE_PRICE_HIGHER_THAN_MARKET_PRICE};

        return new Object[][]{

                TestDataActions.getTestDataWithExternalParameters(method, priceConditions,10).toArray()
        };
    }

    @RequiredSetting(type = FTSetting.SHOW_MARKET_PRICE)
    @RequiredSetting(type = FTSetting.COMPARISON_OF_DISCOUNT_DEPRECATION)
    @Test(groups = {TestGroups.DND2, TestGroups.COMPARE_COMBINE_DD}, dataProvider = ADD_FROM_CATALOG_WHERE_PRODUCT_PRICE_IS_HIGHER_THAN_MARKET_PRIDE_DATA_PROVIDER,
            description = "Add claim with product from catalog where market price is higher than product price")
    public void addFromCatalogWhereProductPriceIsHigherThanMarketPriceTest(User user, Claim claim, DatabaseApi.PriceConditions[] priceConditions, int deprecationValue) {

        ProductInfo productInfo = SolrApi.findProduct(getXpricesForConditions(priceConditions));

        loginFlow.loginAndCreateClaim(user, claim)
                .toTextSearchPage()
                .searchBySku(productInfo.getSku())
                .sortOrderableFirst()
                .openSidForFirstProduct()
                .setDepreciation(deprecationValue)
                .valuationGrid()
                .doAssert(asserts -> {
                    asserts.assertMarketPriceVisible();
                    asserts.assertCatalogPriceVisible();
                    asserts.assertPriceIsSameInTwoColumns(CATALOG_PRICE);
                    asserts.assertCashCompensationIsDepreciated(deprecationValue, MARKET_PRICE);
                    asserts.assertIsLowestPriceValuationSelected(MARKET_PRICE, CATALOG_PRICE);
                });
    }

    @DataProvider(name = DEPRECATION_DATA_PROVIDER)
    public static Object[][] deprecationDataProvider(Method method) {

        return new Object[][]{

                TestDataActions.getTestDataWithExternalParameters(method, 10).toArray()
        };
    }

    @RequiredSetting(type = FTSetting.SHOW_MARKET_PRICE)
    @RequiredSetting(type = FTSetting.COMPARISON_OF_DISCOUNT_DEPRECATION)
    @Test(groups = {TestGroups.DND2, TestGroups.COMPARE_COMBINE_DD}, enabled = false, dataProvider = DEPRECATION_DATA_PROVIDER,
            description = "Add claim with product from catalog where market price equals product price")
    public void addFromCatalogWhereProductPriceIsEqualMarketPriceAndHaveOnlyVoucherReplacementTest(User user, Claim claim, int deprecationValue) {
        ProductInfo productInfo = SolrApi.findProduct(getXpricesForConditions(ORDERABLE, PRODUCT_AS_VOUCHER_ONLY, INVOICE_PRICE_EQUALS_MARKET_PRICE));

        loginFlow.loginAndCreateClaim(user, claim)
                .toTextSearchPage()
                .searchBySku(productInfo.getSku())
                .sortOrderableFirst()
                .openSidForFirstProduct()
                .setDepreciation(deprecationValue)
                .setDescription("test")
                .valuationGrid()
                .doAssert(asserts -> {
                    asserts.assertMarketPriceVisible();
                    asserts.assertCatalogPriceVisible();
                    asserts.assertPriceIsSameInTwoColumns(CATALOG_PRICE);
                    asserts.assertCashCompensationIsDepreciated(deprecationValue, MARKET_PRICE);
                    asserts.assertIsLowestPriceValuationSelected(VOUCHER);
                });
    }

    @RequiredSetting(type = FTSetting.DO_NOT_DEPRECIATE_CUSTOMER_DEMAND, enabled = false, isDefault = true)
    @RequiredSetting(type = FTSetting.COMPARISON_OF_DISCOUNT_DEPRECATION)
    @Test(groups = {TestGroups.DND2, TestGroups.COMPARE_COMBINE_DD}, dataProvider = DEPRECATION_DATA_PROVIDER,
            description = "Add claim item manually and check if new price, customer demand are discounted")
    public void charlie586_addManually(User user, Claim claim, ClaimItem claimItem, int deprecationValue) {

        loginFlow.loginAndCreateClaim(user, claim)
                .openSidAndFill(cat -> cat.withCategory(claimItem.getCategoryBabyItems())
                        .withNewPrice(claimItem.getTrygNewPrice())
                        .withCustomerDemandPrice(claimItem.getCustomerDemand())
                )
                .openAddValuationForm()
                .addValuationType(claimItem.getValuationTypeUsedPrice())
                .addValuationPrice(claimItem.getUsedPrice())
                .closeValuationDialogWithOk()
                .setDescription(claimItem.getTextFieldSP())
                .setDepreciation(deprecationValue)
                .doAssert(asserts -> {
                    asserts.assertIsVoucherDiscountApplied(claimItem.getTrygNewPrice());
                })
                .valuationGrid()
                .doAssert(asserts -> {
                    asserts.assertCashCompensationIsDepreciated(deprecationValue, NEW_PRICE);
                    asserts.assertCashCompensationIsDepreciated(deprecationValue, CUSTOMER_DEMAND);
                    asserts.assertPriceIsSameInTwoColumns(USED_PRICE);

                    asserts.assertIsLowestPriceValuationSelected(VOUCHER, NEW_PRICE,
                            USED_PRICE, CUSTOMER_DEMAND);
                });
    }

    @RequiredSetting(type = FTSetting.COMPARISON_OF_DISCOUNT_DEPRECATION)
    @RequiredSetting(type = FTSetting.DO_NOT_DEPRECIATE_CUSTOMER_DEMAND)
    @Test(groups = {TestGroups.DND2, TestGroups.COMPARE_COMBINE_DD}, dataProvider = DEPRECATION_DATA_PROVIDER,
            description = "Add claim item manually and check if customer demand is not discounted. FT Do not depreciate CD is on")
    public void charlie586_addManually_DoNotDepreciateCustomerDemandIsOn(User user, Claim claim, ClaimItem claimItem, int deprecationValue) {

        Double initialCustomerDemand = claimItem.getCustomerDemand();

        loginFlow.loginAndCreateClaim(user, claim)
                .openSidAndFill(cat -> cat.withCategory(claimItem.getCategoryBabyItems()))
                .setNewPrice(claimItem.getTrygNewPrice())
                .setCustomerDemand(initialCustomerDemand)
                .setDescription(claimItem.getTextFieldSP())
                .setDepreciation(deprecationValue)
                .doAssert(asserts -> {
                    asserts.assertIsVoucherDiscountApplied(claimItem.getTrygNewPrice());
                })
                .valuationGrid()
                .doAssert(asserts -> {
                    asserts.assertCashCompensationIsDepreciated(deprecationValue, NEW_PRICE);
                    asserts.assertCashCompensationIsNotDepreciated(CUSTOMER_DEMAND, initialCustomerDemand);
                    asserts.assertIsLowestPriceValuationSelected(VOUCHER, NEW_PRICE, CUSTOMER_DEMAND);
                });
    }

    @RequiredSetting(type = FTSetting.COMPARISON_OF_DISCOUNT_DEPRECATION)
    @RequiredSetting(type = FTSetting.ENABLE_DEPRECIATION_COLUMN)
    @Test(groups = {TestGroups.DND2, TestGroups.COMPARE_COMBINE_DD}, dataProvider = "testDataProvider",
            description = "Add items manually and check if depreciation is lower than voucher discount correct item is selected")
    public void charlie586_addManuallyWithVoucherAndDepreciationLowerThanVoucherDiscount(User user, Claim claim, ClaimItem claimItem) {

        SettlementDialog settlementDialog = loginFlow.loginAndCreateClaim(user, claim)
                .openSidAndFill(cat -> cat.withCategory(claimItem.getCategoryBabyItems()));
        SettlementPage settlementPage = settlementDialog.setNewPrice(claimItem.getTrygNewPrice())
                .setDescription(claimItem.getTextFieldSP())
                .setDepreciation(settlementDialog.getVoucherPercentage() / 2)
                .closeSidWithOk();
        Objects.requireNonNull(Browser.driver()).navigate().refresh();
        settlementPage.parseFirstClaimLine()
                .editLine()
                .valuationGrid()
                .doAssert(asserts -> {
                    asserts.assertCashCompensationIsDepreciated(settlementDialog.getVoucherPercentage() / 2, NEW_PRICE);
                    asserts.assertIsLowestPriceValuationSelected(VOUCHER, NEW_PRICE);
                })
                .getValuationRow(VOUCHER)
                .doAssert(asserts -> asserts.assertDepreciationPercentageIs(0));
    }

    @RequiredSetting(type = FTSetting.COMPARISON_OF_DISCOUNT_DEPRECATION)
    @RequiredSetting(type = FTSetting.ENABLE_DEPRECIATION_COLUMN)
    @Test(groups = {TestGroups.DND2, TestGroups.COMPARE_COMBINE_DD}, dataProvider = "testDataProvider",
            description = "Add items manually and check if depreciation is bigger than voucher discount correct item is selected")
    public void charlie586_addManuallyWithVoucherAndDepreciationHigherThanVoucherDiscount(User user, Claim claim, ClaimItem claimItem) {

        SettlementDialog settlementDialog = loginFlow.loginAndCreateClaim(user, claim)
                .openSidAndFill(cat -> cat.withCategory(claimItem.getCategoryBabyItems()))
                .setNewPrice(claimItem.getTrygNewPrice())
                .setDescription(claimItem.getTextFieldSP());
        int depreciationPercentage = settlementDialog.getVoucherPercentage() * 2;
        settlementDialog.setDepreciation(depreciationPercentage)
                .valuationGrid()
                .doAssert(asserts -> {
                    asserts.assertCashCompensationIsDepreciated(depreciationPercentage, NEW_PRICE);
                    asserts.assertIsLowestPriceValuationSelected(VOUCHER, NEW_PRICE);
                })
                .getValuationRow(NEW_PRICE)
                .doAssert(asserts -> asserts.assertDepreciationPercentageIs(depreciationPercentage));
    }

    @RequiredSetting(type = FTSetting.COMPARISON_OF_DISCOUNT_DEPRECATION, enabled = false)
    @RequiredSetting(type = FTSetting.ENABLE_DEPRECIATION_COLUMN)
    @Test(groups = {TestGroups.DND2, TestGroups.COMPARE_COMBINE_DD}, dataProvider = "testDataProvider",
            description = "Add item manually with comparision of dd disabled, check if both are depreciated and lower price is selected")
    public void charlie586_addManuallyWithComparisionOfDiscountAndDeprecationDisabled(User user, Claim claim, ClaimItem claimItem) {
        SettlementDialog settlementDialog = loginFlow.loginAndCreateClaim(user, claim)
                .openSidAndFill(
                        cat -> cat.withCategory(claimItem.getCategoryBabyItems())
                                .withNewPrice(claimItem.getTrygNewPrice())
                );
        int depreciationPercentage = settlementDialog.getVoucherPercentage() * 2;
        settlementDialog.setDepreciation(depreciationPercentage)
                .valuationGrid()
                .doAssert(asserts -> {
                    asserts.assertCashCompensationIsDepreciated(depreciationPercentage, NEW_PRICE);
                    asserts.assertCashCompensationIsDepreciated(depreciationPercentage, VOUCHER);
                    asserts.assertIsLowestPriceValuationSelected(VOUCHER, NEW_PRICE);
                });
    }

    //@TODO: find solution how to deal with react.js components in ss
    //TODO
    @RequiredSetting(type = FTSetting.USE_SELF_SERVICE2)
    @RequiredSetting(type = FTSetting.ENABLE_SELF_SERVICE)
    @RequiredSetting(type = FTSetting.ENABLE_REGISTRATION_LINE_SELF_SERVICE)
    @Test(groups = {TestGroups.DND2, TestGroups.COMPARE_COMBINE_DD}, enabled = false, dataProvider = "testDataProvider",
            description = "Add item from self service with reduction rule and check if depreciation is applied")
    public void charlie586_addFromSelfServiceWithRedRule(User user, Claim claim, ClaimItem claimItem) {
        loginFlow.loginAndCreateClaim(user, claim)
                .requestSelfServiceWithEnabledAutoClose(claim, Constants.DEFAULT_PASSWORD)
                .toMailsPage()
                .viewMail(MailsPage.MailType.SELFSERVICE_CUSTOMER_WELCOME)
                .findSelfServiceNewLinkAndOpenIt()
                .login(Constants.DEFAULT_PASSWORD)
                .addDescriptionWithOutSuggestions("test_product")
                .selectCategory(claimItem.getCategoryVideoCamera())
                .selectPurchaseYear(String.valueOf(Year.now().getValue()))
                .selectPurchaseMonth("Apr")
                .addNewPrice(Constants.PRICE_500)
                .saveItem()
                .sendResponseToEcc();

        loginFlow.login(user)
                .openActiveRecentClaim();
        new SettlementSummary().ensureAuditInfoPanelVisible()
                .checkStatusFromAudit("APPROVED");
    }
}
