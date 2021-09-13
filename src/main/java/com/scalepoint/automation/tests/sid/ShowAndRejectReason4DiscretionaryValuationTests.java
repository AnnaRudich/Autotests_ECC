package com.scalepoint.automation.tests.sid;

import com.scalepoint.automation.pageobjects.dialogs.SettlementDialog;
import com.scalepoint.automation.pageobjects.pages.MyPage;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.services.externalapi.FunctionalTemplatesApi;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.services.usersmanagement.CompanyCode;
import com.scalepoint.automation.testGroups.TestGroups;
import com.scalepoint.automation.testGroups.UserCompanyGroups;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.utils.annotations.Jira;
import com.scalepoint.automation.utils.annotations.UserAttributes;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.input.Claim;
import com.scalepoint.automation.utils.data.entity.input.ClaimItem;
import com.scalepoint.automation.utils.data.entity.input.Translations;
import com.scalepoint.automation.utils.data.entity.input.Voucher;
import org.testng.annotations.Test;

import static com.scalepoint.automation.grid.ValuationGrid.Valuation.*;
import static com.scalepoint.automation.services.externalapi.ftemplates.FTSettings.disable;
import static com.scalepoint.automation.services.usersmanagement.UsersManager.getSystemUser;
import static com.scalepoint.automation.utils.Constants.PRICE_500;
import static com.scalepoint.automation.utils.Constants.TEXT_LINE;

@Jira("https://jira.scalepoint.com/browse/CHARLIE-508")
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
    @Test(groups = {TestGroups.SID, TestGroups.SHOW_AND_REJECT_REASON_FOR_DISCRETIONARY_REASON},
            dataProvider = "testDataProvider",
            description = "CHARLIE-508 Verify Discretionary and Reject Reason when FT is OFF")
    public void charlie_508_1_verifyDiscretionaryAndRejectReasonFTOFF(User user, Claim claim) {
        loginAndCreateClaim(user, claim)
                .openSid()
                .doAssert(sid -> {
                    sid.assertRejectReasonDisabled();
                    sid.assertDiscretionaryReasonInvisible();
                });
    }

    /**
     * WHEN:FT is OFF
     * AND: Create claim
     * AND: Open SID
     * THEN: Dropdown is greyed out by default
     */
    @RequiredSetting(type = FTSetting.MAKE_DISCREATIONARY_REASON_MANDATORY, enabled = false)
    @RequiredSetting(type = FTSetting.MAKE_REJECT_REASON_MANDATORY, enabled = false)
    @Test(groups = {TestGroups.SID,
            TestGroups.SHOW_AND_REJECT_REASON_FOR_DISCRETIONARY_REASON,
            UserCompanyGroups.TRYGFORSIKRING},
            dataProvider = "testDataProvider",
            description = "CHARLIE-508 Verify Discretionary and Reject Reason when FT is OFF")
    public void charlie_508_2_verifyDiscretionaryAndRejectReasonFTOFF(@UserAttributes(company = CompanyCode.TRYGFORSIKRING) User user, Claim claim) {
        loginAndCreateClaim(user, claim)
                .openSid()
                .doAssert(sid -> {
                    sid.assertRejectReasonDisabled();
                    sid.assertDiscretionaryReasonDisabled();
                });
    }

    /**
     * WHEN:FT is ON
     * AND: Create claim
     * AND: Open SID
     * THEN: A drop-down for choosing reason for choosing discretionary valuation is shown
     */
    @Test(groups = {TestGroups.SID,
            TestGroups.SHOW_AND_REJECT_REASON_FOR_DISCRETIONARY_REASON,
            UserCompanyGroups.TRYGFORSIKRING},
            dataProvider = "testDataProvider",
            description = "CHARLIE-508 Verify Discretionary and Reject Reason when FT is ON")
    public void charlie_508_3_verifyDiscretionaryAndRejectReasonFTON(@UserAttributes(company = CompanyCode.TRYGFORSIKRING) User user, Claim claim) {
        loginAndCreateClaim(user, claim)
                .openSid()
                .doAssert(sid -> {
                    sid.assertRejectReasonVisible();
                    sid.assertDiscretionaryReasonVisible();
                });
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
    @Test(groups = {TestGroups.SID,
            TestGroups.SHOW_AND_REJECT_REASON_FOR_DISCRETIONARY_REASON,
            UserCompanyGroups.TRYGFORSIKRING},
            dataProvider = "testDataProvider",
            description = "CHARLIE-508 Verify drop down for choosing reason is enabled after adding discretionary valuation.FT=ON")
    public void charlie_508_4_verifyDiscretionaryReasonFTON(@UserAttributes(company = CompanyCode.TRYGFORSIKRING) User user, Claim claim, ClaimItem claimItem) {
        loginAndCreateClaim(user, claim)
                .openSid()
                .openAddValuationForm()
                .addValuation(claimItem.getValuationTypeDiscretionary(), claimItem.getTrygNewPrice())
                .setValuation(DISCRETIONARY)
                .doAssert(SettlementDialog.Asserts::assertDiscretionaryReasonEnabled);
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
    @Test(groups = {TestGroups.SID,
            TestGroups.SHOW_AND_REJECT_REASON_FOR_DISCRETIONARY_REASON,
            UserCompanyGroups.TRYGFORSIKRING},
            dataProvider = "testDataProvider",
            description = "CHARLIE-508 Verify drop down for choosing reason is enabled" +
                    " after adding Input manually discretionary depreciation.FT=ON")
    public void charlie_508_5_verifyDiscretionaryReasonFTON(@UserAttributes(company = CompanyCode.TRYGFORSIKRING) User user, Claim claim, ClaimItem claimItem) {
        loginAndCreateClaim(user, claim)
                .openSid()
                .openAddValuationForm()
                .addValuation(claimItem.getValuationTypeNewPrice(), claimItem.getTrygNewPrice())
                .setDiscretionaryPrice(PRICE_500)
                .setValuation(DISCRETIONARY)
                .doAssert(SettlementDialog.Asserts::assertDiscretionaryReasonEnabled);
    }

    /**
     * WHEN:FT is ON
     * AND: Create claimline which is matched with some voucher
     * AND: Add the manual discretionary depreciation (combine checkbox is unchecked)
     * THEN: Drop-down is greyed out when the voucher is selected.
     */
    @RequiredSetting(type = FTSetting.DISPLAY_VOUCHER_VALUE_WITH_DEPRECATION_DEDUCTION)
    @RequiredSetting(type = FTSetting.COMBINE_DISCOUNT_DEPRECATION)
    @Test(groups = {TestGroups.SID,
            TestGroups.SHOW_AND_REJECT_REASON_FOR_DISCRETIONARY_REASON,
            UserCompanyGroups.TRYGFORSIKRING},
            dataProvider = "testDataProvider",
            description = "CHARLIE-508 Verify drop down for choosing reason is greyed out" +
                    " when the voucher is selected and adding the manual discretionary depreciation (combine checkbox is unchecked).FT=ON")
    public void charlie_508_6_verifyDiscretionaryReasonFTON(@UserAttributes(company = CompanyCode.TRYGFORSIKRING) User user, Claim claim, ClaimItem claimItem, Voucher voucher) {
        loginAndCreateClaim(user, claim)
                .openSidAndFill(sid -> {
                    sid
                            .withCategory(claimItem.getCategoryBicycles())
                            .withDiscretionaryPrice(PRICE_500)
                            .withDiscountAndDepreciation(false)
                            .withValuation(NOT_SELECTED)
                            .withAvailableVoucher(voucher.getTrygVoucher());
                })
                .doAssert(SettlementDialog.Asserts::assertDiscretionaryReasonDisabled);
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
    @Test(groups = {TestGroups.SID,
            TestGroups.SHOW_AND_REJECT_REASON_FOR_DISCRETIONARY_REASON,
            UserCompanyGroups.TRYGFORSIKRING},
            dataProvider = "testDataProvider",
            description = "CHARLIE-508 Verify drop down for choosing reason is enabled" +
                    " adding the manual discretionary depreciation (combine checkbox is checked).FT=ON")
    public void charlie_508_7_verifyDiscretionaryReasonFTON(@UserAttributes(company = CompanyCode.TRYGFORSIKRING) User user, Claim claim, ClaimItem claimItem) {
        loginAndCreateClaim(user, claim)
                .openSidAndFill(sid -> {
                    sid
                            .withCategory(claimItem.getCategoryBicycles())
                            .withDiscretionaryPrice(PRICE_500)
                            .withDiscountAndDepreciation(true);
                })
                .doAssert(SettlementDialog.Asserts::assertDiscretionaryReasonEnabled);
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
    @RequiredSetting(type = FTSetting.DO_NOT_DEPRECIATE_CUSTOMER_DEMAND, enabled = false, isDefault = true)
    @RequiredSetting(type = FTSetting.DISPLAY_VOUCHER_VALUE_WITH_DEPRECATION_DEDUCTION)
    @RequiredSetting(type = FTSetting.COMBINE_DISCOUNT_DEPRECATION)
    @Test(groups = {TestGroups.SID,
            TestGroups.SHOW_AND_REJECT_REASON_FOR_DISCRETIONARY_REASON,
            UserCompanyGroups.TRYGFORSIKRING},
            dataProvider = "testDataProvider",
            description = "CHARLIE-508 verify the reason's representation for different valuations.FT=ON")
    public void charlie_508_8_verifyDiscretionaryReasonFTON(@UserAttributes(company = CompanyCode.TRYGFORSIKRING) User user, Claim claim, ClaimItem claimItem,
                                                            Translations translations) {
        String reasonText = translations.getDiscretionaryReason().getMaxCoverage();
        loginAndCreateClaim(user, claim)
                .openSidAndFill(claimItem.getCategoryJewelry(), sid -> {
                    sid
                            .withDiscountAndDepreciation(false)
                            .withDiscretionaryPrice(1000.00)
                            .withNewPrice(3000.00)
                            .withCustomerDemandPrice(3000.00)
                            .withDepreciation(19, SettlementDialog.DepreciationType.DISCRETIONARY)
                            .withValuation(NEW_PRICE)
                            .withDiscretionaryReason(reasonText);

                })
                .doAssert(sid -> {
                    sid.assertDiscretionaryReasonEnabled();
                    sid.assertDiscretionaryReasonEqualTo(reasonText);
                })
                .setValuation(DISCRETIONARY)
                .doAssert(sid -> {
                    sid.assertDiscretionaryReasonEnabled();
                    sid.assertDiscretionaryReasonEqualTo(reasonText);
                })
                .setValuation(CUSTOMER_DEMAND)
                .doAssert(sid -> {
                    sid.assertDiscretionaryReasonEnabled();
                    sid.assertDiscretionaryReasonEqualTo(reasonText);
                })
                .setValuation(VOUCHER)
                .doAssert(SettlementDialog.Asserts::assertDiscretionaryReasonDisabled);
    }

    /**
     * WHEN: Create claimline
     * AND: Add new price valuation with discretionary deducted by rules
     * AND: Select new price
     * THEN: Drop-down for choosing reason is enabled
     */
    @RequiredSetting(type = FTSetting.SHOW_POLICY_TYPE)
    @RequiredSetting(type = FTSetting.COMBINE_DISCOUNT_DEPRECATION)
    @Test(groups = {TestGroups.SID,
            TestGroups.SHOW_AND_REJECT_REASON_FOR_DISCRETIONARY_REASON,
            UserCompanyGroups.TRYGFORSIKRING},
            dataProvider = "testDataProvider",
            description = "CHARLIE-508 Verify Discretionary Reason when FT is ON and adding new price valuation" +
                    " with discretionary deducted by rule")
    public void charlie_508_9_verifyDiscretionaryReasonFTON(@UserAttributes(company = CompanyCode.TRYGFORSIKRING) User user,
                                                            Claim claim,
                                                            ClaimItem claimItem) {
        loginAndCreateClaimToEditPolicyDialog(user, claim)
                .cancel()
                .openSidAndFill(sid -> sid
                        .withCategory(claimItem.getCategoryMusic())
                        .withNewPrice(PRICE_500)
                        .withAge(1, 4)
                        .withDepreciation(20, SettlementDialog.DepreciationType.DISCRETIONARY)
                        .withValuation(NEW_PRICE))
                .doAssert(SettlementDialog.Asserts::assertDiscretionaryReasonEnabled);
    }

    /**
     * WHEN: Create claimline
     * AND: Add new price valuation with discretionary deducted by rules
     * AND: Switch the depreciation manually from  discretionary to policy.
     * THEN: Drop-down is greyed out
     */
    @RequiredSetting(type = FTSetting.COMBINE_DISCOUNT_DEPRECATION)
    @Test(groups = {TestGroups.SID,
            TestGroups.SHOW_AND_REJECT_REASON_FOR_DISCRETIONARY_REASON,
            UserCompanyGroups.TRYGFORSIKRING},
            dataProvider = "testDataProvider",
            description = "CHARLIE-508 Verify Discretionary Reason when FT is ON and adding new price valuation" +
                    " with discretionary deducted by rule")
    public void charlie_508_10_verifyDiscretionaryReasonFTON(@UserAttributes(company = CompanyCode.TRYGFORSIKRING) User user,
                                                             Claim claim,
                                                             ClaimItem claimItem) {
        loginAndCreateClaim(user, claim)
                .openSidAndFill(sid -> sid
                        .withCategory(claimItem.getCategoryMusic())
                        .withNewPrice(PRICE_500)
                        .withAge(1, 4)
                        .withDepreciation(SettlementDialog.DepreciationType.POLICY)
                        .withValuation(NEW_PRICE))
                .doAssert(SettlementDialog.Asserts::assertDiscretionaryReasonDisabled);
    }

    /**
     * WHEN: Create claimline
     * AND: Add new price valuation with policy deducted by the rules
     * AND: Select new price
     * THEN: Drop-down is greyed out
     */
    @RequiredSetting(type = FTSetting.SHOW_POLICY_TYPE)
    @RequiredSetting(type = FTSetting.COMBINE_DISCOUNT_DEPRECATION)
    @Test(groups = {TestGroups.SID,
            TestGroups.SHOW_AND_REJECT_REASON_FOR_DISCRETIONARY_REASON,
            UserCompanyGroups.TRYGFORSIKRING},
            dataProvider = "testDataProvider",
            description = "CHARLIE-508 Verify Discretionary Reason when FT is ON and adding new price valuation" +
                    " with policy deducted by the rules")
    public void charlie_508_11_verifyDiscretionaryReasonFTON(@UserAttributes(company = CompanyCode.TRYGFORSIKRING) User user,
                                                             Claim claim,
                                                             ClaimItem claimItem) {
        loginAndCreateClaimToEditPolicyDialog(user, claim)
                .cancel()
                .openSidAndFill(sid -> sid
                        .withCategory(claimItem.getCategoryMusic())
                        .withNewPrice(10.00)
                        .withAge(3, 0)
                        .withValuation(NEW_PRICE)
                        .withReductionRule(20)
                        .withDepreciation(SettlementDialog.DepreciationType.POLICY))
                .doAssert(SettlementDialog.Asserts::assertDiscretionaryReasonDisabled);
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
    @Test(groups = {TestGroups.SID,
            TestGroups.SHOW_AND_REJECT_REASON_FOR_DISCRETIONARY_REASON,
            UserCompanyGroups.TRYGFORSIKRING},
            dataProvider = "testDataProvider",
            description = "CHARLIE-508 Verify the reason's icon with the hover on settlement page.FT=ON")
    public void charlie_508_12_verifyDiscretionaryReasonIconFTON(@UserAttributes(company = CompanyCode.TRYGFORSIKRING) User user,
                                                                 Claim claim,
                                                                 ClaimItem claimItem,
                                                                 Translations translations) {
        String reasonText = translations.getDiscretionaryReason().getEstimatedCompensationDueToLackOfDocumentation();
        loginAndCreateClaim(user, claim)
                .openSidAndFill(sid -> {
                    sid
                            .withCategory(claimItem.getCategoryShoes())
                            .withAgeDisabled()
                            .withCustomerDemandPrice(3000.00)
                            .withDiscountAndDepreciation(false)
                            .withDiscretionaryPrice(1000.00)
                            .withNewPrice(3000.00)
                            .withDepreciation(5, SettlementDialog.DepreciationType.DISCRETIONARY)
                            .withValuation(DISCRETIONARY)
                            .withDiscretionaryReason(reasonText);
                })
                .doAssert(sid -> sid.assertDiscretionaryReasonEqualTo(reasonText))
                .setValuation(VOUCHER)
                .closeSidWithOk()
                .findClaimLine(TEXT_LINE)
                .doAssert(SettlementPage.ClaimLine.Asserts::assertVoucherPresent);
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
    @Test(groups = {TestGroups.SID,
            TestGroups.SHOW_AND_REJECT_REASON_FOR_DISCRETIONARY_REASON,
            UserCompanyGroups.TRYGFORSIKRING},
            dataProvider = "testDataProvider",
            description = "CHARLIE-508 Verify the reason's icon with the hover on settlement page " +
                    "Customer demand price is matched with discretionary rule.FT=ON")
    public void charlie_508_13_verifyDiscretionaryReasonIconFTON(@UserAttributes(company = CompanyCode.TRYGFORSIKRING) User user,
                                                                 Claim claim, ClaimItem claimItem,
                                                                 Translations translations) {
        String reasonText = translations.getDiscretionaryReason().getEstimatedCompensationDueToConsumption();
        loginAndCreateClaim(user, claim)
                .openSidAndFill(sid -> {
                    sid
                            .withCategory(claimItem.getCategoryShoes())
                            .withCustomerDemandPrice(1000.00)
                            .withAge(0, 6)
                            .withDiscountAndDepreciation(false)
                            .withDepreciation(5, SettlementDialog.DepreciationType.DISCRETIONARY)
                            .withValuation(CUSTOMER_DEMAND)
                            .withDiscretionaryReason(reasonText);
                })
                .doAssert(sid -> sid.assertDiscretionaryReasonEqualTo(reasonText))
                .closeSidWithOk()
                .findClaimLine(TEXT_LINE)
                .doAssert(sid -> {
                    sid.assertDiscretionaryPresent();
                    sid.assertTooltipPresent(reasonText);
                });
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
    @Test(groups = {TestGroups.SID,
            TestGroups.SHOW_AND_REJECT_REASON_FOR_DISCRETIONARY_REASON,
            UserCompanyGroups.TRYGFORSIKRING},
            dataProvider = "testDataProvider",
            description = "CHARLIE-508 Verify the reason's icon with the hover on settlement page." +
                    "Discretionary valuation is added.FT=ON")
    public void charlie_508_14_verifyDiscretionaryReasonIconFTON(@UserAttributes(company = CompanyCode.TRYGFORSIKRING) User user,
                                                                 Claim claim,
                                                                 ClaimItem claimItem,
                                                                 Translations translations) {
        String reasonText = translations.getDiscretionaryReason().getEstimatedCompensationDueToConsumption();
        createClaimAndFillSid(user, claim, claimItem)
                .openAddValuationForm()
                .addValuation(claimItem.getValuationTypeDiscretionary(), 1000.00)
                .setNewPrice(3000.00)
                .setDiscountAndDepreciation(false)
                .setDescription(TEXT_LINE)
                .setValuation(DISCRETIONARY)
                .selectDiscretionaryReason(reasonText)
                .doAssert(sid -> sid.assertDiscretionaryReasonEqualTo(reasonText))
                .closeSidWithOk()
                .findClaimLine(TEXT_LINE)
                .doAssert(sid -> {
                    sid.assertDiscretionaryPresent();
                    sid.assertTooltipPresent(reasonText);
                });
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
    @Test(groups = {TestGroups.SID,
            TestGroups.SHOW_AND_REJECT_REASON_FOR_DISCRETIONARY_REASON,
            UserCompanyGroups.TRYGFORSIKRING},
            dataProvider = "testDataProvider",
            description = "CHARLIE-508 Verify the reason's icon with the hover on settlement page." +
                    "claimline is matched with some voucher and discretionary rule (combine checkbox is checked)FT=ON")
    public void charlie_508_15_verifyDiscretionaryReasonIconFTON(@UserAttributes(company = CompanyCode.TRYGFORSIKRING) User user,
                                                                 Claim claim,
                                                                 ClaimItem claimItem,
                                                                 Translations translations) {
        String reasonText = translations.getDiscretionaryReason().getEstimatedCompensationDueToConsumption();
        createClaimAndFillSid(user, claim, claimItem)
                .openAddValuationForm()
                .addValuation(claimItem.getValuationTypeDiscretionary(), 1000.00)
                .setNewPrice(3000.00)
                .setDiscountAndDepreciation(true)
                .setDescription(TEXT_LINE)
                .setValuation(DISCRETIONARY)
                .selectDiscretionaryReason(reasonText)
                .doAssert(sid -> sid.assertDiscretionaryReasonEqualTo(reasonText))
                .closeSidWithOk()
                .findClaimLine(TEXT_LINE)
                .doAssert(sid -> {
                    sid.assertDiscretionaryPresent();
                    sid.assertTooltipPresent(reasonText);
                });
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
    @Test(groups = {TestGroups.SID,
            TestGroups.SHOW_AND_REJECT_REASON_FOR_DISCRETIONARY_REASON,
            UserCompanyGroups.TRYGFORSIKRING},
            dataProvider = "testDataProvider",
            description = "CHARLIE-508 Verify the reason's icon with the hover on settlement page " +
                    "after disabling FT 'Show 'Valuation reason'block' .FT=ON")
    public void charlie_508_16_verifyDiscretionaryReasonIconFTONandOFF(@UserAttributes(company = CompanyCode.TRYGFORSIKRING) User user,
                                                                       Claim claim,
                                                                       ClaimItem claimItem,
                                                                       Translations translations) {
        String reasonText = translations.getDiscretionaryReason().getEstimatedCompensationDueToLackOfDocumentation();
        createClaimAndFillSid(user, claim, claimItem)
                .openAddValuationForm()
                .addValuationType(claimItem.getValuationTypeDiscretionary())
                .addValuationPrice(1000.00)
                .closeValuationDialogWithOk()
                .setNewPrice(3000.00)
                .setDiscountAndDepreciation(false)
                .setDescription(TEXT_LINE)
                .setValuation(DISCRETIONARY)
                .selectDiscretionaryReason(reasonText)
                .doAssert(sid -> sid.assertDiscretionaryReasonEqualTo(reasonText))
                .closeSidWithOk()
                .findClaimLine(TEXT_LINE)
                .doAssert(sid -> {
                    sid.assertDiscretionaryPresent();
                    sid.assertTooltipPresent(reasonText);
                })
                .selectLine()
                .saveClaim(claim)
                .getClaimMenu()
                .logout();

        login(getSystemUser())
                .getMainMenu()
                .toAdminPage();
        FunctionalTemplatesApi functionalTemplatesApi = new FunctionalTemplatesApi(getSystemUser());
        functionalTemplatesApi.updateTemplate(user, MyPage.class, disable(FTSetting.SHOW_DISCREATIONARY_REASON))
                .getClaimMenu()
                .logout();

        login(user, MyPage.class)
                .openRecentClaim()
                .reopenClaim()
                .findClaimLine(TEXT_LINE)
                .doAssert(claimLine -> {
                    claimLine.assertDiscretionaryPresent();
                    claimLine.assertTooltipPresent(reasonText);
                })
                .editLine()
                .doAssert(SettlementDialog.Asserts::assertDiscretionaryReasonInvisible);
    }


    private SettlementDialog createClaimAndFillSid(User user, Claim claim, ClaimItem claimItem) {
        return loginAndCreateClaim(user, claim)
                .openSidAndFill(sid -> {
                    sid
                            .withCategory(claimItem.getCategoryShoes())
                            .withCustomerDemandPrice(1000.00)
                            .withAge(0, 6)
                            .withDepreciation(5, SettlementDialog.DepreciationType.DISCRETIONARY);
                });
    }

}
