package com.scalepoint.automation.tests.sid;

import com.scalepoint.automation.BaseTest;
import com.scalepoint.automation.pageobjects.dialogs.ReplacementDialog;
import com.scalepoint.automation.pageobjects.dialogs.SettlementDialog;
import com.scalepoint.automation.pageobjects.modules.CustomerDetails;
import com.scalepoint.automation.pageobjects.pages.CustomerDetailsPage;
import com.scalepoint.automation.pageobjects.pages.MailsPage;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.pageobjects.pages.oldshop.ShopWelcomePage;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.tests.sid.SidCalculator.VoucherValuation;
import com.scalepoint.automation.utils.annotations.Bug;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.ClaimItem;
import com.scalepoint.automation.utils.data.entity.Voucher;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.testng.annotations.Test;

import static com.scalepoint.automation.utils.OperationalUtils.assertEqualsDouble;

/**
 * GIVEN: FT "Display voucher value with depreciation deducted" ON
 * AND: FT "Compare discount and depreciation" ON
 * AND: FT "Combine discount and depreciation" ON
 * WHEN: ClaimHandler(CH) created claim
 * AND: Add manual line in Category (C1) with voucher (V1) with discount(VD1) assigned based on New Price (NP)
 * AND: Depreciation D1 >0%
 * AND: "Compare discount and depreciation" CHECKED
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
 *//*
/**
  * GIVEN: FT "Display voucher value with depreciation deducted" ON
  * AND: FT"Compare discount and depreciation" ON
  * AND: FT "Combine discount and depreciation" ON
  * WHEN: ClaimHandler(CH) created claim
  * AND:Combine discount and depreciation UNCHECKED
  * AND: Add manual line in Category (C1) with voucher (V1) with discount(VD1) assigned based on New Price (NP)
  * AND: Depreciation D1 >0%
  * WHEN: CH add line(CL1) manually
  * THEN: Face value = New Price , Cash Value = New Price - VD1%
  * WHEN: CH add CL1 to Settlement
  * THEN: Face value = New Price
  * WHEN: CH complete claim through Replacement Wizard
  * THEN: Face value = New Price , Cash Value = New Price - VD1%
  * WHEN: User (U1) get Customer Welcome email
  * THAN: Face value = New Price , Cash Value = New Price - VD1%
  * WHEN: User (U1) login to the shop
  * THAN: Face value = New Price, Cash Value = New Price - VD1%
  * WHEN: CH review completed claim details
  * THAN: Face value = New Price , Cash Value = New Price - VD1%
 */

@RequiredSetting(type = FTSetting.USE_UCOMMERCE_SHOP, enabled = false)
@RequiredSetting(type = FTSetting.REVIEW_ALL_CLAIM_TO_COMPLETE_CLAIM, enabled = false)
@RequiredSetting(type = FTSetting.DISPLAY_VOUCHER_VALUE_WITH_DEPRECATION_DEDUCTION)
@RequiredSetting(type = FTSetting.COMPARISON_OF_DISCOUNT_DEPRECATION)
@RequiredSetting(type = FTSetting.ENABLE_NEW_SETTLEMENT_ITEM_DIALOG)
public class DepreciationDeductedCombinedTests extends BaseTest {

    @Bug(bug = "CHARLIE-417,CHARLIE-772")
    @Test(dataProvider = "testDataProvider", description = "ECC-3288 Display voucher value with 'Combine discount and depreciation' UNCHECKED")
    @RequiredSetting(type = FTSetting.COMBINE_DISCOUNT_DEPRECATION, enabled = false)
    public void ecc3288_1_verifyDndD2AndFTRelationCombineDnDOFF(User user, Claim claim, ClaimItem claimItem, Voucher voucher) {

        VoucherValuation voucherValuation = SidCalculator.calculateVoucherValuation(claimItem.getNewPriceSP_2400(),
                voucher.getDiscount(), claimItem.getDepAmount1_10());
        Double expectedCashValue = voucherValuation.getCashCompensationOfVoucher();
        Double expectedNewPrice = Double.valueOf(claimItem.getNewPriceSP_2400());

        loginAndCreateClaim(user, claim)
                .openAddManuallyDialog()
                .fillDescription(claimItem.getTextFieldSP())
                .fillCustomerDemand(claimItem.getBigCustomDemandPrice())
                .fillNewPrice(claimItem.getNewPriceSP_2400())
                .fillDepreciation(claimItem.getDepAmount1_10())
                .fillCategory(claimItem.getExistingCat1_Born())
                .fillSubCategory(claimItem.getExistingSubCat1_Babyudstyr())
                .fillVoucher(claimItem.getExistingVoucher1())
                .assertVoucherCashValueIs(expectedCashValue)
                .assertVoucherFaceValueIs(expectedNewPrice)
                .closeSidWithOk()
                .assertFaceValueTooltipIs(expectedNewPrice)
                .toCompleteClaimPage()
                .fillClaimFormWithPassword(claim, "12341234")
                .completeWithEmail()
                .openRecentClaim()
                .toMailsPage()
                .viewMail(MailsPage.MailType.CUSTOMER_WELCOME)
                .findLoginToShopLinkAndOpenIt()
                .enterPassword("12341234")
                .login()
                .assertProductCashValueIs(expectedCashValue)
                .assertProductFaceValueIs(expectedNewPrice)
                .logout();

        login(user).openRecentClaim()
                .assertCustomerCashValueIs(expectedCashValue)
                .assertCustomerFaceValueTooltipIs(expectedNewPrice)
                .reopenClaim()
                .toCompleteClaimPage()
                .fillClaimFormWithPassword(claim, "12341234")
                .openReplacementWizard()
                .assertItemPriceValueIs(expectedCashValue)
                .assertVoucherFaceValueIs(expectedNewPrice)
                .closeReplacementDialog();
    }


    @Bug(bug = "CHARLIE-417")
    @Test(dataProvider = "testDataProvider", description = "ECC-3288 Display voucher value with 'Combine discount and depreciation' CHECKED")
    @RequiredSetting(type = FTSetting.COMBINE_DISCOUNT_DEPRECATION)
    public void ecc3288_3281_2_verifyDndD2AndFTRelationCombineDDON(User user, Claim claim, ClaimItem claimItem, Voucher voucher) {
        VoucherValuation voucherValuation = SidCalculator.calculateVoucherValuation(claimItem.getNewPriceSP_2400(),
                voucher.getDiscount(), claimItem.getDepAmount1_10());

        double calculatedFaceValue = voucherValuation.getCashCompensationOfVoucher();
        double calculatedCashValue = voucherValuation.getCashCompensationWithDepreciation();

        loginAndCreateClaim(user, claim)
                .openAddManuallyDialog()
                .fillDescription(claimItem.getTextFieldSP())
                .fillCustomerDemand(claimItem.getBigCustomDemandPrice())
                .fillNewPrice(claimItem.getNewPriceSP_2400())
                .fillDepreciation(claimItem.getDepAmount1_10())
                .fillCategory(claimItem.getExistingCat1_Born())
                .fillSubCategory(claimItem.getExistingSubCat1_Babyudstyr())
                .fillVoucher(claimItem.getExistingVoucher1())
                .setDiscountAndDepreciation(true)
                .assertVoucherFaceValueIs(calculatedFaceValue)
                .assertVoucherCashValueIs(calculatedCashValue)
                .closeSidWithOk()
                .assertFaceValueTooltipIs(calculatedFaceValue)
                .toCompleteClaimPage()
                .fillClaimFormWithPassword(claim, "12341234")
                .completeWithEmail()
                .openRecentClaim()
                .toMailsPage()
                .viewMail(MailsPage.MailType.CUSTOMER_WELCOME)
                .findLoginToShopLinkAndOpenIt()
                .enterPassword("12341234")
                .login()
                .assertProductFaceValueIs(calculatedFaceValue)
                .assertProductCashValueIs(calculatedCashValue);

        login(user).openRecentClaim()
                .reopenClaim()
                .toCompleteClaimPage()
                .fillClaimFormWithPassword(claim, "12341234")
                .openReplacementWizard()
                .assertItemPriceValueIs(calculatedCashValue)
                .assertVoucherFaceValueIs(calculatedFaceValue)
                .closeReplacementDialog();
    }
}
