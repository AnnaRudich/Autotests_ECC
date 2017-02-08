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

import static com.scalepoint.automation.utils.OperationalUtils.assertEqualsDouble;

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
        VoucherValuation expectedCalculation = SidCalculator.calculateVoucherValuation(
                claimItem.getNewPriceSP_2400(),
                voucher.getDiscount(),
                claimItem.getDepAmount1_10());
        Double expectedCashValue = expectedCalculation.getCashCompensationOfVoucher();
        Double expectedFaceValue = Double.valueOf(claimItem.getNewPriceSP_2400());
        Double voucherValue = expectedCalculation.getCashCompensationWithDepreciation();
        String password = "12341234";

        loginAndCreateClaim(user, claim)
                .openAddManuallyDialog()
                .fillDescription(claimItem.getTextFieldSP())
                .fillCustomerDemand(claimItem.getBigCustomDemandPrice())
                .fillNewPrice(claimItem.getNewPriceSP_2400())
                .fillDepreciation(claimItem.getDepAmount1_10())
                .fillCategory(categoryInfo)
                .fillVoucher(voucher.getVoucherNameSP())
                .waitASecond()
                .assertVoucherCashValueIs(expectedCashValue)
                .assertVoucherFaceValueIs(expectedFaceValue)
                .closeSidWithOk()
                .assertFaceValueTooltipIs(expectedFaceValue)
                .toCompleteClaimPage()
                .fillClaimFormWithPassword(claim, password)
                .completeWithEmail()
                .openRecentClaim()
                .toMailsPage()
                .viewMail(MailsPage.MailType.CUSTOMER_WELCOME)
                .findLoginToShopLinkAndOpenIt()
                .enterPassword(password)
                .login()
                .assertProductCashValueIs(expectedCashValue)
                .assertProductFaceValueIs(expectedFaceValue)
                .logout();


        checkReplacementWizard(user, claim, expectedCashValue, expectedFaceValue, voucherValue);
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
        VoucherValuation expectedCalculation = SidCalculator.calculateVoucherValuation(claimItem.getNewPriceSP_2400(), voucher.getDiscount(), claimItem.getDepAmount1_10());

        Double expectedCashValue = expectedCalculation.getCashCompensationWithDepreciation();
        Double expectedFaceValue = expectedCalculation.getCashCompensationOfVoucher();
        String password = "12341234";

        loginAndCreateClaim(user, claim)
                .openAddManuallyDialog()
                .fillDescription(claimItem.getTextFieldSP())
                .fillCustomerDemand(claimItem.getBigCustomDemandPrice())
                .fillNewPrice(claimItem.getNewPriceSP_2400())
                .fillDepreciation(claimItem.getDepAmount1_10())
                .fillCategory(claimItem.getExistingCat1_Born())
                .fillSubCategory(claimItem.getExistingSubCat1_Babyudstyr())
                .fillVoucher(claimItem.getExistingVoucher1())
                .waitASecond()
                .assertVoucherCashValueIs(expectedCashValue)
                .assertVoucherFaceValueIs(expectedFaceValue)
                .closeSidWithOk()
                .assertFaceValueTooltipIs(expectedFaceValue)
                .toCompleteClaimPage()
                .fillClaimFormWithPassword(claim, password)
                .completeWithEmail()
                .openRecentClaim()
                .toMailsPage()
                .viewMail(MailsPage.MailType.CUSTOMER_WELCOME)
                .findLoginToShopLinkAndOpenIt()
                .enterPassword(password)
                .login()
                .assertProductCashValueIs(expectedCashValue)
                .assertProductFaceValueIs(expectedFaceValue)
                .logout();

        checkReplacementWizard(user, claim, expectedCashValue, expectedFaceValue, expectedCashValue);
    }

    private void checkReplacementWizard(User user, Claim claim, Double expectedCashValue, Double expectedFaceValue, Double voucherValue) {
        login(user).openRecentClaim()
                .assertCustomerCashValueIs(voucherValue)
                .assertCustomerFaceValueTooltipIs(expectedFaceValue)
                .reopenClaim()
                .toCompleteClaimPage()
                .fillClaimForm(claim)
                .openReplacementWizard()
                .assertVoucherFaceValueIs(expectedFaceValue)
                .assertItemPriceValueIs(expectedCashValue)
                .closeReplacementDialog();
    }

}
