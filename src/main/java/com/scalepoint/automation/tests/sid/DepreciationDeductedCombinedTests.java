package com.scalepoint.automation.tests.sid;

import com.scalepoint.automation.pageobjects.dialogs.ReplacementDialog;
import com.scalepoint.automation.pageobjects.dialogs.SettlementDialog;
import com.scalepoint.automation.pageobjects.modules.CustomerDetails;
import com.scalepoint.automation.pageobjects.pages.CustomerDetailsPage;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.pageobjects.pages.ShopWelcomePage;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.tests.sid.SidCalculations.VoucherValuation;
import com.scalepoint.automation.utils.annotations.Bug;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.ClaimItem;
import com.scalepoint.automation.utils.data.entity.Voucher;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.listeners.InvokedMethodListener;
import org.testng.annotations.*;

import static org.testng.Assert.assertEquals;

import com.scalepoint.automation.BaseTest;

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

@Listeners(value = {InvokedMethodListener.class})
@RequiredSetting(type = FTSetting.REVIEW_ALL_CLAIM_TO_COMPLETE_CLAIM, enabled = false)
@RequiredSetting(type = FTSetting.DISPLAY_VOUCHER_VALUE_WITH_DEPRECATION_DEDUCTION)
@RequiredSetting(type = FTSetting.COMPARISON_OF_DISCOUNT_DEPRECATION)
@RequiredSetting(type = FTSetting.ENABLE_NEW_SETTLEMENT_ITEM_DIALOG)
public class DepreciationDeductedCombinedTests extends BaseTest {

    @Bug(bug = "CHARLIE-417,CHARLIE-772")
    @Test(description = "ECC-3288 Display voucher value with 'Combine discount and depreciation' UNCHECKED", dataProvider = "testDataProvider")
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

        VoucherValuation voucherValuation = SidCalculations.calculateVoucherValuation(claimItem.getNewPriceSP_2400(),
                voucher.getDiscount(), claimItem.getDepAmount1_10());

        String calculatedCashValue = String.format("%.2f", voucherValuation.getCashCompensationOfVoucher());
        String faceValue = String.format("%.2f", settlementDialog.voucherFaceValueFieldText());
        String cashValue = String.format("%.2f", settlementDialog.voucherCashValueFieldText());
        String newPrice = String.format("%.2f", Double.valueOf(claimItem.getNewPriceSP_2400()));

        assertEquals(faceValue, newPrice,
                "Voucher face value " + faceValue + " should be equal to not depreciated new Price " + newPrice);
        assertEquals(cashValue, calculatedCashValue,
                "Voucher cash value " + cashValue + " should be equal to not depreciated voucher cash value " + calculatedCashValue);

        settlementDialog.ok();

        String fetchedFaceTooltipValue = String.format("%.2f", settlementPage.getFaceTooltipValue());
        assertEquals(fetchedFaceTooltipValue, newPrice,
                "Tooltip face value " + fetchedFaceTooltipValue + " should be equal to not  depreciated new price " + newPrice);

        ShopWelcomePage shopWelcomePage = settlementPage.completeClaim().
                fillClaimFormWithPassword(claim, "12341234").
                completeWithEmail().
                openRecentClient().
                toMailsPage().
                openWelcomeCustomerMail().
                findLoginToShopLinkAndOpenIt().
                enterPassword("12341234").
                login();

        String fetchedProductCashValue = String.format("%.2f", shopWelcomePage.getProductCashValue());
        String fetchedProductFaceValue = String.format("%.2f", shopWelcomePage.getProductFaceValue());
        assertEquals(fetchedProductCashValue, calculatedCashValue,
                "Voucher cash value " + shopWelcomePage.getProductCashValue() + " should be equal to not depreciated voucher cash value " + calculatedCashValue);
        assertEquals(fetchedProductFaceValue, newPrice,
                "Voucher face value " + shopWelcomePage.getProductFaceValue() + "should be equal to not depreciated new price " + newPrice);

        shopWelcomePage.logout();
        CustomerDetailsPage customerDetailsPage = login(user).openRecentClient();
        CustomerDetails customerDetails = customerDetailsPage.getCustomerDetails();
        String fetchedCustomerCashValue = String.format("%.2f", customerDetails.getCashValue());
        String fetchedCustomerFaceTooltipValue = String.format("%.2f", customerDetails.getFaceTooltipValue());
        assertEquals(fetchedCustomerCashValue, calculatedCashValue,
                "Voucher cash value " + customerDetails.getCashValue() + " should be equal to not depreciated voucher cash value " + calculatedCashValue);
        assertEquals(fetchedCustomerFaceTooltipValue, newPrice,
                "Voucher face value " + customerDetails.getFaceValue() + " should be equal to not depreciated new price " + newPrice);
        ReplacementDialog replacementDialog = customerDetailsPage.
                reopenClaim().
                completeClaim().
                fillClaimFormWithPassword(claim, "12341234").
                replaceClaim();

        String fetchedReplacementDialogVoucherFaceValue = String.format("%.2f", replacementDialog.getVoucherFaceValue());
        String fetchedReplacementDialogItemPriceValue = String.format("%.2f", replacementDialog.getItemPriceValue());

        assertEquals(fetchedReplacementDialogVoucherFaceValue, newPrice,
                "Voucher face value " + fetchedReplacementDialogVoucherFaceValue + "should be equal to not depreciated new price " + newPrice);
        assertEquals(fetchedReplacementDialogItemPriceValue, calculatedCashValue,
                "Voucher cash value " + fetchedReplacementDialogItemPriceValue + " should be equal to not depreciated voucher cash value " + calculatedCashValue);
        replacementDialog.
                closeReplacementDialog();
    }


    @Bug(bug = "CHARLIE-417")
    @Test(description = "ECC-3288 Display voucher value with 'Combine discount and depreciation' CHECKED", dataProvider = "testDataProvider")
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

        VoucherValuation voucherValuation = SidCalculations.calculateVoucherValuation(claimItem.getNewPriceSP_2400(),
                voucher.getDiscount(), claimItem.getDepAmount1_10());

        String calculatedFaceValue = String.format("%.2f", voucherValuation.getCashCompensationOfVoucher());
        String calculatedCashValue = String.format("%.2f", voucherValuation.getCashCompensationWithDepreciation());
        String faceValue = String.format("%.2f", settlementDialog.voucherFaceValueFieldText());
        String cashValue = String.format("%.2f", settlementDialog.voucherCashValueFieldText());

        assertEquals(faceValue, calculatedFaceValue,
                "Voucher face value " + faceValue + " should be equal to depreciated new Price " + calculatedFaceValue);
        assertEquals(cashValue, calculatedCashValue,
                "Voucher cash value " + cashValue + " should be equal to depreciated voucher cash value " + calculatedCashValue);

        settlementDialog.ok();

        String fetchedFaceTooltipValue = String.format("%.2f", settlementPage.getFaceTooltipValue());
        assertEquals(fetchedFaceTooltipValue, calculatedFaceValue,
                "Tooltip face value " + fetchedFaceTooltipValue + " should be equal to depreciated new price " + calculatedFaceValue);
        ShopWelcomePage shopWelcomePage = settlementPage.completeClaim().
                fillClaimFormWithPassword(claim, "12341234").
                completeWithEmail().
                openRecentClient().
                toMailsPage().
                openWelcomeCustomerMail().
                findLoginToShopLinkAndOpenIt().
                enterPassword("12341234").
                login();

        String fetchedProductCashValue = String.format("%.2f", shopWelcomePage.getProductCashValue());
        String fetchedProductFaceValue = String.format("%.2f", shopWelcomePage.getProductFaceValue());

        assertEquals(fetchedProductCashValue, calculatedCashValue,
                "Voucher cash value " + fetchedProductCashValue + " should be equal to depreciated voucher cash value " + calculatedCashValue);
        assertEquals(fetchedProductFaceValue, calculatedFaceValue,
                "Voucher face value " + fetchedProductFaceValue + " should be equal to depreciated new price " + calculatedFaceValue);

        CustomerDetailsPage customerDetailsPage = login(user).openRecentClient();
        CustomerDetails customerDetails = customerDetailsPage.getCustomerDetails();

        ReplacementDialog replacementDialog = customerDetailsPage.
                reopenClaim().
                completeClaim().
                fillClaimFormWithPassword(claim, "12341234").
                replaceClaim();


        String fetchedReplacementDialogVoucherFaceValue = String.format("%.2f",replacementDialog.getVoucherFaceValue());
        String fetchedReplacementDialogItemPriceValue = String.format("%.2f",replacementDialog.getItemPriceValue());

        assertEquals(fetchedReplacementDialogVoucherFaceValue, calculatedFaceValue,
                "Voucher face value " + fetchedReplacementDialogVoucherFaceValue + " should be equal to depreciated new price " + calculatedCashValue);
        assertEquals(fetchedReplacementDialogItemPriceValue,calculatedCashValue,
                "Voucher cash value " + fetchedReplacementDialogItemPriceValue + " should be equal to depreciated voucher cash value " + calculatedCashValue);
        replacementDialog
                .closeReplacementDialog();
    }
}
