package com.scalepoint.automation.tests.sid.categoryBulkUpdate;

import com.scalepoint.automation.grid.ValuationGrid;
import com.scalepoint.automation.pageobjects.dialogs.SettlementDialog;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.services.usersmanagement.CompanyCode;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.utils.Constants;
import com.scalepoint.automation.utils.RandomUtils;
import com.scalepoint.automation.utils.annotations.RunOn;
import com.scalepoint.automation.utils.annotations.UserCompany;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.input.Claim;
import com.scalepoint.automation.utils.data.entity.input.ClaimItem;
import com.scalepoint.automation.utils.data.entity.input.PseudoCategory;
import com.scalepoint.automation.utils.driver.DriverType;
import org.testng.annotations.Test;

import static com.scalepoint.automation.grid.ValuationGrid.Valuation.NEW_PRICE;

@RequiredSetting(type = FTSetting.ENABLE_BULK_UPDATE_CATEGORY)
public class BulkUpdateCategoryOnManualLinesWithReductionRulesTest extends BaseTest {

    private int lineAgeYears = 2;
    private int lineAgeMonths = 0;
    private static String depreciationPercentageFromReductionRule;
    private double newPriceValue = Constants.PRICE_100;

    /*  pre-condition: there are two lines with ReductionRules applied;
        Apply reduction rules automatically was checked

        1. using bulk category update change category to some without rules mapped
        EXPECTED: in SID depreciation is NOT displayed, no rules also
     */
    @RunOn(DriverType.CHROME)
    @Test(dataProvider = "testDataProvider", description = "select category with NO reduction rules mapped, apply rules automatically is enabled")
    public void bulkUpdateLinesWithCategoriesWhereNoReductionRulesMapped_applyRulesAutomatically(
            @UserCompany(CompanyCode.TRYGFORSIKRING) User user, Claim claim, ClaimItem claimItem) {

        PseudoCategory categoryWithNoReductionRulesMapped = claimItem.getCategoryPersonalMedicine();

        loginAndCreateClaim(user, claim);
        new BulkUpdateCategoryOnManualLinesWithReductionRulesTest()
                .addTwoLineWithReductionRules(claimItem, true)


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
                            sid.assertThereIsNoReductionRules();
                            sid.assertAgeIs(lineAgeYears, lineAgeMonths);
                            sid.assertIsVoucherDiscountAppliedToNewPrice(newPriceValue);
                            sid.assertCategoriesTextIs(categoryWithNoReductionRulesMapped);
                        });
    }

    /*  pre-condition: there are two lines with ReductionRules applied;
        Apply reduction rules automatically was NOT checked

        1. using bulk category update change category to some without rules mapped
        EXPECTED: in SID depreciation IS displayed, but no rules
      */
    @RunOn(DriverType.CHROME)
    @Test(dataProvider = "testDataProvider", description = "select category with NO reduction rules mapped, apply rules automatically is disabled")
    public void bulkUpdateLinesWithCategoriesWhereNoReductionRulesMapped_applyRulesManually(
            @UserCompany(CompanyCode.TRYGFORSIKRING) User user, Claim claim, ClaimItem claimItem) {

        PseudoCategory categoryWithNoReductionRulesMapped = claimItem.getCategoryPersonalMedicine();

        loginAndCreateClaim(user, claim);

        new BulkUpdateCategoryOnManualLinesWithReductionRulesTest()
                .addTwoLineWithReductionRules(claimItem, false)


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
                            sid.assertDepreciationPercentageIs(depreciationPercentageFromReductionRule);//actual 30, expected 30.0
                            sid.assertThereIsNoReductionRules();
                            sid.assertAgeIs(lineAgeYears, lineAgeMonths);
                            sid.assertIsVoucherDiscountAppliedToNewPrice(newPriceValue);
                            sid.assertCategoriesTextIs(categoryWithNoReductionRulesMapped);
                        })
                .valuationGrid()
                .parseValuationRow(NEW_PRICE)
                .doAssert(ValuationGrid.ValuationRow.Asserts::assertValuationIsSelected);
    }



    private SettlementPage addTwoLineWithReductionRules(ClaimItem claimItem, Boolean automaticDepreciationSetting) {
        for (int i = 0; i < 2; i++) {
            addLine(claimItem, automaticDepreciationSetting);
        }
        return Page.at(SettlementPage.class);
    }

    private void addLine(ClaimItem claimItem, Boolean automaticDepreciationSetting) {

        PseudoCategory categoryWithReductionRulesMapped = claimItem.getCategoryMobilePhones();
        depreciationPercentageFromReductionRule =
                String.valueOf(
                        new SettlementPage()
                                .openSid()
                                .setDescription(RandomUtils.randomName("claimLine"))
                                .setCategory(categoryWithReductionRulesMapped)
                                .setNewPrice(newPriceValue)
                                .enableAge(String.valueOf(lineAgeYears))
                                .setValuation(ValuationGrid.Valuation.NEW_PRICE)
                                .automaticDepreciation(automaticDepreciationSetting)
                                .getDepreciationPercentage());

        new SettlementDialog().closeSidWithOk();
    }
}

