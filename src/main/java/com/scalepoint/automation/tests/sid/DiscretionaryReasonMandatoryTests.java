package com.scalepoint.automation.tests.sid;

import com.scalepoint.automation.BaseTest;
import com.scalepoint.automation.pageobjects.dialogs.SettlementDialog;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.services.usersmanagement.CompanyCode;
import com.scalepoint.automation.utils.annotations.UserCompany;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.*;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.testng.annotations.Test;

import static com.scalepoint.automation.pageobjects.dialogs.SettlementDialog.Valuation.*;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Created by asa on 12/13/2016.
 */
@RequiredSetting(type = FTSetting.SHOW_DISCREATIONARY_REASON)
@RequiredSetting(type = FTSetting.SHOW_POLICY_TYPE, enabled = false)
@RequiredSetting(type = FTSetting.MAKE_DISCREATIONARY_REASON_MANDATORY)
@RequiredSetting(type = FTSetting.SHOW_NOT_CHEAPEST_CHOICE_POPUP, enabled = false)
public class DiscretionaryReasonMandatoryTests extends BaseTest {

    /*
     * WHEN: Enable the Make "Discretionary reason" mandatory to fill option in FT
     * AND: Add the item with discretionary depreciation
     * AND: Select depreciable valuation
     * AND: click OK button
     * THEN: the field has red frame; the dialog is not closed
     */

    @Test(dataProvider = "testDataProvider", description = "CHARLIE-508 Verify FT Make 'Discretionary reason' mandatory to fill option while adding discretionary depreciation")
    public void charlie_508_1_verifyDiscretionaryReasonField(@UserCompany(CompanyCode.TRYGFORSIKRING) User user,
                                                             Claim claim,
                                                             ClaimItem claimItem) {
        SettlementDialog settlementDialog = createClaimAndFillSid(user, claim, claimItem);
        settlementDialog.
                fillDepreciation(10).
                selectDepreciationType(1).
                fillDescription(claimItem.getTextFieldSP()).
                selectValuation(NEW_PRICE).
                clickOK();
        assertTrue(settlementDialog.isDiscretionaryReasonEnabled(), "Discretionary Reason field should be enabled");
        assertTrue(settlementDialog.isDiscretionaryReasonHasRedBorder(), "Discretionary Reason field should have red border");

    }

    /*
     * WHEN: Enable the Make "Discretionary reason" mandatory to fill option in FT
     * AND: Add the item with discretionary valuation
     * AND: Select depreciable valuation
     * AND: click OK button
     * THEN: the field has red frame; the dialog is not closed
     */

    @Test(dataProvider = "testDataProvider", description = "CHARLIE-508 Verify FT Make 'Discretionary reason' mandatory to fill option")
    public void charlie_508_2_verifyDiscretionaryReasonField(@UserCompany(CompanyCode.TRYGFORSIKRING) User user,
                                                             Claim claim,
                                                             ClaimItem claimItem) {
        SettlementDialog settlementDialog = createClaimAndFillSid(user, claim, claimItem);
        settlementDialog.
                fillDiscretionaryPrice(300).
                fillDescription(claimItem.getTextFieldSP()).
                selectValuation(ANDEN_VURDERING).
                clickOK();
        assertTrue(settlementDialog.isDiscretionaryReasonEnabled(), "Discretionary Reason field should be enabled");
        assertTrue(settlementDialog.isDiscretionaryReasonHasRedBorder(), "Discretionary Reason field should have red border");
    }

    /*
     * WHEN: Enable the Make "Discretionary reason" mandatory to fill option in FT
     * AND: Add the item with discretionary depreciation
     * AND: Select non-depreciable valuation
     * AND: click OK button
     * THEN: the field is disabled; the dialog is closed
     */

    @Test(dataProvider = "testDataProvider", description = "CHARLIE-508 Verify FT Make 'Discretionary reason' mandatory to fill option while adding discretionary depreciation")
    public void charlie_508_3_verifyDiscretionaryReasonField(@UserCompany(CompanyCode.TRYGFORSIKRING) User user,
                                                             Claim claim,
                                                             ClaimItem claimItem) {
        SettlementDialog settlementDialog = createClaimAndFillSid(user, claim, claimItem);
        settlementDialog.
                fillDepreciation(10).
                selectDepreciationType(1).
                fillDescription(claimItem.getTextFieldSP());
        assertFalse(settlementDialog.isDiscretionaryReasonEnabled(), "Discretionary Reason field should be disabled");
        assertFalse(settlementDialog.isDiscretionaryReasonHasRedBorder(), "Discretionary Reason field should have no red border");
        settlementDialog.ok();
        removeLine(claimItem.getTextFieldSP());
    }


    /*
     * WHEN: Enable the Make "Discretionary reason" mandatory to fill option in FT
     * AND: Add the item with policy depreciation
     * AND: Select depreciable valuation
     * AND: click OK button
     * THEN: the field is disabled; the dialog is closed
     */

    @Test(dataProvider = "testDataProvider", description = "CHARLIE-508 Verify FT Make 'Discretionary reason' mandatory to fill option while adding policy depreciation")
    public void charlie_508_4_verifyDiscretionaryReasonField(@UserCompany(CompanyCode.TRYGFORSIKRING) User user,
                                                             Claim claim,
                                                             ClaimItem claimItem) {
        SettlementDialog settlementDialog = createClaimAndFillSid(user, claim, claimItem);
        settlementDialog.
                fillDepreciation(10).
                selectDepreciationType(0).
                fillDescription(claimItem.getTextFieldSP()).
                selectValuation(NEW_PRICE);
        assertFalse(settlementDialog.isDiscretionaryReasonEnabled(), "Discretionary Reason field should be disabled");
        assertFalse(settlementDialog.isDiscretionaryReasonHasRedBorder(), "Discretionary Reason field should have no red border");
        settlementDialog.ok();
        removeLine(claimItem.getTextFieldSP());
    }

     /*
     * WHEN: Enable the Make "Discretionary reason" mandatory to fill option in FT
     * AND: Add the item with discretionary depreciation = 0%
     * AND: Select depreciable valuation
     * AND: click OK button
     * THEN: the field is disabled; the dialog is closed
     */

    @Test(dataProvider = "testDataProvider", description = "CHARLIE-508 Verify FT Make 'Discretionary reason' mandatory to fill option while adding discretionary depreciation=0%")
    public void charlie_508_5_verifyDiscretionaryReasonField(@UserCompany(CompanyCode.TRYGFORSIKRING) User user,
                                                             Claim claim,
                                                             ClaimItem claimItem) {
        SettlementDialog settlementDialog = createClaimAndFillSid(user, claim, claimItem);
        settlementDialog.
                fillDepreciation(0).
                selectDepreciationType(1).
                fillDescription(claimItem.getTextFieldSP()).
                selectValuation(NEW_PRICE);
        assertFalse(settlementDialog.isDiscretionaryReasonEnabled(), "Discretionary Reason field should be disabled");
        assertFalse(settlementDialog.isDiscretionaryReasonHasRedBorder(), "Discretionary Reason field should have no red border");
        settlementDialog.ok();
        removeLine(claimItem.getTextFieldSP());
    }

    private SettlementDialog createClaimAndFillSid(User user, Claim claim, ClaimItem claimItem) {
        String month = "6 ";
        return loginAndCreateClaim(user, claim).
                addManually().
                fillCategory(claimItem.getExistingCat4()).
                fillSubCategory(claimItem.getExistingSubCat4()).
                fillCustomerDemand(1000).
                fillNewPrice(100).
                enableAge().
                selectMonth(month + claimItem.getMonths());
    }

    private void removeLine(String claimLine) {
        SettlementPage claim = new SettlementPage();
        assertTrue(claim.isItemPresent(claimLine), "Claim item is created");
        claim.findClaimLine(claimLine).
                selectLine().
                getToolBarMenu().
                removeSelected();
    }

}
