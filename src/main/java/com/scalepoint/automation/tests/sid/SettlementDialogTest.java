package com.scalepoint.automation.tests.sid;

import com.scalepoint.automation.BaseTest;
import com.scalepoint.automation.pageobjects.dialogs.SettlementDialog;
import com.scalepoint.automation.pageobjects.pages.LoginPage;
import com.scalepoint.automation.services.externalapi.VoucherAgreementApi;
import com.scalepoint.automation.services.externalapi.ftemplates.FT;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.ClaimItem;
import com.scalepoint.automation.utils.data.entity.Voucher;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class SettlementDialogTest extends BaseTest {


    @Test(description = "ECC-3025 It's possible to assign existing category for new voucher and select categories in Add/Edit dialog", dataProvider = "testDataProvider")
    public void ecc3025_selectVoucherExistingCatAddDialog(User user, Claim claim, Voucher voucher) throws Exception {
        enableNewSid(user);
        VoucherAgreementApi.AssignedCategory categoryInfo = new VoucherAgreementApi(user).createVoucher(voucher);

        boolean voucherListed = loginAndCreateClaim(user, claim).
                addManually().
                fillCategory(categoryInfo).isVoucherListed(voucher);

        assertTrue(voucherListed);
    }


    /**
     * GIVEN: Existing category C1 with existing group G1 and mapped to G1-C1 voucher V1
     * WHEN: User selects C1, G1 and V1 in Settlement dialog
     * WHEN: User adds new price P1
     * WHEN: User adds depreciation D1
     * THAN: Cash compensation is P1 - V1 discount - D1
     * THAN: Depreciation is D1 amount of Cash Compensation
     */
    @Test(description = "ECC-3025 Cash compensation with depreciation field value is (New price minus voucher percent)" +
            " - depreciation percent if voucher selected in Add settlement dialog", dataProvider = "testDataProvider")
    public void ecc3025_cashCompensationWithAddedDepVoucher(User user, Claim claim, ClaimItem item, Voucher voucher) {
        enableNewSid(user);

        updateFT(user, LoginPage.class, FT.disable(FTSetting.COMPARISON_DISCOUNT_DEPRECATION));

        VoucherAgreementApi.AssignedCategory categoryInfo = new VoucherAgreementApi(user).createVoucher(voucher);

        SettlementDialog settlementDialog = loginAndCreateClaim(user, claim).addManually()
                .fillDescription(item.getTextFieldSP())
                .fillCustomerDemand(item.getBigCustomDemandPrice())
                .fillNewPrice(item.getNewPriceSP())
                .fillDepreciation(item.getDepAmount1())
                .fillCategory(categoryInfo)
                .fillVoucher(item.getExistingVoucher1());

        String fetchedCashValue = toString(settlementDialog.cashCompensationValue());
        String calculatedCashValue = toString(calculateCashCompensation(item, voucher) - calculateDepreciation(item, voucher));
        assertEquals(fetchedCashValue, calculatedCashValue, "Cash compensation incorrect");

        String fetchedDepreciationValue = toString(settlementDialog.DeprecationValue());
        String calculatedDepreciationValue = toString(calculateDepreciation(item, voucher));
        assertEquals(fetchedDepreciationValue, calculatedDepreciationValue, "Depreciation incorrect");
    }

    private String toString(Double value) {
        return String.format("%.2f", value);
    }

    private Double calculateCashCompensation(ClaimItem claimItem, Voucher voucher) {
        return Double.valueOf(claimItem.getNewPriceSP()) - (Double.valueOf(claimItem.getNewPriceSP()) * Double.valueOf(voucher.getDiscount())) / 100;
    }

    private Double calculateDepreciation(ClaimItem claimItem, Voucher voucher) {
        return calculateCashCompensation(claimItem, voucher) * Double.valueOf(claimItem.getDepAmount1()) / 100;
    }

}
