package com.scalepoint.automation.tests.sid;

import com.scalepoint.automation.pageobjects.dialogs.EditVoucherValuationDialog;
import com.scalepoint.automation.pageobjects.dialogs.SettlementDialog;
import com.scalepoint.automation.services.externalapi.SolrApi;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.shared.ProductInfo;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.utils.Constants;
import com.scalepoint.automation.utils.annotations.Jira;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.ClaimItem;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static com.scalepoint.automation.grid.ValuationGrid.Valuation.NEW_PRICE;
import static com.scalepoint.automation.grid.ValuationGrid.Valuation.VOUCHER;
import static com.scalepoint.automation.services.externalapi.DatabaseApi.PriceConditions.ORDERABLE;
import static com.scalepoint.automation.services.externalapi.DatabaseApi.PriceConditions.PRODUCT_AS_VOUCHER_ONLY;

@Jira("https://jira.scalepoint.com/browse/CHARLIE-539")
@RequiredSetting(type = FTSetting.COMPARISON_OF_DISCOUNT_DEPRECATION, enabled = false)
@RequiredSetting(type = FTSetting.COMBINE_DISCOUNT_DEPRECATION, enabled = false)
@RequiredSetting(type = FTSetting.MAKE_DISCREATIONARY_REASON_MANDATORY, enabled = false)
@RequiredSetting(type = FTSetting.SHOW_DEPRECIATION_AUTOMATICALLY_UPDATED, enabled = false)
@RequiredSetting(type = FTSetting.SHOW_SUGGESTED_DEPRECIATION_SECTION, enabled = false)
public class PostDepreciationCalculationOrderTests extends BaseTest {

    @Test(dataProvider = "testDataProvider",
            description = "ECC-3636 Calculations order of 'Post_depreciation_logic' claims")
    public void ecc3636_manualItem(User user, Claim claim, ClaimItem claimItem) {
        double purchasePrice = 1000.00;
        double replacementPrice = 870.00;
        double depreciationAmount = 130.00;
        int depreciationPercentage = 13;

        loginAndCreateClaim(user, claim)
                .openSidAndFill(sid -> prepareBaseFiller(claimItem, purchasePrice, sid).withDepreciation(depreciationPercentage))
                .valuationGrid()
                .parseValuationRow(NEW_PRICE)
                .makeActive(false)
                .doAssert(row -> row.assertTotalAmountIs(purchasePrice))
                .toSettlementDialog()
                .doAssert(sid -> {
                    sid.assertCashValueIs(replacementPrice);
                    sid.assertDepreciationAmountIs(depreciationAmount);
                })
                .closeSidWithOk()
                .findClaimLine(Constants.TEXT_LINE)
                .doAssert(claimLine -> {
                    claimLine.assertPurchasePriceIs(purchasePrice);
                    claimLine.assertReplacementPriceIs(replacementPrice);
                });
    }

    @Test(dataProvider = "testDataProvider",
            description = "ECC-3636 Calculations order of 'Post_depreciation_logic' claims")
    public void ecc3636_manualLineWithVoucherDefaultDD(User user, Claim claim, ClaimItem claimItem) {

        double purchasePrice = 1000.00;
        double discountedVoucherAmount = 900.00;
        double voucherFaceValue = 870.00;
        double replacementPrice = 783.00;
        double depreciationAmount = 117.00;
        int depreciationPercentage = 13;

        loginAndCreateClaim(user, claim)
                .openSidAndFill(sid -> {
                    prepareBaseFiller(claimItem, purchasePrice, sid)
                            .withVoucher(claimItem.getExistingVoucher1())
                            .withDepreciation(depreciationPercentage);
                })
                .valuationGrid()
                .parseValuationRow(VOUCHER)
                .doAssert(row -> row.assertTotalAmountIs(discountedVoucherAmount))
                .toSettlementDialog()
                .doAssert(sid -> doGeneralAssert(voucherFaceValue, replacementPrice, depreciationAmount, sid))
                .closeSidWithOk()
                .findClaimLine(Constants.TEXT_LINE)
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

        double purchasePrice = 1000.00;
        double discountedVoucherAmount = 960;
        double voucherFaceValue = 928.00;
        double replacementPrice = 835.20;
        double depreciationAmount = 124.80;
        int depreciationPercentage = 13;

        loginAndCreateClaim(user, claim)
                .openSidAndFill(sid -> {
                    prepareBaseFiller(claimItem, purchasePrice, sid)
                            .withVoucher(claimItem.getExistingVoucher1())
                            .withDepreciation(depreciationPercentage);
                })
                .distributeDiscountForVoucherValuation(EditVoucherValuationDialog.DistributeTo.CUSTOMER, 6)
                .valuationGrid()
                .parseValuationRow(VOUCHER)
                .doAssert(row -> row.assertTotalAmountIs(discountedVoucherAmount))
                .toSettlementDialog()
                .doAssert(sid -> doGeneralAssert(voucherFaceValue, replacementPrice, depreciationAmount, sid))
                .closeSidWithOk()
                .findClaimLine(Constants.TEXT_LINE)
                .doAssert(line -> {
                    line.assertPurchasePriceIs(discountedVoucherAmount);
                    line.assertReplacementPriceIs(replacementPrice);
                });
    }

    @Test(dataProvider = "testDataProvider",
            description = "ECC-3638 Calculations order of PRE-depreciation_logic claims")
    public void ecc3636_productWithVoucherDefaultDD(User user, Claim claim) {

        ProductInfo product = SolrApi.findProduct(getXpricesForConditions(ORDERABLE, PRODUCT_AS_VOUCHER_ONLY));

        SettlementDialog settlementDialog = loginAndCreateClaim(user, claim)
                .toTextSearchPage()
                .searchBySku(product.getSku())
                .openSidForProductWithVoucher();

        int voucherPercentage = settlementDialog.getVoucherPercentage();
        double voucherCashValue = settlementDialog
                .valuationGrid()
                .parseValuationRow(VOUCHER).getTotalPrice();

        int depreciationPercentage = 10;
        double depreciationAmount = voucherCashValue * (double) depreciationPercentage / 100;
        double depreciationAmountRoundHalfDown = BigDecimal.valueOf(depreciationAmount).setScale(4, BigDecimal.ROUND_HALF_DOWN).setScale(2, RoundingMode.HALF_DOWN).doubleValue();
        double replacementPrice = voucherCashValue - depreciationAmount;
        double voucherFaceValue = (replacementPrice * 100) / (100 - voucherPercentage);

        settlementDialog.closeSidWithOk()
                .editFirstClaimLine()
                .valuationGrid()
                .parseValuationRow(VOUCHER)
                .makeActive(false)
                .doAssert(row -> row.assertTotalAmountIs(voucherCashValue))
                .toSettlementDialog()
                .setDepreciation(depreciationPercentage)
                .doAssert(sid -> doGeneralAssert(voucherFaceValue, replacementPrice, depreciationAmountRoundHalfDown, sid))
                .closeSidWithOk()
                .parseFirstClaimLine()
                .doAssert(claimLine -> {
                    claimLine.assertPurchasePriceIs(voucherCashValue);
                    claimLine.assertReplacementPriceIs(replacementPrice);
                });

    }

    private SettlementDialog.FormFiller prepareBaseFiller(ClaimItem claimItem, double purchasePrice, SettlementDialog.FormFiller sid) {
        return sid
                .withCategory(claimItem.getCategoryBabyItems())
                .withNewPrice(purchasePrice);
    }
}
