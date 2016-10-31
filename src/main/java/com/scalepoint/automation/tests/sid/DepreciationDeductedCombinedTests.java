package com.scalepoint.automation.tests.sid;

import com.scalepoint.automation.BaseTest;
import com.scalepoint.automation.pageobjects.dialogs.ReplacementDialog;
import com.scalepoint.automation.pageobjects.dialogs.SettlementDialog;
import com.scalepoint.automation.pageobjects.modules.CustomerDetails;
import com.scalepoint.automation.pageobjects.pages.CustomerDetailsPage;
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
        SettlementPage settlementPage = loginAndCreateClaim(user, claim);

        SettlementDialog settlementDialog = settlementPage.
                addManually().
                fillDescription(claimItem.getTextFieldSP()).
                fillCustomerDemand(claimItem.getBigCustomDemandPrice()).
                fillNewPrice(claimItem.getNewPriceSP_2400()).
                fillDepreciation(claimItem.getDepAmount1_10()).
                fillCategory(claimItem.getExistingCat1_Born()).
                fillSubCategory(claimItem.getExistingSubCat1_Babyudstyr()).
                fillVoucher(claimItem.getExistingVoucher1());

        VoucherValuation voucherValuation = SidCalculator.calculateVoucherValuation(claimItem.getNewPriceSP_2400(),
                voucher.getDiscount(), claimItem.getDepAmount1_10());

        Double expectedCashValue = voucherValuation.getCashCompensationOfVoucher();
        Double actualCashValue = settlementDialog.voucherCashValueFieldText();
        Double actualFaceValue = settlementDialog.voucherFaceValueFieldText();
        Double expectedNewPrice = Double.valueOf(claimItem.getNewPriceSP_2400());

        assertEqualsDouble(actualFaceValue, expectedNewPrice, "Voucher face value %s should be assertEqualsDouble to not depreciated new Price %s");
        assertEqualsDouble(actualCashValue, expectedCashValue, "Voucher cash value %s should be assertEqualsDouble to not depreciated voucher cash value %s");

        settlementDialog.ok();

        assertEqualsDouble(settlementPage.getFaceTooltipValue(), expectedNewPrice, "Tooltip face value %s should be assertEqualsDouble to not  depreciated new price %s");

        ShopWelcomePage shopWelcomePage = settlementPage.toCompleteClaimPage().
                fillClaimFormWithPassword(claim, "12341234").
                completeWithEmail().
                openRecentClaim().
                toMailsPage().
                openWelcomeCustomerMail().
                findLoginToShopLinkAndOpenIt().
                enterPassword("12341234").
                login();

        Double fetchedProductCashValue = shopWelcomePage.getProductCashValue();
        Double fetchedProductFaceValue = shopWelcomePage.getProductFaceValue();

        assertEqualsDouble(fetchedProductCashValue, expectedCashValue, "Voucher cash value %s should be assertEqualsDouble to not depreciated voucher cash value %s");
        assertEqualsDouble(fetchedProductFaceValue, expectedNewPrice, "Voucher face value %s should be assertEqualsDouble to not depreciated new price %s");

        shopWelcomePage.logout();

        CustomerDetailsPage customerDetailsPage = login(user).openRecentClaim();
        CustomerDetails customerDetails = customerDetailsPage.getCustomerDetails();

        Double fetchedCustomerCashValue = customerDetails.getCashValue();
        Double fetchedCustomerFaceTooltipValue = customerDetails.getFaceTooltipValue();
        assertEqualsDouble(fetchedCustomerCashValue, expectedCashValue, "Voucher cash value %s should be assertEqualsDouble to not depreciated voucher cash value %s");
        assertEqualsDouble(fetchedCustomerFaceTooltipValue, expectedNewPrice, "Voucher face value %s should be assertEqualsDouble to not depreciated new price %s");

        ReplacementDialog replacementDialog = customerDetailsPage.
                reopenClaim().
                toCompleteClaimPage().
                fillClaimFormWithPassword(claim, "12341234").
                openReplacementWizard();

        Double fetchedReplacementDialogVoucherFaceValue = replacementDialog.getVoucherFaceValue();
        Double fetchedReplacementDialogItemPriceValue = replacementDialog.getItemPriceValue();

        assertEqualsDouble(fetchedReplacementDialogVoucherFaceValue, expectedNewPrice, "Voucher face value %s should be assertEqualsDouble to not depreciated new price %s");
        assertEqualsDouble(fetchedReplacementDialogItemPriceValue, expectedCashValue, "Voucher cash value %s should be assertEqualsDouble to not depreciated voucher cash value %s");
        replacementDialog.closeReplacementDialog();
    }


    @Bug(bug = "CHARLIE-417")
    @Test(dataProvider = "testDataProvider", description = "ECC-3288 Display voucher value with 'Combine discount and depreciation' CHECKED")
    @RequiredSetting(type = FTSetting.COMBINE_DISCOUNT_DEPRECATION)
    public void ecc3288_3281_2_verifyDndD2AndFTRelationCombineDDON(User user, Claim claim, ClaimItem claimItem, Voucher voucher) {
        SettlementPage settlementPage = loginAndCreateClaim(user, claim);
        SettlementDialog settlementDialog = settlementPage.
                addManually().
                fillDescription(claimItem.getTextFieldSP()).
                fillCustomerDemand(claimItem.getBigCustomDemandPrice()).
                fillNewPrice(claimItem.getNewPriceSP_2400()).
                fillDepreciation(claimItem.getDepAmount1_10()).
                fillCategory(claimItem.getExistingCat1_Born()).
                fillSubCategory(claimItem.getExistingSubCat1_Babyudstyr()).
                fillVoucher(claimItem.getExistingVoucher1()).
                setDiscountAndDepreciation(true);

        VoucherValuation voucherValuation = SidCalculator.calculateVoucherValuation(claimItem.getNewPriceSP_2400(),
                voucher.getDiscount(), claimItem.getDepAmount1_10());

        double calculatedFaceValue = voucherValuation.getCashCompensationOfVoucher();
        double calculatedCashValue = voucherValuation.getCashCompensationWithDepreciation();
        Double actualFaceValue = settlementDialog.voucherFaceValueFieldText();
        Double actualCashValue = settlementDialog.voucherCashValueFieldText();

        assertEqualsDouble(actualFaceValue, calculatedFaceValue, "Voucher face value %s should be assertEqualsDouble to depreciated new Price %s");
        assertEqualsDouble(actualCashValue, calculatedCashValue, "Voucher cash value %s should be assertEqualsDouble to depreciated voucher cash value %s");

        settlementDialog.ok();

        Double fetchedFaceTooltipValue = settlementPage.getFaceTooltipValue();
        assertEqualsDouble(fetchedFaceTooltipValue, calculatedFaceValue, "Tooltip face value %s should be assertEqualsDouble to depreciated new price %s");

        ShopWelcomePage shopWelcomePage = settlementPage.toCompleteClaimPage().
                fillClaimFormWithPassword(claim, "12341234").
                completeWithEmail().
                openRecentClaim().
                toMailsPage().
                openWelcomeCustomerMail().
                findLoginToShopLinkAndOpenIt().
                enterPassword("12341234").
                login();

        Double fetchedProductCashValue = shopWelcomePage.getProductCashValue();
        Double fetchedProductFaceValue = shopWelcomePage.getProductFaceValue();

        assertEqualsDouble(fetchedProductCashValue, calculatedCashValue, "Voucher cash value %s should be assertEqualsDouble to depreciated voucher cash value %s");
        assertEqualsDouble(fetchedProductFaceValue, calculatedFaceValue, "Voucher face value %s should be assertEqualsDouble to depreciated new price %s");

        CustomerDetailsPage customerDetailsPage = login(user).openRecentClaim();

        ReplacementDialog replacementDialog = customerDetailsPage.
                reopenClaim().
                toCompleteClaimPage().
                fillClaimFormWithPassword(claim, "12341234").
                openReplacementWizard();


        Double replacementDialogVoucherFaceValue = replacementDialog.getVoucherFaceValue();
        Double replacementDialogItemPriceValue = replacementDialog.getItemPriceValue();

        assertEqualsDouble(replacementDialogVoucherFaceValue, calculatedFaceValue, "Voucher face value %s should be assertEqualsDouble to depreciated new price %s");
        assertEqualsDouble(replacementDialogItemPriceValue, calculatedCashValue, "Voucher cash value %s should be assertEqualsDouble to depreciated voucher cash value %s");
        replacementDialog.closeReplacementDialog();
    }
}
