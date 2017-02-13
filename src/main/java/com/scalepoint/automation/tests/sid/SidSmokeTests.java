package com.scalepoint.automation.tests.sid;

import com.scalepoint.automation.BaseTest;
import com.scalepoint.automation.pageobjects.dialogs.SettlementDialog;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.pageobjects.pages.admin.PseudoCategoryGroupPage;
import com.scalepoint.automation.services.externalapi.VoucherAgreementApi;
import com.scalepoint.automation.services.externalapi.VoucherAgreementApi.AssignedCategory;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.tests.sid.SidCalculator.PriceValuation;
import com.scalepoint.automation.utils.OperationalUtils;
import com.scalepoint.automation.utils.annotations.Bug;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.ClaimItem;
import com.scalepoint.automation.utils.data.entity.Voucher;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.driver.Browser;
import org.testng.annotations.Test;

import java.util.List;

import static com.scalepoint.automation.pageobjects.dialogs.SettlementDialog.Valuation.CUSTOMER_DEMAND;
import static com.scalepoint.automation.pageobjects.dialogs.SettlementDialog.Valuation.NEW_PRICE;
import static com.scalepoint.automation.services.externalapi.ftemplates.FTSetting.ALLOW_USERS_TO_MARK_SETTLEMENT_REVIEWED;
import static com.scalepoint.automation.services.externalapi.ftemplates.FTSetting.REVIEW_ALL_CLAIM_TO_COMPLETE_CLAIM;
import static com.scalepoint.automation.utils.OperationalUtils.assertEqualsDouble;

@RequiredSetting(type = FTSetting.ENABLE_NEW_SETTLEMENT_ITEM_DIALOG)
@RequiredSetting(type = FTSetting.SHOW_NOT_CHEAPEST_CHOICE_POPUP, enabled = false)
public class SidSmokeTests extends BaseTest {

    @Test(dataProvider = "testDataProvider", description = "ECC-3025 It's possible to assign existing category for new voucher and select categories in Add/Edit dialog")
    public void ecc3025_selectVoucherExistingCatAddDialog(User user, Claim claim, Voucher voucher) {
        AssignedCategory categoryInfo = new VoucherAgreementApi(user).createVoucher(voucher);
        loginAndCreateClaim(user, claim)
                .openAddManuallyDialog()
                .fillCategory(categoryInfo)
                .assertVoucherListed(voucher.getVoucherNameSP());
    }

    /**
     * GIVEN: Existing category C1 with existing group G1 and mapped to G1-C1 voucher V1
     * WHEN: User selects C1, G1 and V1 in Settlement dialog
     * WHEN: User adds new price P1
     * WHEN: User adds depreciation D1
     * THAN: Cash compensation is P1 - V1 discount - D1
     * THAN: Depreciation is D1 amount of Cash Compensation
     */
    @Test(dataProvider = "testDataProvider", description = "ECC-3025 Cash compensation with depreciation field value is (New price minus voucher percent)" +
            " - depreciation percent if voucher selected in Add settlement dialog")
    @RequiredSetting(type = FTSetting.COMPARISON_OF_DISCOUNT_DEPRECATION, enabled = false)
    public void ecc3025_cashCompensationWithAddedDepVoucher(User user, Claim claim, ClaimItem item, Voucher voucher) {

        AssignedCategory categoryInfo = new VoucherAgreementApi(user).createVoucher(voucher);

        SettlementDialog settlementDialog = loginAndCreateClaim(user, claim)
                .openAddManuallyDialog()
                .fillDescription(item.getTextFieldSP())
                .fillCustomerDemand(item.getBigCustomDemandPrice())
                .fillNewPrice(item.getNewPriceSP_2400())
                .fillDepreciation(item.getDepAmount1_10())
                .fillCategory(categoryInfo)
                .fillVoucher(voucher.getVoucherNameSP());

        SidCalculator.VoucherValuation expectedCalculations =
                SidCalculator.calculateVoucherValuation(item.getNewPriceSP_2400(), voucher.getDiscount(), item.getDepAmount1_10());

        Double fetchedCashValue = settlementDialog.getCashCompensationValue();
        Double calculatedCashValue = expectedCalculations.getCashCompensationWithDepreciation();
        assertEqualsDouble(fetchedCashValue, calculatedCashValue, "Cash compensation incorrect");

        Double fetchedDepreciationValue = settlementDialog.DeprecationValue();
        Double calculatedDepreciationValue = expectedCalculations.getDepreciatedAmount();
        assertEqualsDouble(fetchedDepreciationValue, calculatedDepreciationValue, "Depreciation incorrect");
    }

    /**
     * WHEN: Include in claim option is ON
     * THEN:Amount of claim line is summed up to the total amount of claim
     */
    @Test(dataProvider = "testDataProvider", description = "ECC-3144 Verify Include in claim option is ON")
    public void ecc3144_1_setIncludeInClaimCheckbox(User user, Claim claim, ClaimItem claimItem) {
        loginAndCreateClaim(user, claim)
                .openAddManuallyDialog()
                .fillNewPrice(claimItem.getNewPriceSP_2400())
                .includeInClaim(true)
                .assertIncludeInClaimSelected()
                .includeInClaim(false)
                .assertIncludeInClaimNotSelected();
    }

    /**
     * WHEN: Select category
     * THEN: Categories are selected according to Pseudocategories mapping on Admin page
     */
    @Test(dataProvider = "testDataProvider", description = "ECC-3144 Verify it is possible to select categories")
    public void ecc3144_2_selectCategory(User user, Claim claim, ClaimItem claimItem) {
        loginAndCreateClaim(user, claim);

        String currentUrl = Browser.driver().getCurrentUrl();

        List<String> allSubCategories = Page.to(PseudoCategoryGroupPage.class)
                .editGroup(claimItem.getExistingCat1_Born())
                .getAllPseudoCategories();

        Browser.driver().get(currentUrl);

        Page.at(SettlementPage.class)
                .openAddManuallyDialog()
                .fillCategory(claimItem.getExistingCat1_Born())
                .assertSubCategoriesListEqualTo(allSubCategories);
    }

    /**
     * WHEN: input Custom demand
     * THEN: A new valuation is added to the table
     */
    @Test(dataProvider = "testDataProvider", description = "ECC-3144 Verify it is possible to input Customer demand")
    public void ecc3144_3_inputCustomDemand(User user, Claim claim, ClaimItem claimItem) {
        Double customerDemand = claimItem.getCustomerDemand_500();
        loginAndCreateClaim(user, claim)
                .openAddManuallyDialog()
                .fillCustomerDemand(customerDemand)
                .assertAmountOfValuationEqualTo(customerDemand, CUSTOMER_DEMAND);
    }


    /**
     * WHEN: input New Price
     * THEN: A new valuation is added to the table
     */
    @Test(dataProvider = "testDataProvider", description = "ECC-3144 Verify it is possible to input New price")
    public void ecc3144_4_inputNewPrice(User user, Claim claim, ClaimItem claimItem) {
        Double newPrice = claimItem.getNewPriceSP_2400();
        loginAndCreateClaim(user, claim)
                .openAddManuallyDialog()
                .fillNewPrice(newPrice)
                .assertAmountOfValuationEqualTo(newPrice, SettlementDialog.Valuation.NEW_PRICE);
    }

    /**
     * WHEN: input depreciation into the field Depreciation
     * THEN: Depreciation percent is deducted from the selected valuation in the table
     * THEN: Amount of depreciation is shown at the bottom of SID
     * THEN: Depreciation percent is shown on the settlement page after saving SID
     */
    @Test(dataProvider = "testDataProvider", description = "ECC-3144 Verify a manual depreciation can be entered into the field Depreciation")
    @RequiredSetting(type = FTSetting.ENABLE_DEPRECIATION_COLUMN)
    @RequiredSetting(type = FTSetting.SHOW_DEPRECIATION_AUTOMATICALLY_UPDATED)
    @RequiredSetting(type = FTSetting.SHOW_SUGGESTED_DEPRECIATION_SECTION)
    public void ecc3144_5_manualDepreciation(User user, Claim claim, ClaimItem claimItem, Voucher voucher) {
        AssignedCategory categoryInfo = new VoucherAgreementApi(user).createVoucher(voucher);

        SettlementDialog dialog = loginAndCreateClaim(user, claim)
                .openAddManuallyDialog()
                .fillDescription(claimItem.getTextFieldSP())
                .fillCustomerDemand(claimItem.getCustomerDemand_500())
                .fillCategory(categoryInfo)
                .fillVoucher(voucher.getVoucherNameSP())
                .fillDepreciation(claimItem.getDepAmount1_10())
                .selectValuation(CUSTOMER_DEMAND);

        PriceValuation expectedCalculations = SidCalculator.calculatePriceValuation(claimItem.getCustomerDemand_500(), claimItem.getDepAmount1_10());

        dialog.assertCashValueIs(expectedCalculations.getCashValue())
                .assertDepreciationAmountIs(expectedCalculations.getDepreciation())
                .assertTotalAmountOfValuationIs(expectedCalculations.getCashValue(), CUSTOMER_DEMAND)
                .assertDepreciationPercentageEqualTo(10, CUSTOMER_DEMAND);
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
                .openAddManuallyDialog()
                .fillBaseData(claimItem)
                .closeSidWithOk()
                .findClaimLine(claimItem.getTextFieldSP()).editLine()
                .assertDescriptionIs(claimItem.getTextFieldSP())
                .assertCategoryTextIs(claimItem.getExistingCat1_Born())
                .assertSubCategoryTextIs(claimItem.getExistingSubCat1_Babyudstyr())
                .assertAmountOfValuationEqualTo(claimItem.getCustomerDemand_500(), CUSTOMER_DEMAND)
                .assertTotalAmountOfValuationIs(claimItem.getNewPriceSP_2400(), NEW_PRICE);
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
                .openAddManuallyDialog()
                .fillBaseData(claimItem)
                .closeSidWithOk()
                .findClaimLine(claimItem.getTextFieldSP())
                .editLine()
                .assertDescriptionIs(claimItem.getTextFieldSP())
                .assertCategoryTextIs(claimItem.getExistingCat1_Born())
                .assertSubCategoryTextIs(claimItem.getExistingSubCat1_Babyudstyr())
                .assertAmountOfValuationEqualTo(claimItem.getCustomerDemand_500(), CUSTOMER_DEMAND)
                .assertTotalAmountOfValuationIs(claimItem.getNewPriceSP_2400(), SettlementDialog.Valuation.NEW_PRICE)
                .fillBaseData(claimItem)
                .cancel()
                .findClaimLine(claimItem.getTextFieldSP())
                .editLine()
                .assertDescriptionIs(claimItem.getTextFieldSP())
                .assertCategoryTextIs(claimItem.getExistingCat1_Born())
                .assertSubCategoryTextIs(claimItem.getExistingSubCat1_Babyudstyr())
                .assertTotalAmountOfValuationIs(claimItem.getCustomerDemand_500(), CUSTOMER_DEMAND)
                .assertTotalAmountOfValuationIs(claimItem.getNewPriceSP_2400(), SettlementDialog.Valuation.NEW_PRICE);
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
                .openAddManuallyDialog()
                .fillBaseData(claimItem)
                .setReviewed(true)
                .includeInClaim(false)
                .selectValuation(SettlementDialog.Valuation.NEW_PRICE)
                .waitASecond()
                .assertTotalAmountOfValuationIs(claimItem.getNewPriceSP_2400(), NEW_PRICE);
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
                .openAddManuallyDialog()
                .fillBaseData(claimItem)
                .setReviewed(true)
                .includeInClaim(false)
                .closeSidWithOk()
                .findClaimLine(claimItem.getTextFieldSP())
                .assertLineHasColor(claimItem.getBlueColor())
                .selectLine()
                .getBottomMenu()
                .assertClaimSumValueIs(0.00);
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
        SettlementDialog dialog = settlementPage.openAddManuallyDialog()
                .fillBaseData(claimItem)
                .setReviewed(false)
                .includeInClaim(false)
                .closeSidWithOk()
                .findClaimLine(claimItem.getTextFieldSP())
                .assertLineHasComputedColor(claimItem.getPinkColor())
                .selectLine()
                .openAddManuallyDialog()
                .fillBaseData(claimItem)
                .fillDescription(secondClaimDescription)
                .setReviewed(true)
                .includeInClaim(true);

        Double claimValue = dialog.customerDemandValue();
        dialog.closeSidWithOk()
                .getBottomMenu()
                .assertClaimSumValueIs(claimValue)
                .assertSubtotalSumValueIs(claimValue);
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
                .openAddManuallyDialog()
                .fillBaseData(claimItem)
                .setReviewed(false)
                .includeInClaim(false)
                .closeSidWithOk()
                .findClaimLine(claimItem.getTextFieldSP())
                .assertLineHasComputedColor(claimItem.getPinkColor());
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
                .openAddManuallyDialog()
                .fillBaseData(claimItem)
                .setReviewed(false)
                .closeSidWithOk()
                .getBottomMenu()
                .assertCompleteClaimEnabled();
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
                .openAddManuallyDialog()
                .assertReviewedNotPresent();
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
                .openAddManuallyDialog()
                .fillBaseData(claimItem)
                .setReviewed(false)
                .closeSidWithOk()
                .getBottomMenu()
                .assertCompleteClaimEnabled();
    }

    /**
     * WHEN: U1 opens SID, fills all field with valid values(description D1 etc)
     * WHEN: select cancel
     * THEN: Cancelled claim line is not added to the claim
     */
    @Test(dataProvider = "testDataProvider", description = "ECC-3144 Verify cancelled claim line is not added to the claim")
    public void ecc3144_15_cancelledClaimNotAdded(User user, Claim claim, ClaimItem claimItem) {
        loginAndCreateClaim(user, claim)
                .openAddManuallyDialog()
                .fillBaseData(claimItem)
                .cancel()
                .assertItemNotPresent(claimItem.getTextFieldSP());
    }


    /**
     * WHEN: U1 opens SID
     * WHEN: U1 adds New price amount V1
     * THEN: Cash compensation CC is assertEqualsDouble to V1
     */
    @Test(dataProvider = "testDataProvider", description = "ECC-3144 Verify Cash compensation CC is equal to V1")
    public void ecc3144_16_cashCompensationEqualV1(User user, Claim claim, ClaimItem claimItem) {
        loginAndCreateClaim(user, claim)
                .openAddManuallyDialog()
                .fillBaseData(claimItem)
                .assertCashValueIs(claimItem.getCustomerDemand_500());
    }

    /**
     * WHEN: U1 opens SID
     * WHEN: U1 adds New price amount V1
     * THEN: Cash compensation CC is assertEqualsDouble to V1
     * WHEN: U1 selects Add valuation option
     * THEN: Add valuation dialog is displayed
     */
    @Test(dataProvider = "testDataProvider", description = "ECC-3144 Verify it's possible to open Add Valuation dialog in SID")
    public void ecc3144_17_openAddValuationDialogInSID(User user, Claim claim, ClaimItem claimItem) {
        loginAndCreateClaim(user, claim)
                .openAddManuallyDialog()
                .fillBaseData(claimItem)
                .addValuation();
    }

    /**
     * WHEN: U1 opens SID
     * WHEN: U1 adds New price amount V1
     * THEN: Cash compensation CC is assertEqualsDouble to V1
     * WHEN: U1 selects Add valuation option
     * THEN: Add valuation dialog is displayed
     * WHEN: U1 selects third valuation type
     * WHEN: U1 adds valuation amount V2 < V1 and selects closeSidWithOk option
     * THEN: V2 is displayed in SID
     * THEN: CC is assertEqualsDouble to V2
     */
    @Test(dataProvider = "testDataProvider", description = "ECC-3144 Verify it's possible to add new valuation price in add " +
            "valuation dialog (user selects 3d type)")
    public void ecc3144_18_addNewValuationPriceInAddValuationDialog(User user, Claim claim, ClaimItem claimItem) {
        loginAndCreateClaim(user, claim)
                .openAddManuallyDialog()
                .fillBaseData(claimItem)
                .assertCashValueIs(claimItem.getCustomerDemand_500())
                .addValuation()
                .addValuationType(claimItem.getValuationType3())
                .addValuationPrice(claimItem.getLowerPrice())
                .closeValuationDialogWithOk()
                .assertCashValueIs(claimItem.getLowerPrice());
    }

    /**
     * WHEN: U1 opens SID
     * WHEN: U1 adds New price amount V1
     * THEN: Cash compensation CC is assertEqualsDouble to V1
     * WHEN: U1 selects Add valuation option
     * THEN: Add valuation dialog is displayed
     * WHEN: U1 selects fourth valuation type
     * WHEN: U1 adds valuation amount V2 < V1 and selects closeSidWithOk option
     * THEN: V2 is displayed in SID
     * THEN: CC is assertEqualsDouble to V2
     */
    @Test(dataProvider = "testDataProvider", description = "ECC-3144 Verify it's possible to add new valuation price in add valuation dialog (user selects 4th type)")
    public void ecc3144_19_addNewValuationPriceInAddValuationDialog(User user, Claim claim, ClaimItem claimItem) {
        loginAndCreateClaim(user, claim)
                .openAddManuallyDialog()
                .fillBaseData(claimItem)
                .assertCashValueIs(claimItem.getCustomerDemand_500())
                .addValuation()
                .addValuationType(claimItem.getValuationType4())
                .addValuationPrice(claimItem.getLowerPrice())
                .closeValuationDialogWithOk()
                .assertCashValueIs(claimItem.getLowerPrice());
    }


    /**
     * WHEN: U1 opens SID
     * WHEN: U1 adds New price amount V1
     * THEN: Cash compensation CC is assertEqualsDouble to V1
     * WHEN: U1 selects Add valuation option
     * THEN: Add valuation dialog is displayed
     * WHEN: U1 selects fifth valuation type
     * WHEN: U1 adds valuation amount V2 < V1 and selects closeSidWithOk option
     * THEN: V2 is displayed in SID
     * THEN: CC is assertEqualsDouble to V2
     */
    @Test(dataProvider = "testDataProvider", description = "ECC-3144 Verify it's possible to add new valuation price in " +
            "add valuation dialog (user selects 5th type)")
    public void ecc3144_20_addNewValuationPriceInAddValuationDialog(User user, Claim claim, ClaimItem claimItem) {
        loginAndCreateClaim(user, claim)
                .openAddManuallyDialog()
                .fillBaseData(claimItem)
                .assertCashValueIs(claimItem.getCustomerDemand_500())
                .addValuation()
                .addValuationType(claimItem.getValuationType5())
                .addValuationPrice(claimItem.getLowerPrice())
                .closeValuationDialogWithOk()
                .assertCashValueIs(claimItem.getLowerPrice());
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
                .openAddManuallyDialog()
                .fillBaseData(claimItem)
                .enableAge()
                .assertAgeYearsEnabled()
                .assertMonthMenuEnabled();
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
                .openAddManuallyDialog()
                .fillBaseData(claimItem)
                .enableAge("10")
                .selectMonth("6")
                .selectValuation(SettlementDialog.Valuation.NEW_PRICE)
                .closeSidWithOk()
                .findClaimLine(claimItem.getTextFieldSP())
                .editLine()
                .assertYearsValueIs("10")
                .assertMonthValueIs("6");
    }


    /**
     * WHEN: U1 opens SID
     * WHEN: U1 disables Age option and saves
     * AND: U1 opens SID again
     * THEN: Age option is disabled
     */
    @Test(dataProvider = "testDataProvider", description = "ECC-3144 Verify it's possible to disable age checkbox")
    public void ecc3144_24_disableAgeAndSave(User user, Claim claim, ClaimItem claimItem) {
        loginAndCreateClaim(user, claim)
                .openAddManuallyDialog()
                .fillBaseData(claimItem)
                .enableAge()
                .assertAgeYearsEnabled()
                .assertMonthMenuEnabled()
                .disableAge()
                .closeSidWithOk()
                .findClaimLine(claimItem.getTextFieldSP())
                .editLine()
                .assertAgeYearsDisabled()
                .assertMonthMenuDisabled();
    }

}
