package com.scalepoint.automation.tests.sid;

import com.scalepoint.automation.BaseTest;
import com.scalepoint.automation.domain.ProductInfo;
import com.scalepoint.automation.pageobjects.dialogs.EditDiscountDistributionDialog.DistributeTo;
import com.scalepoint.automation.pageobjects.dialogs.SettlementDialog;
import com.scalepoint.automation.pageobjects.dialogs.SettlementDialog.Valuation;
import com.scalepoint.automation.services.externalapi.SolrApi;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.ClaimItem;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.testng.annotations.Test;

@RequiredSetting(type = FTSetting.COMPARISON_OF_DISCOUNT_DEPRECATION, enabled = false)
@RequiredSetting(type = FTSetting.COMBINE_DISCOUNT_DEPRECATION, enabled = false)
@RequiredSetting(type = FTSetting.MAKE_DISCREATIONARY_REASON_MANDATORY, enabled = false)
@RequiredSetting(type = FTSetting.SHOW_DEPRECIATION_AUTOMATICALLY_UPDATED, enabled = false)
@RequiredSetting(type = FTSetting.SHOW_SUGGESTED_DEPRECIATION_SECTION, enabled = false)
public class PostDepreciationCalculationOrderTests extends BaseTest {

    @Test(dataProvider = "testDataProvider",
            description = "ECC-3636 Calculations order of 'Post_depreciation_logic' claims")
    public void ecc3636_manualItem(User user, Claim claim, ClaimItem claimItem) {
        String description = "test";
        double purchasePrice = 1000.00;
        double replacementPrice = 870.00;
        double depreciationAmount = 130.00;
        int depreciationPercentage = 13;

        loginAndCreateClaim(user, claim)
                .openSidAndFill(sid -> {
                    new SettlementDialog.FormFiller(sid)
                            .withText(description)
                            .withCategory(claimItem.getCategoryBorn())
                            .withSubCategory(claimItem.getSubcategoryBornBabyudstyr())
                            .withNewPrice(purchasePrice)
                            .withDepreciation(depreciationPercentage);
                    return sid;
                })
                .parseValuationRow(Valuation.NEW_PRICE)
                .makeActive()
                .doAssert(row -> row.assertTotalAmountIs(purchasePrice))
                .doAssert(sid -> {
                    sid.assertCashValueIs(replacementPrice);
                    sid.assertDepreciationAmountIs(depreciationAmount);
                })
                .closeSidWithOk()
                .findClaimLine(description)
                .doAssert(claimLine -> {
                    claimLine.assertPurchasePriceIs(purchasePrice);
                    claimLine.assertReplacementPriceIs(replacementPrice);
                });
    }

    @Test(dataProvider = "testDataProvider",
            description = "ECC-3636 Calculations order of 'Post_depreciation_logic' claims")
    public void ecc3636_manualLineWithVoucherDefaultDD(User user, Claim claim, ClaimItem claimItem) {

        String description = "test";
        double purchasePrice = 1000.00;
        double discountedVoucherAmount = 900.00;
        double voucherFaceValue = 870.00;
        double replacementPrice = 783.00;
        double depreciationAmount = 117.00;
        int depreciationPercentage = 13;

        loginAndCreateClaim(user, claim)
                .openSidAndFill(sid -> {
                    new SettlementDialog.FormFiller(sid)
                            .withText(description)
                            .withCategory(claimItem.getCategoryBorn())
                            .withSubCategory(claimItem.getSubcategoryBornBabyudstyr())
                            .withNewPrice(purchasePrice)
                            .withVoucher(claimItem.getExistingVoucher_10())
                            .withDepreciation(depreciationPercentage);
                    return sid;
                })
                .parseValuationRow(Valuation.VOUCHER)
                .doAssert(row -> row.assertTotalAmountIs(discountedVoucherAmount))
                .doAssert(sid -> doGeneralAssert(voucherFaceValue, replacementPrice, depreciationAmount, sid))
                .closeSidWithOk()
                .findClaimLine(description)
                .doAssert(claimLine -> {
                    claimLine.assertPurchasePriceIs(discountedVoucherAmount);
                    claimLine.assertReplacementPriceIs(replacementPrice);

                });

    }

    private void doGeneralAssert(double voucherFaceValue, double replacementPrice, double depreciationAmount, SettlementDialog.Asserts sid) {
        sid.assertCashValueIs(replacementPrice);
        sid.assertDepreciationAmountIs(depreciationAmount);
        sid.assertVoucherFaceValueIs(voucherFaceValue);
        sid.assertVoucherCashValueIs(replacementPrice);
    }


    @Test(dataProvider = "testDataProvider",
            description = "ECC-3636 Calculations order of 'Post_depreciation_logic' claims")
    public void ecc3636_manualLineWithVoucherCustomDD(User user, Claim claim, ClaimItem claimItem) {

        String description = "test";
        double purchasePrice = 1000.00;
        double discountedVoucherAmount = 960;
        double voucherFaceValue = 928.00;
        double replacementPrice = 835.20;
        double depreciationAmount = 124.80;
        int depreciationPercentage = 13;

        loginAndCreateClaim(user, claim)
                .openSidAndFill(sid -> {
                    new SettlementDialog.FormFiller(sid)
                            .withText(description)
                            .withCategory(claimItem.getCategoryBorn())
                            .withSubCategory(claimItem.getSubcategoryBornBabyudstyr())
                            .withNewPrice(purchasePrice)
                            .withVoucher(claimItem.getExistingVoucher_10())
                            .withDepreciation(depreciationPercentage);
                    return sid;
                })
                .distributeDiscountForVoucherValuation(DistributeTo.CUSTOMER, 6)
                .parseValuationRow(Valuation.VOUCHER)
                .doAssert(row -> row.assertTotalAmountIs(discountedVoucherAmount))
                .doAssert(sid -> doGeneralAssert(voucherFaceValue, replacementPrice, depreciationAmount, sid))
                .closeSidWithOk()
                .findClaimLine(description)
                .doAssert(line -> {
                    line.assertPurchasePriceIs(discountedVoucherAmount);
                    line.assertReplacementPriceIs(replacementPrice);
                });
    }

    @Test(dataProvider = "testDataProvider",
            description = "ECC-3638 Calculations order of PRE-depreciation_logic claims")
    public void ecc3636_productWithVoucherDefaultDD(User user, Claim claim) {

        ProductInfo product = SolrApi.findProductAsVoucher();

        SettlementDialog settlementDialog = loginAndCreateClaim(user, claim)
                .toTextSearchPage(product.getModel())
                .openSidForFirstProduct();

        int voucherPercentage = settlementDialog.getVoucherPercentage();
        double voucherCashValue = settlementDialog.parseValuationRow(Valuation.VOUCHER).getTotalPrice();

        int depreciationPercentage = 13;
        double depreciationAmount = voucherCashValue * (double) depreciationPercentage / 100;
        double replacementPrice = voucherCashValue - depreciationAmount;
        double voucherFaceValue = (replacementPrice * 100) / (100 - voucherPercentage);

        settlementDialog.closeSidWithOk()
                .editFirstClaimLine()
                .parseValuationRow(Valuation.VOUCHER)
                .makeActive()
                .doAssert(row -> row.assertTotalAmountIs(voucherCashValue))
                .fillDepreciation(depreciationPercentage)
                .doAssert(sid -> doGeneralAssert(voucherFaceValue, replacementPrice, depreciationAmount, sid))
                .closeSidWithOk()
                .parseFirstClaimLine()
                .doAssert(claimLine -> {
                    claimLine.assertPurchasePriceIs(voucherCashValue);
                    claimLine.assertReplacementPriceIs(replacementPrice);
                });

    }
}
