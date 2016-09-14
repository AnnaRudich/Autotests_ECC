package com.scalepoint.automation.tests.sid;

import com.scalepoint.automation.BaseTest;
import com.scalepoint.automation.pageobjects.dialogs.SettlementDialog;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.PseudoCategoryGroupPage;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.services.externalapi.VoucherAgreementApi;
import com.scalepoint.automation.services.externalapi.ftemplates.FT;
import com.scalepoint.automation.utils.OperationalUtils;
import com.scalepoint.automation.utils.annotations.Bug;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.ClaimItem;
import com.scalepoint.automation.utils.data.entity.Voucher;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.testng.annotations.Test;

import java.util.List;

import static com.scalepoint.automation.services.externalapi.ftemplates.FTSetting.*;
import static org.testng.Assert.*;

public class SettlementDialogSmokeTests extends BaseTest {
    /**
     * WHEN: Include in claim option is ON
     * THEN:Amount of claim line is summed up to the total amount of claim
     */
    @Test(description = "ECC-3144 Verify Include in claim option is ON", dataProvider = "testDataProvider")
    public void ecc3144_1_setIncludeInClaimCheckbox(User user, Claim claim, ClaimItem claimItem) {
        enableNewSid(user);

        SettlementDialog settlementDialog = loginAndCreateClaim(user, claim).
                addManually().
                fillNewPrice(claimItem.getDepAmount1()).
                includeInClaim(true);

        assertTrue(settlementDialog.isIncludeInClaimSet(), "The 'Include in Claim is not set'");
        settlementDialog.includeInClaim(false);
        assertFalse(settlementDialog.isIncludeInClaimSet(), "The 'Include in Claim is set'");
    }

    /**
     * WHEN: Select category
     * THEN: Categories are selected according to Pseudocategories mapping on Admin page
     */

    @Test(description = "ECC-3144 Verify it is possible to select categories", dataProvider = "testDataProvider")
    public void ecc3144_2_selectCategory(User user, Claim claim, ClaimItem claimItem) {
        enableNewSid(user);

        loginAndCreateClaim(user, claim);

        List<String> allPseudoCategories = Page.to(PseudoCategoryGroupPage.class).
                editPseudoCategory(claimItem.getExistingCat1()).
                getAllPseudoCategories();

        List<String> subCategories = Page.to(SettlementPage.class).addManually()
                .fillCategory(claimItem.getExistingCat1())
                .getCategoriesList();

        assertEqualsNoOrder(allPseudoCategories.toArray(), subCategories.toArray(), "Category is not selected");
    }

    /**
     * WHEN: input Custom demand
     * THEN: A new valuation is added to the table
     */
    @Test(description = "ECC-3144 Verify it is possible to input Customer demand", dataProvider = "testDataProvider")
    public void ecc3144_3_inputCustomDemand(User user, Claim claim, ClaimItem claimItem) {
        enableNewSid(user);

        Integer customerDemand = claimItem.getCustomerDemand();
        SettlementDialog settlementDialog = loginAndCreateClaim(user, claim).
                addManually().
                fillCustomerDemand(customerDemand);
        assertTrue((settlementDialog.isValuationPresent(customerDemand)), "The Customer Demand has not been added");
    }


    /**
     * WHEN: input New Price
     * THEN: A new valuation is added to the table
     */
    @Test(description = "ECC-3144 Verify it is possible to input New price", dataProvider = "testDataProvider")
    public void ecc3144_4_inputNewPrice(User user, Claim claim, ClaimItem claimItem) {
        enableNewSid(user);

        Integer newPrice = claimItem.getNewPriceSP();
        SettlementDialog settlementDialog = loginAndCreateClaim(user, claim).addManually().fillNewPrice(newPrice);
        assertTrue((settlementDialog.isValuationPresent(newPrice)), "The New Price has not been added");
    }

    /**
     * WHEN: input depreciation into the field Depreciation
     * THEN: Depreciation percent is deducted from the selected valuation in the table
     * THEN: Amount of depreciation is shown at the bottom of SID
     * THEN: Depreciation percent is shown on the settlement page after saving SID
     */
    @Test(description = "ECC-3144 Verify a manual depreciation can be entered into the field Depreciation", dataProvider = "testDataProvider")
    public void ecc3144_5_manualDepreciation(User user, Claim claim, ClaimItem claimItem, Voucher voucher) {
        enableNewSid(user);

        VoucherAgreementApi.AssignedCategory categoryInfo = new VoucherAgreementApi(user).createVoucher(voucher);
        updateFT(user, SettlementPage.class,
                FT.enable(ENABLE_DEPRECIATION_COLUMN),
                FT.enable(SHOW_DEPRECIATION_AUTOMATICALLY_UPDATED),
                FT.enable(SHOW_SUGGESTED_DEPRECIATION_SECTION)
        );

        SettlementDialog settlementDialog = loginAndCreateClaim(user, claim).addManually()
                .fillDescription(claimItem.getTextFieldSP())
                .fillCategory(categoryInfo)
                .fillVoucher(voucher.getVoucherNameSP())
                .fillDepreciation(claimItem.getDepAmount1())
                .fillCustomerDemand(claimItem.getCustomerDemand())
                .selectValuation(SettlementDialog.Valuation.CUSTOMER_DEMAND);

        String fetchedCashValue = toString(settlementDialog.cashCompensationValue());
        String calculatedCashValue = toString(Double.valueOf(calculateCashCompensation(claimItem, voucher)));
        String fetchedDepreciation = toString(settlementDialog.fetchDepreciation());

        assertEquals(fetchedCashValue, calculatedCashValue, "Cash compensation incorrect");
        assertEquals(fetchedDepreciation, calculateDepreciation(claimItem, voucher), "Depreciation incorrect");
        assertTrue(settlementDialog.isNewValuationPresent(calculateCashCompensation(claimItem)), "Depreciation is incorrect");
        assertTrue(settlementDialog.isDepreciationPercentPresent("Afskrivning", "10"), "Depreciation Percent is not displayed");
    }

    /**
     * WHEN: fill in all the fields
     * WHEN: Click Save button
     * WHEN: Open again SID
     * THEN: all the results are present
     */
    @Test(description = "ECC-3144 Verify it is possible to Save all results entered", dataProvider = "testDataProvider")
    public void ecc3144_6_SaveAllEnteredResults(User user, Claim claim, ClaimItem claimItem) {
        enableNewSid(user);

        SettlementPage settlementPage = loginAndCreateClaim(user, claim);
        settlementPage.addManually().
                fillDescription(claimItem.getTextFieldSP()).
                fillCustomerDemand(claimItem.getCustomerDemand()).
                fillNewPrice(claimItem.getNewPriceSP()).
                fillCategory(claimItem.getExistingCat1()).
                fillSubCategory(claimItem.getExistingSubCat1()).
                ok();

        SettlementDialog settlementDialog = settlementPage.openEditSettlementDialogByClaimDescr(claimItem.getTextFieldSP());
        assertEquals(settlementDialog.getDescriptionText(), claimItem.getTextFieldSP(), "The Description is not saved");
        assertEquals(settlementDialog.getCategoryText(), claimItem.getExistingCat1(), "The Category is not Saved");
        assertEquals(settlementDialog.getSubCategoryText(), claimItem.getExistingSubCat1(), "The SubCategory is not Saved");
        assertTrue(settlementDialog.isValuationPresent(claimItem.getCustomerDemand()), "The Customer Demand price is not Saved");
        assertTrue(settlementDialog.isNewValuationPresent(claimItem.getNewPriceSP()), "The New Price is not Saved");
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
    @Test(description = "ECC-3144 Verify clicking Cancel doesn't save entered info", dataProvider = "testDataProvider")
    public void ecc3144_7_CancelEnteredResults(User user, Claim claim, ClaimItem claimItem) {
        enableNewSid(user);

        String newClaimDescription = claimItem.getTextFieldSP().concat("new");

        SettlementPage settlementPage = loginAndCreateClaim(user, claim);
        settlementPage.
                addManually().
                fillDescription(claimItem.getTextFieldSP()).
                fillCustomerDemand(claimItem.getCustomerDemand()).
                fillNewPrice(claimItem.getNewPriceSP()).
                fillCategory(claimItem.getExistingCat1()).
                fillSubCategory(claimItem.getExistingSubCat1()).
                ok();

        SettlementDialog settlementDialog = settlementPage.openEditSettlementDialogByClaimDescr(claimItem.getTextFieldSP());

        assertEquals(settlementDialog.getDescriptionText(), claimItem.getTextFieldSP(), "The Description is not Saved");
        assertEquals(settlementDialog.getCategoryText(), claimItem.getExistingCat1(), "The Category is not Saved");
        assertEquals(settlementDialog.getSubCategoryText(), claimItem.getExistingSubCat1(), "The SubCategory is not Saved");
        assertTrue(settlementDialog.isValuationPresent(claimItem.getCustomerDemand()), "The Customer Demand price is not Saved");
        assertTrue(settlementDialog.isNewValuationPresent(claimItem.getNewPriceSP()), "The New Price is not Saved");

        settlementDialog.
                fillDescription(newClaimDescription).
                fillCustomerDemand(claimItem.getDepAmount1()).
                fillNewPrice(Integer.valueOf(claimItem.getDepAmount2())).
                fillCategory(claimItem.getExistingCat2()).
                fillSubCategory(claimItem.getExistingSubCat2()).
                cancel();

        settlementPage.openEditSettlementDialogByClaimDescr(claimItem.getTextFieldSP());

        assertEquals(settlementDialog.getDescriptionText(), claimItem.getTextFieldSP());
        assertEquals(settlementDialog.getCategoryText(), claimItem.getExistingCat1(), "The New Category is Saved");
        assertEquals(settlementDialog.getSubCategoryText(), claimItem.getExistingSubCat1(), "The New SubCategory is Saved");
        assertTrue(settlementDialog.isValuationPresent(claimItem.getCustomerDemand()), "The New Customer Demand price is Saved");
        assertTrue(settlementDialog.isNewValuationPresent(claimItem.getNewPriceSP()), "The New Price is Saved");
        settlementDialog.ok();
    }


    /**
     * WHEN: Click Add new valuation
     * WHEN: Select new valuation
     * WHEN: Input price
     * WHEN: U1 fills settlement dialog with valid values
     * THEN: New valuation appears in SID
     */
    @Test(description = "ECC-3144 Verify it is possible to add new valuation", dataProvider = "testDataProvider")
    public void ecc3144_8_addNewValuation(User user, Claim claim, ClaimItem claimItem) {
        enableNewSid(user);

        updateFT(user, FT.enable(ALLOW_MARK_SETTLEMENT_REVIEWED));

        SettlementDialog settlementDialog = loginAndCreateClaim(user, claim).
                addManually().
                fillDescription(claimItem.getTextFieldSP()).
                fillCategory(claimItem.getExistingCat1()).
                fillSubCategory(claimItem.getExistingSubCat1()).
                fillCustomerDemand(claimItem.getCustomerDemand()).
                fillNewPrice(claimItem.getNewPriceSP()).
                setReviewed(true).
                includeInClaim(false).
                selectValuation(SettlementDialog.Valuation.NEW_PRICE);
        assertTrue(settlementDialog.isNewValuationPresent(claimItem.getNewPriceSP()), "New valuation has been not added");
    }

    /**
     * WHEN: Input price
     * WHEN: U1 fills settlement dialog with valid values
     * WHEN: "Reviewed" option is enabled (checked)
     * WHEN: "Include in claim" option is disabled
     * THEN: description D1 of claim line CL1 is colored in blue
     * THEN: Claim sum value CSV = 0.00
     */


    @Test(description = "ECC-3144 Verify Claim line description is displayed in blue if the options \"Include in claim\" disabled" +
            "- Claim line value is not added to Total claims sum", dataProvider = "testDataProvider")
    public void ecc3144_9_disableIncludeInClaim(User user, Claim claim, ClaimItem claimItem) {
        enableNewSid(user);

        SettlementPage settlementPage = loginAndCreateClaim(user, claim);
        settlementPage.
                addManually().
                fillDescription(claimItem.getTextFieldSP()).
                fillCustomerDemand(claimItem.getCustomerDemand()).
                fillNewPrice(claimItem.getNewPriceSP()).
                fillCategory(claimItem.getExistingCat1()).
                fillSubCategory(claimItem.getExistingSubCat1()).
                setReviewed(true).
                includeInClaim(false).
                ok();
        assertTrue(settlementPage.getClaimColorByDescription(claimItem.getTextFieldSP(), claimItem.getBlueColor()), "Claim is not in blue color");
        assertEquals(OperationalUtils.toNumber(settlementPage.getBottomMenu().getClaimSumValue()), 0.00, "Claim sum is not equal zero");
    }

    /**
     * WHEN: Input price
     * WHEN: U1 fills settlement dialog with valid values
     * WHEN: "Reviewed" option is disabled (unchecked)
     * WHEN: "Include in claim" option is disabled
     * THEN: description D1 of claim line CL1 is colored in pink
     */
    @Test(description = "ECC-3144 Verify Claim line description is displayed in pink if the options 'Include in claim'  " +
            "and 'Reviewed' disabled", dataProvider = "testDataProvider")
    public void ecc3144_10_disableIncludeInClaimAndReviewed(User user, Claim claim, ClaimItem claimItem) {
        enableNewSid(user);

        SettlementPage settlementPage = loginAndCreateClaim(user, claim);
        settlementPage.
                addManually().
                fillDescription(claimItem.getTextFieldSP()).
                fillCustomerDemand(claimItem.getCustomerDemand()).
                fillNewPrice(claimItem.getNewPriceSP()).
                fillCategory(claimItem.getExistingCat1()).
                fillSubCategory(claimItem.getExistingSubCat1()).
                setReviewed(false).
                includeInClaim(false).
                ok();
        assertTrue(settlementPage.getComputedClaimColorByDescription(claimItem.getTextFieldSP(), claimItem.getPinkColor()), "Claim is not in pink color");
    }

    /**
     * WHEN: U1 adds claim line 1 with and review and  "Include in claim" option disabled
     * WHEN: U1 adds claim line 2 CL2 with review and include to claim options are enabled
     * WHEN: CL2 value = V1
     * THEN: Claim line value is not added to Total claims sum if "Include in claim" option is disabled in SID:
     * THEN: CSV = V1
     * THEN: Subtotal claim value SCV = V1
     */
    @Test(description = "ECC-3144 Verify that second claim line value is not added to Total claims sum if the options " +
            "'Include in claim' and 'Reviewed' enabled", dataProvider = "testDataProvider")
    public void ecc3144_11_enableIncludeInClaimSecondClaim(User user, Claim claim, ClaimItem claimItem) {
        enableNewSid(user);

        String secondClaimDescription = claimItem.getTextFieldSP().concat("second");
        SettlementPage settlementPage = loginAndCreateClaim(user, claim);
        settlementPage.addManually().
                fillDescription(claimItem.getTextFieldSP()).
                fillCustomerDemand(claimItem.getCustomerDemand()).
                fillNewPrice(claimItem.getNewPriceSP()).
                fillCategory(claimItem.getExistingCat1()).
                fillSubCategory(claimItem.getExistingSubCat1()).
                setReviewed(false).
                includeInClaim(false).
                ok();
        assertTrue(settlementPage.getComputedClaimColorByDescription(claimItem.getTextFieldSP(), claimItem.getPinkColor()));

        SettlementDialog settlementDialog = settlementPage.addManually().
                fillDescription(secondClaimDescription).
                fillCustomerDemand(claimItem.getCustomerDemand()).
                fillNewPrice(claimItem.getNewPriceSP()).
                fillCategory(claimItem.getExistingCat1()).
                fillSubCategory(claimItem.getExistingSubCat1()).
                setReviewed(true).
                includeInClaim(true);

        Double claimValue = settlementDialog.CustomerDemandValue();

        settlementDialog.ok();
        assertEquals(OperationalUtils.toNumber(settlementPage.getBottomMenu().getClaimSumValue()), claimValue, "Claim sum value is not incremented");
        assertEquals(OperationalUtils.toNumber(settlementPage.getBottomMenu().getSubtotalSumValue()), claimValue, "Subtotal Sum Value is not correct");
    }

    /**
     * WHEN: Allow users to mark settlement items as reviewed" is enabled in FT
     * WHEN: Review of all claim lines are required to complete claim" is disabled in FT
     * WHEN: U1 adds claim line CL1 where "Reviewed" option is disabled
     * THEN: "Complete claim" button is enabled
     */
    @Test(description = "ECC-3144 Verify 'Complete claim' is enable if 'Reviewed' is disabled in SID", dataProvider = "testDataProvider")
    public void ecc3144_12_completeClaimIsEnabled(User user, Claim claim, ClaimItem claimItem) {
        enableNewSid(user);

        updateFT(user, FT.enable(ALLOW_MARK_SETTLEMENT_REVIEWED));

        SettlementPage settlementPage = loginAndCreateClaim(user, claim);
        settlementPage.addManually().
                fillDescription(claimItem.getTextFieldSP()).
                fillCustomerDemand(claimItem.getCustomerDemand()).
                fillNewPrice(claimItem.getNewPriceSP()).
                fillCategory(claimItem.getExistingCat1()).
                fillSubCategory(claimItem.getExistingSubCat1()).
                setReviewed(false).
                ok();


        assertTrue(settlementPage.getBottomMenu().isCompleteClaimEnabled(), "Complete Claim button is disabled");
    }

    /**
     * WHEN: Allow users to mark settlement items as reviewed" is enabled in FT
     * WHEN: Review of all claim lines are required to complete claim" is enabled in FT
     * WHEN: U1 adds claim line CL1 where "Reviewed" option is disabled
     * THEN: "Complete claim" button is enabled
     */
    @Test(description = "ECC-3144 Verify 'Complete claim' is enabled if 'Reviewed' is disabled in SID", dataProvider = "testDataProvider")
    public void ecc3144_13_completeClaimIsEnabled(User user, Claim claim, ClaimItem claimItem) {
        enableNewSid(user);

        updateFT(user, FT.enable(ALLOW_MARK_SETTLEMENT_REVIEWED), FT.enable(REVIEW_ALL_CLAIM_TO_COMPLETE_CLAIM));
        SettlementPage settlementPage = loginAndCreateClaim(user, claim);
        settlementPage.addManually().
                fillDescription(claimItem.getTextFieldSP()).
                fillCustomerDemand(claimItem.getCustomerDemand()).
                fillNewPrice(claimItem.getNewPriceSP()).
                fillCategory(claimItem.getExistingCat1()).
                fillSubCategory(claimItem.getExistingSubCat1()).
                setReviewed(false).ok();

        assertTrue(settlementPage.getBottomMenu().isCompleteClaimEnabled(), "Complete Claim button is disabled");
    }

    /**
     * WHEN: Allow users to mark settlement items as reviewed" is disabled in FT
     * WHEN: Review of all claim lines are required to complete claim" is disabled in FT
     * THEN "Reviewed" option is not displayed in SID
     */
    @Bug(bug = "CHARLIE-391")
    @Test(description = "ECC-3144 Verify 'Reviewed' box is not displayed", dataProvider = "testDataProvider")
    public void ecc3144_14_ReviewedBoxNotDisplayed(User user, Claim claim) {
        enableNewSid(user);
        updateFT(user, FT.disable(ALLOW_MARK_SETTLEMENT_REVIEWED), FT.disable(REVIEW_ALL_CLAIM_TO_COMPLETE_CLAIM));

        SettlementDialog settlementDialog = loginAndCreateClaim(user, claim).addManually();
        assertFalse(settlementDialog.isReviewedPresent(), "Reviewed checkbox is enabled");
    }

    /**
     * WHEN: U1 opens SID, fills all field with valid values(description D1 etc)
     * WHEN: select cancel
     * THEN: Cancelled claim line is not added to the claim
     */
    @Test(description = "ECC-3144 Verify cancelled claim line is not added to the claim", dataProvider = "testDataProvider")
    public void ecc3144_15_cancelledClaimNotAdded(User user, Claim claim, ClaimItem claimItem) {
        enableNewSid(user);

        SettlementPage settlementPage = loginAndCreateClaim(user, claim);
        settlementPage.addManually().
                fillDescription(claimItem.getTextFieldSP()).
                fillCustomerDemand(claimItem.getCustomerDemand()).
                fillNewPrice(claimItem.getNewPriceSP()).
                fillCategory(claimItem.getExistingCat1()).
                fillSubCategory(claimItem.getExistingSubCat1()).
                cancel();

        assertFalse(settlementPage.isItemPresent(claimItem.getTextFieldSP()), "The claim has been was added");
    }


    /**
     * WHEN: U1 opens SID
     * WHEN: U1 adds New price amount V1
     * THEN: Cash compensation CC is equal to V1
     */
    @Test(description = "ECC-3144 Verify Cash compensation CC is equal to V1", dataProvider = "testDataProvider")
    public void ecc3144_16_cashCompensationEqualV1(User user, Claim claim, ClaimItem claimItem) {
        enableNewSid(user);

        SettlementPage settlementPage = loginAndCreateClaim(user, claim);
        SettlementDialog settlementDialog = settlementPage.addManually().
                fillDescription(claimItem.getTextFieldSP()).
                fillCustomerDemand(claimItem.getCustomerDemand()).
                fillNewPrice(claimItem.getNewPriceSP()).
                fillCategory(claimItem.getExistingCat1()).
                fillSubCategory(claimItem.getExistingSubCat1());
        assertEquals(Double.valueOf(claimItem.getCustomerDemand()), settlementDialog.cashCompensationValue(), "Customer price is not equal to cash compensation value");
    }

    /**
     * WHEN: U1 opens SID
     * WHEN: U1 adds New price amount V1
     * THEN: Cash compensation CC is equal to V1
     * WHEN: U1 selects Add valuation option
     * THEN: Add valuation dialog is displayed
     */
    @Test(description = "ECC-3144 Verify it's possible to open Add Valuation dialog in SID", dataProvider = "testDataProvider")
    public void ecc3144_17_openAddValuationDialogInSID(User user, Claim claim, ClaimItem claimItem) {
        enableNewSid(user);

        SettlementPage settlementPage = loginAndCreateClaim(user, claim);
        settlementPage.addManually().
                fillDescription(claimItem.getTextFieldSP()).
                fillCustomerDemand(claimItem.getCustomerDemand()).
                fillNewPrice(claimItem.getNewPriceSP()).
                fillCategory(claimItem.getExistingCat1()).
                fillSubCategory(claimItem.getExistingSubCat1()).
                addValuation();
    }

    /**
     * WHEN: U1 opens SID
     * WHEN: U1 adds New price amount V1
     * THEN: Cash compensation CC is equal to V1
     * WHEN: U1 selects Add valuation option
     * THEN: Add valuation dialog is displayed
     * WHEN: U1 selects third valuation type
     * WHEN: U1 adds valuation amount V2 < V1 and selects ok option
     * THEN: V2 is displayed in SID
     * THEN: CC is equal to V2
     */
    @Test(description = "ECC-3144 Verify it's possible to add new valuation price in add " +
            "valuation dialog (user selects 3d type)", dataProvider = "testDataProvider")
    public void ecc3144_18_addNewValuationPriceInAddValuationDialog(User user, Claim claim, ClaimItem claimItem) {
        enableNewSid(user);

        SettlementPage settlementPage = loginAndCreateClaim(user, claim);
        SettlementDialog settlementDialog = settlementPage.addManually().
                fillDescription(claimItem.getTextFieldSP()).
                fillCustomerDemand(claimItem.getCustomerDemand()).
                fillNewPrice(claimItem.getNewPriceSP()).
                fillCategory(claimItem.getExistingCat1()).
                fillSubCategory(claimItem.getExistingSubCat1());
        assertEquals(Double.valueOf(claimItem.getCustomerDemand()), settlementDialog.cashCompensationValue(), "Cash Compensation value is not equal to Customer Price");

        settlementDialog.addValuation().
                addValuationType(claimItem.getValuationType3()).
                addValuationPrice(claimItem.getLowerPrice()).
                ok();
        assertEquals(settlementDialog.cashCompensationValue(), Double.valueOf(claimItem.getLowerPrice()), "Cash Compensation Value is not equal Valuation price");
    }

    /**
     * WHEN: U1 opens SID
     * WHEN: U1 adds New price amount V1
     * THEN: Cash compensation CC is equal to V1
     * WHEN: U1 selects Add valuation option
     * THEN: Add valuation dialog is displayed
     * WHEN: U1 selects fourth valuation type
     * WHEN: U1 adds valuation amount V2 < V1 and selects ok option
     * THEN: V2 is displayed in SID
     * THEN: CC is equal to V2
     */
    @Test(description = "ECC-3144 Verify it's possible to add new valuation price in add valuation dialog (user selects 4th type)", dataProvider = "testDataProvider")
    public void ecc3144_19_addNewValuationPriceInAddValuationDialog(User user, Claim claim, ClaimItem claimItem) {
        enableNewSid(user);

        SettlementPage settlementPage = loginAndCreateClaim(user, claim);
        SettlementDialog settlementDialog = settlementPage.addManually().
                fillDescription(claimItem.getTextFieldSP()).
                fillCustomerDemand(claimItem.getCustomerDemand()).
                fillNewPrice(claimItem.getNewPriceSP()).
                fillCategory(claimItem.getExistingCat1()).
                fillSubCategory(claimItem.getExistingSubCat1());
        assertEquals(Double.valueOf(claimItem.getCustomerDemand()), settlementDialog.cashCompensationValue(), "Customer price is not equal to cash compensation value");

        settlementDialog.addValuation().
                addValuationType(claimItem.getValuationType4()).
                addValuationPrice(claimItem.getLowerPrice()).
                ok();
        assertEquals(Double.valueOf(claimItem.getLowerPrice()), settlementDialog.cashCompensationValue(), "Cash Compensation Value is not equal Valuation price");
    }


    /**
     * WHEN: U1 opens SID
     * WHEN: U1 adds New price amount V1
     * THEN: Cash compensation CC is equal to V1
     * WHEN: U1 selects Add valuation option
     * THEN: Add valuation dialog is displayed
     * WHEN: U1 selects fifth valuation type
     * WHEN: U1 adds valuation amount V2 < V1 and selects ok option
     * THEN: V2 is displayed in SID
     * THEN: CC is equal to V2
     */
    @Test(description = "ECC-3144 Verify it's possible to add new valuation price in " +
            "add valuation dialog (user selects 5th type)", dataProvider = "testDataProvider")
    public void ecc3144_20_addNewValuationPriceInAddValuationDialog(User user, Claim claim, ClaimItem claimItem) {
        enableNewSid(user);

        SettlementPage settlementPage = loginAndCreateClaim(user, claim);
        SettlementDialog settlementDialog = settlementPage.addManually().
                fillDescription(claimItem.getTextFieldSP()).
                fillCustomerDemand(claimItem.getCustomerDemand()).
                fillNewPrice(claimItem.getNewPriceSP()).
                fillCategory(claimItem.getExistingCat1()).
                fillSubCategory(claimItem.getExistingSubCat1());
        assertEquals(Double.valueOf(claimItem.getCustomerDemand()), settlementDialog.cashCompensationValue(), "Customer price is not equal to cash compensation value");

        settlementDialog.addValuation().
                addValuationType(claimItem.getValuationType5()).
                addValuationPrice(claimItem.getLowerPrice()).
                ok();
        assertEquals(Double.valueOf(claimItem.getLowerPrice()), settlementDialog.cashCompensationValue(), "Cash Compensation Value is not equal Valuation price");
    }


    /**
     * WHEN: U1 opens SID
     * AND: U1 enables Age option (switch the radio button on)
     * THEN: Years field is enabled
     * THEN: Months drop dwn menu is enabled
     */
    @Test(description = "ECC-3144 Verify it's possible to enable age option", dataProvider = "testDataProvider")
    public void ecc3144_22_enableAgeOption(User user, Claim claim, ClaimItem claimItem) {
        enableNewSid(user);

        SettlementPage settlementPage = loginAndCreateClaim(user, claim);
        SettlementDialog settlementDialog = settlementPage.addManually().
                fillDescription(claimItem.getTextFieldSP()).
                fillCustomerDemand(claimItem.getCustomerDemand()).
                fillNewPrice(claimItem.getNewPriceSP()).
                fillCategory(claimItem.getExistingCat1()).
                fillSubCategory(claimItem.getExistingSubCat1()).
                enableAge();
        assertTrue(settlementDialog.ageYearsIsEnabled(), "Age Years field is disabled");
        assertTrue(settlementDialog.monthMenuIsEnabled(), "Month DropDown is disabled");
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

    @Test(description = "ECC-3144 Verify it's possible to add years & month and save set", dataProvider = "testDataProvider")
    public void ecc3144_23_addYearsAndMonthAndSave(User user, Claim claim, ClaimItem claimItem) {
        enableNewSid(user);

        SettlementPage settlementPage = loginAndCreateClaim(user, claim);
        settlementPage.addManually().
                fillDescription(claimItem.getTextFieldSP()).
                fillCategory(claimItem.getExistingCat1()).
                fillSubCategory(claimItem.getExistingSubCat1()).
                fillCustomerDemand(claimItem.getCustomerDemand()).
                fillNewPrice(claimItem.getNewPriceSP()).
                enableAge("2016").
                selectMonth("6 " + claimItem.getMonths()).
                selectValuation(SettlementDialog.Valuation.NEW_PRICE).
                ok();

        SettlementDialog settlementDialog = settlementPage.openEditSettlementDialogByClaimDescr(claimItem.getTextFieldSP());
        assertEquals(settlementDialog.getAgeYears(), "2016", "The age year is not saved");
        assertEquals(settlementDialog.getMonthValue().trim(), "6 " + claimItem.getMonths(), "The month is not saved");
    }


    /**
     * WHEN: U1 opens SID
     * WHEN: U1 disables Age option and saves
     * AND: U1 opens SID again
     * THEN: Age option is disabled
     */
    @Test(description = "ECC-3144 Verify it's possible to disable age checkbox", dataProvider = "testDataProvider")
    public void ecc3144_24_disableAgeAndSave(User user, Claim claim, ClaimItem claimItem) {
        enableNewSid(user);

        SettlementPage settlementPage = loginAndCreateClaim(user, claim);
        SettlementDialog settlementDialog = settlementPage.addManually().
                fillDescription(claimItem.getTextFieldSP()).
                fillCustomerDemand(claimItem.getCustomerDemand()).
                fillNewPrice(claimItem.getNewPriceSP()).
                fillCategory(claimItem.getExistingCat1()).
                fillSubCategory(claimItem.getExistingSubCat1()).
                enableAge();

        assertTrue(settlementDialog.ageYearsIsEnabled(), "Age Years field is disabled");
        assertTrue(settlementDialog.monthMenuIsEnabled(), "Month DropDown is disabled");

        settlementDialog.disableAge().ok();
        settlementPage.openEditSettlementDialogByClaimDescr(claimItem.getTextFieldSP());

        assertFalse(settlementDialog.ageYearsIsEnabled(), "Age Years field is enabled");
        assertFalse(settlementDialog.monthMenuIsEnabled(), "Month DropDown is enabled");
    }


    private String calculateCashCompensation(ClaimItem claimItem) {
        Double cashCompensation = Double.valueOf(claimItem.getCustomerDemand()) - Double.valueOf(calculateDepreciation(claimItem));
        return String.valueOf(cashCompensation);
    }

    private String calculateCashCompensation(ClaimItem claimItem, Voucher voucher) {
        Double cashCompensation = Double.valueOf(claimItem.getCustomerDemand()) - Double.valueOf(calculateDepreciation(claimItem));
        Integer discount = Integer.valueOf(voucher.getDiscount());
        return toString(cashCompensation * (100 - discount) / 100);
    }

    private String calculateDepreciation(ClaimItem claimItem) {
        Double depreciation = Double.valueOf(claimItem.getCustomerDemand()) * Double.valueOf(claimItem.getDepAmount1()) / 100;
        return toString(depreciation);
    }

    private String calculateDepreciation(ClaimItem claimItem, Voucher voucher) {
        Double depreciation = Double.valueOf(claimItem.getCustomerDemand()) * Double.valueOf(claimItem.getDepAmount1()) / 100;
        Integer discount = Integer.valueOf(voucher.getDiscount());
        return toString(depreciation * (100 - discount) / 100);
    }

    private String toString(Double value) {
        return String.format("%.2f", value);
    }

}
