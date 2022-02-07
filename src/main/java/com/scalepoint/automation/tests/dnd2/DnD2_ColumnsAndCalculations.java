package com.scalepoint.automation.tests.dnd2;

import com.scalepoint.automation.pageobjects.dialogs.SettlementDialog;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.services.usersmanagement.CompanyCode;
import com.scalepoint.automation.testGroups.TestGroups;
import com.scalepoint.automation.testGroups.UserCompanyGroups;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.utils.annotations.Jira;
import com.scalepoint.automation.utils.annotations.UserAttributes;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.TestDataActions;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.input.Claim;
import com.scalepoint.automation.utils.data.entity.input.ClaimItem;
import com.scalepoint.automation.utils.data.entity.input.Translations;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

import static com.scalepoint.automation.grid.ValuationGrid.Valuation.NEW_PRICE;

@Jira("https://jira.scalepoint.com/browse/CHARLIE-514")
public class DnD2_ColumnsAndCalculations extends BaseTest {

    private static final String DEPRECIATION_DATA_PROVIDER = "depreciationDataProvider";

    @RequiredSetting(type = FTSetting.ENABLE_DEPRECIATION_COLUMN)
    @RequiredSetting(type = FTSetting.COMPARISON_OF_DISCOUNT_DEPRECATION)
    @Test(groups = {TestGroups.DND2, TestGroups.COLUMNS_AND_CALCULATIONS}, dataProvider = TEST_DATA_PROVIDER,
            description = "Test total and sub total sum value when no depreciation and no voucher is added to claim line")
    public void charlie514_totalNewPriceShouldBeEqualNewPriceWhenNoVoucherIsUsedAndDeprecationAmountIs0(User user, Claim claim, ClaimItem claimItem) {

        loginFlow.loginAndCreateClaim(user, claim)
                .openSidAndFill(formFiller -> formFiller
                        .withDepreciation(0)
                        .withNewPrice(claimItem.getTrygNewPrice())
                        .withCategory(claimItem.getExistingGroupWithPolicyDepreciationTypeAndReductionRule()))
                .closeSidWithOk()
                .getSettlementSummary()
                .doAssert(asserts -> {
                    asserts.assertClaimSumValueIs(claimItem.getTrygNewPrice());
                    asserts.assertSubtotalSumValueIs(claimItem.getTrygNewPrice());
                });
    }

    @RequiredSetting(type = FTSetting.ENABLE_DEPRECIATION_COLUMN)
    @RequiredSetting(type = FTSetting.COMPARISON_OF_DISCOUNT_DEPRECATION)
    @Test(groups = {TestGroups.DND2, TestGroups.COLUMNS_AND_CALCULATIONS}, dataProvider = TEST_DATA_PROVIDER,
            description = "Test total and sub total sum value when voucher is added to claim line but no depreciation")
    public void charlie514_totalNewPriceShouldBeEqualNewPriceMinusVoucherValueWhenDeprecationAmountIs0(User user, Claim claim, ClaimItem claimItem) {

        SettlementDialog settlementDialog = loginFlow.loginAndCreateClaim(user, claim)
                .openSidAndFill(formFiller -> formFiller
                        .withDepreciation(0)
                        .withNewPrice(claimItem.getTrygNewPrice())
                        .withCategory(claimItem.getCategoryVideoCamera().getGroupName())
                        .withSubCategory(claimItem.getExistingSubCategoryForVideoGroupWithReductionRuleAndDepreciationPolicy()));
        int percentage = settlementDialog.getVoucherPercentage();
        settlementDialog.closeSidWithOk()
                .getSettlementSummary()
                .doAssert(asserts -> {
                    asserts.assertClaimSumValueIs(claimItem.getTrygNewPrice() - (claimItem.getTrygNewPrice() * percentage / 100));
                    asserts.assertSubtotalSumValueIs(claimItem.getTrygNewPrice() - (claimItem.getTrygNewPrice() * percentage / 100));
                });
    }

    @RequiredSetting(type = FTSetting.ENABLE_DEPRECIATION_COLUMN)
    @RequiredSetting(type = FTSetting.COMPARISON_OF_DISCOUNT_DEPRECATION)
    @Test(groups = {TestGroups.DND2, TestGroups.COLUMNS_AND_CALCULATIONS}, dataProvider = DEPRECIATION_DATA_PROVIDER,
            description = "Test total and sub total sum value when voucher and depreciation is added to claim line")
    public void charlie514_totalNewPriceShouldBeEqualNewPriceMinusDepreciationValueWhenVoucherAndDepreciationIsAddedToLine(User user, Claim claim, ClaimItem claimItem, int depreciationValue) {

        loginFlow.loginAndCreateClaim(user, claim)
                .openSidAndFill(claimItem.getCategoryBabyItems(), formFiller -> formFiller
                        .withNewPrice(claimItem.getTrygNewPrice())
                        .withVoucher(claimItem.getExistingVoucher1())
                        .withDepreciation(depreciationValue))
                .doAssert(asserts -> {
                    asserts.assertIsVoucherDiscountApplied(claimItem.getTrygNewPrice());
                })
                .valuationGrid()
                .doAssert(asserts -> {
                    asserts.assertCashCompensationIsDepreciated(depreciationValue, NEW_PRICE);

                })
                .toSettlementDialog()
                .closeSidWithOk()
                .getSettlementSummary()
                .doAssert(asserts -> {
                    asserts.assertClaimSumValueIs(claimItem.getTrygNewPrice() - (claimItem.getTrygNewPrice() * depreciationValue / 100));
                    asserts.assertSubtotalSumValueIs(claimItem.getTrygNewPrice() - (claimItem.getTrygNewPrice() * depreciationValue / 100));
                });
    }

    @RequiredSetting(type = FTSetting.ENABLE_DEPRECIATION_COLUMN)
    @RequiredSetting(type = FTSetting.COMPARISON_OF_DISCOUNT_DEPRECATION)
    @Test(groups = {TestGroups.DND2, TestGroups.COLUMNS_AND_CALCULATIONS}, dataProvider = DEPRECIATION_DATA_PROVIDER,
            description = "Test total and sub total sum value when depreciation is added and voucher not added to claim line")
    public void charlie514_totalNewPriceShouldBeEqualNewPriceMinusDepreciationWhenNoVoucherIsUsedAndDeprecationAmountIsAdded(User user, Claim claim, ClaimItem claimItem, int depreciationValue) {
        loginFlow.loginAndCreateClaim(user, claim)
                .openSidAndFill(formFiller -> formFiller
                        .withDepreciation(depreciationValue)
                        .withNewPrice(claimItem.getTrygNewPrice())
                        .withCategory(claimItem.getCategoryOther())
                        .withValuation(NEW_PRICE))
                .closeSidWithOk()
                .getSettlementSummary()
                .doAssert(asserts -> {
                    asserts.assertClaimSumValueIs(claimItem.getTrygNewPrice() - (claimItem.getTrygNewPrice() * depreciationValue / 100));
                    asserts.assertSubtotalSumValueIs(claimItem.getTrygNewPrice() - (claimItem.getTrygNewPrice() * depreciationValue / 100));
                });
    }

    @RequiredSetting(type = FTSetting.ENABLE_DEPRECIATION_COLUMN)
    @RequiredSetting(type = FTSetting.COMPARISON_OF_DISCOUNT_DEPRECATION)
    @Test(groups = {TestGroups.DND2, TestGroups.COLUMNS_AND_CALCULATIONS}, dataProvider = DEPRECIATION_DATA_PROVIDER,
            description = "Test total and sub total sum value when depreciation is added and voucher not added to claim line and red rule is discretionary type")
    public void charlie514_totalNewPriceShouldBeEqualNewPriceMinusDepreciationWhenNoVoucherIsUsedAndDeprecationAmountIsAddedAndRedRuleIsDiscretionaryType(User user, Claim claim, ClaimItem claimItem, int depreciationValue) {
        loginFlow.loginAndCreateClaim(user, claim)
                .openSidAndFill(formFiller -> formFiller
                        .withCategory(claimItem.getExistingGroupWithDiscretionaryDepreciationTypeAndReductionRule())
                        .withDepreciation(depreciationValue)
                        .withNewPrice(claimItem.getTrygNewPrice())
                )
                .closeSidWithOk()
                .getSettlementSummary()
                .doAssert(asserts -> {
                    asserts.assertClaimSumValueIs(claimItem.getTrygNewPrice() - (claimItem.getTrygNewPrice() * depreciationValue / 100));
                    asserts.assertSubtotalSumValueIs(claimItem.getTrygNewPrice() - (claimItem.getTrygNewPrice() * depreciationValue / 100));
                });
    }
    @RequiredSetting(type = FTSetting.ENABLE_DEPRECIATION_COLUMN)
    @RequiredSetting(type = FTSetting.COMPARISON_OF_DISCOUNT_DEPRECATION)
    @RequiredSetting(type = FTSetting.COMBINE_DISCOUNT_DEPRECATION)
    @RequiredSetting(type = FTSetting.SHOW_DISCREATIONARY_REASON)
    @Test(groups = {TestGroups.DND2, TestGroups.COLUMNS_AND_CALCULATIONS, UserCompanyGroups.SCALEPOINT},
            dataProvider = TEST_DATA_PROVIDER,
            description = "Test total and sub total sum value when voucher and depreciation is added to claim line and red rule is discretionary type")
    public void charlie514_totalNewPriceShouldBeEqualNewPriceMinusDepreciationValueAndVoucherValueWhenVoucherAndDepreciationIsAddedToLineAndRedRuleIsDiscretionaryType(
            @UserAttributes(company = CompanyCode.SCALEPOINT) User user, Claim claim, ClaimItem claimItem, Translations translations) {
        SettlementDialog settlementDialog = loginFlow.loginAndCreateClaim(user, claim)
                .openSidAndFill(formFiller -> formFiller
                        .withNewPrice(claimItem.getNewPriceSP())
                        .withCategory(claimItem.getCategoryVideoCamera().getGroupName())
                        .withSubCategory(claimItem.getExistingSubCategoryForVideoGroupWithReductionRuleAndDiscretionaryType()))
                .enableAge()
                .setDiscountAndDepreciation(true)
                .selectDiscretionaryReason(translations.getDiscretionaryReason().getFairValue());
        double voucherPercentage = settlementDialog.getVoucherPercentage();
        double depreciationPercentage = settlementDialog.getDepreciationPercentage();
        SettlementPage settlementPage = settlementDialog.closeSidWithOk();
        settlementPage.parseFirstClaimLine()
                .doAssert(asserts -> {
                    asserts.assertPurchasePriceIs(claimItem.getNewPriceSP() * (1 - voucherPercentage / 100));
                    asserts.assertReplacementPriceIs(claimItem.getNewPriceSP() * (1 - voucherPercentage / 100) * (1 - depreciationPercentage / 100));
                });
        settlementPage.getSettlementSummary()
                .doAssert(asserts -> {
                    asserts.assertClaimSumValueIs(claimItem.getNewPriceSP() * (1 - voucherPercentage / 100) * (1 - depreciationPercentage / 100));
                    asserts.assertSubtotalSumValueIs(claimItem.getNewPriceSP() * (1 - voucherPercentage / 100) * (1 - depreciationPercentage / 100));
                });
    }

    @RequiredSetting(type = FTSetting.ENABLE_DEPRECIATION_COLUMN)
    @RequiredSetting(type = FTSetting.COMPARISON_OF_DISCOUNT_DEPRECATION)
    @Test(groups = {TestGroups.DND2, TestGroups.COLUMNS_AND_CALCULATIONS}, dataProvider = TEST_DATA_PROVIDER,
            description = "convert for claim line without voucher and depreciation and no reduction rule")
    public void charlie514_claimLineWithoutVoucherAndDepreciationAmount(User user, Claim claim, ClaimItem claimItem) {
        SettlementPage settlementPage = loginFlow.loginAndCreateClaim(user, claim)
                .openSidAndFill(formFiller -> formFiller
                        .withNewPrice(claimItem.getTrygNewPrice())
                        .withCategory(claimItem.getCategoryOther())
                        .withValuation(NEW_PRICE))
                .closeSidWithOk();
        settlementPage.parseFirstClaimLine()
                .doAssert(asserts -> {
                    asserts.assertPurchasePriceIs(claimItem.getTrygNewPrice());
                    asserts.assertReplacementPriceIs(claimItem.getTrygNewPrice());
                });
        settlementPage.getSettlementSummary()
                .doAssert(asserts -> {
                    asserts.assertClaimSumValueIs(claimItem.getTrygNewPrice());
                    asserts.assertSubtotalSumValueIs(claimItem.getTrygNewPrice());
                });
    }

    @RequiredSetting(type = FTSetting.ENABLE_DEPRECIATION_COLUMN)
    @RequiredSetting(type = FTSetting.COMPARISON_OF_DISCOUNT_DEPRECATION)
    @Test(groups = {TestGroups.DND2, TestGroups.COLUMNS_AND_CALCULATIONS}, dataProvider = TEST_DATA_PROVIDER,
            description = "convert for claim line with voucher and no depreciation and reduction rule")
    public void charlie514_claimLineWithVoucherAndNoDepreciationAmount(User user, Claim claim, ClaimItem claimItem) {
        SettlementDialog settlementDialog = loginFlow.loginAndCreateClaim(user, claim)
                .openSidAndFill(claimItem.getCategoryBabyItems(), formFiller -> formFiller
                        .withNewPrice(claimItem.getTrygNewPrice()));
        double voucherPercentage = settlementDialog.getVoucherPercentage();
        SettlementPage settlementPage = settlementDialog.closeSidWithOk();
        settlementPage.parseFirstClaimLine()
                .doAssert(asserts -> {
                    asserts.assertPurchasePriceIs(claimItem.getTrygNewPrice() * (1 - voucherPercentage / 100));
                    asserts.assertReplacementPriceIs(claimItem.getTrygNewPrice() * (1 - voucherPercentage / 100));
                });
        settlementPage.getSettlementSummary()
                .doAssert(asserts -> {
                    asserts.assertClaimSumValueIs(claimItem.getTrygNewPrice() * (1 - voucherPercentage / 100));
                    asserts.assertSubtotalSumValueIs(claimItem.getTrygNewPrice() * (1 - voucherPercentage / 100));
                });
    }

    @RequiredSetting(type = FTSetting.ENABLE_DEPRECIATION_COLUMN)
    @RequiredSetting(type = FTSetting.COMPARISON_OF_DISCOUNT_DEPRECATION)
    @Test(groups = {TestGroups.DND2, TestGroups.COLUMNS_AND_CALCULATIONS}, dataProvider = DEPRECIATION_DATA_PROVIDER,
            description = "convert for claim line with voucher and depreciation and no reduction rule")
    public void charlie514_claimLineWithVoucherAndDepreciationAmount(User user, Claim claim, ClaimItem claimItem, int depreciationValue) {
        SettlementPage settlementPage = loginFlow.loginAndCreateClaim(user, claim)
                .openSidAndFill(claimItem.getCategoryBabyItems(), formFiller -> formFiller
                        .withNewPrice(claimItem.getTrygNewPrice())
                        .withVoucher(claimItem.getExistingVoucher1())
                        .withDepreciation(depreciationValue))
                .closeSidWithOk();
        settlementPage.parseFirstClaimLine()
                .doAssert(asserts -> {
                    asserts.assertPurchasePriceIs(claimItem.getTrygNewPrice());
                    asserts.assertReplacementPriceIs(claimItem.getTrygNewPrice() * (1 - (double) depreciationValue / 100));
                });
        settlementPage.getSettlementSummary()
                .doAssert(asserts -> {
                    asserts.assertClaimSumValueIs(claimItem.getTrygNewPrice() * (1 - (double) depreciationValue / 100));
                    asserts.assertSubtotalSumValueIs(claimItem.getTrygNewPrice() * (1 - (double) depreciationValue / 100));
                });
    }

    @RequiredSetting(type = FTSetting.ENABLE_DEPRECIATION_COLUMN)
    @RequiredSetting(type = FTSetting.COMPARISON_OF_DISCOUNT_DEPRECATION)
    @RequiredSetting(type = FTSetting.COMBINE_DISCOUNT_DEPRECATION)
    @Test(groups = {TestGroups.DND2, TestGroups.COLUMNS_AND_CALCULATIONS}, dataProvider = DEPRECIATION_DATA_PROVIDER,
            description = "Test total and sub total sum value when voucher and depreciation is added to claim line and red rule is discretionary type")
    public void charlie514_totalNewPriceShouldBeEqualNewPriceMinusDepreciationValueAndVoucherValueWhenVoucherAndDepreciationIsAddedToLine(User user, Claim claim, ClaimItem claimItem, int depreciationValue) {
        SettlementDialog settlementDialog = loginFlow.loginAndCreateClaim(user, claim)
                .openSidAndFill(claimItem.getCategoryBabyItems(), formFiller -> formFiller
                        .withNewPrice(claimItem.getTrygNewPrice()))
                .setDepreciation(depreciationValue)
                .setDiscountAndDepreciation(true);
        double voucherPercentage = settlementDialog.getVoucherPercentage();
        double depreciationPercentage = settlementDialog.getDepreciationPercentage();
        SettlementPage settlementPage = settlementDialog.closeSidWithOk();
        settlementPage.parseFirstClaimLine()
                .doAssert(asserts -> {
                    asserts.assertPurchasePriceIs(claimItem.getTrygNewPrice() * (1 - voucherPercentage / 100));
                    asserts.assertReplacementPriceIs(claimItem.getTrygNewPrice() * (1 - voucherPercentage / 100) * (1 - depreciationPercentage / 100));
                });
        settlementPage.getSettlementSummary()
                .doAssert(asserts -> {
                    asserts.assertClaimSumValueIs(claimItem.getTrygNewPrice() * (1 - voucherPercentage / 100) * (1 - depreciationPercentage / 100));
                    asserts.assertSubtotalSumValueIs(claimItem.getTrygNewPrice() * (1 - voucherPercentage / 100) * (1 - depreciationPercentage / 100));
                });
    }

    @DataProvider(name = DEPRECIATION_DATA_PROVIDER)
    public static Object[][] depreciationDataProvider(Method method) {

        return new Object[][]{

                TestDataActions.getTestDataWithExternalParameters(method, 20).toArray()
        };
    }
}
