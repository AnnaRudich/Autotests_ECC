package com.scalepoint.automation.tests.sid;

import com.scalepoint.automation.pageobjects.dialogs.SettlementDialog;
import com.scalepoint.automation.pageobjects.pages.MailsPage;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.tests.sid.SidCalculator.VoucherValuationWithDepreciation;
import com.scalepoint.automation.utils.Constants;
import com.scalepoint.automation.utils.annotations.Jira;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.ClaimItem;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.testng.annotations.Test;

/**
 * GIVEN: FT "Display voucher value with depreciation deducted" ON
 * AND: FT "Compare discount and depreciation" ON
 * AND: FT "Combine discount and depreciation" ON
 * WHEN: ClaimHandler(CH) created claim
 * AND: Add manual line in Category (C1) with voucher (V1) with discount(VD1) assigned based on New Price (NP)
 * AND: Depreciation D1 >0%
 * AND: "Compare discount and depreciation" CHECKED/UNCHECKED
 * WHEN: CH add line(CL1) manually
 * THEN: Face value = New Price - D1%, Cash Value = New Price - VD1% - D1%
 * WHEN: CH add CL1 to Settlement
 * THEN: Face value = New Price
 * WHEN: CH complete claim through Replacement Wizard
 * THEN: Face value = New Price - D1%, Cash Value = New Price - VD1% - D1%
 * WHEN: User (U1) get Customer Welcome email
 * THAN: Face value = New Price - D1%, Cash Value = New Price - VD1% - D1%
 * WHEN: User (U1) login to the shop
 * THAN: Face value = New Price - D1%, Cash Value = New Price - VD1% - D1%
 * WHEN: CH review completed claim details
 * THAN: Face value = New Price - D1%, Cash Value = New Price - VD1% - D1%
 */
@Jira("https://jira.scalepoint.com/browse/CHARLIE-531")
@RequiredSetting(type = FTSetting.USE_UCOMMERCE_SHOP, enabled = false)
@RequiredSetting(type = FTSetting.REVIEW_ALL_CLAIM_TO_COMPLETE_CLAIM, enabled = false)
@RequiredSetting(type = FTSetting.DISPLAY_VOUCHER_VALUE_WITH_DEPRECATION_DEDUCTION)
@RequiredSetting(type = FTSetting.COMPARISON_OF_DISCOUNT_DEPRECATION)
public class DepreciationDeductedCombinedTests extends BaseTest {

    private static final VoucherValuationWithDepreciation VOUCHER_VALUATION_WITH_DEPRECIATION = SidCalculator.calculateVoucherValuation(
            Constants.PRICE_2400,
            Constants.VOUCHER_DISCOUNT_10,
            Constants.DEPRECIATION_10
    );

    @Test(dataProvider = "testDataProvider", description = "ECC-3288 Display voucher value with 'Combine discount and depreciation' UNCHECKED")
    @RequiredSetting(type = FTSetting.COMBINE_DISCOUNT_DEPRECATION, enabled = false)
    @RequiredSetting(type = FTSetting.USE_NEW_REPLACEMENT_DIALOG)
    public void ecc3288_1_verifyDndD2AndFTRelationCombineDnDOFF(User user, Claim claim, ClaimItem claimItem) {
        Double expectedCashValue = VOUCHER_VALUATION_WITH_DEPRECIATION.getCashCompensationOfVoucher();
        Double expectedNewPrice = Constants.PRICE_2400;
        verify(user, claim, claimItem, expectedNewPrice, expectedCashValue, false);
    }

    @Test(dataProvider = "testDataProvider", description = "ECC-3288 Display voucher value with 'Combine discount and depreciation' CHECKED")
    @RequiredSetting(type = FTSetting.USE_NEW_REPLACEMENT_DIALOG)
    @RequiredSetting(type = FTSetting.COMBINE_DISCOUNT_DEPRECATION)
    public void ecc3288_3281_2_verifyDndD2AndFTRelationCombineDDON(User user, Claim claim, ClaimItem claimItem) {
        Double expectedNewPrice = VOUCHER_VALUATION_WITH_DEPRECIATION.getCashCompensationOfVoucher();
        Double expectedCashValue = VOUCHER_VALUATION_WITH_DEPRECIATION.getCashCompensationWithDepreciation();
        verify(user, claim, claimItem, expectedNewPrice, expectedCashValue, true);
    }

    private void verify(User user, Claim claim, ClaimItem claimItem, double expectedNewPrice, double expectedCashValue, boolean setDiscountAndDepreciation) {
        SettlementDialog settlementDialog = loginAndCreateClaim(user, claim)
                .openSidAndFill(sidForm -> {
                    sidForm.withText(Constants.TEXT_LINE)
                            .withCustomerDemandPrice(Constants.PRICE_100_000)
                            .withNewPrice(Constants.PRICE_2400)
                            .withDepreciation(Constants.DEPRECIATION_10)
                            .withCategory(claimItem.getCategoryGroupBorn())
                            .withSubCategory(claimItem.getCategoryBornBabyudstyr())
                            .withVoucher(claimItem.getExistingVoucher_10());
                });

        if (setDiscountAndDepreciation) {
            settlementDialog.setDiscountAndDepreciation(true);
        }

        settlementDialog
                .doAssert(sid -> {
                    sid.assertVoucherCashValueIs(expectedCashValue);
                    sid.assertVoucherFaceValueIs(expectedNewPrice);
                })
                .closeSidWithOk()
                .doAssert(page -> page.assertFaceValueTooltipIs(expectedNewPrice))
                .toCompleteClaimPage()
                .fillClaimFormWithPassword(claim)
                .completeWithEmail()
                .openRecentClaim()
                .toMailsPage()
                .viewMail(MailsPage.MailType.CUSTOMER_WELCOME)
                .findLoginToShopLinkAndOpenIt()
                .enterPassword(Constants.DEFAULT_PASSWORD)
                .login()
                .doAssert(shopWelcomePage -> {
                    shopWelcomePage.assertProductCashValueIs(expectedCashValue);
                    shopWelcomePage.assertProductFaceValueIs(expectedNewPrice);
                });

        login(user).openRecentClaim()
                .doAssert(customerDetailsPage -> {
                    customerDetailsPage.assertCustomerCashValueIs(expectedCashValue);
                    customerDetailsPage.assertCustomerFaceValueTooltipIs(expectedNewPrice);
                })
                .reopenClaim()
                .toCompleteClaimPage()
                .fillClaimFormWithPassword(claim)
                .openReplacementWizard()
                .doAssert(replacementDialog -> {
                    replacementDialog.assertItemPriceValueIs(expectedCashValue);
                    replacementDialog.assertVoucherFaceValueIs(expectedNewPrice);
                })
                .closeReplacementDialog();
    }
}
