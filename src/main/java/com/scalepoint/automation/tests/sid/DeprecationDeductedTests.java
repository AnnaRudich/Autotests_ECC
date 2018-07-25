package com.scalepoint.automation.tests.sid;

import com.scalepoint.automation.pageobjects.pages.MailsPage;
import com.scalepoint.automation.services.externalapi.VoucherAgreementApi;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.tests.sid.SidCalculator.VoucherValuationWithDepreciation;
import com.scalepoint.automation.utils.Constants;
import com.scalepoint.automation.utils.annotations.Bug;
import com.scalepoint.automation.utils.annotations.Jira;
import com.scalepoint.automation.utils.annotations.RunOn;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.ClaimItem;
import com.scalepoint.automation.utils.data.entity.Voucher;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.driver.DriverType;
import org.testng.annotations.Test;

@Jira("https://jira.scalepoint.com/browse/CHARLIE-531")
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
  @RequiredSetting(type = FTSetting.USE_NEW_REPLACEMENT_DIALOG)
  @RequiredSetting(type = FTSetting.DISPLAY_VOUCHER_VALUE_WITH_DEPRECATION_DEDUCTION, enabled = false)
  public void ecc3288_1_displayVoucherValueWithDeprecationDeductedOFF(User user, Claim claim, Voucher voucher) {

    VoucherAgreementApi.AssignedCategory categoryInfo = new VoucherAgreementApi(user).createVoucher(voucher);
    VoucherValuationWithDepreciation expectedCalculation = SidCalculator.calculateVoucherValuation(Constants.PRICE_2400, Constants.VOUCHER_DISCOUNT_10, Constants.DEPRECIATION_10);

    Double expectedCashValue = expectedCalculation.getCashCompensationOfVoucher();
    Double expectedFaceValue = Constants.PRICE_2400;
    Double voucherValue = expectedCalculation.getCashCompensationWithDepreciation();

    String category = categoryInfo.getCategory();
    String subCategory = categoryInfo.getSubCategory();
    String voucherName = voucher.getVoucherGeneratedName();

    verify(user, claim, expectedCashValue, expectedFaceValue, voucherValue, category, subCategory, voucherName);
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
  @Bug(bug="CHARLIE-416")
  @RequiredSetting(type = FTSetting.USE_NEW_REPLACEMENT_DIALOG)
  @Test(dataProvider = "testDataProvider", description = "ECC-3288 Display voucher value with depreciation deducted (on)")
  @RequiredSetting(type = FTSetting.DISPLAY_VOUCHER_VALUE_WITH_DEPRECATION_DEDUCTION)
  public void ecc3288_2_displayVoucherValueWithDeprecationDeductedON(User user, Claim claim, ClaimItem claimItem) {
    VoucherValuationWithDepreciation expectedCalculation = SidCalculator.calculateVoucherValuation(Constants.PRICE_2400, Constants.VOUCHER_DISCOUNT_10, Constants.DEPRECIATION_10);

    Double expectedCashValue = expectedCalculation.getCashCompensationWithDepreciation();
    Double expectedFaceValue = expectedCalculation.getCashCompensationOfVoucher();

    String category = claimItem.getCategoryGroupBorn();
    String subcategory = claimItem.getCategoryBornBabyudstyr();
    String voucherName = claimItem.getExistingVoucher_10();

    verify(user, claim, expectedCashValue, expectedFaceValue, expectedCashValue, category, subcategory, voucherName);
  }

  private void verify(User user, Claim claim, Double expectedVoucherCashValue, Double expectedVoucherFaceValue, Double customerCashValue,
                      String category, String subCategory, String voucherNameSP) {
    loginAndCreateClaim(user, claim)
            .openSidAndFill(sidForm -> {
              sidForm.withCustomerDemandPrice(Constants.PRICE_100_000)
                      .withNewPrice(Constants.PRICE_2400)
                      .withDepreciation(Constants.DEPRECIATION_10)
                      .withCategory(category)
                      .withSubCategory(subCategory)
                      .withVoucher(voucherNameSP);
            })
            .closeSidWithOk()
            .doAssert(page -> page.assertFaceValueTooltipIs(expectedVoucherFaceValue))
            .toCompleteClaimPage()
            .fillClaimFormWithPassword(claim)
            .completeWithEmail()
            .openRecentClaim()
            .toMailsPage()
            .viewMail(MailsPage.MailType.CUSTOMER_WELCOME)
            .findLoginToShopLinkAndOpenIt()
            .enterPassword(Constants.PASSWORD)
            .login()
            .doAssert(shopWelcomePage -> {
              shopWelcomePage.assertProductCashValueIs(expectedVoucherCashValue);
              shopWelcomePage.assertProductFaceValueIs(expectedVoucherFaceValue);
            })
            .logout();

    login(user).openRecentClaim()
            .doAssert(customerDetailsPage -> {
              customerDetailsPage.assertCustomerCashValueIs(customerCashValue);
              customerDetailsPage.assertCustomerFaceValueTooltipIs(expectedVoucherFaceValue);
            })
            .reopenClaim()
            .toCompleteClaimPage()
            .fillClaimForm(claim)
            .openReplacementWizard()
            .doAssert(replacementDialog -> {
              replacementDialog.assertVoucherFaceValueIs(expectedVoucherFaceValue);
              replacementDialog.assertItemPriceValueIs(expectedVoucherCashValue);
            })
            .closeReplacementDialog();
  }
}
