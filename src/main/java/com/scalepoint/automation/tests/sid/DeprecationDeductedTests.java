package com.scalepoint.automation.tests.sid;

import com.scalepoint.automation.BaseTest;
import com.scalepoint.automation.pageobjects.dialogs.ReplacementDialog;
import com.scalepoint.automation.pageobjects.dialogs.SettlementDialog;
import com.scalepoint.automation.pageobjects.modules.CustomerDetails;
import com.scalepoint.automation.pageobjects.pages.CustomerDetailsPage;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.pageobjects.pages.ShopWelcomePage;
import com.scalepoint.automation.services.externalapi.VoucherAgreementApi;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.utils.annotations.Bug;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;

import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.ClaimItem;
import com.scalepoint.automation.utils.data.entity.Voucher;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.listeners.InvokedMethodListener;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

@Listeners({InvokedMethodListener.class})
@RequiredSetting(type = FTSetting.ENABLE_NEW_SETTLEMENT_ITEM_DIALOG)
@RequiredSetting(type = FTSetting.ENABLE_DEPRECIATION_COLUMN)
@RequiredSetting(type = FTSetting.USE_UCOMMERCE_SHOP, enabled = false)
@RequiredSetting(type = FTSetting.REVIEW_ALL_CLAIM_TO_COMPLETE_CLAIM, enabled = false)
@RequiredSetting(type = FTSetting.COMPARISON_OF_DISCOUNT_DEPRECATION, enabled = false)
public class DeprecationDeductedTests extends BaseTest {
    /**
     * GIVEN: FT "Display voucher value with depreciation deducted" OFF
     * WHEN: ClaimHandler(CH) created claim
     * AND: Add manual line in Category (C1) with voucher (V1) with discount(VD1) assigned based on New Price (NP)
     * AND: Depreciation D1 >0%
     * WHEN: CH add line(CL1) manually
     * THEN: Face value = New Price, Cash Value = New Price - VD1%
     * WHEN: CH add CL1 to Settlement
     * THEN: Face value = New Price
     * WHEN: CH complete claim through Replacement Wizard
     * THEN: Face value = New Price, Cash Value = New Price - VD1%
     * WHEN: User (U1) get Customer Welcome email
     * THEN: Face value = New Price, Cash Value = New Price - VD1%
     * WHEN: User (U1) login to the shop
     * THAN: Face value = New Price, Cash Value = New Price - VD1%
     * WHEN: CH review completed claim details
     * THAN: Face value = New Price, Cash Value = New Price - VD1%
     */
    @Bug(bug = "CHARLIE-404")
    @Test(dataProvider = "testDataProvider", description = "ECC-3288 Display voucher value with depreciation deducted (off)")
    @RequiredSetting(type = FTSetting.DISPLAY_VOUCHER_VALUE_WITH_DEPRECATION_DEDUCTION, enabled = false)
    public void ecc3288_1_displayVoucherValueWithDeprecationDeductedOFF(User user, Claim claim, ClaimItem claimItem, Voucher voucher) {
        VoucherAgreementApi.AssignedCategory categoryInfo = new VoucherAgreementApi(user).createVoucher(voucher);

        SettlementPage settlementPage = loginAndCreateClaim(user, claim);
        SettlementDialog settlementDialog = settlementPage.
                addManually().
                fillDescription(claimItem.getTextFieldSP()).
                fillCustomerDemand(claimItem.getBigCustomDemandPrice()).
                fillNewPrice(claimItem.getNewPriceSP_2400()).
                fillDepreciation(claimItem.getDepAmount1_10()).
                fillCategory(categoryInfo).
                fillVoucher(voucher.getVoucherNameSP());

        SidCalculations.VoucherValuation expectedCalculation = SidCalculations.calculateVoucherValuation(claimItem.getNewPriceSP_2400(), voucher.getDiscount(), claimItem.getDepAmount1_10());

        Double calculatedCashValue = expectedCalculation.getCashCompensationOfVoucher();
        Double faceValue = settlementDialog.voucherFaceValueFieldText();
        Double cashValue = settlementDialog.voucherCashValueFieldText();
        Double newPrice = Double.valueOf(claimItem.getNewPriceSP_2400());

        assertEqualsDouble(faceValue, newPrice, "Face value should be assertEqualsDouble to new price");
        assertEqualsDouble(cashValue, calculatedCashValue, "Voucher cash value should be assertEqualsDouble to calculatedCashValue");
        settlementDialog.ok();

        Double fetchedFaceTooltipValue = settlementPage.getFaceTooltipValue();
        assertEqualsDouble(fetchedFaceTooltipValue, newPrice, "Face tooltip should be assertEqualsDouble new Price");

        String password = "12341234";

        ShopWelcomePage shopWelcomePage = settlementPage.
                completeClaim().
                fillClaimFormWithPassword(claim, password).
                completeWithEmail().
                openRecentClaim().
                toMailsPage().
                openWelcomeCustomerMail().
                findLoginToShopLinkAndOpenIt().
                enterPassword(password).
                login();

        Double fetchedProductCashValue = shopWelcomePage.getProductCashValue();
        Double fetchedProductFaceValue = shopWelcomePage.getProductFaceValue();

        assertEqualsDouble(fetchedProductCashValue, calculatedCashValue, "Voucher cash value should be assertEqualsDouble to calculated calculatedCashValue");
        assertEqualsDouble(fetchedProductFaceValue, newPrice, "Voucher face value should be assertEqualsDouble to entered new Price ");
        shopWelcomePage.logout();

        CustomerDetailsPage customerDetailsPage = login(user).openRecentClaim();
        CustomerDetails customerDetails = customerDetailsPage.getCustomerDetails();
        Double fetchedCustomerCashValue = customerDetails.getCashValue();
        Double fetchedCustomerFaceTooltipValue = customerDetails.getFaceTooltipValue();

        Double voucherValue = expectedCalculation.getCashCompensationWithDepreciation();
        assertEqualsDouble(fetchedCustomerCashValue, voucherValue, "Voucher cash value should be assertEqualsDouble to calculatedCashValue");
        assertEqualsDouble(fetchedCustomerFaceTooltipValue, newPrice, "Voucher face value should be assertEqualsDouble to new Price");

        ReplacementDialog replacementDialog = customerDetailsPage.
                reopenClaim().
                completeClaim().
                fillClaimForm(claim).
                replaceClaim();

        Double fetchedReplacementDialogVoucherFaceValue = replacementDialog.getVoucherFaceValue();
        Double fetchedReplacementDialogItemPriceValue = replacementDialog.getItemPriceValue();

        assertEqualsDouble(fetchedReplacementDialogVoucherFaceValue, newPrice, "Voucher face value %s should be assertEqualsDouble to new Price %s");

        assertEqualsDouble(fetchedReplacementDialogItemPriceValue, calculatedCashValue, "Voucher cash value %s should be assertEqualsDouble to calculated %s");
        replacementDialog.closeReplacementDialog();
    }

    /**
     * GIVEN: FT "Display voucher value with depreciation deducted" ON
     * WHEN: ClaimHandler(CH) created claim
     * AND: Add manual line in Category (C1) with voucher (V1) with discount(VD1) assigned based on New Price (NP)
     * AND: Depreciation D1 >0%
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
    @Bug(bug = "CHARLIE-416")
    @Test(dataProvider = "testDataProvider", description = "ECC-3288 Display voucher value with depreciation deducted (on)")
    @RequiredSetting(type = FTSetting.DISPLAY_VOUCHER_VALUE_WITH_DEPRECATION_DEDUCTION)
    public void ecc3288_2_displayVoucherValueWithDeprecationDeductedON(User user, Claim claim, ClaimItem claimItem, Voucher voucher) {
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
                waitASecond();

        SidCalculations.VoucherValuation expectedCalculation = SidCalculations.calculateVoucherValuation(claimItem.getNewPriceSP_2400(), voucher.getDiscount(), claimItem.getDepAmount1_10());

        Double calculatedFaceValue = expectedCalculation.getCashCompensationOfVoucher();
        Double calculatedCashValue = expectedCalculation.getCashCompensationWithDepreciation();

        Double faceValue = settlementDialog.voucherFaceValueFieldText();
        Double cashValue = settlementDialog.voucherCashValueFieldText();

        assertEquals(faceValue, calculatedFaceValue, "Voucher face value %s should be assertEqualsDouble to depreciated new Price %s");
        assertEquals(cashValue, calculatedCashValue, "Voucher cash value %s should be assertEqualsDouble to depreciated voucher cash value %s");

        settlementDialog.ok();

        Double fetchedFaceTooltipValue = settlementPage.getFaceTooltipValue();

        assertEqualsDouble(fetchedFaceTooltipValue, calculatedFaceValue, "Tooltip face value %s should be assertEqualsDouble to depreciated new price %s");

        String password = "12341234";
        ShopWelcomePage shopWelcomePage = settlementPage.completeClaim().
                fillClaimFormWithPassword(claim, password).
                completeWithEmail().
                openRecentClaim().
                toMailsPage().
                openWelcomeCustomerMail().
                findLoginToShopLinkAndOpenIt().
                enterPassword(password).
                login();

        Double fetchedProductCashValue = shopWelcomePage.getProductCashValue();
        Double fetchedProductFaceValue = shopWelcomePage.getProductFaceValue();

        assertEqualsDouble(fetchedProductCashValue, calculatedCashValue, "Voucher cash value %s should be assertEqualsDouble to depreciated voucher cash value %s");
        assertEqualsDouble(fetchedProductFaceValue, calculatedFaceValue, "Voucher face value %s should be assertEqualsDouble to depreciated new price %s");

        shopWelcomePage.logout();

        CustomerDetailsPage customerDetailsPage = login(user).openRecentClaim().toCustomerDetails();
        CustomerDetails customerDetails = customerDetailsPage.getCustomerDetails();

        Double fetchedCustomerCashValue = customerDetails.getCashValue();
        Double fetchedCustomerFaceTooltipValue = customerDetails.getFaceTooltipValue();

        assertEqualsDouble(fetchedCustomerCashValue, calculatedCashValue, "Voucher cash value %s should be assertEqualsDouble to depreciated voucher cash value %s");
        assertEqualsDouble(fetchedCustomerFaceTooltipValue, calculatedFaceValue, "Voucher face value %s should be assertEqualsDouble to depreciated new price %s");

        ReplacementDialog replacementDialog = customerDetailsPage.
                reopenClaim().
                completeClaim().
                fillClaimFormWithPassword(claim, password).
                replaceClaim();

        Double fetchedReplacementDialogVoucherFaceValue = replacementDialog.getVoucherFaceValue();
        Double fetchedReplacementDialogItemPriceValue = replacementDialog.getItemPriceValue();

        assertEqualsDouble(fetchedReplacementDialogVoucherFaceValue, calculatedFaceValue, "Voucher face value %s should be assertEqualsDouble to depreciated new price %s");
        assertEqualsDouble(fetchedReplacementDialogItemPriceValue, calculatedCashValue, "Voucher cash value %s should be assertEqualsDouble to depreciated voucher cash value %s");

        replacementDialog.closeReplacementDialog();
    }

}
