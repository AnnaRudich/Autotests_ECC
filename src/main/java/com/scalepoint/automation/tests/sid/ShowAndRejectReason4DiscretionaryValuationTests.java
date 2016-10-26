package com.scalepoint.automation.tests.sid;

import com.scalepoint.automation.BaseTest;
import com.scalepoint.automation.pageobjects.dialogs.SettlementDialog;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.services.usersmanagement.CompanyCode;
import com.scalepoint.automation.utils.annotations.UserCompany;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.ClaimItem;
import com.scalepoint.automation.utils.data.entity.Voucher;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Created by asa on 10/17/2016.
 */
@RequiredSetting(type = FTSetting.ENABLE_NEW_SETTLEMENT_ITEM_DIALOG)
@RequiredSetting(type = FTSetting.SHOW_POLICY_TYPE, enabled = false)
@RequiredSetting(type = FTSetting.MAKE_DISCREATIONARY_REASON_MANDATORY)
@RequiredSetting(type = FTSetting.MAKE_REJECT_REASON_MANDATORY)
@RequiredSetting(type = FTSetting.SHOW_DISCREATIONARY_REASON)
public class ShowAndRejectReason4DiscretionaryValuationTests extends BaseTest {

    /**
     * WHEN:FT is OFF
     * AND: Create claim
     * AND: Open SID
     * THEN: Dropdown is not shown
     */
    @RequiredSetting(type = FTSetting.SHOW_DISCREATIONARY_REASON, enabled = false)
    @Test(dataProvider = "testDataProvider", description = "CHARLIE-508 Verify Discretionary and Reject Reason when FT is OFF")
    public void charlie_508_1_verifyDiscretionaryAndRejectReasonFTOFF(User user, Claim claim) {
        SettlementPage settlementPage = loginAndCreateClaim(user, claim);
        SettlementDialog settlementDialog = settlementPage.
                addManually();
        assertFalse(settlementDialog.isRejectReasonEnabled(), "Rejection Reason dropdown should be disabled");
        assertFalse(settlementDialog.isDiscretionaryReasonVisible(),"Discretionary Reason dropdown should NOT be visible");
    }
    /**
     * WHEN:FT is OFF
     * AND: Create claim
     * AND: Open SID
     * THEN: Dropdown is greyed out by default
     */
    @RequiredSetting(type = FTSetting.MAKE_DISCREATIONARY_REASON_MANDATORY, enabled = false)
    @RequiredSetting(type = FTSetting.MAKE_REJECT_REASON_MANDATORY, enabled = false)
    @Test(dataProvider = "testDataProvider", description = "CHARLIE-508 Verify Discretionary and Reject Reason when FT is OFF")
    public void charlie_508_2_verifyDiscretionaryAndRejectReasonFTOFF(@UserCompany(CompanyCode.TRYGFORSIKRING) User user, Claim claim) {
        SettlementPage settlementPage = loginAndCreateClaim(user, claim);
        SettlementDialog settlementDialog = settlementPage.
                addManually();
        assertFalse(settlementDialog.isRejectReasonEnabled(), "Rejection Reason should be disabled");
        assertFalse(settlementDialog.isDiscretionaryReasonEnabled(),"Discretionary Reason should be disabled");
    }

    /**
     * WHEN:FT is ON
     * AND: Create claim
     * AND: Open SID
     * THEN: A drop-down for choosing reason for choosing discretionary valuation is shown
     */
    @Test(dataProvider = "testDataProvider", description = "CHARLIE-508 Verify Discretionary and Reject Reason when FT is ON")
    public void charlie_508_3_verifyDiscretionaryAndRejectReasonFTON(@UserCompany(CompanyCode.TRYGFORSIKRING)User user, Claim claim) {
        SettlementPage settlementPage = loginAndCreateClaim(user, claim);
        SettlementDialog settlementDialog = settlementPage.
                addManually();
        assertTrue(settlementDialog.isRejectReasonVisible(), "Rejection Reason dropdown should be visible");
        assertTrue(settlementDialog.isDiscretionaryReasonVisible(), "Discretionary Reason should be visible");
    }

    /**
     * WHEN:FT is ON
     * AND: Create claim
     * AND: Open SID
     * AND: Add discretionary valuation
     * AND: Select discretionary valuation
     * THEN: Drop-down for choosing reason is enabled
     */
    @RequiredSetting(type = FTSetting.DISPLAY_VOUCHER_VALUE_WITH_DEPRECATION_DEDUCTION)
    @Test(dataProvider = "testDataProvider", description = "CHARLIE-508 Verify drop down for choosing reason is enabled after adding discretionary valuation.FT=ON")
    public void charlie_508_4_verifyDiscretionaryAndRejectReasonFTON(@UserCompany(CompanyCode.TRYGFORSIKRING)User user, Claim claim, ClaimItem claimItem) {
        SettlementPage settlementPage = loginAndCreateClaim(user, claim);
        SettlementDialog settlementDialog = settlementPage.
                addManually().
                addValuation().
                addValuationPrice(claimItem.getTrygNewPrice()).
                addValuationType(claimItem.getValuationType4()).
                ok().
                selectValuation(SettlementDialog.Valuation.ANDEN_VURDERING);
        assertTrue(settlementDialog.isDiscretionaryReasonEnabled(),"Reason for discretionary valuation should be enabled");
    }

    /**
     * WHEN:FT is ON
     * AND: Create claim
     * AND: Open SID
     * AND: Add new price valuation
     * AND: Input manually discretionary depreciation
     * AND: Select new price
     * THEN: Drop-down for choosing reason is enabled
     */
    @RequiredSetting(type = FTSetting.DISPLAY_VOUCHER_VALUE_WITH_DEPRECATION_DEDUCTION)
    @Test(dataProvider = "testDataProvider", description = "CHARLIE-508 Verify drop down for choosing reason is enabled" +
            " after adding Input manually discretionary depreciation.FT=ON")
    public void charlie_508_5_verifyDiscretionaryAndRejectReasonFTON(@UserCompany(CompanyCode.TRYGFORSIKRING)User user, Claim claim, ClaimItem claimItem) {
        SettlementPage settlementPage = loginAndCreateClaim(user, claim);
        SettlementDialog settlementDialog = settlementPage.
                addManually().
                addValuation().
                addValuationPrice(claimItem.getTrygNewPrice()).
                addValuationType(claimItem.getValuationType1()).
                ok().
                fillDepreciationValue(claimItem.getUsedPrice()).
                selectValuation(SettlementDialog.Valuation.ANDEN_VURDERING);
        assertTrue(settlementDialog.isDiscretionaryReasonEnabled(),"Discretionary reason drop down should be enabled");
    }

    /**
     * WHEN:FT is ON
     * AND: Create claimline which is matched with some voucher
     * AND: Add the manual discretionary depreciation (combine checkbox is unchecked)
     * THEN: Drop-down is greyed out when the voucher is selected.
     */
    @RequiredSetting(type = FTSetting.DISPLAY_VOUCHER_VALUE_WITH_DEPRECATION_DEDUCTION)
    @RequiredSetting(type = FTSetting.COMBINE_DISCOUNT_DEPRECATION)
    @Test(dataProvider = "testDataProvider", description = "CHARLIE-508 Verify drop down for choosing reason is greyed out" +
            " when the voucher is selected and adding the manual discretionary depreciation (combine checkbox is unchecked).FT=ON")
    public void charlie_508_6_verifyDiscretionaryAndRejectReasonFTON(@UserCompany(CompanyCode.TRYGFORSIKRING)User user, Claim claim, ClaimItem claimItem, Voucher voucher) {
        SettlementPage settlementPage = loginAndCreateClaim(user, claim);
        SettlementDialog settlementDialog = settlementPage.
                addManually().
                addValuation().
                addValuationPrice(claimItem.getTrygNewPrice()).
                addValuationType(claimItem.getValuationType1()).
                ok().
                fillCategory(claimItem.getTrygCategory()).
                fillSubCategory(claimItem.getTrygSubCategory()).
                fillDepreciationValue(claimItem.getUsedPrice()).
                setDiscountAndDepreciation(false).
                fillVoucher(voucher.getTrygVoucher());
        assertFalse(settlementDialog.isDiscretionaryReasonEnabled(),"Discretionary reason drop down should be disabled");
    }

    /**
     * WHEN:FT is ON
     * AND: Create claimline which is matched with some voucher
     * AND: Add the manual discretionary depreciation
     * AND: Enable the combine checkbox
     * THEN: Drop-down is enabled when the voucher is selected.
     */
    @RequiredSetting(type = FTSetting.DISPLAY_VOUCHER_VALUE_WITH_DEPRECATION_DEDUCTION)
    @RequiredSetting(type = FTSetting.COMBINE_DISCOUNT_DEPRECATION)
    @Test(dataProvider = "testDataProvider", description = "CHARLIE-508 Verify drop down for choosing reason is enabled" +
            " when the voucher is selected and adding the manual discretionary depreciation (combine checkbox is unchecked).FT=ON")
    public void charlie_508_7_verifyDiscretionaryAndRejectReasonFTON(@UserCompany(CompanyCode.TRYGFORSIKRING)User user, Claim claim, ClaimItem claimItem, Voucher voucher) {
        SettlementPage settlementPage = loginAndCreateClaim(user, claim);
        SettlementDialog settlementDialog = settlementPage.
                addManually().
                addValuation().
                addValuationPrice(claimItem.getTrygNewPrice()).
                addValuationType(claimItem.getValuationType1()).
                ok().
                fillCategory(claimItem.getTrygCategory()).
                fillSubCategory(claimItem.getTrygSubCategory()).
                fillDepreciationValue(claimItem.getUsedPrice()).
                setDiscountAndDepreciation(true).
                fillVoucher(voucher.getTrygVoucher()).
                selectValuation(SettlementDialog.Valuation.ANDEN_VURDERING);
        assertTrue(settlementDialog.isDiscretionaryReasonEnabled(),"Discretionary reason drop down should be enabled");
    }

}
