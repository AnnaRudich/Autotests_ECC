package com.scalepoint.automation.tests.sid;

import com.scalepoint.automation.pageobjects.dialogs.SettlementDialog;
import com.scalepoint.automation.pageobjects.modules.SettlementSummary;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.pageobjects.pages.admin.PseudoCategoryGroupPage;
import com.scalepoint.automation.services.externalapi.VoucherAgreementApi;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.testGroups.TestGroups;
import com.scalepoint.automation.tests.sharedTests.SidManualItemsSharedTests;
import com.scalepoint.automation.tests.sid.SidCalculator.PriceValuation;
import com.scalepoint.automation.utils.Constants;
import com.scalepoint.automation.utils.annotations.Bug;
import com.scalepoint.automation.utils.annotations.Jira;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.input.Claim;
import com.scalepoint.automation.utils.data.entity.input.ClaimItem;
import com.scalepoint.automation.utils.data.entity.input.PseudoCategory;
import com.scalepoint.automation.utils.data.entity.input.Voucher;
import com.scalepoint.automation.utils.threadlocal.Browser;
import org.testng.annotations.Test;

import java.util.List;

import static com.scalepoint.automation.grid.ValuationGrid.Valuation.CUSTOMER_DEMAND;
import static com.scalepoint.automation.grid.ValuationGrid.Valuation.NEW_PRICE;
import static com.scalepoint.automation.services.externalapi.ftemplates.FTSetting.ALLOW_USERS_TO_MARK_SETTLEMENT_REVIEWED;
import static com.scalepoint.automation.services.externalapi.ftemplates.FTSetting.REVIEW_ALL_CLAIM_TO_COMPLETE_CLAIM;

@Test(groups = {TestGroups.SID, TestGroups.SID_MANUAL_ITEMS})
@Jira("https://jira.scalepoint.com/browse/CHARLIE-536")
@RequiredSetting(type = FTSetting.SHOW_NOT_CHEAPEST_CHOICE_POPUP, enabled = false)
public class SidManualItemsTests extends SidManualItemsSharedTests {

    /**
     * WHEN: Include in claim option is ON
     * THEN:Amount of claim line is summed up to the total amount of claim
     */
    @Test(dataProvider = "testDataProvider",
            description = "ECC-3144 Verify Include in claim option is ON")
    public void setIncludeInClaimCheckboxTest(User user, Claim claim, ClaimItem claimItem) {
        setIncludeInClaimCheckboxSharedTest(user, claim, claimItem);
    }

    /**
     * WHEN: Select category
     * THEN: Categories are selected according to Pseudocategories mapping on Admin page
     */

    @Jira("https://jira.scalepoint.com/browse/CHARLIE-536")
    @Test(dataProvider = "testDataProvider",
            description = "ECC-3144 Verify it is possible to select categories")
    public void ecc3144_2_selectCategory(User user, Claim claim, ClaimItem claimItem) {
        loginFlow.loginAndCreateClaim(user, claim);

        String currentUrl = Browser.driver().getCurrentUrl();

        String categoryGroup = claimItem.getCategoryBabyItems().getGroupName();

        List<String> allSubCategories = Page.to(PseudoCategoryGroupPage.class)
                .editGroup(categoryGroup)
                .getAllPseudoCategories();

        Browser.driver().get(currentUrl);

        Page.at(SettlementPage.class)
                .openSid()
                .setCategory(categoryGroup)
                .doAssert(sid -> sid.assertSubCategoriesListEqualTo(allSubCategories));
    }

    /**
     * WHEN: input Custom demand
     * THEN: A new valuation is added to the table
     */

    @Jira("https://jira.scalepoint.com/browse/CHARLIE-536")
    @Test(dataProvider = "testDataProvider",
            description = "ECC-3144 Verify it is possible to input Customer demand")
    public void inputCustomDemandTest(User user, Claim claim, ClaimItem claimItem) {
        inputCustomDemandSharedTest(user, claim, claimItem);
    }

    /**
     * WHEN: input New Price
     * THEN: A new valuation is added to the table
     */
    @Jira("https://jira.scalepoint.com/browse/CHARLIE-536")
    @Test( dataProvider = "testDataProvider",
            description = "ECC-3144 Verify it is possible to input New price")
    public void inputNewPriceTest(User user, Claim claim, ClaimItem claimItem) {
        inputNewPriceSharedTest(user, claim, claimItem);
    }

    /**
     * WHEN: input depreciation into the field Depreciation
     * THEN: Depreciation percent is deducted from the selected valuation in the table
     * THEN: Amount of depreciation is shown at the bottom of SID
     * THEN: Depreciation percent is shown on the settlement page after saving SID
     */
    @Jira("https://jira.scalepoint.com/browse/CHARLIE-536")
    @Test(dataProvider = "testDataProvider",
            description = "ECC-3144 Verify a manual depreciation can be entered into the field Depreciation")
    @RequiredSetting(type = FTSetting.DO_NOT_DEPRECIATE_CUSTOMER_DEMAND, enabled = false, isDefault = true)
    @RequiredSetting(type = FTSetting.ENABLE_DEPRECIATION_COLUMN)
    @RequiredSetting(type = FTSetting.SHOW_DEPRECIATION_AUTOMATICALLY_UPDATED)
    @RequiredSetting(type = FTSetting.SHOW_SUGGESTED_DEPRECIATION_SECTION)
    public void ecc3144_5_manualDepreciation(User user, Claim claim, ClaimItem claimItem, Voucher voucher) {
        PseudoCategory pseudoCategory = new VoucherAgreementApi(user).createVoucher(voucher);

        SettlementDialog dialog = loginFlow.loginAndCreateClaim(user, claim)
                .openSidAndFill(pseudoCategory, sid -> {
                    sid.withText(claimItem.getTextFieldSP())
                            .withCustomerDemandPrice(Constants.PRICE_500)
                            .withVoucher(voucher.getVoucherNameSP())
                            .withDepreciation(Constants.DEPRECIATION_10)
                            .withValuation(CUSTOMER_DEMAND);
                });

        PriceValuation expectedCalculations = SidCalculator.calculatePriceValuation(Constants.PRICE_500, Constants.DEPRECIATION_10);

        dialog.doAssert(sid -> {
            sid.assertCashValueIs(expectedCalculations.getCashValue());
            sid.assertDepreciationAmountIs(expectedCalculations.getDepreciation());
        })
                .valuationGrid()
                .getValuationRow(CUSTOMER_DEMAND)
                .doAssert(valuationRow -> {
                    valuationRow.assertCashCompensationIs(expectedCalculations.getCashValue());
                    valuationRow.assertDepreciationPercentageIs(10);
                });

    }

    @Jira("https://jira.scalepoint.com/browse/CHARLIE-532")
    @Test(dataProvider = "testDataProvider",
            description = "ECC-3953 Verify depreciation is not updated if type of depreciation is changed")
    @RequiredSetting(type = FTSetting.ENABLE_DEPRECIATION_COLUMN)
    @RequiredSetting(type = FTSetting.SHOW_DEPRECIATION_AUTOMATICALLY_UPDATED)
    @RequiredSetting(type = FTSetting.SHOW_SUGGESTED_DEPRECIATION_SECTION)
    public void depreciationFromSuggestionShouldBeNotUpdatedAfterChangingTest(User user, Claim claim, ClaimItem claimItem) {
        depreciationFromSuggestionShouldBeNotUpdatedAfterChangingSharedTest(user, claim, claimItem);
    }

    @Jira("https://jira.scalepoint.com/browse/CHARLIE-532")
    @Test(dataProvider = "testDataProvider",
            description = "ECC-3953 .doAssert(sid -> sid.assertDepreciationPercentageIs(\"10\"))")
    @RequiredSetting(type = FTSetting.SHOW_DEPRECIATION_AUTOMATICALLY_UPDATED)
    @RequiredSetting(type = FTSetting.SHOW_SUGGESTED_DEPRECIATION_SECTION, enabled = false)
    public void depreciationEnteredManuallyShouldBeNotUpdatedAfterActionsInSidTest(User user, Claim claim, ClaimItem claimItem) {
        depreciationEnteredManuallyShouldBeNotUpdatedAfterActionsInSidSharedTest(user, claim, claimItem);
    }

    /**
     * WHEN: fill in all the fields
     * WHEN: Click Save button
     * WHEN: Open again SID
     * THEN: all the results are present
     */

    @Test(dataProvider = "testDataProvider",
            description = "ECC-3144 Verify it is possible to Save all results entered")
    @RequiredSetting(type = FTSetting.ENABLE_DEPRECIATION_COLUMN)
    public void saveAllEnteredResultsTest(User user, Claim claim, ClaimItem claimItem) {
        saveAllEnteredResultsSharedTest(user, claim, claimItem);
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
    @Test(dataProvider = "testDataProvider",
            description = "ECC-3144 Verify clicking Cancel doesn't save entered info")
    @RequiredSetting(type = FTSetting.ENABLE_DEPRECIATION_COLUMN)
    public void cancelEnteredResultsTest(User user, Claim claim, ClaimItem claimItem) {
        cancelEnteredResultsSharedTest(user, claim, claimItem);
    }

    /**
     * WHEN: Click Add new valuation
     * WHEN: Select new valuation
     * WHEN: Input price
     * WHEN: U1 fills settlement dialog with valid values
     * THEN: New valuation appears in SID
     */
    @Test(dataProvider = "testDataProvider",
            description = "ECC-3144 Verify it is possible to add new valuation")
    @RequiredSetting(type = ALLOW_USERS_TO_MARK_SETTLEMENT_REVIEWED)
    @RequiredSetting(type = FTSetting.ENABLE_DEPRECIATION_COLUMN)
    public void addNewValuationTest(User user, Claim claim, ClaimItem claimItem) {
        addNewValuationSharedTest(user, claim, claimItem);
    }

    /**
     * WHEN: Input price
     * WHEN: U1 fills settlement dialog with valid values
     * WHEN: "Reviewed" option is enabled (checked)
     * WHEN: "Include in claim" option is disabled
     * THEN: description D1 of claim line CL1 is colored in blue
     * THEN: Claim sum value CSV = 0.00
     */
    @Test(dataProvider = "testDataProvider",
            description = "ECC-3144 Verify Claim line description is displayed in blue if the options \"Include in claim\" disabled" +
                    "- Claim line value is not added to Total claims sum")
    @RequiredSetting(type = ALLOW_USERS_TO_MARK_SETTLEMENT_REVIEWED)
    public void disableIncludeInClaimTest(User user, Claim claim, ClaimItem claimItem) {
        disableIncludeInClaimSharedTest(user, claim, claimItem);
    }

    /**
     * WHEN: U1 adds claim line 1 with and review and  "Include in claim" option disabled
     * WHEN: U1 adds claim line 2 CL2 with review and include to claim options are enabled
     * WHEN: CL2 value = V1
     * THEN: Claim line value is not added to Total claims sum if "Include in claim" option is disabled in SID:
     * THEN: CSV = V1
     * THEN: Subtotal claim value SCV = V1
     */
    @Test(dataProvider = "testDataProvider",
            description = "ECC-3144 Verify that second claim line value is not added to Total claims sum if the options " +
                    "'Include in claim' and 'Reviewed' enabled")
    @RequiredSetting(type = ALLOW_USERS_TO_MARK_SETTLEMENT_REVIEWED)
    public void enableIncludeInClaimSecondClaimTest(User user, Claim claim, ClaimItem claimItem) {
        enableIncludeInClaimSecondClaimSharedTest(user,claim, claimItem);
    }

    /**
     * WHEN: Input price
     * WHEN: U1 fills settlement dialog with valid values
     * WHEN: "Reviewed" option is disabled (unchecked)
     * WHEN: "Include in claim" option is disabled
     * THEN: description D1 of claim line CL1 is colored in pink
     */

    @Test(dataProvider = "testDataProvider",
            description = "ECC-3144 Verify Claim line description is displayed in pink if the options 'Include in claim'  " +
                    "and 'Reviewed' disabled")
    public void disableIncludeInClaimAndReviewedTest(User user, Claim claim, ClaimItem claimItem) {
        disableIncludeInClaimAndReviewedSharedTest(user, claim, claimItem);
    }

    /**
     * WHEN: Allow users to mark settlement items as reviewed" is enabled in FT
     * WHEN: Review of all claim lines are required to complete claim" is disabled in FT
     * WHEN: U1 adds claim line CL1 where "Reviewed" option is disabled
     * THEN: "Complete claim" button is enabled
     */

    @Test(dataProvider = "testDataProvider",
            description = "ECC-3144 Verify 'Complete claim' is enable if 'Reviewed' is disabled in SID")
    @RequiredSetting(type = ALLOW_USERS_TO_MARK_SETTLEMENT_REVIEWED)
    public void completeClaimIsEnabledTest(User user, Claim claim, ClaimItem claimItem) {
        completeClaimIsEnabledSharedTest(user, claim, claimItem);
    }

    /**
     * WHEN: Allow users to mark settlement items as reviewed" is disabled in FT
     * WHEN: Review of all claim lines are required to complete claim" is disabled in FT
     * THEN "Reviewed" option is not displayed in SID
     */
    @Bug(bug = "CHARLIE-391")
    @Test(dataProvider = "testDataProvider",
            description = "ECC-3144 Verify 'Reviewed' box is not displayed")
    @RequiredSetting(type = REVIEW_ALL_CLAIM_TO_COMPLETE_CLAIM, enabled = false)
    @RequiredSetting(type = ALLOW_USERS_TO_MARK_SETTLEMENT_REVIEWED, enabled = false)
    public void reviewedBoxNotDisplayedTest(User user, Claim claim) {
        reviewedBoxNotDisplayedSharedTest(user, claim);
    }

    /**
     * WHEN: Allow users to mark settlement items as reviewed" is enabled in FT
     * WHEN: Review of all claim lines are required to complete claim" is enabled in FT
     * WHEN: U1 adds claim line CL1 where "Reviewed" option is disabled
     * THEN: "Complete claim" button is enabled
     */
    @Test(dataProvider = "testDataProvider",
            description = "ECC-3144 Verify 'Complete claim' is enabled if 'Reviewed' is disabled in SID")
    @RequiredSetting(type = ALLOW_USERS_TO_MARK_SETTLEMENT_REVIEWED)
    @RequiredSetting(type = REVIEW_ALL_CLAIM_TO_COMPLETE_CLAIM)
    public void completeClaimIsEnabled2Test(User user, Claim claim, ClaimItem claimItem) {
        completeClaimIsEnabled2SharedTest(user, claim, claimItem);
    }

    /**
     * WHEN: U1 opens SID, fills all field with valid values(description D1 etc)
     * WHEN: select cancel
     * THEN: Cancelled claim line is not added to the claim
     */
    @Test(dataProvider = "testDataProvider",
            description = "ECC-3144 Verify cancelled claim line is not added to the claim")
    public void cancelledClaimNotAddedTest(User user, Claim claim, ClaimItem claimItem) {
        cancelledClaimNotAddedSharedTest(user, claim, claimItem);
    }

    /**
     * WHEN: U1 opens SID
     * WHEN: U1 adds New price amount V1
     * THEN: Cash compensation CC is assertEqualsDouble to V1
     */

    @Test(dataProvider = "testDataProvider",
            description = "ECC-3144 Verify Cash compensation CC is equal to V1")
    public void cashCompensationEqualV1Test(User user, Claim claim, ClaimItem claimItem) {
        cashCompensationEqualV1SharedTest(user, claim, claimItem);
    }

    /**
     * WHEN: U1 opens SID
     * WHEN: U1 adds New price amount V1
     * THEN: Cash compensation CC is equal to V1
     * WHEN: U1 selects Add valuation option
     * THEN: Add valuation dialogs is displayed
     */
    @Test(dataProvider = "testDataProvider",
            description = "ECC-3144 Verify it's possible to open Add Valuation dialogs in SID")
    public void openAddValuationDialogInSidTest(User user, Claim claim, ClaimItem claimItem) {
        openAddValuationDialogInSidSharedTest(user, claim, claimItem);
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
    @Test(dataProvider = "testDataProvider",
            description = "ECC-3144 Verify it's possible to add new valuation price in add " +
                    "valuation dialogs (user selects 3d type)")
    public void addNewValuationPriceInAddValuationDialogTest(User user, Claim claim, ClaimItem claimItem) {
        addNewValuationPriceInAddValuationDialogSharedTest(user, claim, claimItem);
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
    @Test(dataProvider = "testDataProvider",
            description = "ECC-3144 Verify it's possible to add new valuation price in add valuation dialogs (user selects 4th type)")
    public void addNewValuationPriceInAddValuationDialog2Test(User user, Claim claim, ClaimItem claimItem) {
        addNewValuationPriceInAddValuationDialog2SharedTest(user, claim, claimItem);
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
    @Test(dataProvider = "testDataProvider",
            description = "ECC-3144 Verify it's possible to add new valuation price in " +
                    "add valuation dialogs (user selects 5th type)")
    public void addNewValuationPriceInAddValuationDialog3Test(User user, Claim claim, ClaimItem claimItem) {
        addNewValuationPriceInAddValuationDialog3SharedTest(user, claim, claimItem);
    }

    /**
     * WHEN: U1 opens SID
     * AND: U1 enables Age option (switch the radio button on)
     * THEN: Years field is enabled
     * THEN: Months drop dwn menu is enabled
     */
    @Test(dataProvider = "testDataProvider",
            description = "ECC-3144 Verify it's possible to enable age option")
    public void enableAgeOptionTest(User user, Claim claim, ClaimItem claimItem) {
        enableAgeOptionSharedTest(user, claim, claimItem);

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
    @Test(dataProvider = "testDataProvider",
            description = "ECC-3144 Verify it's possible to add years & month and save set")
    public void addYearsAndMonthAndSaveTest(User user, Claim claim, ClaimItem claimItem) {
        addYearsAndMonthAndSaveSharedTest(user, claim, claimItem);
    }

    /**
     * WHEN: U1 opens SID
     * WHEN: U1 disables Age option and saves
     * AND: U1 opens SID again
     * THEN: Age option is disabled
     */
    @Jira("https://jira.scalepoint.com/browse/CHARLIE-1382")
    @Test(enabled = false, dataProvider = "testDataProvider",
            description = "ECC-3144 Verify it's possible to disable age checkbox")
    public void ecc3144_24_disableAgeAndSave(User user, Claim claim, ClaimItem claimItem) {
        loginFlow.loginAndCreateClaim(user, claim)
                .openSid()
                .setBaseData(claimItem)
                .enableAge()
                .doAssert(sid -> {
                    sid.assertAgeYearsEnabled();
                    sid.assertMonthMenuEnabled();
                })
                .disableAge()
                .closeSidWithOk()
                .findClaimLine(claimItem.getTextFieldSP())
                .editLine()
                .doAssert(sid -> {
                    sid.assertAgeYearsDisabled();
                    sid.assertMonthMenuDisabled();
                });
    }

    @Jira("https://jira.scalepoint.com/browse/CLAIMSHOP-4667")
    @Test(dataProvider = "testDataProvider",
            description = "possible to add one manual line when SID_ADD_BUTTON_ON_NEW_MANUAL_ITEM FeatureToggle is ON")
    public void addOneManualLineWhenMultipleAddIsOn(User user, Claim claim, ClaimItem claimItem){

        loginFlow.loginAndCreateClaim(user, claim)
                .openSid()
                .setBaseData(claimItem)
                .setValuation(NEW_PRICE)
                .closeSidWithOk()
                .doAssert(sid->{
                    sid.assertItemIsPresent(claimItem.getTextFieldSP());
                });

        new SettlementSummary().doAssert(asserts -> asserts.assertSubtotalSumValueIs(claimItem.getNewPriceSP()));

        new SettlementPage()
                .openSid()
                .setBaseData(claimItem)
                .setValuation(NEW_PRICE)
                .closeSidWithOk();
    }

    @Jira("https://jira.scalepoint.com/browse/CLAIMSHOP-4667")
    @Test(dataProvider = "testDataProvider",
            description = "possible to add one manual line when SID_ADD_BUTTON_ON_NEW_MANUAL_ITEM FeatureToggle is ON")
    public void E2E_addButtonOnNewManualItem(User user, Claim claim, ClaimItem claimItem1, ClaimItem claimItem2){

        loginFlow.loginAndCreateClaim(user, claim)
                .openSid()
                .setBaseData(claimItem1)
                .setValuation(NEW_PRICE)
                .addOneMoreManualLine()
                .setBaseData(claimItem2)
                .setValuation(NEW_PRICE)
                .closeSidWithOk()
                .doAssert(sid->{
                    sid.assertItemIsPresent(claimItem1.getTextFieldSP());
                    sid.assertItemIsPresent(claimItem2.getTextFieldSP());
                });

        new SettlementSummary().doAssert(asserts -> asserts.assertSubtotalSumValueIs(claimItem2.getNewPriceSP()+claimItem1.getNewPriceSP()));

        new SettlementPage().editFirstClaimLine()
                .doAssert(SettlementDialog.Asserts::assertThereIsNoAddButton);
    }
}
