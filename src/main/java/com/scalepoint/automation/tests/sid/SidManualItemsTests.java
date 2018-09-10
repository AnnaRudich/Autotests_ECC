package com.scalepoint.automation.tests.sid;

import com.scalepoint.automation.pageobjects.dialogs.SettlementDialog;
import com.scalepoint.automation.pageobjects.modules.SettlementSummary;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.pageobjects.pages.admin.PseudoCategoryGroupPage;
import com.scalepoint.automation.services.externalapi.VoucherAgreementApi;
import com.scalepoint.automation.services.externalapi.VoucherAgreementApi.AssignedCategory;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.tests.sid.SidCalculator.PriceValuation;
import com.scalepoint.automation.utils.Constants;
import com.scalepoint.automation.utils.annotations.Bug;
import com.scalepoint.automation.utils.annotations.Jira;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.ClaimItem;
import com.scalepoint.automation.utils.data.entity.Voucher;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.threadlocal.Browser;
import org.testng.annotations.Test;

import java.util.List;

import static com.scalepoint.automation.pageobjects.dialogs.SettlementDialog.Valuation.CUSTOMER_DEMAND;
import static com.scalepoint.automation.pageobjects.dialogs.SettlementDialog.Valuation.NEW_PRICE;
import static com.scalepoint.automation.services.externalapi.ftemplates.FTSetting.ALLOW_USERS_TO_MARK_SETTLEMENT_REVIEWED;
import static com.scalepoint.automation.services.externalapi.ftemplates.FTSetting.REVIEW_ALL_CLAIM_TO_COMPLETE_CLAIM;

@Jira("https://jira.scalepoint.com/browse/CHARLIE-536")
@RequiredSetting(type = FTSetting.SHOW_NOT_CHEAPEST_CHOICE_POPUP, enabled = false)
public class SidManualItemsTests extends BaseTest {

    /**
     * WHEN: Include in claim option is ON
     * THEN:Amount of claim line is summed up to the total amount of claim
     */

    @Test(dataProvider = "testDataProvider", description = "ECC-3144 Verify Include in claim option is ON")
    public void ecc3144_1_setIncludeInClaimCheckbox(User user, Claim claim, ClaimItem claimItem) {
        loginAndCreateClaim(user, claim)
                .openSid()
                .setNewPrice(Constants.PRICE_2400)
                .includeInClaim(true)
                .doAssert(SettlementDialog.Asserts::assertIncludeInClaimSelected)
                .includeInClaim(false)
                .doAssert(SettlementDialog.Asserts::assertIncludeInClaimNotSelected);
    }

    /**
     * WHEN: Select category
     * THEN: Categories are selected according to Pseudocategories mapping on Admin page
     */
    @Jira("https://jira.scalepoint.com/browse/CHARLIE-536")
    @Test(dataProvider = "testDataProvider", description = "ECC-3144 Verify it is possible to select categories")
    public void ecc3144_2_selectCategory(User user, Claim claim, ClaimItem claimItem) {
        loginAndCreateClaim(user, claim);

        String currentUrl = Browser.driver().getCurrentUrl();

        List<String> allSubCategories = Page.to(PseudoCategoryGroupPage.class)
                .editGroup(claimItem.getCategoryGroupBorn())
                .getAllPseudoCategories();

        Browser.driver().get(currentUrl);

        Page.at(SettlementPage.class)
                .openSid()
                .setCategory(claimItem.getCategoryGroupBorn())
                .doAssert(sid -> sid.assertSubCategoriesListEqualTo(allSubCategories));
    }

    /**
     * WHEN: input Custom demand
     * THEN: A new valuation is added to the table
     */
    @Jira("https://jira.scalepoint.com/browse/CHARLIE-536")
    @Test(dataProvider = "testDataProvider", description = "ECC-3144 Verify it is possible to input Customer demand")
    public void ecc3144_3_inputCustomDemand(User user, Claim claim, ClaimItem claimItem) {
        Double customerDemand = Constants.PRICE_500;
        loginAndCreateClaim(user, claim)
                .openSid()
                .setCustomerDemand(customerDemand)
                .parseValuationRow(CUSTOMER_DEMAND)
                .doAssert(valuation -> valuation.assertCashCompensationIs(customerDemand));
    }


    /**
     * WHEN: input New Price
     * THEN: A new valuation is added to the table
     */
    @Jira("https://jira.scalepoint.com/browse/CHARLIE-536")
    @Test(dataProvider = "testDataProvider", description = "ECC-3144 Verify it is possible to input New price")
    public void ecc3144_4_inputNewPrice(User user, Claim claim, ClaimItem claimItem) {
        Double newPrice = Constants.PRICE_2400;
        loginAndCreateClaim(user, claim)
                .openSid()
                .setNewPrice(newPrice)
                .parseValuationRow(NEW_PRICE)
                .doAssert(valuation -> valuation.assertCashCompensationIs(newPrice));
    }

    /**
     * WHEN: input depreciation into the field Depreciation
     * THEN: Depreciation percent is deducted from the selected valuation in the table
     * THEN: Amount of depreciation is shown at the bottom of SID
     * THEN: Depreciation percent is shown on the settlement page after saving SID
     */
    @Jira("https://jira.scalepoint.com/browse/CHARLIE-536")
    @Test(dataProvider = "testDataProvider", description = "ECC-3144 Verify a manual depreciation can be entered into the field Depreciation")
    @RequiredSetting(type = FTSetting.DO_NOT_DEPRECIATE_CUSTOMER_DEMAND, enabled=false, isDefault = true)
    @RequiredSetting(type = FTSetting.ENABLE_DEPRECIATION_COLUMN)
    @RequiredSetting(type = FTSetting.SHOW_DEPRECIATION_AUTOMATICALLY_UPDATED)
    @RequiredSetting(type = FTSetting.SHOW_SUGGESTED_DEPRECIATION_SECTION)
    public void ecc3144_5_manualDepreciation(User user, Claim claim, ClaimItem claimItem, Voucher voucher) {
        AssignedCategory categoryInfo = new VoucherAgreementApi(user).createVoucher(voucher);

        SettlementDialog dialog = loginAndCreateClaim(user, claim)
                .openSid()
                .setDescription(claimItem.getTextFieldSP())
                .setCustomerDemand(Constants.PRICE_500)
                .setCategory(categoryInfo)
                .fillVoucher(voucher.getVoucherGeneratedName())
                .setDepreciation(Constants.DEPRECIATION_10)
                .setValuation(CUSTOMER_DEMAND);

        PriceValuation expectedCalculations = SidCalculator.calculatePriceValuation(Constants.PRICE_500, Constants.DEPRECIATION_10);

        dialog
                .doAssert(sid -> {
                    sid.assertCashValueIs(expectedCalculations.getCashValue());
                    sid.assertDepreciationAmountIs(expectedCalculations.getDepreciation());
                }).parseValuationRow(CUSTOMER_DEMAND)
                .doAssert(valuationRow -> {
                    valuationRow.assertCashCompensationIs(expectedCalculations.getCashValue());
                    valuationRow.assertDepreciationPercentageIs(10);
                });

    }

    @Jira("https://jira.scalepoint.com/browse/CHARLIE-532")
    @Test(dataProvider = "testDataProvider", description = "ECC-3953 Verify depreciation is not updated if type of depreciation is changed")
    @RequiredSetting(type = FTSetting.ENABLE_DEPRECIATION_COLUMN)
    @RequiredSetting(type = FTSetting.SHOW_DEPRECIATION_AUTOMATICALLY_UPDATED)
    @RequiredSetting(type = FTSetting.SHOW_SUGGESTED_DEPRECIATION_SECTION)
    public void charlie_532_depreciationFromSuggestionShouldBeNotUpdatedAfterChanging(User user, Claim claim, ClaimItem claimItem, Voucher voucher){
        loginAndCreateClaim(user, claim)
                .openSid()
                .setDescription(claimItem.getTextFieldSP())
                .setNewPrice(Constants.PRICE_500)
                .setCategory(claimItem.getExistingGroupFotoAndVideo())
                .setSubCategory(claimItem.getExistingSubCategoryForVideoGroupWithReductionRuleAndDepreciationPolicy())
                .fillVoucher(voucher.getVoucherGeneratedName())
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

    @Jira("https://jira.scalepoint.com/browse/CHARLIE-532")
    @Test(dataProvider = "testDataProvider", description = "ECC-3953 .doAssert(sid -> sid.assertDepreciationPercentageIs(\"10\"))")
    @RequiredSetting(type = FTSetting.SHOW_DEPRECIATION_AUTOMATICALLY_UPDATED)
    @RequiredSetting(type = FTSetting.SHOW_SUGGESTED_DEPRECIATION_SECTION, enabled = false)
    public void charlie_532_depreciationEnteredManuallyShouldBeNotUpdatedAfterActionsInSid(User user, Claim claim, ClaimItem claimItem, Voucher voucher){
        loginAndCreateClaim(user, claim)
                .openSid()
                .setDescription(claimItem.getTextFieldSP())
                .setNewPrice(Constants.PRICE_500)
                .setCategory(claimItem.getExistingGroupFotoAndVideo())
                .setSubCategory(claimItem.getExistingSubCategoryForVideoGroupWithReductionRuleAndDepreciationPolicy())
                .fillVoucher(voucher.getVoucherGeneratedName())
                .automaticDepreciation(false)
                .doAssert(SettlementDialog.Asserts::assertAutomaticDepreciationLabelColor)
                .enableAge()
                .setValuation(NEW_PRICE)
                .setDepreciation(15)
                .doAssert(sid -> sid.assertDepreciationPercentageIs("15"))
                .setCategory(claimItem.getExistingCatWithoutVoucherAndSubCategory())
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
    @Test(dataProvider = "testDataProvider", description = "ECC-3144 Verify it is possible to Save all results entered")
    @RequiredSetting(type = FTSetting.ENABLE_DEPRECIATION_COLUMN)
    public void ecc3144_6_SaveAllEnteredResults(User user, Claim claim, ClaimItem claimItem) {
        loginAndCreateClaim(user, claim)
                .openSid()
                .setBaseData(claimItem)
                .closeSidWithOk()
                .findClaimLine(claimItem.getTextFieldSP()).editLine()
                .doAssert(claimLine -> {
                    claimLine.assertDescriptionIs(claimItem.getTextFieldSP());
                    claimLine.assertCategoryTextIs(claimItem.getCategoryGroupBorn());
                    claimLine.assertSubCategoryTextIs(claimItem.getCategoryBornBabyudstyr());
                })
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
    @Test(dataProvider = "testDataProvider", description = "ECC-3144 Verify clicking Cancel doesn't save entered info")
    @RequiredSetting(type = FTSetting.ENABLE_DEPRECIATION_COLUMN)
    public void ecc3144_7_CancelEnteredResults(User user, Claim claim, ClaimItem claimItem) {
        loginAndCreateClaim(user, claim)
                .openSid()
                .setBaseData(claimItem)
                .closeSidWithOk()
                .findClaimLine(claimItem.getTextFieldSP())
                .editLine()
                .doAssert(claimLine -> {
                    claimLine.assertDescriptionIs(claimItem.getTextFieldSP());
                    claimLine.assertCategoryTextIs(claimItem.getCategoryGroupBorn());
                    claimLine.assertSubCategoryTextIs(claimItem.getCategoryBornBabyudstyr());
                })
                .parseValuationRow(CUSTOMER_DEMAND)
                .doAssert(valuationRow -> valuationRow.assertCashCompensationIs(claimItem.getCustomerDemand()))
                .parseValuationRow(NEW_PRICE)
                .doAssert(valuationRow -> valuationRow.assertTotalAmountIs(Constants.PRICE_2400))
                .setBaseData(claimItem)
                .cancel()
                .findClaimLine(claimItem.getTextFieldSP())
                .editLine()
                .doAssert(claimLine -> {
                    claimLine.assertDescriptionIs(claimItem.getTextFieldSP());
                    claimLine.assertCategoryTextIs(claimItem.getCategoryGroupBorn());
                    claimLine.assertSubCategoryTextIs(claimItem.getCategoryBornBabyudstyr());
                })
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
    @Test(dataProvider = "testDataProvider", description = "ECC-3144 Verify it is possible to add new valuation")
    @RequiredSetting(type = ALLOW_USERS_TO_MARK_SETTLEMENT_REVIEWED)
    @RequiredSetting(type = FTSetting.ENABLE_DEPRECIATION_COLUMN)
    public void ecc3144_8_addNewValuation(User user, Claim claim, ClaimItem claimItem) {
        loginAndCreateClaim(user, claim)
                .openSid()
                .setBaseData(claimItem)
                .setReviewed(true)
                .includeInClaim(false)
                .setValuation(SettlementDialog.Valuation.NEW_PRICE)
                .waitASecond()
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
    @Test(dataProvider = "testDataProvider", description = "ECC-3144 Verify Claim line description is displayed in blue if the options \"Include in claim\" disabled" +
            "- Claim line value is not added to Total claims sum")
    @RequiredSetting(type = ALLOW_USERS_TO_MARK_SETTLEMENT_REVIEWED)
    public void ecc3144_9_disableIncludeInClaim(User user, Claim claim, ClaimItem claimItem) {
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
    @Test(dataProvider = "testDataProvider", description = "ECC-3144 Verify that second claim line value is not added to Total claims sum if the options " +
            "'Include in claim' and 'Reviewed' enabled")
    @RequiredSetting(type = ALLOW_USERS_TO_MARK_SETTLEMENT_REVIEWED)
    public void ecc3144_11_enableIncludeInClaimSecondClaim(User user, Claim claim, ClaimItem claimItem) {
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
    @Test(dataProvider = "testDataProvider", description = "ECC-3144 Verify Claim line description is displayed in pink if the options 'Include in claim'  " +
            "and 'Reviewed' disabled")
    public void ecc3144_10_disableIncludeInClaimAndReviewed(User user, Claim claim, ClaimItem claimItem) {
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
    @Test(dataProvider = "testDataProvider", description = "ECC-3144 Verify 'Complete claim' is enable if 'Reviewed' is disabled in SID")
    @RequiredSetting(type = ALLOW_USERS_TO_MARK_SETTLEMENT_REVIEWED)
    public void ecc3144_12_completeClaimIsEnabled(User user, Claim claim, ClaimItem claimItem) {
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
    @Bug(bug = "CHARLIE-391")
    @Test(dataProvider = "testDataProvider", description = "ECC-3144 Verify 'Reviewed' box is not displayed")
    @RequiredSetting(type = REVIEW_ALL_CLAIM_TO_COMPLETE_CLAIM, enabled = false)
    @RequiredSetting(type = ALLOW_USERS_TO_MARK_SETTLEMENT_REVIEWED, enabled = false)
    public void ecc3144_14_ReviewedBoxNotDisplayed(User user, Claim claim) {
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
    @Test(dataProvider = "testDataProvider", description = "ECC-3144 Verify 'Complete claim' is enabled if 'Reviewed' is disabled in SID")
    @RequiredSetting(type = ALLOW_USERS_TO_MARK_SETTLEMENT_REVIEWED)
    @RequiredSetting(type = REVIEW_ALL_CLAIM_TO_COMPLETE_CLAIM)
    public void ecc3144_13_completeClaimIsEnabled(User user, Claim claim, ClaimItem claimItem) {
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
    @Test(dataProvider = "testDataProvider", description = "ECC-3144 Verify cancelled claim line is not added to the claim")
    public void ecc3144_15_cancelledClaimNotAdded(User user, Claim claim, ClaimItem claimItem) {
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
    @Test(dataProvider = "testDataProvider", description = "ECC-3144 Verify Cash compensation CC is equal to V1")
    public void ecc3144_16_cashCompensationEqualV1(User user, Claim claim, ClaimItem claimItem) {
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
     * THEN: Add valuation dialog is displayed
     */
    @Test(dataProvider = "testDataProvider", description = "ECC-3144 Verify it's possible to open Add Valuation dialog in SID")
    public void ecc3144_17_openAddValuationDialogInSID(User user, Claim claim, ClaimItem claimItem) {
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
     * THEN: Add valuation dialog is displayed
     * WHEN: U1 selects third valuation type
     * WHEN: U1 adds valuation amount V2 < V1 and selects closeSidWithOk option
     * THEN: V2 is displayed in SID
     * THEN: CC is equal to V2
     */
    @Test(dataProvider = "testDataProvider", description = "ECC-3144 Verify it's possible to add new valuation price in add " +
            "valuation dialog (user selects 3d type)")
    public void ecc3144_18_addNewValuationPriceInAddValuationDialog(User user, Claim claim, ClaimItem claimItem) {
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
     * THEN: Add valuation dialog is displayed
     * WHEN: U1 selects fourth valuation type
     * WHEN: U1 adds valuation amount V2 < V1 and selects closeSidWithOk option
     * THEN: V2 is displayed in SID
     * THEN: CC is equal to V2
     */
    @Test(dataProvider = "testDataProvider", description = "ECC-3144 Verify it's possible to add new valuation price in add valuation dialog (user selects 4th type)")
    public void ecc3144_19_addNewValuationPriceInAddValuationDialog(User user, Claim claim, ClaimItem claimItem) {
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
     * THEN: Add valuation dialog is displayed
     * WHEN: U1 selects fifth valuation type
     * WHEN: U1 adds valuation amount V2 < V1 and selects closeSidWithOk option
     * THEN: V2 is displayed in SID
     * THEN: CC is equal to V2
     */
    @Test(dataProvider = "testDataProvider", description = "ECC-3144 Verify it's possible to add new valuation price in " +
            "add valuation dialog (user selects 5th type)")
    public void ecc3144_20_addNewValuationPriceInAddValuationDialog(User user, Claim claim, ClaimItem claimItem) {
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
    @Test(dataProvider = "testDataProvider", description = "ECC-3144 Verify it's possible to enable age option")
    public void ecc3144_22_enableAgeOption(User user, Claim claim, ClaimItem claimItem) {
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
    @Test(dataProvider = "testDataProvider", description = "ECC-3144 Verify it's possible to add years & month and save set")
    public void ecc3144_23_addYearsAndMonthAndSave(User user, Claim claim, ClaimItem claimItem) {
        loginAndCreateClaim(user, claim)
                .openSid()
                .setBaseData(claimItem)
                .enableAge("10")
                .selectMonth("6")
                .setValuation(SettlementDialog.Valuation.NEW_PRICE)
                .closeSidWithOk()
                .findClaimLine(claimItem.getTextFieldSP())
                .editLine()
                .doAssert(sid -> {
                    sid.assertYearsValueIs("10");
                    sid.assertMonthValueIs("6");
                });
    }


    /**
     * WHEN: U1 opens SID
     * WHEN: U1 disables Age option and saves
     * AND: U1 opens SID again
     * THEN: Age option is disabled
     */
    @Jira("https://jira.scalepoint.com/browse/CHARLIE-1382")
    @Test(enabled = false, dataProvider = "testDataProvider", description = "ECC-3144 Verify it's possible to disable age checkbox")
    public void ecc3144_24_disableAgeAndSave(User user, Claim claim, ClaimItem claimItem) {
        loginAndCreateClaim(user, claim)
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

}
