package com.scalepoint.automation.tests.sid;

import com.scalepoint.automation.BaseTest;
import com.scalepoint.automation.pageobjects.dialogs.ReplacementDialog;
import com.scalepoint.automation.pageobjects.dialogs.SettlementDialog;
import com.scalepoint.automation.pageobjects.modules.CustomerDetails;
import com.scalepoint.automation.pageobjects.pages.CustomerDetailsPage;
import com.scalepoint.automation.pageobjects.pages.MailsPage;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.pageobjects.pages.oldshop.ShopWelcomePage;
import com.scalepoint.automation.services.externalapi.VoucherAgreementApi;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.tests.sid.SidCalculator.VoucherValuation;
import com.scalepoint.automation.utils.annotations.Bug;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.ClaimItem;
import com.scalepoint.automation.utils.data.entity.Voucher;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.testng.annotations.Test;

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

        VoucherValuation expectedCalculation = SidCalculator.calculateVoucherValuation(claimItem.getNewPriceSP_2400(),
                voucher.getDiscount(),
                claimItem.getDepAmount1_10());

        Double expectedCashValue = expectedCalculation.getCashCompensationOfVoucher();
        Double expectedNewPrice = Double.valueOf(claimItem.getNewPriceSP_2400());

        Double actualFaceValue = settlementDialog.voucherFaceValueFieldText();
        Double actualCashValue = settlementDialog.voucherCashValueFieldText();

        assertEqualsDouble(actualFaceValue, expectedNewPrice, "Face value should be equals to new price");
        assertEqualsDouble(actualCashValue, expectedCashValue, "Voucher cash value should be equals to expectedCashValue");
        settlementDialog.ok();

        Double fetchedFaceTooltipValue = settlementPage.getFaceTooltipValue();
        assertEqualsDouble(fetchedFaceTooltipValue, expectedNewPrice, "Face tooltip should be equals new Price");

        String password = "12341234";

        ShopWelcomePage shopWelcomePage = settlementPage.
                toCompleteClaimPage().
                fillClaimFormWithPassword(claim, password).
                completeWithEmail().
                openRecentClaim().
                toMailsPage().
                viewMail(MailsPage.MailType.CUSTOMER_WELCOME).
                findLoginToShopLinkAndOpenIt().
                enterPassword(password).
                login();

        Double fetchedProductCashValue = shopWelcomePage.getProductCashValue();
        Double fetchedProductFaceValue = shopWelcomePage.getProductFaceValue();

        assertEqualsDouble(fetchedProductCashValue, expectedCashValue, "Voucher cash value should be equals to calculated expectedCashValue");
        assertEqualsDouble(fetchedProductFaceValue, expectedNewPrice, "Voucher face value should be equals to entered new Price ");
        shopWelcomePage.logout();

        CustomerDetailsPage customerDetailsPage = login(user).openRecentClaim();
        CustomerDetails customerDetails = customerDetailsPage.getCustomerDetails();
        Double fetchedCustomerCashValue = customerDetails.getCashValue();
        Double fetchedCustomerFaceTooltipValue = customerDetails.getFaceTooltipValue();

        Double voucherValue = expectedCalculation.getCashCompensationWithDepreciation();
        assertEqualsDouble(fetchedCustomerCashValue, voucherValue, "Voucher cash value should be equals to expectedCashValue");
        assertEqualsDouble(fetchedCustomerFaceTooltipValue, expectedNewPrice, "Voucher face value should be equals to new Price");

        ReplacementDialog replacementDialog = customerDetailsPage.
                reopenClaim().
                toCompleteClaimPage().
                fillClaimForm(claim).
                openReplacementWizard();

        Double fetchedReplacementDialogVoucherFaceValue = replacementDialog.getVoucherFaceValue();
        Double fetchedReplacementDialogItemPriceValue = replacementDialog.getItemPriceValue();

        assertEqualsDouble(fetchedReplacementDialogVoucherFaceValue, expectedNewPrice, "Voucher face value %s should be equals to new Price %s");

        assertEqualsDouble(fetchedReplacementDialogItemPriceValue, expectedCashValue, "Voucher cash value %s should be equals to calculated %s");
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

        VoucherValuation expectedCalculation = SidCalculator.calculateVoucherValuation(claimItem.getNewPriceSP_2400(), voucher.getDiscount(), claimItem.getDepAmount1_10());

        Double calculatedFaceValue = expectedCalculation.getCashCompensationOfVoucher();
        Double calculatedCashValue = expectedCalculation.getCashCompensationWithDepreciation();

        Double faceValue = settlementDialog.voucherFaceValueFieldText();
        Double cashValue = settlementDialog.voucherCashValueFieldText();

        assertEqualsDouble(faceValue, calculatedFaceValue, "Voucher face value %s should be equals to depreciated new Price %s");
        assertEqualsDouble(cashValue, calculatedCashValue, "Voucher cash value %s should be equals to depreciated voucher cash value %s");

        settlementDialog.ok();

        Double fetchedFaceTooltipValue = settlementPage.getFaceTooltipValue();

        assertEqualsDouble(fetchedFaceTooltipValue, calculatedFaceValue, "Tooltip face value %s should be equals to depreciated new price %s");

        String password = "12341234";
        ShopWelcomePage shopWelcomePage = settlementPage.toCompleteClaimPage().
                fillClaimFormWithPassword(claim, password).
                completeWithEmail().
                openRecentClaim().
                toMailsPage().
                viewMail(MailsPage.MailType.CUSTOMER_WELCOME).
                findLoginToShopLinkAndOpenIt().
                enterPassword(password).
                login();

        Double fetchedProductCashValue = shopWelcomePage.getProductCashValue();
        Double fetchedProductFaceValue = shopWelcomePage.getProductFaceValue();

        assertEqualsDouble(fetchedProductCashValue, calculatedCashValue, "Voucher cash value %s should be equals to depreciated voucher cash value %s");
        assertEqualsDouble(fetchedProductFaceValue, calculatedFaceValue, "Voucher face value %s should be equals to depreciated new price %s");

        shopWelcomePage.logout();

        CustomerDetailsPage customerDetailsPage = login(user).openRecentClaim().toCustomerDetails();
        CustomerDetails customerDetails = customerDetailsPage.getCustomerDetails();

        Double fetchedCustomerCashValue = customerDetails.getCashValue();
        Double fetchedCustomerFaceTooltipValue = customerDetails.getFaceTooltipValue();

        assertEqualsDouble(fetchedCustomerCashValue, calculatedCashValue, "Voucher cash value %s should be equals to depreciated voucher cash value %s");
        assertEqualsDouble(fetchedCustomerFaceTooltipValue, calculatedFaceValue, "Voucher face value %s should be equals to depreciated new price %s");

        ReplacementDialog replacementDialog = customerDetailsPage.
                reopenClaim().
                toCompleteClaimPage().
                fillClaimFormWithPassword(claim, password).
                openReplacementWizard();

        Double fetchedReplacementDialogVoucherFaceValue = replacementDialog.getVoucherFaceValue();
        Double fetchedReplacementDialogItemPriceValue = replacementDialog.getItemPriceValue();

        assertEqualsDouble(fetchedReplacementDialogVoucherFaceValue, calculatedFaceValue, "Voucher face value %s should be equals to depreciated new price %s");
        assertEqualsDouble(fetchedReplacementDialogItemPriceValue, calculatedCashValue, "Voucher cash value %s should be equals to depreciated voucher cash value %s");

        replacementDialog.closeReplacementDialog();
    }

}
