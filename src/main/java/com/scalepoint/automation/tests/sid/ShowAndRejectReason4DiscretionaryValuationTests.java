package com.scalepoint.automation.tests.sid;

import com.scalepoint.automation.BaseTest;
import com.scalepoint.automation.pageobjects.dialogs.SettlementDialog;
import com.scalepoint.automation.pageobjects.pages.MyPage;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.services.externalapi.FunctionalTemplatesApi;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.services.usersmanagement.CompanyCode;
import com.scalepoint.automation.utils.annotations.UserCompany;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.*;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.testng.annotations.Test;

import static com.scalepoint.automation.pageobjects.dialogs.SettlementDialog.Valuation.*;
import static com.scalepoint.automation.services.externalapi.ftemplates.FTSettings.disable;
import static com.scalepoint.automation.services.usersmanagement.UsersManager.getSystemUser;
import static org.testng.Assert.assertEquals;
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
        assertFalse(settlementDialog.isDiscretionaryReasonVisible(), "Discretionary Reason dropdown should NOT be visible");
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
        assertFalse(settlementDialog.isDiscretionaryReasonEnabled(), "Discretionary Reason should be disabled");
    }

    /**
     * WHEN:FT is ON
     * AND: Create claim
     * AND: Open SID
     * THEN: A drop-down for choosing reason for choosing discretionary valuation is shown
     */
    @Test(dataProvider = "testDataProvider", description = "CHARLIE-508 Verify Discretionary and Reject Reason when FT is ON")
    public void charlie_508_3_verifyDiscretionaryAndRejectReasonFTON(@UserCompany(CompanyCode.TRYGFORSIKRING) User user, Claim claim) {
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
    public void charlie_508_4_verifyDiscretionaryReasonFTON(@UserCompany(CompanyCode.TRYGFORSIKRING) User user, Claim claim, ClaimItem claimItem) {
        SettlementPage settlementPage = loginAndCreateClaim(user, claim);
        SettlementDialog settlementDialog = settlementPage.
                addManually().
                addValuation().
                addValuationPrice(claimItem.getTrygNewPrice()).
                addValuationType(claimItem.getValuationType4()).
                ok().
                selectValuation(ANDEN_VURDERING);
        assertTrue(settlementDialog.isDiscretionaryReasonEnabled(), "Reason for discretionary valuation should be enabled");
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
    public void charlie_508_5_verifyDiscretionaryReasonFTON(@UserCompany(CompanyCode.TRYGFORSIKRING) User user, Claim claim, ClaimItem claimItem) {
        SettlementPage settlementPage = loginAndCreateClaim(user, claim);
        SettlementDialog settlementDialog = settlementPage.
                addManually().
                addValuation().
                addValuationPrice(claimItem.getTrygNewPrice()).
                addValuationType(claimItem.getValuationType1()).
                ok().
                fillDiscretionaryPrice(claimItem.getUsedPrice()).
                selectValuation(ANDEN_VURDERING);
        assertTrue(settlementDialog.isDiscretionaryReasonEnabled(), "Discretionary reason drop down should be enabled");
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
    public void charlie_508_6_verifyDiscretionaryReasonFTON(@UserCompany(CompanyCode.TRYGFORSIKRING) User user, Claim claim, ClaimItem claimItem, Voucher voucher) {
        SettlementPage settlementPage = loginAndCreateClaim(user, claim);
        SettlementDialog settlementDialog = settlementPage.
                addManually().
                fillCategory(claimItem.getTrygCategory()).
                fillSubCategory(claimItem.getTrygSubCategory()).
                fillDiscretionaryPrice(claimItem.getUsedPrice()).
                setDiscountAndDepreciation(false).
                selectValuation(NOT_SELECTED).
                fillVoucher(voucher.getTrygVoucher());
        assertFalse(settlementDialog.isDiscretionaryReasonEnabled(), "Discretionary reason drop down should be disabled");
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
    public void charlie_508_7_verifyDiscretionaryReasonFTON(@UserCompany(CompanyCode.TRYGFORSIKRING) User user, Claim claim, ClaimItem claimItem) {
        SettlementPage settlementPage = loginAndCreateClaim(user, claim);
        SettlementDialog settlementDialog = settlementPage.
                addManually().
                fillCategory(claimItem.getTrygCategory()).
                fillSubCategory(claimItem.getTrygSubCategory()).
                fillDiscretionaryPrice(claimItem.getUsedPrice()).
                setDiscountAndDepreciation(true);
        assertTrue(settlementDialog.isDiscretionaryReasonEnabled(), "Discretionary reason drop down should be enabled");
    }

    /**
     * WHEN:FT is ON
     * AND: Create claimline which is matched with some voucher (combine checkbox is unchecked)
     * AND: Add the discretionary valuation
     * AND: Add the New price and apply the manual discretionary depreciation.
     * AND: Select the reason 1 for New price.
     * THEN: Verify the reason's representation for different valuations.
     * THEN: Selected reason is displayed for all valuation types with enable state except of the voucher valuation
     */
    @RequiredSetting(type = FTSetting.DISPLAY_VOUCHER_VALUE_WITH_DEPRECATION_DEDUCTION)
    @RequiredSetting(type = FTSetting.COMBINE_DISCOUNT_DEPRECATION)
    @Test(dataProvider = "testDataProvider", description = "CHARLIE-508 verify the reason's representation for different valuations.FT=ON")
    public void charlie_508_8_verifyDiscretionaryReasonFTON(@UserCompany(CompanyCode.TRYGFORSIKRING) User user, Claim claim, ClaimItem claimItem,
                                                            DiscretionaryReason discretionaryReason) {
        SettlementPage settlementPage = loginAndCreateClaim(user, claim);
        SettlementDialog settlementDialog = settlementPage.
                addManually().
                fillDescription(claimItem.getTextFieldSP()).
                fillCategory(claimItem.getAlkaCategory()).
                fillSubCategory(claimItem.getExistingSubCat2()).
                setDiscountAndDepreciation(false).
                fillDiscretionaryPrice(1000).
                fillNewPrice(3000).
                fillCustomerDemand(3000).
                selectDepreciationType(1).
                fillDepreciation(19).
                selectValuation(NEW_PRICE);

        String reasonText = discretionaryReason.getDiscretionaryReason2();

        settlementDialog.selectDiscretionaryReason(reasonText);
        assertTrue(settlementDialog.isDiscretionaryReasonEnabled(), "Reason must be enabled for New Price");
        assertEquals(settlementDialog.getDiscretionaryReasonText(), reasonText, "Wrong reason selected for New Price");

        settlementDialog.selectValuation(ANDEN_VURDERING);
        assertTrue(settlementDialog.isDiscretionaryReasonEnabled(), "Reason must be enabled for Discretionary Price");
        assertEquals(settlementDialog.getDiscretionaryReasonText(), reasonText, "Wrong reason selected for Discretionary Price");

        settlementDialog.selectValuation(CUSTOMER_DEMAND);
        assertTrue(settlementDialog.isDiscretionaryReasonEnabled(), "Reason field must be enabled for Customer Demand");
        assertEquals(settlementDialog.getDiscretionaryReasonText(), reasonText, "Wrong reason selected for Customer Demand");

        settlementDialog.selectValuation(VOUCHER);
        assertFalse(settlementDialog.isDiscretionaryReasonEnabled(), "Discretionary reason must be disabled for Voucher");
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
    public void charlie_508_9_verifyDiscretionaryReasonFTON(@UserCompany(CompanyCode.TRYGFORSIKRING) User user,
                                                            Claim claim,
                                                            ClaimItem claimItem,
                                                            DepreciationType depreciationType) {
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
                selectValuation(NEW_PRICE);
        assertTrue(settlementDialog.isDiscretionaryReasonEnabled(), "Discretionary Reason should be enabled");
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
    public void charlie_508_10_verifyDiscretionaryReasonFTON(@UserCompany(CompanyCode.TRYGFORSIKRING) User user,
                                                             Claim claim,
                                                             ClaimItem claimItem,
                                                             DepreciationType depreciationType) {
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
                selectValuation(NEW_PRICE).
                selectDepreciationType(depreciationType.getPolicyType());
        assertFalse(settlementDialog.isDiscretionaryReasonEnabled(), "Discretionary Reason should be disabled");
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
    public void charlie_508_11_verifyDiscretionaryReasonFTON(@UserCompany(CompanyCode.TRYGFORSIKRING) User user,
                                                             Claim claim,
                                                             ClaimItem claimItem,
                                                             DepreciationType depreciationType) {
        String age = "3";
        Integer reductionRule = 20;
        SettlementPage settlementPage = loginAndCreateClaim(user, claim);
        SettlementDialog settlementDialog = settlementPage.
                addManually().
                fillCategory(claimItem.getTrygCategory1()).
                fillNewPrice(10).
                enableAge().
                enterAgeYears(age).
                selectValuation(NEW_PRICE).
                applyReductionRuleByValue(reductionRule).
                selectDepreciationType(depreciationType.getPolicyType()).
                selectValuation(NEW_PRICE);
        assertFalse(settlementDialog.isDiscretionaryReasonEnabled(), "Discretionary Reason should be disabled");
    }

    /**
     * WHEN:FT is ON
     * AND: Create claimline which is matched with some voucher (combine checkbox is unchecked)
     * AND: Add the discretionary valuation
     * AND: reason 1 is selected
     * AND: Select Voucher valuation
     * AND: go to Settlement page
     * THEN: no discretionary icon and no hover is displayed
     */
    @RequiredSetting(type = FTSetting.DISPLAY_VOUCHER_VALUE_WITH_DEPRECATION_DEDUCTION)
    @RequiredSetting(type = FTSetting.COMBINE_DISCOUNT_DEPRECATION)
    @RequiredSetting(type = FTSetting.SHOW_NOT_CHEAPEST_CHOICE_POPUP, enabled = false)
    @Test(dataProvider = "testDataProvider", description = "CHARLIE-508 Verify the reason's icon with the hover on settlement page.FT=ON")
    public void charlie_508_12_verifyDiscretionaryReasonIconFTON(@UserCompany(CompanyCode.TRYGFORSIKRING) User user,
                                                                 Claim claim,
                                                                 ClaimItem claimItem,
                                                                 DiscretionaryReason discretionaryReason) {
        SettlementPage settlementPage = loginAndCreateClaim(user, claim);
        SettlementDialog settlementDialog = settlementPage.addManually().
                fillCategory(claimItem.getExistingCat4()).
                fillSubCategory(claimItem.getExistingSubCat4()).
                fillCustomerDemand(claimItem.getUsedPrice()).
                disableAge().
                setDiscountAndDepreciation(false).
                fillDiscretionaryPrice(1000).
                fillNewPrice(3000).
                selectDepreciationType(1).
                fillDepreciation(5).
                fillDescription(claimItem.getTextFieldSP()).
                fillCustomerDemand(3000).
                selectValuation(ANDEN_VURDERING).
                selectDiscretionaryReason(discretionaryReason.getDiscretionaryReason6());
        assertEquals(settlementDialog.getDiscretionaryReasonText(), discretionaryReason.getDiscretionaryReason6(), "Incorrect text discretionary reason");
        settlementDialog.selectValuation(VOUCHER).
                ok();
        assertTrue(settlementPage.isVoucherIconPresent(claimItem.getTextFieldSP()),"Voucher icon should be displayed");
    }

    /**
     * WHEN:FT is ON
     * AND: Add Customer Demand price
     * AND: Customer demand price is matched with discretionary rule.
     * AND: reason 2 is selected
     * AND: Go to settlement page
     * THEN: discretionary icon and the hover with reason 2 text are displayed.
     */
    @RequiredSetting(type = FTSetting.DISPLAY_VOUCHER_VALUE_WITH_DEPRECATION_DEDUCTION)
    @RequiredSetting(type = FTSetting.COMBINE_DISCOUNT_DEPRECATION)
    @RequiredSetting(type = FTSetting.SHOW_NOT_CHEAPEST_CHOICE_POPUP, enabled = false)
    @Test(dataProvider = "testDataProvider", description = "CHARLIE-508 Verify the reason's icon with the hover on settlement page " +
            "Customer demand price is matched with discretionary rule.FT=ON")
    public void charlie_508_13_verifyDiscretionaryReasonIconFTON(@UserCompany(CompanyCode.TRYGFORSIKRING)User user,
                                                                 Claim claim, ClaimItem claimItem,
                                                                 DiscretionaryReason discretionaryReason) {
        String month = "6 ";
        SettlementPage settlementPage = loginAndCreateClaim(user, claim);
        SettlementDialog settlementDialog = settlementPage.addManually().
                fillCategory(claimItem.getExistingCat4()).
                fillSubCategory(claimItem.getExistingSubCat4()).
                fillCustomerDemand(1000).
                enableAge().
                selectMonth(month + claimItem.getMonths()).
                setDiscountAndDepreciation(false).
                selectDepreciationType(1).
                fillDepreciation(5).
                fillDescription(claimItem.getTextFieldSP()).
                selectValuation(CUSTOMER_DEMAND).
                selectDiscretionaryReason(discretionaryReason.getDiscretionaryReason1());
        assertEquals(settlementDialog.getDiscretionaryReasonText(), discretionaryReason.getDiscretionaryReason1(), "Incorrect text discretionary reason");
        settlementDialog.ok();
        assertTrue(settlementPage.isDiscretionaryIconPresent(claimItem.getTextFieldSP()),"Discretionary reason icon should be displayed");
        assertTrue(settlementPage.isTooltipPresent(claimItem.getTextFieldSP(),discretionaryReason.getDiscretionaryReason1()),"Discretionary Reason Tooltip should be displayed");
    }

    /**
     * WHEN:FT is ON
     * AND: Create claimline which is matched with some voucher (combine checkbox is unchecked)
     * AND: Add the discretionary valuation
     * AND: reason 3 is selected
     * AND: Go to settlement page
     * THEN:  discretionary icon and the hover with reason 3 text are displayed.
     */

    @RequiredSetting(type = FTSetting.DISPLAY_VOUCHER_VALUE_WITH_DEPRECATION_DEDUCTION)
    @RequiredSetting(type = FTSetting.COMBINE_DISCOUNT_DEPRECATION)
    @RequiredSetting(type = FTSetting.SHOW_NOT_CHEAPEST_CHOICE_POPUP, enabled = false)
    @Test(dataProvider = "testDataProvider", description = "CHARLIE-508 Verify the reason's icon with the hover on settlement page." +
            "Discretionary valuation is added.FT=ON")
    public void charlie_508_14_verifyDiscretionaryReasonIconFTON(@UserCompany(CompanyCode.TRYGFORSIKRING)User user,
                                                                 Claim claim,
                                                                 ClaimItem claimItem,
                                                                 DiscretionaryReason discretionaryReason) {
        SettlementDialog settlementDialog = createClaimAndFillSid(user, claim, claimItem);
        settlementDialog.addValuation().
                addValuationType(claimItem.getValuationType4()).
                addValuationPrice(1000).
                ok().
                fillNewPrice(3000).
                setDiscountAndDepreciation(false).
                fillDescription(claimItem.getTextFieldSP()).
                selectValuation(ANDEN_VURDERING).
                selectDiscretionaryReason(discretionaryReason.getDiscretionaryReason7());

        assertEquals(settlementDialog.getDiscretionaryReasonText(), discretionaryReason.getDiscretionaryReason7(), "Incorrect text discretionary reason");

        SettlementPage settlementPage = settlementDialog.ok();
        assertTrue(settlementPage.isDiscretionaryIconPresent(claimItem.getTextFieldSP()),"Discretionary reason icon should be displayed");
        assertTrue(settlementPage.isTooltipPresent(claimItem.getTextFieldSP(),discretionaryReason.getDiscretionaryReason7()),"Discretionary Reason Tooltip should be displayed");

    }
    /**
     * WHEN:FT is ON
     * AND: Create claimline which is matched with some voucher and discretionary rule (combine checkbox is checked).     *
     * AND: reason 3 is selected
     * AND: Go to settlement page
     * THEN: discretionary icon and the hover with reason 3 text are displayed.
     */
    @RequiredSetting(type = FTSetting.DISPLAY_VOUCHER_VALUE_WITH_DEPRECATION_DEDUCTION)
    @RequiredSetting(type = FTSetting.COMBINE_DISCOUNT_DEPRECATION)
    @RequiredSetting(type = FTSetting.SHOW_NOT_CHEAPEST_CHOICE_POPUP, enabled = false)
    @Test(dataProvider = "testDataProvider", description = "CHARLIE-508 Verify the reason's icon with the hover on settlement page." +
            "claimline is matched with some voucher and discretionary rule (combine checkbox is checked)FT=ON")
    public void charlie_508_15_verifyDiscretionaryReasonIconFTON(@UserCompany(CompanyCode.TRYGFORSIKRING)User user,
                                                                 Claim claim,
                                                                 ClaimItem claimItem,
                                                                 DiscretionaryReason discretionaryReason) {
        SettlementDialog settlementDialog = createClaimAndFillSid(user, claim, claimItem);
        settlementDialog.addValuation().
                addValuationType(claimItem.getValuationType4()).
                addValuationPrice(1000).
                ok().
                fillNewPrice(3000).
                setDiscountAndDepreciation(true).
                fillDescription(claimItem.getTextFieldSP()).
                selectValuation(ANDEN_VURDERING).
                selectDiscretionaryReason(discretionaryReason.getDiscretionaryReason7());

        assertEquals(settlementDialog.getDiscretionaryReasonText(), discretionaryReason.getDiscretionaryReason7(), "Incorrect text discretionary reason");

        SettlementPage settlementPage = settlementDialog.ok();
        assertTrue(settlementPage.isDiscretionaryIconPresent(claimItem.getTextFieldSP()),"Discretionary reason icon should be displayed");
        assertTrue(settlementPage.isTooltipPresent(claimItem.getTextFieldSP(), discretionaryReason.getDiscretionaryReason7()), "Discretionary Reason Tooltip should be displayed");
    }

    /**
     * WHEN:FT is ON
     * AND: Create claimline
     * AND: Add the discretionary valuation
     * AND: Add a reason 1
     * AND: Save the claim
     * AND: Disable the FT "Show "Valuation reason" block"
     * AND: Reopen the claim
     * THEN: Discretionary icon and the hover with reason 1 text are still displayed.
     * THEN: On the SID no drop-down is shown
     */
    @RequiredSetting(type = FTSetting.DISPLAY_VOUCHER_VALUE_WITH_DEPRECATION_DEDUCTION)
    @RequiredSetting(type = FTSetting.COMBINE_DISCOUNT_DEPRECATION)
    @RequiredSetting(type = FTSetting.SHOW_NOT_CHEAPEST_CHOICE_POPUP, enabled = false)
    @Test(dataProvider = "testDataProvider", description = "CHARLIE-508 Verify the reason's icon with the hover on settlement page " +
            "after disabling FT 'Show 'Valuation reason'block' .FT=ON")
    public void charlie_508_16_verifyDiscretionaryReasonIconFTONandOFF(@UserCompany(CompanyCode.TRYGFORSIKRING)User user,
                                                                 Claim claim, ClaimItem claimItem,
                                                                 DiscretionaryReason discretionaryReason) {
        SettlementDialog settlementDialog = createClaimAndFillSid(user, claim, claimItem);
        settlementDialog.addValuation().
                addValuationType(claimItem.getValuationType4()).
                addValuationPrice(1000).
                ok().
                fillNewPrice(3000).
                setDiscountAndDepreciation(false).
                fillDescription(claimItem.getTextFieldSP()).
                selectValuation(ANDEN_VURDERING).
                selectDiscretionaryReason(discretionaryReason.getDiscretionaryReason6());
        assertEquals(settlementDialog.getDiscretionaryReasonText(), discretionaryReason.getDiscretionaryReason6(), "Incorrect text discretionary reason");

        SettlementPage settlementPage = settlementDialog.ok();
        assertTrue(settlementPage.isDiscretionaryIconPresent(claimItem.getTextFieldSP()),"Discretionary reason icon should be displayed");
        assertTrue(settlementPage.isTooltipPresent(claimItem.getTextFieldSP(),discretionaryReason.getDiscretionaryReason6()),"Discretionary Reason Tooltip should be displayed");

        settlementPage.saveClaim().
                getClaimMenu().
                logout();
                login(getSystemUser()).
                getMainMenu().
                toAdminPage();
        FunctionalTemplatesApi functionalTemplatesApi = new FunctionalTemplatesApi(getSystemUser());
        functionalTemplatesApi.updateTemplate(user.getFtId(), MyPage.class,
                disable(FTSetting.SHOW_DISCREATIONARY_REASON)).
                getClaimMenu().
                logout();
                login(user);
        SettlementPage line = new MyPage().openRecentClaim().
                reopenClaim();

        assertTrue(settlementPage.isDiscretionaryIconPresent(claimItem.getTextFieldSP()),"Discretionary reason icon should be displayed");
        assertTrue(settlementPage.isTooltipPresent(claimItem.getTextFieldSP(),discretionaryReason.getDiscretionaryReason6()),"Discretionary Reason Tooltip should be displayed");

        line.editClaimLine(claimItem.getTextFieldSP());
        assertFalse(settlementDialog.isDiscretionaryReasonVisible(), "Discretionary Reason should not be shown");
    }


    private SettlementDialog createClaimAndFillSid(User user, Claim claim, ClaimItem claimItem) {
        String month = "6 ";
        return loginAndCreateClaim(user, claim).
                addManually().
                fillCategory(claimItem.getExistingCat4()).
                fillSubCategory(claimItem.getExistingSubCat4()).
                fillCustomerDemand(1000).
                enableAge().
                selectMonth(month + claimItem.getMonths()).
                selectDepreciationType(1).
                fillDepreciation(5);
    }

}
