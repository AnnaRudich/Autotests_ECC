package com.scalepoint.automation.tests.sid;

import com.scalepoint.automation.BaseTest;
import com.scalepoint.automation.pageobjects.dialogs.EditVoucherValuationDialog;
import com.scalepoint.automation.pageobjects.dialogs.SettlementDialog;
import com.scalepoint.automation.pageobjects.dialogs.VoucherTermsAndConditionsDialog;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.services.externalapi.VoucherAgreementApi;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.services.usersmanagement.CompanyCode;
import com.scalepoint.automation.utils.OperationalUtils;
import com.scalepoint.automation.utils.annotations.UserCompany;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.ClaimItem;
import com.scalepoint.automation.utils.data.entity.Voucher;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.listeners.InvokedMethodListener;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

@RequiredSetting(type=FTSetting.ENABLE_NEW_SETTLEMENT_ITEM_DIALOG)
@RequiredSetting(type=FTSetting.SHOW_COMPACT_SETTLEMENT_ITEM_DIALOG, enabled = false)
public class SettlementDialogShowVoucherDetails extends BaseTest {

    /**
     * WHEN: Create claim
     * AND: Add shared voucher V1 to settlement
     * AND: Open SID
     * AND: See tabs Brands and Tags
     * AND: Click View voucher details
     * THEN: Brands and Tags contain correct info (entered in SM Admin)
     * <p>
     * WHEN: Create claim
     * AND: Open SID
     * AND: Open Terms and conditions dialog
     * THEN: Verify that terms and conditions are visible
     * WHEN: Close dialog
     * AND: Click View voucher details
     * AND: Open Terms and conditions dialog
     * THEN: Verify that terms and conditions are visible and equals the ones previous
     * <p>
     * GIVEN: FT Show compact settlement item dialog is ON
     * WHEN: Create claim
     * AND: Open SID
     * THEN: Verify that BRAND AND TAGS are visible in compact mode
     * <p>
     * WHEN: Create claim
     * AND: Add shared voucher V1 to settlement
     * AND: Open SID
     * AND: Change discount distribution
     * THEN: New discount distribution is displayed
     * <p>
     * WHEN: Create claim
     * AND: Add shared voucher V1 to settlement
     * AND: Open SID
     * AND: Select again another voucher
     * THEN: Check that another voucher is displayed
     * <p>
     * WHEN: Create claim
     * AND: Open SID
     * AND: Open Terms and conditions dialog
     * THEN: Verify that terms and conditions are visible
     * WHEN: Close dialog
     * AND: Click View voucher details
     * AND: Open Terms and conditions dialog
     * THEN: Verify that terms and conditions are visible and equals the ones previous
     * <p>
     * GIVEN: FT Show compact settlement item dialog is ON
     * WHEN: Create claim
     * AND: Open SID
     * THEN: Verify that BRAND AND TAGS are visible in compact mode
     * <p>
     * WHEN: Create claim
     * AND: Add shared voucher V1 to settlement
     * AND: Open SID
     * AND: Change discount distribution
     * THEN: New discount distribution is displayed
     * <p>
     * WHEN: Create claim
     * AND: Add shared voucher V1 to settlement
     * AND: Open SID
     * AND: Select again another voucher
     * THEN: Check that another voucher is displayed
     */
    @Test(dataProvider = "testDataProvider", description = "ECC-5519 Verify shared voucher")
    public void ecc5519_1_sharedVoucherAndTagsBrandInSID(User user, Claim claim, ClaimItem claimItem, Voucher voucher) {
        checkBrandsAndTags(user, claim, claimItem, voucher);
    }

    /**
     * WHEN: Create claim
     * AND: Add exclusive voucher V1 to settlement
     * AND: Open SID
     * AND: See tabs Brands and Tags
     * AND: Click View voucher details
     * THEN: Brands and Tags contain correct info (entered in SM Admin)
     */
    @Test(dataProvider = "testDataProvider", description = "ECC-5519 Verify exclusive voucher")
    public void ecc5519_1_exclusiveVoucherAndTagsBrandInSID(@UserCompany(CompanyCode.ALKA) User user, Claim claim, ClaimItem claimItem, Voucher voucher) {
        checkBrandsAndTags(user, claim, claimItem, voucher);
    }

    private void checkBrandsAndTags(User user, Claim claim, ClaimItem claimItem, Voucher voucher) {
        VoucherAgreementApi.AssignedCategory assignedCategory = new VoucherAgreementApi(user).createVoucher(voucher);

        EditVoucherValuationDialog editVoucherValuationDialog = loginAndCreateClaim(user, claim).addManually().
                fillDescription(claimItem.getTextFieldSP()).
                fillCustomerDemand(claimItem.getBigCustomDemandPrice()).
                fillNewPrice(claimItem.getNewPriceSP_2400()).
                fillCategory(assignedCategory).
                fillDepreciation(claimItem.getDepAmount1_10()).
                fillVoucher(voucher.getVoucherNameSP()).
                openVoucherValuationCard();
        assertEquals(editVoucherValuationDialog.getBrands(), voucher.getBrandsText(), "Wrong Brand is displayed");
        assertEquals(editVoucherValuationDialog.getTags(), voucher.getTagsText(), "Wrong Tags are displayed");
    }

    /**
     * WHEN: Create claim
     * AND: Open SID
     * AND: Open Terms and conditions dialog
     * THEN: Verify that terms and conditions are visible
     * WHEN: Close dialog
     * AND: Click View voucher details
     * AND: Open Terms and conditions dialog
     * THEN: Verify that terms and conditions are visible and equals the ones previous
     */
    @Test(dataProvider = "testDataProvider", description = "ECC-5519 Verify 'Trade and conditions' buttons and window")
    public void ecc5519_2_voucherTradeTermsAndConditions(User user, Claim claim, ClaimItem claimItem) {
        SettlementDialog settlementDialog = loginAndCreateClaim(user, claim).
                addManually().
                fillDescription(claimItem.getTextFieldSP()).
                fillCustomerDemand(claimItem.getBigCustomDemandPrice()).
                fillNewPrice(claimItem.getNewPriceSP_2400()).
                fillDepreciation(claimItem.getDepAmount1_10()).
                fillCategory(claimItem.getExistingCat1_Born()).fillSubCategory(claimItem.getExistingSubCat1_Babyudstyr()).
                fillVoucher(claimItem.getExistingVoucher2());

        VoucherTermsAndConditionsDialog voucherTermsAndConditionsDialog = settlementDialog.openVoucherTermAndConditions();
        String termsAndConditions = voucherTermsAndConditionsDialog.getTermsAndConditions();

        assertEquals(voucherTermsAndConditionsDialog.getTermsAndConditions(), termsAndConditions, "The Terms And Conditions are different");
        voucherTermsAndConditionsDialog.close();
    }

    /**
     * GIVEN: FT Show compact settlement item dialog is ON
     * WHEN: Create claim
     * AND: Open SID
     * THEN: Verify that BRAND AND TAGS are visible in compact modev
     * <p>
     * WHEN: Create claim
     * AND: Add shared voucher V1 to settlement
     * AND: Open SID
     * AND: Change discount distribution
     * THEN: New discount distribution is displayed
     * <p>
     * WHEN: Create claim
     * AND: Add shared voucher V1 to settlement
     * AND: Open SID
     * AND: Select again another voucher
     * THEN: Check that another voucher is displayed
     */
    @Test(dataProvider = "testDataProvider", description = "ECC-5519 Verify that Brands and Tags are visible in compact mode")
    @RequiredSetting(type = FTSetting.SHOW_COMPACT_SETTLEMENT_ITEM_DIALOG)
    public void ecc5519_3_voucherBrandTagInSIDCompactMode(User user, Claim claim, ClaimItem claimItem, Voucher voucher) {
        SettlementPage settlementPage = loginAndCreateClaim(user, claim);
        SettlementDialog settlementDialog = settlementPage.
                addManually().
                fillDescription(claimItem.getTextFieldSP()).
                fillCustomerDemand(claimItem.getBigCustomDemandPrice()).
                fillNewPrice(claimItem.getNewPriceSP_2400()).
                fillDepreciation(claimItem.getDepAmount1_10()).
                fillCategory(claimItem.getExistingCat1_Born()).
                fillSubCategory(claimItem.getExistingSubCat1_Babyudstyr()).
                fillVoucher(claimItem.getExistingVoucher2());

        assertEquals(settlementDialog.getBrandText(), voucher.getBrandLink(), "Wrong Brand is Displayed");
        settlementDialog.cancel();
    }


    /**
     * WHEN: Create claim
     * AND: Add shared voucher V1 to settlement
     * AND: Open SID
     * AND: Change discount distribution
     * THEN: New discount distribution is displayed
     * <p>
     * WHEN: Create claim
     * AND: Add shared voucher V1 to settlement
     * AND: Open SID
     * AND: Select again another voucher
     * THEN: Check that another voucher is displayed
     */
    @Test(dataProvider = "testDataProvider", description = "ECC-5519 Verify that discount distribution can be changed")
    public void ecc5519_4_discountDistributionIsChanged(User user, Claim claim, ClaimItem claimItem) {
        SettlementDialog settlementDialog = loginAndCreateClaim(user, claim).
                addManually().
                fillDescription(claimItem.getTextFieldSP()).
                fillCustomerDemand(claimItem.getBigCustomDemandPrice()).
                fillNewPrice(claimItem.getNewPriceSP_2400()).
                fillCategory(claimItem.getExistingCat1_Born()).
                fillSubCategory(claimItem.getExistingSubCat1_Babyudstyr()).
                fillVoucher(claimItem.getExistingVoucher2());

        EditVoucherValuationDialog editVoucherValuationDialog = settlementDialog.
                openVoucherValuationCard().
                discountDistribution("5");

        String voucherCashValue = editVoucherValuationDialog.getVoucherCashValue();
        editVoucherValuationDialog.ok();

        assertEquals(settlementDialog.voucherCashValueFieldText(), OperationalUtils.getDoubleValue(voucherCashValue));
    }


    /**
     * WHEN: Create claim
     * AND: Add shared voucher V1 to settlement
     * AND: Open SID
     * AND: Select again another voucher
     * THEN: Check that another voucher is displayed
     */
    @Test(dataProvider = "testDataProvider", description = "ECC-5519 Verify that user can re-select voucher")
    public void ecc5519_5_reselectVoucherInSID(User user, Claim claim, ClaimItem claimItem) {
        SettlementDialog settlementDialog = loginAndCreateClaim(user, claim).
                addManually().
                fillDescription(claimItem.getTextFieldSP()).
                fillCustomerDemand(claimItem.getBigCustomDemandPrice()).
                fillNewPrice(claimItem.getNewPriceSP_2400()).
                fillDepreciation(claimItem.getDepAmount1_10()).
                fillCategory(claimItem.getExistingCat1_Born()).
                fillSubCategory(claimItem.getExistingSubCat1_Babyudstyr()).
                fillVoucher(claimItem.getExistingVoucher2());

        assertEquals(settlementDialog.getBrandText(), claimItem.getExistingVoucher2(), "Wrong voucher is displayed");

        String brandText = settlementDialog.fillVoucher(claimItem.getExistingVoucher4()).getBrandText();
        assertEquals(brandText, claimItem.getBrandLinkVoucher4(), "Wrong voucher is displayed");
    }

}
