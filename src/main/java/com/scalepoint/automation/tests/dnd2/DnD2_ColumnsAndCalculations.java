package com.scalepoint.automation.tests.dnd2;

import com.scalepoint.automation.pageobjects.dialogs.SettlementDialog;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.services.usersmanagement.CompanyCode;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.utils.annotations.Jira;
import com.scalepoint.automation.utils.annotations.UserCompany;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.ClaimItem;
import com.scalepoint.automation.utils.data.entity.DiscretionaryReason;
import com.scalepoint.automation.utils.data.entity.Voucher;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.testng.annotations.Test;

@Jira("https://jira.scalepoint.com/browse/CHARLIE-514")
public class DnD2_ColumnsAndCalculations extends BaseTest{

    private int depreciationValue = 20;

    @RequiredSetting(type = FTSetting.ENABLE_DEPRECIATION_COLUMN)
    @RequiredSetting(type = FTSetting.COMPARISON_OF_DISCOUNT_DEPRECATION)
    @Test(dataProvider = "testDataProvider", description = "Test total and sub total sum value when no depreciation and no voucher is added to claim line")
    public void charlie514_totalNewPriceShouldBeEqualNewPriceWhenNoVoucherIsUsedAndDeprecationAmountIs0(User user, Claim claim, ClaimItem claimItem){
        loginAndCreateClaim(user, claim)
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
    @Test(dataProvider = "testDataProvider", description = "Test total and sub total sum value when voucher is added to claim line but no depreciation")
    public void charlie514_totalNewPriceShouldBeEqualNewPriceMinusVoucherValueWhenDeprecationAmountIs0(User user, Claim claim, ClaimItem claimItem, Voucher voucher){
        SettlementDialog settlementDialog = loginAndCreateClaim(user, claim)
                .openSidAndFill(formFiller -> formFiller
                        .withDepreciation(0)
                        .withNewPrice(claimItem.getTrygNewPrice())
                        .withCategory(claimItem.getExistingGroupFotoAndVideo())
                        .withSubCategory(claimItem.getExistingSubCategoryForVideoGroupWithReductionRuleAndDepreciationPolicy()));
        int percentage = settlementDialog.getVoucherPercentage();
        settlementDialog.closeSidWithOk()
                .getSettlementSummary()
                .doAssert(asserts -> {
                    asserts.assertClaimSumValueIs(claimItem.getTrygNewPrice()-(claimItem.getTrygNewPrice() * percentage/100));
                    asserts.assertSubtotalSumValueIs(claimItem.getTrygNewPrice()-(claimItem.getTrygNewPrice() * percentage/100));
                });
    }

    @RequiredSetting(type = FTSetting.ENABLE_DEPRECIATION_COLUMN)
    @RequiredSetting(type = FTSetting.COMPARISON_OF_DISCOUNT_DEPRECATION)
    @Test(dataProvider = "testDataProvider", description = "Test total and sub total sum value when voucher and depreciation is added to claim line")
    public void charlie514_totalNewPriceShouldBeEqualNewPriceMinusDepreciationValueWhenVoucherAndDepreciationIsAddedToLine(User user, Claim claim, ClaimItem claimItem, Voucher voucher){
        loginAndCreateClaim(user, claim)
                .openSidAndFill(formFiller -> formFiller
                        .withNewPrice(claimItem.getTrygNewPrice())
                        .withCategory(claimItem.getCategoryGroupBorn())
                        .withSubCategory(claimItem.getCategoryBornBabyudstyr())
                        .withVoucher(claimItem.getExistingVoucher_10())
                        .withDepreciation(depreciationValue))
                .doAssert(asserts -> {
                    asserts.assertCashCompensationIsDepreciated(depreciationValue, SettlementDialog.Valuation.NEW_PRICE);
                    asserts.assertIsVoucherDiscountApplied(claimItem.getTrygNewPrice());
                })
                .closeSidWithOk()
                .getSettlementSummary()
                .doAssert(asserts -> {
                    asserts.assertClaimSumValueIs(claimItem.getTrygNewPrice()-(claimItem.getTrygNewPrice() * depreciationValue/100));
                    asserts.assertSubtotalSumValueIs(claimItem.getTrygNewPrice()-(claimItem.getTrygNewPrice() * depreciationValue/100));
                });
    }

    @RequiredSetting(type = FTSetting.ENABLE_DEPRECIATION_COLUMN)
    @RequiredSetting(type = FTSetting.COMPARISON_OF_DISCOUNT_DEPRECATION)
    @Test(dataProvider = "testDataProvider", description = "Test total and sub total sum value when depreciation is added and voucher not added to claim line")
    public void charlie514_totalNewPriceShouldBeEqualNewPriceMinusDepreciationWhenNoVoucherIsUsedAndDeprecationAmountIsAdded(User user, Claim claim, ClaimItem claimItem){
        loginAndCreateClaim(user, claim)
                .openSidAndFill(formFiller -> formFiller
                        .withDepreciation(depreciationValue)
                        .withNewPrice(claimItem.getTrygNewPrice())
                        .withCategory(claimItem.getExistingCatWithoutVoucherAndSubCategory())
                        .withValuation(SettlementDialog.Valuation.NEW_PRICE))
                .closeSidWithOk()
                .getSettlementSummary()
                .doAssert(asserts -> {
                    asserts.assertClaimSumValueIs(claimItem.getTrygNewPrice()-(claimItem.getTrygNewPrice() * depreciationValue/100));
                    asserts.assertSubtotalSumValueIs(claimItem.getTrygNewPrice()-(claimItem.getTrygNewPrice() * depreciationValue/100));
                });
    }

    @RequiredSetting(type = FTSetting.ENABLE_DEPRECIATION_COLUMN)
    @RequiredSetting(type = FTSetting.COMPARISON_OF_DISCOUNT_DEPRECATION)
    @Test(dataProvider = "testDataProvider", description = "Test total and sub total sum value when depreciation is added and voucher not added to claim line and red rule is discretionary type")
    public void charlie514_totalNewPriceShouldBeEqualNewPriceMinusDepreciationWhenNoVoucherIsUsedAndDeprecationAmountIsAddedAndRedRuleIsDiscretionaryType(User user, Claim claim, ClaimItem claimItem){
        loginAndCreateClaim(user, claim)
                .openSidAndFill(formFiller -> formFiller
                        .withDepreciation(depreciationValue)
                        .withNewPrice(claimItem.getTrygNewPrice())
                        .withCategory(claimItem.getExistingGroupWithDiscretionaryDepreciationTypeAndReductionRule()))
                .closeSidWithOk()
                .getSettlementSummary()
                .doAssert(asserts -> {
                    asserts.assertClaimSumValueIs(claimItem.getTrygNewPrice()-(claimItem.getTrygNewPrice() * depreciationValue/100));
                    asserts.assertSubtotalSumValueIs(claimItem.getTrygNewPrice()-(claimItem.getTrygNewPrice() * depreciationValue/100));
                });
    }

    @RequiredSetting(type = FTSetting.ENABLE_DEPRECIATION_COLUMN)
    @RequiredSetting(type = FTSetting.COMPARISON_OF_DISCOUNT_DEPRECATION)
    @RequiredSetting(type = FTSetting.COMBINE_DISCOUNT_DEPRECATION)
    @RequiredSetting(type = FTSetting.SHOW_DISCREATIONARY_REASON)
    @Test(dataProvider = "testDataProvider", description = "Test total and sub total sum value when voucher and depreciation is added to claim line and red rule is discretionary type")
    public void charlie514_totalNewPriceShouldBeEqualNewPriceMinusDepreciationValueAndVoucherValueWhenVoucherAndDepreciationIsAddedToLineAndRedRuleIsDiscretionaryType(
            @UserCompany(CompanyCode.SCALEPOINT) User user, Claim claim, ClaimItem claimItem, DiscretionaryReason discretionaryReason){
        SettlementDialog settlementDialog = loginAndCreateClaim(user, claim)
                .openSidAndFill(formFiller -> formFiller
                        .withNewPrice(claimItem.getTrygNewPrice())
                        .withCategory(claimItem.getExistingGroupFotoAndVideo())
                        .withSubCategory(claimItem.getExistingSubCategoryForVideoGroupWithReductionRuleAndDiscretionaryType()))
                .enableAge()
                .setDiscountAndDepreciation(true)
                .selectDiscretionaryReason(discretionaryReason.getDiscretionaryReasonScalepoint());
                double voucherPercentage = settlementDialog.getVoucherPercentage();
                double depreciationPercentage = settlementDialog.getDepreciationPercentage();
                SettlementPage settlementPage = settlementDialog.closeSidWithOk();
        settlementPage.parseFirstClaimLine()
                .doAssert(asserts -> {
                    asserts.assertPurchasePriceIs(claimItem.getTrygNewPrice()*(1-voucherPercentage/100));
                    asserts.assertReplacementPriceIs(claimItem.getTrygNewPrice()*(1-voucherPercentage/100)*(1-depreciationPercentage/100));
                });
        settlementPage.getSettlementSummary()
                .doAssert(asserts -> {
                    asserts.assertClaimSumValueIs(claimItem.getTrygNewPrice()*(1-voucherPercentage/100)*(1-depreciationPercentage/100));
                    asserts.assertSubtotalSumValueIs(claimItem.getTrygNewPrice()*(1-voucherPercentage/100)*(1-depreciationPercentage/100));
                });
    }

    @RequiredSetting(type = FTSetting.ENABLE_DEPRECIATION_COLUMN)
    @RequiredSetting(type = FTSetting.COMPARISON_OF_DISCOUNT_DEPRECATION)
    @Test(dataProvider = "testDataProvider", description = "test for claim line without voucher and depreciation and no reduction rule")
    public void charlie514_claimLineWithoutVoucherAndDepreciationAmount(User user, Claim claim, ClaimItem claimItem){
        SettlementPage settlementPage = loginAndCreateClaim(user, claim)
                .openSidAndFill(formFiller -> formFiller
                        .withNewPrice(claimItem.getTrygNewPrice())
                        .withCategory(claimItem.getExistingCatWithoutVoucherAndSubCategory())
                        .withValuation(SettlementDialog.Valuation.NEW_PRICE))
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
    @Test(dataProvider = "testDataProvider", description = "test for claim line with voucher and no depreciation and reduction rule")
    public void charlie514_claimLineWithVoucherAndNoDepreciationAmount(User user, Claim claim, ClaimItem claimItem){
        SettlementDialog settlementDialog = loginAndCreateClaim(user, claim)
                .openSidAndFill(formFiller -> formFiller
                        .withNewPrice(claimItem.getTrygNewPrice())
                        .withCategory(claimItem.getCategoryGroupBorn())
                        .withSubCategory(claimItem.getCategoryBornBabyudstyr()));
        double voucherPercentage = settlementDialog.getVoucherPercentage();
        SettlementPage settlementPage = settlementDialog.closeSidWithOk();
        settlementPage.parseFirstClaimLine()
                .doAssert(asserts -> {
                    asserts.assertPurchasePriceIs(claimItem.getTrygNewPrice()*(1-voucherPercentage/100));
                    asserts.assertReplacementPriceIs(claimItem.getTrygNewPrice()*(1-voucherPercentage/100));
                });
        settlementPage.getSettlementSummary()
                .doAssert(asserts -> {
                    asserts.assertClaimSumValueIs(claimItem.getTrygNewPrice()*(1-voucherPercentage/100));
                    asserts.assertSubtotalSumValueIs(claimItem.getTrygNewPrice()*(1-voucherPercentage/100));
                });
    }

    @RequiredSetting(type = FTSetting.ENABLE_DEPRECIATION_COLUMN)
    @RequiredSetting(type = FTSetting.COMPARISON_OF_DISCOUNT_DEPRECATION)
    @Test(dataProvider = "testDataProvider", description = "test for claim line with voucher and depreciation and no reduction rule")
    public void charlie514_claimLineWithVoucherAndDepreciationAmount(User user, Claim claim, ClaimItem claimItem){
        SettlementPage settlementPage = loginAndCreateClaim(user, claim)
                .openSidAndFill(formFiller -> formFiller
                        .withNewPrice(claimItem.getTrygNewPrice())
                        .withCategory(claimItem.getCategoryGroupBorn())
                        .withSubCategory(claimItem.getCategoryBornBabyudstyr())
                        .withVoucher(claimItem.getExistingVoucher_10())
                        .withDepreciation(depreciationValue))
                        .closeSidWithOk();
        settlementPage.parseFirstClaimLine()
                .doAssert(asserts -> {
                    asserts.assertPurchasePriceIs(claimItem.getTrygNewPrice());
                    asserts.assertReplacementPriceIs(claimItem.getTrygNewPrice()*(1- (double) depreciationValue /100));
                });
        settlementPage.getSettlementSummary()
                .doAssert(asserts -> {
                    asserts.assertClaimSumValueIs(claimItem.getTrygNewPrice()*(1- (double) depreciationValue /100));
                    asserts.assertSubtotalSumValueIs(claimItem.getTrygNewPrice()*(1- (double) depreciationValue /100));
                });
    }

    @RequiredSetting(type = FTSetting.ENABLE_DEPRECIATION_COLUMN)
    @RequiredSetting(type = FTSetting.COMPARISON_OF_DISCOUNT_DEPRECATION)
    @RequiredSetting(type = FTSetting.COMBINE_DISCOUNT_DEPRECATION)
    @Test(dataProvider = "testDataProvider", description = "Test total and sub total sum value when voucher and depreciation is added to claim line and red rule is discretionary type")
    public void charlie514_totalNewPriceShouldBeEqualNewPriceMinusDepreciationValueAndVoucherValueWhenVoucherAndDepreciationIsAddedToLine(User user, Claim claim, ClaimItem claimItem){
        SettlementDialog settlementDialog = loginAndCreateClaim(user, claim)
                .openSidAndFill(formFiller -> formFiller
                        .withNewPrice(claimItem.getTrygNewPrice())
                        .withCategory(claimItem.getCategoryGroupBorn())
                        .withSubCategory(claimItem.getCategoryBornBabyudstyr()))
                .setDepreciation(depreciationValue)
                .setDiscountAndDepreciation(true);
        double voucherPercentage = settlementDialog.getVoucherPercentage();
        double depreciationPercentage = settlementDialog.getDepreciationPercentage();
        SettlementPage settlementPage = settlementDialog.closeSidWithOk();
        settlementPage.parseFirstClaimLine()
                .doAssert(asserts -> {
                    asserts.assertPurchasePriceIs(claimItem.getTrygNewPrice()*(1-voucherPercentage/100));
                    asserts.assertReplacementPriceIs(claimItem.getTrygNewPrice()*(1-voucherPercentage/100)*(1-depreciationPercentage/100));
                });
        settlementPage.getSettlementSummary()
                .doAssert(asserts -> {
                    asserts.assertClaimSumValueIs(claimItem.getTrygNewPrice()*(1-voucherPercentage/100)*(1-depreciationPercentage/100));
                    asserts.assertSubtotalSumValueIs(claimItem.getTrygNewPrice()*(1-voucherPercentage/100)*(1-depreciationPercentage/100));
                });
    }
}
