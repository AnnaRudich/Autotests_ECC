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
    public void charlie_508_4_verifyDiscretionaryReasonFTON(@UserCompany(CompanyCode.TRYGFORSIKRING)User user, Claim claim, ClaimItem claimItem) {
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
     * AND: Select Depreciation valuation
     * THEN: Drop-down for choosing reason is enabled
     */
    @RequiredSetting(type = FTSetting.DISPLAY_VOUCHER_VALUE_WITH_DEPRECATION_DEDUCTION)
    @Test(dataProvider = "testDataProvider", description = "CHARLIE-508 Verify drop down for choosing reason is enabled" +
            " after adding Input manually discretionary depreciation.FT=ON")
    public void charlie_508_5_verifyDiscretionaryReasonFTON(@UserCompany(CompanyCode.TRYGFORSIKRING)User user, Claim claim, ClaimItem claimItem) {
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
    public void charlie_508_6_verifyDiscretionaryReasonFTON(@UserCompany(CompanyCode.TRYGFORSIKRING)User user, Claim claim, ClaimItem claimItem, Voucher voucher) {
        SettlementPage settlementPage = loginAndCreateClaim(user, claim);
        SettlementDialog settlementDialog = settlementPage.
                addManually().
                fillCategory(claimItem.getTrygCategory()).
                fillSubCategory(claimItem.getTrygSubCategory()).
                fillDepreciationValue(claimItem.getUsedPrice()).
                setDiscountAndDepreciation(false).
                selectValuation(SettlementDialog.Valuation.NOT_SELECTED).
                fillVoucher(voucher.getTrygVoucher());
        assertFalse(settlementDialog.isDiscretionaryReasonEnabled(),"Discretionary reason drop down should be disabled");
    }

    /**
     * WHEN:FT is ON
     * AND: Create claimline which is matched with some voucher
     * AND: Add the manual discretionary depreciation
     * AND: Enable the combine checkbox
     * THEN: Drop-down is enabled
     */
    @RequiredSetting(type = FTSetting.DISPLAY_VOUCHER_VALUE_WITH_DEPRECATION_DEDUCTION)
    @RequiredSetting(type = FTSetting.COMBINE_DISCOUNT_DEPRECATION)
    @Test(dataProvider = "testDataProvider", description = "CHARLIE-508 Verify drop down for choosing reason is enabled" +
            " adding the manual discretionary depreciation (combine checkbox is checked).FT=ON")
    public void charlie_508_7_verifyDiscretionaryReasonFTON(@UserCompany(CompanyCode.TRYGFORSIKRING)User user, Claim claim, ClaimItem claimItem) {
        SettlementPage settlementPage = loginAndCreateClaim(user, claim);
        SettlementDialog settlementDialog = settlementPage.
                addManually().
                fillCategory(claimItem.getTrygCategory()).
                fillSubCategory(claimItem.getTrygSubCategory()).
                fillDepreciationValue(claimItem.getUsedPrice()).
                setDiscountAndDepreciation(true);
        assertTrue(settlementDialog.isDiscretionaryReasonEnabled(),"Discretionary reason drop down should be enabled");
    }

    /**
     * WHEN:FT is ON
     * AND: Create claimline which is matched with some voucher (combine checkbox is unchecked)
     * AND: Add the discretionary valuation
     * AND: Add the New price and apply the manual discretionary depreciation.
     * AND: Select the reason 1 for the DISCRETIONARY_VALUATION.
     * THEN: Verify the reason's representation for DISCRETIONARY_VALUATION.
     * AND: DISCRETIONARY reason field is enabled.
     */
    @RequiredSetting(type = FTSetting.DISPLAY_VOUCHER_VALUE_WITH_DEPRECATION_DEDUCTION)
    @RequiredSetting(type = FTSetting.COMBINE_DISCOUNT_DEPRECATION)
    @Test(dataProvider = "testDataProvider", description = "CHARLIE-508 verify the reason's representation for Discretionary valuation.FT=ON")
    public void charlie_508_8_verifyDiscretionaryReasonFTON(@UserCompany(CompanyCode.TRYGFORSIKRING)User user, Claim claim, ClaimItem claimItem,
                                                                     DepreciationType depreciationType, DiscretionaryReason discretionaryReason) {
        SettlementDialog settlementDialog = createClaimAndPrepareSid(user, claim, claimItem, depreciationType, discretionaryReason);
        settlementDialog.
                selectValuation(SettlementDialog.Valuation.ANDEN_VURDERING).
                selectDiscretionaryReason(discretionaryReason.getDiscretionaryReason1());
        assertTrue(settlementDialog.isDiscretionaryReasonSelected(discretionaryReason.getDiscretionaryReason1()), "Discretionary reason for Discretionary Valuation" +
                " is selected not correctly");
        assertTrue(settlementDialog.isDiscretionaryReasonEnabled(), "DISCRETIONARY reason field should be enabled for discretionary valuation");
    }

    /**
     * WHEN:FT is ON
     * AND: Create claimline which is matched with some voucher (combine checkbox is unchecked)
     * AND: Add the discretionary valuation
     * AND: Add the New price and apply the manual discretionary depreciation.
     * AND: Select the reason 1 for the New price.
     * THEN: Verify the reason's representation for New price VALUATION.
     * AND: DISCRETIONARY reason field is enabled.
     */
    @RequiredSetting(type = FTSetting.DISPLAY_VOUCHER_VALUE_WITH_DEPRECATION_DEDUCTION)
    @RequiredSetting(type = FTSetting.COMBINE_DISCOUNT_DEPRECATION)
    @Test(dataProvider = "testDataProvider", description = "CHARLIE-508 verify the reason's representation for New Price valuation.FT=ON")
    public void charlie_508_9_verifyDiscretionaryReasonFTON(@UserCompany(CompanyCode.TRYGFORSIKRING)User user, Claim claim, ClaimItem claimItem,
                                                                     DepreciationType depreciationType, DiscretionaryReason discretionaryReason) {
        SettlementDialog settlementDialog = createClaimAndPrepareSid(user, claim, claimItem, depreciationType, discretionaryReason);
        settlementDialog.
                selectValuation(SettlementDialog.Valuation.ANDEN_VURDERING).
                selectDiscretionaryReason(discretionaryReason.getDiscretionaryReason1()).
                selectValuation(SettlementDialog.Valuation.NEW_PRICE).
                selectDepreciationType(depreciationType.getDiscretionaryType()).
                selectDiscretionaryReason(discretionaryReason.getDiscretionaryReason1());
        assertTrue(settlementDialog.isDiscretionaryReasonSelected(discretionaryReason.getDiscretionaryReason1()), "Discretionary reason for New Price " +
                "is selected not correctly");
        assertTrue(settlementDialog.isDiscretionaryReasonEnabled(), "DISCRETIONARY reason field should be enabled for new price valuation");
    }

    /**
     * WHEN:FT is ON
     * AND: Create claimline which is matched with some voucher (combine checkbox is unchecked)
     * AND: Add the discretionary valuation
     * AND: Add the New price and apply the manual discretionary depreciation.
     * AND: Add Customer Demand price
     * AND: Select 0 year, 2 month in calendar
     * AND: Select the reason 'Max dækning' for the Customer Demand price.
     * THEN: Verify the reason's representation for Customer Demand Valuation.
     * AND: DISCRETIONARY reason field is enabled.
     */
    @RequiredSetting(type = FTSetting.DISPLAY_VOUCHER_VALUE_WITH_DEPRECATION_DEDUCTION)
    @RequiredSetting(type = FTSetting.COMBINE_DISCOUNT_DEPRECATION)
    @Test(dataProvider = "testDataProvider", description = "CHARLIE-508 verify the reason's representation for Customer Demand valuation.FT=ON")
    public void charlie_508_10_verifyDiscretionaryReasonFTON(@UserCompany(CompanyCode.TRYGFORSIKRING)User user, Claim claim, ClaimItem claimItem,
                                                                     DepreciationType depreciationType, DiscretionaryReason discretionaryReason) {
        String month = "2 ";
        SettlementDialog settlementDialog = createClaimAndPrepareSid(user, claim, claimItem, depreciationType, discretionaryReason);
        settlementDialog.
                selectValuation(SettlementDialog.Valuation.ANDEN_VURDERING).
                selectDiscretionaryReason(discretionaryReason.getDiscretionaryReason1()).
                fillCustomerDemand(claimItem.getBigCustomDemandPrice()).
                selectValuation(SettlementDialog.Valuation.CUSTOMER_DEMAND).
                selectMonth(month + claimItem.getMonths()).
                selectDiscretionaryReason(discretionaryReason.getDiscretionaryReason2());
        assertTrue(settlementDialog.isDiscretionaryReasonSelected(discretionaryReason.getDiscretionaryReason2()), "Discretionary reason for Custom Demand Price " +
                "is selected not correctly");
        assertTrue(settlementDialog.isDiscretionaryReasonEnabled(), "DISCRETIONARY reason field should be enabled for customer demand valuation");
    }

    /**
     * WHEN:FT is ON
     * AND: Create claimline which is matched with some voucher (combine checkbox is unchecked)
     * AND: Add the discretionary valuation
     * AND: Add the New price and apply the manual discretionary depreciation.
     * AND: Add Customer Demand price
     * AND: Select 0 year, 2 month in calendar
     * AND: Select the reason 'Max dækning' for the Voucher valuation.
     * THEN: Verify the reason's representation for Voucher Valuation.
     * AND: DISCRETIONARY reason field is disabled.
     */
    @RequiredSetting(type = FTSetting.DISPLAY_VOUCHER_VALUE_WITH_DEPRECATION_DEDUCTION)
    @RequiredSetting(type = FTSetting.COMBINE_DISCOUNT_DEPRECATION)
    @Test(dataProvider = "testDataProvider", description = "CHARLIE-508 verify the reason's representation for Customer Demand valuation.FT=ON")
    public void charlie_508_11_verifyDiscretionaryReasonFTON(@UserCompany(CompanyCode.TRYGFORSIKRING)User user, Claim claim, ClaimItem claimItem,
                                                                     DepreciationType depreciationType, DiscretionaryReason discretionaryReason) {
        String month = "2 ";
        SettlementDialog settlementDialog = createClaimAndPrepareSid(user, claim, claimItem, depreciationType, discretionaryReason);
        settlementDialog.
                selectValuation(SettlementDialog.Valuation.ANDEN_VURDERING).
                selectDiscretionaryReason(discretionaryReason.getDiscretionaryReason1()).
                fillCustomerDemand(claimItem.getBigCustomDemandPrice()).
                selectValuation(SettlementDialog.Valuation.VOUCHER).
                selectMonth(month + claimItem.getMonths()).
                selectDiscretionaryReason(discretionaryReason.getDiscretionaryReason2());
        assertTrue(settlementDialog.isDiscretionaryReasonSelected(discretionaryReason.getDiscretionaryReason2()), "Discretionary reason for Custom Demand Price " +
                "is selected not correctly");
        assertFalse(settlementDialog.isDiscretionaryReasonEnabled(), "DISCRETIONARY reason field should be disabled for voucher valuation");
    }

    /**
     * WHEN: Create claimline
     * AND: Add new price valuation with discretionary deducted by rules
     * AND: Select new price
     * THEN: Drop-down for choosing reason is enabled
     */
    @RequiredSetting(type = FTSetting.SHOW_POLICY_TYPE)
    @RequiredSetting(type = FTSetting.COMBINE_DISCOUNT_DEPRECATION)
    @Test(dataProvider = "testDataProvider", description = "CHARLIE-508 Verify Discretionary Reason when FT is ON and adding new price valuation" +
            " with discretionary deducted by rule")
    public void charlie_508_12_verifyDiscretionaryReasonFTON(@UserCompany(CompanyCode.TRYGFORSIKRING) User user, Claim claim,
                                                                      ClaimItem claimItem, DepreciationType depreciationType) {
        String month = "4 ";
        String age = "1";
        SettlementPage settlementPage = loginAndCreateClaim(user, claim);
        SettlementDialog settlementDialog = settlementPage.
                addManually().
                fillCategory(claimItem.getTrygCategory1()).
                fillNewPrice(claimItem.getUsedPrice()).
                enableAge().
                enterAgeYears(age).
                selectMonth(month + claimItem.getMonths()).
                selectDepreciationType(depreciationType.getDiscretionaryType()).
                fillDepreciation(20).
                selectValuation(SettlementDialog.Valuation.NEW_PRICE);
        assertTrue(settlementDialog.isDiscretionaryReasonEnabled(),"Discretionary Reason should be enabled");
    }

    /**
     * WHEN: Create claimline
     * AND: Add new price valuation with discretionary deducted by rules
     * AND: Switch the depreciation manually from  discretionary to policy.
     * THEN: Drop-down is greyed out
     */
    @RequiredSetting(type = FTSetting.SHOW_POLICY_TYPE)
    @RequiredSetting(type = FTSetting.COMBINE_DISCOUNT_DEPRECATION)
    @Test(dataProvider = "testDataProvider", description = "CHARLIE-508 Verify Discretionary Reason when FT is ON and adding new price valuation" +
            " with discretionary deducted by rule")
    public void charlie_508_13_verifyDiscretionaryReasonFTON(@UserCompany(CompanyCode.TRYGFORSIKRING) User user, Claim claim,
                                                             ClaimItem claimItem, DepreciationType depreciationType) {
        String month = "4 ";
        String age = "1";
        SettlementPage settlementPage = loginAndCreateClaim(user, claim);
        SettlementDialog settlementDialog = settlementPage.
                addManually().
                fillCategory(claimItem.getTrygCategory1()).
                fillNewPrice(claimItem.getUsedPrice()).
                enableAge().
                enterAgeYears(age).
                selectMonth(month + claimItem.getMonths()).
                selectDepreciationType(depreciationType.getDiscretionaryType()).
                selectValuation(SettlementDialog.Valuation.NEW_PRICE).
                selectDepreciationType(depreciationType.getPolicyType());
        assertFalse(settlementDialog.isDiscretionaryReasonEnabled(),"Discretionary Reason should be disabled");
    }

    /**
     * WHEN: Create claimline
     * AND: Add new price valuation with policy deducted by the rules
     * AND: Select new price
     * THEN: Drop-down is greyed out
     */
    @RequiredSetting(type = FTSetting.SHOW_POLICY_TYPE)
    @RequiredSetting(type = FTSetting.COMBINE_DISCOUNT_DEPRECATION)
    @Test(dataProvider = "testDataProvider", description = "CHARLIE-508 Verify Discretionary Reason when FT is ON and adding new price valuation" +
            " with policy deducted by the rules")
    public void charlie_508_14_verifyDiscretionaryReasonFTON(@UserCompany(CompanyCode.TRYGFORSIKRING) User user, Claim claim,
                                                             ClaimItem claimItem, DepreciationType depreciationType) {
        String month = "4 ";
        String age = "1";
        Integer reductionRule = 20;
        SettlementPage settlementPage = loginAndCreateClaim(user, claim);
        SettlementDialog settlementDialog = settlementPage.
                addManually().
                fillCategory(claimItem.getTrygCategory1()).
                fillNewPrice(claimItem.getUsedPrice()).
                enableAge().
                enterAgeYears(age).
                selectMonth(month + claimItem.getMonths()).
                selectValuation(SettlementDialog.Valuation.NEW_PRICE).
                applyReductionRuleByValue(reductionRule).
                selectDepreciationType(depreciationType.getPolicyType()).
                selectValuation(SettlementDialog.Valuation.NEW_PRICE);
        assertFalse(settlementDialog.isDiscretionaryReasonEnabled(),"Discretionary Reason should be disabled");
    }

    /**
     * WHEN:FT is ON
     * AND: Create claimline which is matched with some voucher (combine checkbox is unchecked)
     * AND: Add the discretionary valuation
     * AND: Add the New price and apply the manual discretionary depreciation.
     * AND: Add Customer Demand price
     * AND: Select 0 year, 2 month in calendar
     * AND: Select the reason 'Max dækning' for the Voucher valuation.
     * THEN: Verify the reason's representation for Voucher Valuation.
     * WHEN: Go to settlement page
     * THEN: no discretionary icon and no hover is displayed
     */
    @RequiredSetting(type = FTSetting.DISPLAY_VOUCHER_VALUE_WITH_DEPRECATION_DEDUCTION)
    @RequiredSetting(type = FTSetting.COMBINE_DISCOUNT_DEPRECATION)
    @Test(dataProvider = "testDataProvider", description = "CHARLIE-508 Verify the reason's icon with the hover on settlement page.FT=ON")
    public void charlie_508_15_verifyDiscretionaryReasonIconFTON(@UserCompany(CompanyCode.TRYGFORSIKRING)User user, Claim claim, ClaimItem claimItem,
                                                             DepreciationType depreciationType, DiscretionaryReason discretionaryReason) {
        String month = "2 ";
        SettlementDialog settlementDialog = createClaimAndPrepareSid(user, claim, claimItem, depreciationType, discretionaryReason);
        settlementDialog.
                fillDescription(claimItem.getTextFieldSP()).
                selectValuation(SettlementDialog.Valuation.ANDEN_VURDERING).
                SelectDiscretionaryReason(discretionaryReason.getDiscretionaryReason1()).
                fillCustomerDemand(claimItem.getBigCustomDemandPrice()).
                selectValuation(SettlementDialog.Valuation.VOUCHER).
                selectMonth(month + claimItem.getMonths()).
                SelectDiscretionaryReason(discretionaryReason.getDiscretionaryReason2());
        assertTrue(settlementDialog.isDiscretionaryReasonSelected(discretionaryReason.getDiscretionaryReason2()), "Discretionary reason for Custom Demand Price " +
                "is selected not correctly");
        assertFalse(settlementDialog.isDiscretionaryReasonEnabled(), "DISCRETIONARY reason field should be disabled for voucher valuation");
        SettlementPage settlementpage = settlementDialog.ok();

    }

    private SettlementDialog createClaimAndPrepareSid(@UserCompany(CompanyCode.TRYGFORSIKRING)User user, Claim claim, ClaimItem claimItem,
                                                      DepreciationType depreciationType, DiscretionaryReason discretionaryReason) {
        return loginAndCreateClaim(user, claim).
                addManually().
                fillCategory(claimItem.getTrygCategory()).
                fillSubCategory(claimItem.getTrygSubCategory()).
                setDiscountAndDepreciation(false).
                addValuation().
                addValuationPrice(claimItem.getTrygNewPrice()).
                addValuationType(claimItem.getValuationType4()).
                ok().
                addValuation().
                addValuationType(claimItem.getValuationType1()).
                addValuationPrice(claimItem.getUsedPrice()).
                ok().
                fillDepreciation(20).
                selectDepreciationType(depreciationType.getDiscretionaryType());
    }
}
