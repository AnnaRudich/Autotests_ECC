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
@RequiredSetting(type = FTSetting.REVIEW_ALL_CLAIM_TO_COMPLETE_CLAIM, enabled = false)
@RequiredSetting(type = FTSetting.FUNC_ENABLE_MARK_REVIEWED_REQUIRED)
@RequiredSetting(type = FTSetting.USE_UCOMMERCE_SHOP, enabled = false)
@RequiredSetting(type = FTSetting.SHOW_COMPACT_SETTLEMENT_ITEM_DIALOG)
@RequiredSetting(type = FTSetting.ENABLE_NEW_SETTLEMENT_ITEM_DIALOG)
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
    @Test(description = "ECC-3288 Display voucher value with depreciation deducted (off)", dataProvider = "testDataProvider")
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
                fillVoucher(claimItem.getExistingVoucher1());

        SidCalculations.VoucherValuation expectedCalculation = SidCalculations.calculate(claimItem.getNewPriceSP_2400(), voucher.getDiscount(), claimItem.getDepAmount1_10());

        String calculatedCashValue = String.format("%.2f", expectedCalculation.calculatedCashValue);
        String faceValue = String.format("%.2f", settlementDialog.voucherFaceValueFieldText());
        String cashValue = String.format("%.2f", settlementDialog.voucherCashValueFieldText());
        String newPrice = String.format("%.2f", Double.valueOf(claimItem.getNewPriceSP_2400()));

        assertEquals(faceValue, newPrice, "Face value should be equal to new price");
        assertEquals(cashValue, calculatedCashValue, "Voucher cash value should be equal to calculatedCashValue");
        settlementDialog.ok();

        String fetchedFaceTooltipValue = String.format("%.2f", settlementPage.getFaceTooltipValue());
        assertEquals(fetchedFaceTooltipValue, newPrice, "Face tooltip should be equal new Price");

        String password = "12341234";

        ShopWelcomePage shopWelcomePage = settlementPage.
                completeClaim().
                fillClaimFormWithPassword(claim, password).
                completeWithEmail().
                openRecentClient().
                toMailsPage().
                clickVisMail("Kundemail").
                findLoginToShopLinkAndOpenIt().
                enterPassword(password).
                login();

        String fetchedProductCashValue = String.format("%.2f", shopWelcomePage.getProductCashValue());
        String fetchedProductFaceValue = String.format("%.2f", shopWelcomePage.getProductFaceValue());

        assertEquals(fetchedProductCashValue, calculatedCashValue, "Voucher cash value should be equal to calculated calculatedCashValue");
        assertEquals(fetchedProductFaceValue, newPrice, "Voucher face value should be equal to entered new Price ");
        shopWelcomePage.logout();

        CustomerDetailsPage customerDetailsPage = login(user).openRecentClient();
        CustomerDetails customerDetails = customerDetailsPage.getCustomerDetails();
        String fetchedCustomerCashValue = String.format("%.2f", customerDetails.getCashValue());
        String fetchedCustomerFaceTooltipValue = String.format("%.2f", customerDetails.getFaceTooltipValue());

        assertEquals(fetchedCustomerCashValue, calculatedCashValue, "Voucher cash value should be equal to calculatedCashValue");
        assertEquals(fetchedCustomerFaceTooltipValue, newPrice, "Voucher face value should be equal to new Price");

        ReplacementDialog replacementDialog = customerDetailsPage.
                reopenClaim().
                completeClaim().
                fillClaimForm(claim).
                replaceClaim();

        String fetchedReplacementDialogVoucherFaceValue = String.format("%.2f", replacementDialog.getVoucherFaceValue());
        String fetchedReplacementDialogItemPriceValue = String.format("%.2f", replacementDialog.getItemPriceValue());

        assertEquals(fetchedReplacementDialogVoucherFaceValue, newPrice,
                "Voucher face value " + fetchedReplacementDialogVoucherFaceValue + " should be equal to new Price " + newPrice);

        assertEquals(fetchedReplacementDialogItemPriceValue, calculatedCashValue,
                "Voucher cash value " + fetchedReplacementDialogItemPriceValue + " should be equal to calculated " + calculatedCashValue);
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
    @Test(description = "ECC-3288 Display voucher value with depreciation deducted (on)", dataProvider = "testDataProvider")
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

        SidCalculations.VoucherValuation expectedCalculation = SidCalculations.calculate(claimItem.getNewPriceSP_2400(), voucher.getDiscount(), claimItem.getDepAmount1_10());

        String calculatedFaceValue = String.format("%.2f", expectedCalculation.calculatedCashValue);
        String calculatedCashValue = String.format("%.2f", expectedCalculation.voucherValue);

        String faceValue = String.format("%.2f", settlementDialog.voucherFaceValueFieldText());
        String cashValue = String.format("%.2f", settlementDialog.voucherCashValueFieldText());

        assertEquals(faceValue, calculatedFaceValue, "Voucher face value " + faceValue + " should be equal to depreciated new Price " + calculatedFaceValue);
        assertEquals(cashValue, calculatedCashValue, "Voucher cash value " + cashValue + " should be equal to depreciated voucher cash value " + calculatedCashValue);

        settlementDialog.ok();

        String fetchedFaceTooltipValue = String.format("%.2f", settlementPage.getFaceTooltipValue());

        assertEquals(fetchedFaceTooltipValue, calculatedFaceValue, "Tooltip face value " + fetchedFaceTooltipValue + " should be equal to depreciated new price " + calculatedFaceValue);

        String password = "12341234";
        ShopWelcomePage shopWelcomePage = settlementPage.completeClaim().
                fillClaimFormWithPassword(claim, password).
                completeWithEmail().
                openRecentClient().
                toMailsPage().
                clickVisMail("Kundemail").
                findLoginToShopLinkAndOpenIt().
                enterPassword(password).
                login();

        String fetchedProductCashValue = String.format("%.2f", shopWelcomePage.getProductCashValue());
        String fetchedProductFaceValue = String.format("%.2f", shopWelcomePage.getProductFaceValue());

        assertEquals(fetchedProductCashValue, calculatedCashValue, "Voucher cash value " + fetchedProductCashValue + " should be equal to depreciated voucher cash value " + calculatedCashValue);
        assertEquals(fetchedProductFaceValue, calculatedFaceValue, "Voucher face value " + fetchedProductFaceValue + " should be equal to depreciated new price " + calculatedFaceValue);

        shopWelcomePage.logout();

        CustomerDetailsPage customerDetailsPage = login(user).openRecentClient().toCustomerDetails();
        CustomerDetails customerDetails = customerDetailsPage.getCustomerDetails();

        String fetchedCustomerCashValue = String.format("%.2f", customerDetails.getCashValue());
        String fetchedCustomerFaceTooltipValue = String.format("%.2f", customerDetails.getFaceTooltipValue());

        assertEquals(fetchedCustomerCashValue, calculatedCashValue, "Voucher cash value " + fetchedCustomerCashValue + " should be equal to depreciated voucher cash value " + calculatedCashValue);
        assertEquals(fetchedCustomerFaceTooltipValue, calculatedFaceValue, "Voucher face value " + fetchedFaceTooltipValue + " should be equal to depreciated new price " + calculatedFaceValue);

        ReplacementDialog replacementDialog = customerDetailsPage.
                reopenClaim().
                completeClaim().
                fillClaimFormWithPassword(claim, password).
                replaceClaim();

        String fetchedReplacementDialogVoucherFaceValue = String.format("%.2f", replacementDialog.getVoucherFaceValue());
        String fetchedReplacementDialogItemPriceValue = String.format("%.2f", replacementDialog.getItemPriceValue());

        assertEquals(fetchedReplacementDialogVoucherFaceValue, calculatedFaceValue, "Voucher face value " + fetchedReplacementDialogVoucherFaceValue + " should be equal to depreciated new price " + calculatedCashValue);

        assertEquals(fetchedReplacementDialogItemPriceValue, calculatedCashValue, "Voucher cash value " + fetchedReplacementDialogItemPriceValue + " should be equal to depreciated voucher cash value " + calculatedCashValue);
        replacementDialog.closeReplacementDialog();
    }

}
