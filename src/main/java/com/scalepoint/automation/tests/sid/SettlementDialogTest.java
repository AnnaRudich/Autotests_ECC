package com.scalepoint.automation.tests.sid;

import com.scalepoint.automation.BaseTest;
import com.scalepoint.automation.pageobjects.dialogs.SettlementDialog;
import com.scalepoint.automation.services.externalapi.VoucherAgreementApi;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.tests.sid.SidCalculator.VoucherValuation;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.ClaimItem;
import com.scalepoint.automation.utils.data.entity.Voucher;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

@RequiredSetting(type=FTSetting.ENABLE_NEW_SETTLEMENT_ITEM_DIALOG)
public class SettlementDialogTest extends BaseTest {

    @Test(dataProvider = "testDataProvider", description = "ECC-3025 It's possible to assign existing category for new voucher and select categories in Add/Edit dialog")
    public void ecc3025_selectVoucherExistingCatAddDialog(User user, Claim claim, Voucher voucher) {
        VoucherAgreementApi.AssignedCategory categoryInfo = new VoucherAgreementApi(user).createVoucher(voucher);

        boolean voucherListed = loginAndCreateClaim(user, claim).
                addManually().
                fillCategory(categoryInfo).
                isVoucherListed(voucher);

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
    @Test(dataProvider = "testDataProvider", description = "ECC-3025 Cash compensation with depreciation field value is (New price minus voucher percent)" +
            " - depreciation percent if voucher selected in Add settlement dialog")
    @RequiredSetting(type = FTSetting.COMPARISON_OF_DISCOUNT_DEPRECATION, enabled = false)
    public void ecc3025_cashCompensationWithAddedDepVoucher(User user, Claim claim, ClaimItem item, Voucher voucher) {

        VoucherAgreementApi.AssignedCategory categoryInfo = new VoucherAgreementApi(user).createVoucher(voucher);

        SettlementDialog settlementDialog = loginAndCreateClaim(user, claim).
                addManually().
                fillDescription(item.getTextFieldSP()).
                fillCustomerDemand(item.getBigCustomDemandPrice()).
                fillNewPrice(item.getNewPriceSP_2400()).
                fillDepreciation(item.getDepAmount1_10()).
                fillCategory(categoryInfo).
                fillVoucher(voucher.getVoucherNameSP());

        VoucherValuation expectedCalculations = SidCalculator.calculateVoucherValuation(item.getNewPriceSP_2400(),
                voucher.getDiscount(),
                item.getDepAmount1_10());

        Double fetchedCashValue = settlementDialog.cashCompensationValue();
        Double calculatedCashValue = expectedCalculations.getCashCompensationWithDepreciation();
        assertEqualsDouble(fetchedCashValue, calculatedCashValue, "Cash compensation incorrect");

        Double fetchedDepreciationValue = settlementDialog.DeprecationValue();
        Double calculatedDepreciationValue = expectedCalculations.getDepreciatedAmount();
        assertEqualsDouble(fetchedDepreciationValue, calculatedDepreciationValue, "Depreciation incorrect");
    }
}
