package com.scalepoint.automation.tests.sid.categoryBulkUpdate;

import com.scalepoint.automation.grid.ValuationGrid;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.services.usersmanagement.CompanyCode;
import com.scalepoint.automation.testGroups.TestGroups;
import com.scalepoint.automation.testGroups.UserCompanyGroups;
import com.scalepoint.automation.tests.BaseUITest;
import com.scalepoint.automation.utils.Constants;
import com.scalepoint.automation.utils.annotations.Jira;
import com.scalepoint.automation.utils.annotations.UserAttributes;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.input.Claim;
import com.scalepoint.automation.utils.data.entity.input.ClaimItem;
import com.scalepoint.automation.utils.data.entity.input.PseudoCategory;
import org.testng.annotations.Test;

@RequiredSetting(type = FTSetting.ENABLE_BULK_UPDATE_CATEGORY)
@RequiredSetting(type = FTSetting.COMBINE_DISCOUNT_DEPRECATION, enabled = false)
@Jira("https://jira.scalepoint.com/browse/CLAIMSHOP-6693")
public class BulkUpdateCategoryOnManualLinesWithReductionRulesTest extends BaseUITest {

    ClaimLinesHelper claimLinesHelper = new ClaimLinesHelper();

    private int lineAgeYears = 2;
    private int lineAgeMonths = 0;
    private double newPriceValue = Constants.PRICE_100;

    /*  pre-condition: there are two lines with ReductionRules applied;
        Apply reduction rules automatically was checked

        1. using bulk category update change category to some without rules mapped
        EXPECTED: in SID - no reduction rules suggested, but manual depreciation value(from previous rule) is still displayed

        2. using bulk update change category back to one with reduction rules mapped
        EXPECTED: in SID - there is reduction rule suggested
     */
    @Test(groups = {TestGroups.SID,
            TestGroups.CATEGORY_BULK_UPDATE,
            TestGroups.MANULA_LINES_WITH_RR,
            UserCompanyGroups.TRYGFORSIKRING},
            enabled = false, dataProvider = "testDataProvider",
            description = "select category with NO reduction rules mapped, apply rules automatically is enabled")
    public void bulkUpdateCategories_applyRulesAutomatically(
            @UserAttributes(company = CompanyCode.TRYGFORSIKRING) User user, Claim claim, ClaimItem claimItem) {
        PseudoCategory categoryWithNoReductionRulesMapped = claimItem.getCategoryPersonalMedicine();
        PseudoCategory categoryWithReductionRulesMapped = claimItem.getCategoryMobilePhones();

        loginFlow.loginAndCreateClaim(user, claim);

        Integer depreciationPercentageFromReductionRule =
                claimLinesHelper.startAddLine(categoryWithReductionRulesMapped, true, newPriceValue, 2)
                        .getDepreciationPercentage();
        claimLinesHelper.finishAddLine();
        claimLinesHelper.addLine(categoryWithReductionRulesMapped, true, newPriceValue, 2)

                .getToolBarMenu()
                .selectAll()
                .openUpdateCategoriesDialog()
                .toUpdateCategoriesDialog()
                .selectCategory(categoryWithNoReductionRulesMapped.getGroupName())
                .selectSubcategory(categoryWithNoReductionRulesMapped.getCategoryName())
                .closeUpdateCategoriesDialog()

                .editFirstClaimLine()
                .doAssert(
                        sid -> {
                            sid.assertDepreciationAmountIs(0.0);
                            sid.assertDepreciationPercentageIs("0");
                            sid.assertThereIsNoReductionRules();
                            sid.assertAgeIs(lineAgeYears, lineAgeMonths);
                            sid.assertCategoriesTextIs(categoryWithNoReductionRulesMapped);
                        })
                .valuationGrid()
                .getValuationRow(ValuationGrid.Valuation.VOUCHER)
                .doAssert(ValuationGrid.ValuationRow.Asserts::assertValuationIsSelected)
                .toSettlementDialog()
                .closeSidWithOk()

                .getToolBarMenu()
                .selectAll()
                .openUpdateCategoriesDialog()
                .toUpdateCategoriesDialog()
                .selectCategory(categoryWithReductionRulesMapped.getGroupName())
                .selectSubcategory(categoryWithReductionRulesMapped.getCategoryName())
                .closeUpdateCategoriesDialog()

                .editFirstClaimLine()
                .doAssert(
                        sid -> {
                            sid.assertDepreciationAmountIs(Double.valueOf(depreciationPercentageFromReductionRule));
                            sid.assertDepreciationPercentageIs(String.valueOf(depreciationPercentageFromReductionRule));
                            sid.assertThereIsReductionRuleSuggested();
                            sid.assertAgeIs(lineAgeYears, lineAgeMonths);
                            sid.assertCategoriesTextIs(categoryWithReductionRulesMapped);
                        })
                .valuationGrid()
                .getValuationRow(ValuationGrid.Valuation.NEW_PRICE)
                .doAssert(ValuationGrid.ValuationRow.Asserts::assertValuationIsSelected);
    }

    /*  pre-condition: there are two lines with ReductionRules applied;
        Apply reduction rules automatically was NOT checked

        1. using bulk category update change category to some without rules mapped
        EXPECTED: in SID - depreciation IS applied, but no rules suggested

        2. using bulk category update change category back to one with reduction rules mapped
        EXPECTED: in SID - there is depreciation applied, there is reduction rule suggested
      */
    @Test(groups = {TestGroups.SID,
            TestGroups.CATEGORY_BULK_UPDATE,
            TestGroups.MANULA_LINES_WITH_RR,
            UserCompanyGroups.TRYGFORSIKRING},
            enabled = false, dataProvider = "testDataProvider",
            description = "select category with NO reduction rules mapped, apply rules automatically is disabled")
    public void bulkUpdateLinesWithCategoriesWhereNoReductionRulesMapped_applyRulesManually(
            @UserAttributes(company = CompanyCode.TRYGFORSIKRING) User user, Claim claim, ClaimItem claimItem) {

        PseudoCategory categoryWithNoReductionRulesMapped = claimItem.getCategoryPersonalMedicine();
        PseudoCategory categoryWithReductionRulesMapped = claimItem.getCategoryMobilePhones();

        loginFlow.loginAndCreateClaim(user, claim);

        Integer depreciationPercentageFromReductionRule =
                claimLinesHelper
                        .startAddLine(categoryWithReductionRulesMapped, false, newPriceValue, 2)
                        .getDepreciationPercentage();
        claimLinesHelper.finishAddLine();
        claimLinesHelper.addLine(categoryWithReductionRulesMapped, false, newPriceValue, 2)

                .getToolBarMenu()
                .selectAll()
                .openUpdateCategoriesDialog()
                .toUpdateCategoriesDialog()
                .selectCategory(categoryWithNoReductionRulesMapped.getGroupName())
                .selectSubcategory(categoryWithNoReductionRulesMapped.getCategoryName())
                .closeUpdateCategoriesDialog()

                .editFirstClaimLine()
                .doAssert(
                        sid -> {
                            sid.assertDepreciationAmountIs(Double.valueOf(depreciationPercentageFromReductionRule));
                            sid.assertDepreciationPercentageIs(String.valueOf(depreciationPercentageFromReductionRule));
                            sid.assertThereIsNoReductionRules();
                            sid.assertAgeIs(lineAgeYears, lineAgeMonths);
                            sid.assertCategoriesTextIs(categoryWithNoReductionRulesMapped);
                        })
                .valuationGrid()
                .getValuationRow(ValuationGrid.Valuation.NEW_PRICE)
                .doAssert(ValuationGrid.ValuationRow.Asserts::assertValuationIsSelected)
                .toSettlementDialog()
                .closeSidWithOk()

                .getToolBarMenu()
                .selectAll()
                .openUpdateCategoriesDialog()
                .toUpdateCategoriesDialog()
                .selectCategory(categoryWithReductionRulesMapped.getGroupName())
                .selectSubcategory(categoryWithReductionRulesMapped.getCategoryName())
                .closeUpdateCategoriesDialog()

                .editFirstClaimLine()
                .doAssert(
                        sid -> {
                            sid.assertDepreciationAmountIs(Double.valueOf(depreciationPercentageFromReductionRule));
                            sid.assertDepreciationPercentageIs(String.valueOf(depreciationPercentageFromReductionRule));
                            sid.assertThereIsNoReductionRules();
                            sid.assertAgeIs(lineAgeYears, lineAgeMonths);
                            sid.assertCategoriesTextIs(categoryWithReductionRulesMapped);
                        })
                .valuationGrid()
                .getValuationRow(ValuationGrid.Valuation.NEW_PRICE)
                .doAssert(ValuationGrid.ValuationRow.Asserts::assertValuationIsSelected);
    }
}

