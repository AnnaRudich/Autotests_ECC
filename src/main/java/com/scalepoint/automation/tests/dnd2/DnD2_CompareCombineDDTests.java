package com.scalepoint.automation.tests.dnd2;

import com.scalepoint.automation.pageobjects.dialogs.SettlementDialog;
import com.scalepoint.automation.pageobjects.modules.SettlementSummary;
import com.scalepoint.automation.pageobjects.pages.MailsPage;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.services.externalapi.SolrApi;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.shared.ProductInfo;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.utils.Constants;
import com.scalepoint.automation.utils.annotations.Jira;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.ClaimItem;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.threadlocal.Browser;
import org.testng.annotations.Test;

import java.time.Year;
import java.util.Objects;

import static com.scalepoint.automation.pageobjects.dialogs.SettlementDialog.Valuation.CATALOG_PRICE;
import static com.scalepoint.automation.pageobjects.dialogs.SettlementDialog.Valuation.CUSTOMER_DEMAND;
import static com.scalepoint.automation.pageobjects.dialogs.SettlementDialog.Valuation.MARKET_PRICE;
import static com.scalepoint.automation.pageobjects.dialogs.SettlementDialog.Valuation.NEW_PRICE;
import static com.scalepoint.automation.pageobjects.dialogs.SettlementDialog.Valuation.USED_PRICE;
import static com.scalepoint.automation.pageobjects.dialogs.SettlementDialog.Valuation.VOUCHER;
import static com.scalepoint.automation.services.externalapi.DatabaseApi.PriceConditions.INVOICE_PRICE_EQUALS_MARKET_PRICE;
import static com.scalepoint.automation.services.externalapi.DatabaseApi.PriceConditions.INVOICE_PRICE_HIGHER_THAN_MARKET_PRICE;
import static com.scalepoint.automation.services.externalapi.DatabaseApi.PriceConditions.ORDERABLE;
import static com.scalepoint.automation.services.externalapi.DatabaseApi.PriceConditions.PRODUCT_AS_VOUCHER_ONLY;
import static com.scalepoint.automation.services.externalapi.DatabaseApi.PriceConditions.PRODUCT_AS_VOUCHER_ONLY_FALSE;

/**
 * The class represents smoke tests set for D&D2 functionality
 * run only on DK
 */

@Jira("https://jira.scalepoint.com/browse/CHARLIE-586")
@RequiredSetting(type = FTSetting.COMBINE_DISCOUNT_DEPRECATION, enabled = false)
public class DnD2_CompareCombineDDTests extends BaseTest {

    private int deprecationValue = 10;

    @RequiredSetting(type = FTSetting.SHOW_MARKET_PRICE)
    @RequiredSetting(type = FTSetting.COMPARISON_OF_DISCOUNT_DEPRECATION)
    @Test(dataProvider = "testDataProvider", description = "Add claim with product from catalog where market price is higher than product price")
    public void charlie586_addFromCatalogWhereProductPriceIsHigherThanMarketPrice(User user, Claim claim){
        ProductInfo productInfo = SolrApi.findProduct(getXpricesForConditions(ORDERABLE, PRODUCT_AS_VOUCHER_ONLY_FALSE, INVOICE_PRICE_HIGHER_THAN_MARKET_PRICE));

        loginAndCreateClaim(user, claim)
                .toTextSearchPage()
                .searchBySku(productInfo.getSku())
                .sortOrderableFirst()
                .openSidForFirstProduct()
                .setDepreciation(deprecationValue)
                .doAssert(asserts -> {
                    asserts.assertMarketPriceVisible();
                    asserts.assertCatalogPriceVisible();
                    asserts.assertPriceIsSameInTwoColumns(CATALOG_PRICE);
                    asserts.assertCashCompensationIsDepreciated(deprecationValue, MARKET_PRICE);
                    asserts.assertIsLowestPriceValuationSelected(MARKET_PRICE, CATALOG_PRICE);
                });
    }

    @RequiredSetting(type = FTSetting.SHOW_MARKET_PRICE)
    @RequiredSetting(type = FTSetting.COMPARISON_OF_DISCOUNT_DEPRECATION)
    @Test(dataProvider = "testDataProvider", description = "Add claim with product from catalog where market price equals product price")
    public void charlie586_addFromCatalogWhereProductPriceIsEqualMarketPriceAndHaveOnlyVoucherReplacement(User user, Claim claim){
        ProductInfo productInfo = SolrApi.findProduct(getXpricesForConditions(ORDERABLE, PRODUCT_AS_VOUCHER_ONLY, INVOICE_PRICE_EQUALS_MARKET_PRICE));

        loginAndCreateClaim(user, claim)
                .toTextSearchPage()
                .searchBySku(productInfo.getSku())
                .sortOrderableFirst()
                .openSidForFirstProduct()
                .setDepreciation(deprecationValue)
                .setDescription("test")
                .doAssert(asserts -> {
                    asserts.assertMarketPriceVisible();
                    asserts.assertCatalogPriceVisible();
                    asserts.assertPriceIsSameInTwoColumns(CATALOG_PRICE);
                    asserts.assertCashCompensationIsDepreciated(deprecationValue, MARKET_PRICE);
                    asserts.assertIsLowestPriceValuationSelected(VOUCHER);
                });
    }

    @RequiredSetting(type = FTSetting.DO_NOT_DEPRECIATE_CUSTOMER_DEMAND, enabled=false, isDefault = true)
    @RequiredSetting(type = FTSetting.COMPARISON_OF_DISCOUNT_DEPRECATION)
    @Test(dataProvider = "testDataProvider", description = "Add claim item manually and check if new price, customer demand are discounted")
    public void charlie586_addManually(User user, Claim claim, ClaimItem claimItem) {

        loginAndCreateClaim(user, claim)
                .openSidAndFill(cat -> cat.withCategory(claimItem.getCategoryGroupBorn()).withSubCategory(claimItem.getCategoryBornBabyudstyr()))
                .setNewPrice(claimItem.getTrygNewPrice())
                .setCustomerDemand(claimItem.getCustomerDemand())
                .openAddValuationForm()
                .addValuationType(claimItem.getValuationTypeUsedPrice())
                .addValuationPrice(claimItem.getUsedPrice())
                .closeValuationDialogWithOk()
                .setDescription(claimItem.getTextFieldSP())
                .setDepreciation(deprecationValue)
                .doAssert(asserts -> {
                    asserts.assertCashCompensationIsDepreciated(deprecationValue, NEW_PRICE);
                    asserts.assertCashCompensationIsDepreciated(deprecationValue, CUSTOMER_DEMAND);
                    asserts.assertPriceIsSameInTwoColumns(USED_PRICE);
                    asserts.assertIsVoucherDiscountApplied(claimItem.getTrygNewPrice());
                    asserts.assertIsLowestPriceValuationSelected(VOUCHER, NEW_PRICE,
                            USED_PRICE, CUSTOMER_DEMAND);
                });
    }

    @RequiredSetting(type = FTSetting.COMPARISON_OF_DISCOUNT_DEPRECATION)
    @RequiredSetting(type=FTSetting.DO_NOT_DEPRECIATE_CUSTOMER_DEMAND)
    @Test(dataProvider = "testDataProvider", description = "Add claim item manually and check if customer demand is not discounted. FT Do not depreciate CD is on")
    public void charlie586_addManually_DoNotDepreciateCustomerDemandIsOn(User user, Claim claim, ClaimItem claimItem) {

        Double initialCustomerDemand = claimItem.getCustomerDemand();

        loginAndCreateClaim(user, claim)
                .openSidAndFill(cat -> cat.withCategory(claimItem.getCategoryGroupBorn()).withSubCategory(claimItem.getCategoryBornBabyudstyr()))
                .setNewPrice(claimItem.getTrygNewPrice())
                .setCustomerDemand(initialCustomerDemand)
                .setDescription(claimItem.getTextFieldSP())
                .setDepreciation(deprecationValue)
                .doAssert(asserts -> {
                    asserts.assertCashCompensationIsDepreciated(deprecationValue, NEW_PRICE);
                    asserts.assertCashCompensationIsNotDepreciated(CUSTOMER_DEMAND, initialCustomerDemand);
                    asserts.assertIsVoucherDiscountApplied(claimItem.getTrygNewPrice());
                    asserts.assertIsLowestPriceValuationSelected(VOUCHER, NEW_PRICE, CUSTOMER_DEMAND);
                });
    }

    @RequiredSetting(type = FTSetting.COMPARISON_OF_DISCOUNT_DEPRECATION)
    @RequiredSetting(type = FTSetting.ENABLE_DEPRECIATION_COLUMN)
    @Test(dataProvider = "testDataProvider", description = "Add items manually and check if depreciation is lower than voucher discount correct item is selected")
    public void charlie586_addManuallyWithVoucherAndDepreciationLowerThanVoucherDiscount(User user, Claim claim, ClaimItem claimItem){

        SettlementDialog settlementDialog = loginAndCreateClaim(user, claim)
                .openSidAndFill(cat -> cat.withCategory(claimItem.getCategoryGroupBorn()).withSubCategory(claimItem.getCategoryBornBabyudstyr()));
        SettlementPage settlementPage = settlementDialog.setNewPrice(claimItem.getTrygNewPrice())
                .setDescription(claimItem.getTextFieldSP())
                .setDepreciation(settlementDialog.getVoucherPercentage()/2)
                .closeSidWithOk();
                Objects.requireNonNull(Browser.driver()).navigate().refresh();
                settlementPage.parseFirstClaimLine()
                        .editLine()
                .doAssert(asserts -> {
                    asserts.assertCashCompensationIsDepreciated(settlementDialog.getVoucherPercentage()/2, NEW_PRICE);
                    asserts.assertIsLowestPriceValuationSelected(VOUCHER, NEW_PRICE);
                })
                .parseValuationRow(VOUCHER)
                .doAssert(asserts -> asserts.assertDepreciationPercentageIs(0));
    }

    @RequiredSetting(type = FTSetting.COMPARISON_OF_DISCOUNT_DEPRECATION)
    @RequiredSetting(type = FTSetting.ENABLE_DEPRECIATION_COLUMN)
    @Test(dataProvider = "testDataProvider", description = "Add items manually and check if depreciation is bigger than voucher discount correct item is selected")
    public void charlie586_addManuallyWithVoucherAndDepreciationHigherThanVoucherDiscount(User user, Claim claim, ClaimItem claimItem){

        SettlementDialog settlementDialog = loginAndCreateClaim(user, claim)
                .openSidAndFill(cat -> cat.withCategory(claimItem.getCategoryGroupBorn()).withSubCategory(claimItem.getCategoryBornBabyudstyr()))
                .setNewPrice(claimItem.getTrygNewPrice())
                .setDescription(claimItem.getTextFieldSP());
        int depreciationPercentage = settlementDialog.getVoucherPercentage()*2;
        settlementDialog.setDepreciation(depreciationPercentage)
                .doAssert(asserts -> {
                    asserts.assertCashCompensationIsDepreciated(depreciationPercentage, NEW_PRICE);
                    asserts.assertIsLowestPriceValuationSelected(VOUCHER, NEW_PRICE);
                })
                .parseValuationRow(NEW_PRICE)
                .doAssert(asserts -> asserts.assertDepreciationPercentageIs(depreciationPercentage));
    }

    @RequiredSetting(type = FTSetting.COMPARISON_OF_DISCOUNT_DEPRECATION, enabled = false)
    @RequiredSetting(type = FTSetting.ENABLE_DEPRECIATION_COLUMN)
    @Test(dataProvider = "testDataProvider", description = "Add item manually with comparision of dd disabled, check if both are depreciated and lower price is selected")
    public void charlie586_addManuallyWithComparisionOfDiscountAndDeprecationDisabled(User user, Claim claim, ClaimItem claimItem){

        SettlementDialog settlementDialog = loginAndCreateClaim(user, claim)
                .openSidAndFill(cat -> cat.withCategory(claimItem.getCategoryGroupBorn()).withSubCategory(claimItem.getCategoryBornBabyudstyr()))
                .setNewPrice(claimItem.getTrygNewPrice())
                .setDescription(claimItem.getTextFieldSP());
        int depreciationPercentage = settlementDialog.getVoucherPercentage()*2;
        settlementDialog.setDepreciation(depreciationPercentage)
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
    @Test(enabled = false, dataProvider = "testDataProvider", description = "Add item from self service with reduction rule and check if depreciation is applied")
    public void charlie586_addFromSelfServiceWithRedRule(User user, Claim claim) {
        loginAndCreateClaim(user, claim)
                .requestSelfServiceWithEnabledAutoClose(claim, Constants.DEFAULT_PASSWORD)
                .toMailsPage()
                .viewMail(MailsPage.MailType.SELFSERVICE_CUSTOMER_WELCOME)
                .findSelfServiceNewLinkAndOpenIt()
                .login(Constants.DEFAULT_PASSWORD)
                .addDescriptionWithOutSuggestions("test_product")
                .selectCategory("Foto & Video")
                .selectSubCategory("Videokamera")
                .selectPurchaseYear(String.valueOf(Year.now().getValue()))
                .selectPurchaseMonth("Apr")
                .addNewPrice(Constants.PRICE_500)
                .saveItem()
                .sendResponseToEcc();

        login(user)
                .openActiveRecentClaim();
                new SettlementSummary().ensureAuditInfoPanelVisible()
                .checkStatusFromAudit("APPROVED");
    }
}
