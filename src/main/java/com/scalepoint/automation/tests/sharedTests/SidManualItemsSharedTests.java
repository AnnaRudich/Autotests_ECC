package com.scalepoint.automation.tests.sharedTests;

import com.scalepoint.automation.pageobjects.dialogs.SettlementDialog;
import com.scalepoint.automation.pageobjects.modules.SettlementSummary;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.utils.Constants;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.input.Claim;
import com.scalepoint.automation.utils.data.entity.input.ClaimItem;
import com.scalepoint.automation.utils.data.entity.input.PseudoCategory;

import static com.scalepoint.automation.grid.ValuationGrid.Valuation.CUSTOMER_DEMAND;
import static com.scalepoint.automation.grid.ValuationGrid.Valuation.NEW_PRICE;

public class SidManualItemsSharedTests extends BaseTest {

    /**
     * WHEN: Include in claim option is ON
     * THEN:Amount of claim line is summed up to the total amount of claim
     */
    public void setIncludeInClaimCheckboxSharedTest(User user, Claim claim, ClaimItem claimItem) {
        loginAndCreateClaim(user, claim)
                .openSid()
                .setNewPrice(Constants.PRICE_2400)
                .includeInClaim(true)
                .doAssert(SettlementDialog.Asserts::assertIncludeInClaimSelected)
                .includeInClaim(false)
                .doAssert(SettlementDialog.Asserts::assertIncludeInClaimNotSelected);
    }


    /**
     * WHEN: input Custom demand
     * THEN: A new valuation is added to the table
     */
    public void inputCustomDemandSharedTest(User user, Claim claim, ClaimItem claimItem) {
        Double customerDemand = Constants.PRICE_500;
        loginAndCreateClaim(user, claim)
                .openSid()
                .setCustomerDemand(customerDemand)
                .valuationGrid()
                .parseValuationRow(CUSTOMER_DEMAND)
                .doAssert(valuation -> valuation.assertCashCompensationIs(customerDemand));
    }

    /**
     * WHEN: input New Price
     * THEN: A new valuation is added to the table
     */
    public void inputNewPriceSharedTest(User user, Claim claim, ClaimItem claimItem) {
        Double newPrice = Constants.PRICE_2400;
        loginAndCreateClaim(user, claim)
                .openSid()
                .setNewPrice(newPrice)
                .valuationGrid()
                .parseValuationRow(NEW_PRICE)
                .doAssert(valuation -> valuation.assertCashCompensationIs(newPrice));
    }

    public void depreciationFromSuggestionShouldBeNotUpdatedAfterChangingSharedTest(User user, Claim claim, ClaimItem claimItem) {
        loginAndCreateClaim(user, claim)
                .openSid()
                .setDescription(claimItem.getTextFieldSP())
                .setNewPrice(Constants.PRICE_500)
                .setCategory(claimItem.getCategoryVideoCamera().getGroupName())
                .setSubCategory(claimItem.getExistingSubCategoryForVideoGroupWithReductionRuleAndDepreciationPolicy())
                .automaticDepreciation(false)
                .doAssert(SettlementDialog.Asserts::assertAutomaticDepreciationLabelColor)
                .enableAge()
                .setValuation(NEW_PRICE)
                .applyReductionRuleByValue(20)
                .doAssert(sid -> sid.assertDepreciationPercentageIs("20"))
                .setCategory(claimItem.getExistingGroupWithPolicyDepreciationTypeAndReductionRule())
                .doAssert(sid -> sid.assertDepreciationPercentageIs("20"))
                .setNewPrice(Constants.PRICE_2400)
                .doAssert(sid -> sid.assertDepreciationPercentageIs("20"))
                .enterAgeYears("5")
                .doAssert(sid -> sid.assertDepreciationPercentageIs("20"))
                .closeSidWithOk()
                .editFirstClaimLine()
                .doAssert(sid -> sid.assertDepreciationPercentageIs("20"));
    }

    public void depreciationEnteredManuallyShouldBeNotUpdatedAfterActionsInSidSharedTest(User user, Claim claim, ClaimItem claimItem) {
        loginAndCreateClaim(user, claim)
                .openSid()
                .setDescription(claimItem.getTextFieldSP())
                .setNewPrice(Constants.PRICE_500)
                .setCategory(claimItem.getCategoryVideoCamera().getGroupName())
                .setSubCategory(claimItem.getExistingSubCategoryForVideoGroupWithReductionRuleAndDepreciationPolicy())
                .automaticDepreciation(false)
                .doAssert(SettlementDialog.Asserts::assertAutomaticDepreciationLabelColor)
                .enableAge()
                .setValuation(NEW_PRICE)
                .setDepreciation(15)
                .doAssert(sid -> sid.assertDepreciationPercentageIs("15"))
                .setCategory(claimItem.getCategoryOther())
                .doAssert(sid -> sid.assertDepreciationPercentageIs("15"))
                .setCategory(claimItem.getExistingGroupWithDiscretionaryDepreciationTypeAndReductionRule())
                .doAssert(sid -> sid.assertDepreciationPercentageIs("15"))
                .setNewPrice(Constants.PRICE_2400)
                .doAssert(sid -> sid.assertDepreciationPercentageIs("15"))
                .enterAgeYears("20")
                .doAssert(sid -> sid.assertDepreciationPercentageIs("15"))
                .closeSidWithOk()
                .editFirstClaimLine()
                .doAssert(sid -> sid.assertDepreciationPercentageIs("15"));
    }

    /**
     * WHEN: fill in all the fields
     * WHEN: Click Save button
     * WHEN: Open again SID
     * THEN: all the results are present
     */

    public void saveAllEnteredResultsSharedTest(User user, Claim claim, ClaimItem claimItem) {
        PseudoCategory pseudoCategory = claimItem.getCategoryBabyItems();

        loginAndCreateClaim(user, claim)
                .openSid()
                .setBaseData(claimItem)
                .closeSidWithOk()
                .findClaimLine(claimItem.getTextFieldSP()).editLine()
                .doAssert(claimLine -> {
                    claimLine.assertDescriptionIs(claimItem.getTextFieldSP());
                    claimLine.assertCategoriesTextIs(pseudoCategory);
                })
                .valuationGrid()
                .parseValuationRow(CUSTOMER_DEMAND)
                .doAssert(valuationRow -> valuationRow.assertCashCompensationIs(claimItem.getCustomerDemand()))
                .parseValuationRow(NEW_PRICE)
                .doAssert(valuationRow -> valuationRow.assertTotalAmountIs(Constants.PRICE_2400));
    }

    /**
     * WHEN: fill in all the fields
     * WHEN: Click Save button
     * WHEN: Open again SID
     * THEN: all the results are present
     * WHEN: fill in all the fields
     * WHEN: Click Cancel button
     * WHEN: Open again SID
     * THEN: all the new results are not present
     */
    public void cancelEnteredResultsSharedTest(User user, Claim claim, ClaimItem claimItem) {
        PseudoCategory pseudoCategory = claimItem.getCategoryBabyItems();

        loginAndCreateClaim(user, claim)
                .openSid()
                .setBaseData(claimItem)
                .closeSidWithOk()
                .findClaimLine(claimItem.getTextFieldSP())
                .editLine()
                .doAssert(claimLine -> {
                    claimLine.assertDescriptionIs(claimItem.getTextFieldSP());
                    claimLine.assertCategoriesTextIs(pseudoCategory);
                })
                .valuationGrid()
                .parseValuationRow(CUSTOMER_DEMAND)
                .doAssert(valuationRow -> valuationRow.assertCashCompensationIs(claimItem.getCustomerDemand()))
                .parseValuationRow(NEW_PRICE)
                .doAssert(valuationRow -> valuationRow.assertTotalAmountIs(Constants.PRICE_2400))
                .toSettlementDialog()
                .setBaseData(claimItem)
                .cancel()
                .findClaimLine(claimItem.getTextFieldSP())
                .editLine()
                .doAssert(claimLine -> {
                    claimLine.assertDescriptionIs(claimItem.getTextFieldSP());
                    claimLine.assertCategoriesTextIs(pseudoCategory);
                })
                .valuationGrid()
                .parseValuationRow(CUSTOMER_DEMAND)
                .doAssert(valuationRow -> valuationRow.assertTotalAmountIs(claimItem.getCustomerDemand()))
                .parseValuationRow(NEW_PRICE)
                .doAssert(valuationRow -> valuationRow.assertTotalAmountIs(Constants.PRICE_2400));
    }

    /**
     * WHEN: Click Add new valuation
     * WHEN: Select new valuation
     * WHEN: Input price
     * WHEN: U1 fills settlement dialog with valid values
     * THEN: New valuation appears in SID
     */
    public void addNewValuationSharedTest(User user, Claim claim, ClaimItem claimItem) {
        loginAndCreateClaim(user, claim)
                .openSid()
                .setBaseData(claimItem)
                .setReviewed(true)
                .includeInClaim(false)
                .setValuation(NEW_PRICE)
                .valuationGrid()
                .parseValuationRow(NEW_PRICE)
                .doAssert(valuation -> valuation.assertTotalAmountIs(Constants.PRICE_2400));
    }

    /**
     * WHEN: Input price
     * WHEN: U1 fills settlement dialog with valid values
     * WHEN: "Reviewed" option is enabled (checked)
     * WHEN: "Include in claim" option is disabled
     * THEN: description D1 of claim line CL1 is colored in blue
     * THEN: Claim sum value CSV = 0.00
     */
    public void disableIncludeInClaimSharedTest(User user, Claim claim, ClaimItem claimItem) {
        loginAndCreateClaim(user, claim)
                .openSid()
                .setBaseData(claimItem)
                .setReviewed(true)
                .includeInClaim(false)
                .closeSidWithOk()
                .findClaimLine(claimItem.getTextFieldSP())
                .doAssert(claimLine -> claimLine.assertLineHasColor(claimItem.getBlueColor()))
                .selectLine()
                .getSettlementSummary()
                .doAssert(menu -> menu.assertClaimSumValueIs(0.00));
    }

    /**
     * WHEN: U1 adds claim line 1 with and review and  "Include in claim" option disabled
     * WHEN: U1 adds claim line 2 CL2 with review and include to claim options are enabled
     * WHEN: CL2 value = V1
     * THEN: Claim line value is not added to Total claims sum if "Include in claim" option is disabled in SID:
     * THEN: CSV = V1
     * THEN: Subtotal claim value SCV = V1
     */
    public void enableIncludeInClaimSecondClaimSharedTest(User user, Claim claim, ClaimItem claimItem) {
        String secondClaimDescription = claimItem.getTextFieldSP().concat("second");

        SettlementPage settlementPage = loginAndCreateClaim(user, claim);
        SettlementDialog dialog = settlementPage.openSid()
                .setBaseData(claimItem)
                .setReviewed(false)
                .includeInClaim(false)
                .closeSidWithOk()
                .findClaimLine(claimItem.getTextFieldSP())
                .doAssert(claimLine -> claimLine.assertLineHasComputedColor(claimItem.getPinkColor()))
                .selectLine()
                .openSid()
                .setBaseData(claimItem)
                .setDescription(secondClaimDescription)
                .setReviewed(true)
                .includeInClaim(true);

        Double claimValue = dialog.customerDemandValue();
        dialog.closeSidWithOk()
                .getSettlementSummary()
                .doAssert(menu -> {
                    menu.assertClaimSumValueIs(claimValue);
                    menu.assertSubtotalSumValueIs(claimValue);
                });
    }

    /**
     * WHEN: Input price
     * WHEN: U1 fills settlement dialog with valid values
     * WHEN: "Reviewed" option is disabled (unchecked)
     * WHEN: "Include in claim" option is disabled
     * THEN: description D1 of claim line CL1 is colored in pink
     */
    public void disableIncludeInClaimAndReviewedSharedTest(User user, Claim claim, ClaimItem claimItem) {
        loginAndCreateClaim(user, claim)
                .openSid()
                .setBaseData(claimItem)
                .setReviewed(false)
                .includeInClaim(false)
                .closeSidWithOk()
                .findClaimLine(claimItem.getTextFieldSP())
                .doAssert(claimLine -> claimLine.assertLineHasComputedColor(claimItem.getPinkColor()));
    }

    /**
     * WHEN: Allow users to mark settlement items as reviewed" is enabled in FT
     * WHEN: Review of all claim lines are required to complete claim" is disabled in FT
     * WHEN: U1 adds claim line CL1 where "Reviewed" option is disabled
     * THEN: "Complete claim" button is enabled
     */
    public void completeClaimIsEnabledSharedTest(User user, Claim claim, ClaimItem claimItem) {
        loginAndCreateClaim(user, claim)
                .openSid()
                .setBaseData(claimItem)
                .setReviewed(false)
                .closeSidWithOk()
                .getSettlementSummary()
                .doAssert(SettlementSummary.Asserts::assertCompleteClaimEnabled);
    }

    /**
     * WHEN: Allow users to mark settlement items as reviewed" is disabled in FT
     * WHEN: Review of all claim lines are required to complete claim" is disabled in FT
     * THEN "Reviewed" option is not displayed in SID
     */
    public void reviewedBoxNotDisplayedSharedTest(User user, Claim claim) {
        loginAndCreateClaim(user, claim)
                .openSid()
                .doAssert(SettlementDialog.Asserts::assertReviewedNotPresent);
    }

    /**
     * WHEN: Allow users to mark settlement items as reviewed" is enabled in FT
     * WHEN: Review of all claim lines are required to complete claim" is enabled in FT
     * WHEN: U1 adds claim line CL1 where "Reviewed" option is disabled
     * THEN: "Complete claim" button is enabled
     */
    public void completeClaimIsEnabled2SharedTest(User user, Claim claim, ClaimItem claimItem) {
        loginAndCreateClaim(user, claim)
                .openSid()
                .setBaseData(claimItem)
                .setReviewed(false)
                .closeSidWithOk()
                .getSettlementSummary()
                .doAssert(SettlementSummary.Asserts::assertCompleteClaimEnabled);
    }

    /**
     * WHEN: U1 opens SID, fills all field with valid values(description D1 etc)
     * WHEN: select cancel
     * THEN: Cancelled claim line is not added to the claim
     */
    public void cancelledClaimNotAddedSharedTest(User user, Claim claim, ClaimItem claimItem) {
        loginAndCreateClaim(user, claim)
                .openSid()
                .setBaseData(claimItem)
                .cancel()
                .doAssert(settlementPage -> settlementPage.assertItemNotPresent(claimItem.getTextFieldSP()));
    }

    /**
     * WHEN: U1 opens SID
     * WHEN: U1 adds New price amount V1
     * THEN: Cash compensation CC is assertEqualsDouble to V1
     */

    public void cashCompensationEqualV1SharedTest(User user, Claim claim, ClaimItem claimItem) {
        loginAndCreateClaim(user, claim)
                .openSid()
                .setBaseData(claimItem)
                .doAssert(sid -> sid.assertCashValueIs(claimItem.getCustomerDemand()));
    }

    /**
     * WHEN: U1 opens SID
     * WHEN: U1 adds New price amount V1
     * THEN: Cash compensation CC is equal to V1
     * WHEN: U1 selects Add valuation option
     * THEN: Add valuation dialogs is displayed
     */
    public void openAddValuationDialogInSidSharedTest(User user, Claim claim, ClaimItem claimItem) {
        loginAndCreateClaim(user, claim)
                .openSid()
                .setBaseData(claimItem)
                .openAddValuationForm();
    }

    /**
     * WHEN: U1 opens SID
     * WHEN: U1 adds New price amount V1
     * THEN: Cash compensation CC is equal to V1
     * WHEN: U1 selects Add valuation option
     * THEN: Add valuation dialogs is displayed
     * WHEN: U1 selects third valuation type
     * WHEN: U1 adds valuation amount V2 < V1 and selects closeSidWithOk option
     * THEN: V2 is displayed in SID
     * THEN: CC is equal to V2
     */
    public void addNewValuationPriceInAddValuationDialogSharedTest(User user, Claim claim, ClaimItem claimItem) {
        loginAndCreateClaim(user, claim)
                .openSid()
                .setBaseData(claimItem)
                .doAssert(sid -> sid.assertCashValueIs(claimItem.getCustomerDemand()))
                .openAddValuationForm()
                .addValuation(claimItem.getValuationTypeUsedPrice(), claimItem.getLowerPrice())
                .doAssert(sid -> sid.assertCashValueIs(claimItem.getLowerPrice()));
    }

    /**
     * WHEN: U1 opens SID
     * WHEN: U1 adds New price amount V1
     * THEN: Cash compensation CC is equal to V1
     * WHEN: U1 selects Add valuation option
     * THEN: Add valuation dialogs is displayed
     * WHEN: U1 selects fourth valuation type
     * WHEN: U1 adds valuation amount V2 < V1 and selects closeSidWithOk option
     * THEN: V2 is displayed in SID
     * THEN: CC is equal to V2
     */
    public void addNewValuationPriceInAddValuationDialog2SharedTest(User user, Claim claim, ClaimItem claimItem) {
        loginAndCreateClaim(user, claim)
                .openSid()
                .setBaseData(claimItem)
                .doAssert(sid -> sid.assertCashValueIs(claimItem.getCustomerDemand()))
                .openAddValuationForm()
                .addValuation(claimItem.getValuationTypeDiscretionary(), claimItem.getLowerPrice())
                .doAssert(sid -> sid.assertCashValueIs(claimItem.getLowerPrice()));
    }

    /**
     * WHEN: U1 opens SID
     * WHEN: U1 adds New price amount V1
     * THEN: Cash compensation CC is equal to V1
     * WHEN: U1 selects Add valuation option
     * THEN: Add valuation dialogs is displayed
     * WHEN: U1 selects fifth valuation type
     * WHEN: U1 adds valuation amount V2 < V1 and selects closeSidWithOk option
     * THEN: V2 is displayed in SID
     * THEN: CC is equal to V2
     */
    public void addNewValuationPriceInAddValuationDialog3SharedTest(User user, Claim claim, ClaimItem claimItem) {
        loginAndCreateClaim(user, claim)
                .openSid()
                .setBaseData(claimItem)
                .doAssert(sid -> sid.assertCashValueIs(claimItem.getCustomerDemand()))
                .openAddValuationForm()
                .addValuation(claimItem.getValuationTypeRepair(), claimItem.getLowerPrice())
                .doAssert(sid -> sid.assertCashValueIs(claimItem.getLowerPrice()));
    }

    /**
     * WHEN: U1 opens SID
     * AND: U1 enables Age option (switch the radio button on)
     * THEN: Years field is enabled
     * THEN: Months drop dwn menu is enabled
     */
    public void enableAgeOptionSharedTest(User user, Claim claim, ClaimItem claimItem) {
        loginAndCreateClaim(user, claim)
                .openSid()
                .setBaseData(claimItem)
                .enableAge()
                .doAssert(sid -> {
                    sid.assertAgeYearsEnabled();
                    sid.assertMonthMenuEnabled();
                });

    }

    /**
     * WHEN: U1 opens SID
     * AND: U1 enables Age option (switch the radio button on)
     * THEN: Years field is enabled
     * THEN: Months drop dwn menu is enabled
     * WHEN: U1 adds years age Y1
     * AND: U1 adds Months age M1
     * AND: U1 fills the rest fields with valid values and saves
     * AND: U1 opens SID again
     * THEN: Y1 and M1 are displayed
     */
    public void addYearsAndMonthAndSaveSharedTest(User user, Claim claim, ClaimItem claimItem) {
        loginAndCreateClaim(user, claim)
                .openSid()
                .setBaseData(claimItem)
                .enableAge("10")
                .selectMonth("6")
                .setValuation(NEW_PRICE)
                .closeSidWithOk()
                .findClaimLine(claimItem.getTextFieldSP())
                .editLine()
                .doAssert(sid -> {
                    sid.assertYearsValueIs("10");
                    sid.assertMonthValueIs("6");
                });
    }
}