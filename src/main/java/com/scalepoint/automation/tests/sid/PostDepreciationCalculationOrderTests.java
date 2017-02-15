package com.scalepoint.automation.tests.sid;

import com.scalepoint.automation.BaseTest;
import com.scalepoint.automation.domain.ProductInfo;
import com.scalepoint.automation.pageobjects.dialogs.EditDiscountDistributionDialog;
import com.scalepoint.automation.pageobjects.dialogs.SettlementDialog;
import com.scalepoint.automation.services.externalapi.SolrApi;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.utils.OperationalUtils;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.ClaimItem;
import com.scalepoint.automation.utils.data.entity.TextSearch;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.testng.annotations.Test;

@RequiredSetting(type = FTSetting.COMPARISON_DEPRECATION_DISCOUNT, enabled = false)
@RequiredSetting(type = FTSetting.COMBINE_DISCOUNT_DEPRECATION, enabled = false)
@RequiredSetting(type = FTSetting.MAKE_DISCREATIONARY_REASON_MANDATORY, enabled = false)
@RequiredSetting(type = FTSetting.SHOW_DEPRECIATION_AUTOMATICALLY_UPDATED, enabled = false)
@RequiredSetting(type = FTSetting.SHOW_SUGGESTED_DEPRECIATION_SECTION, enabled = false)
public class PostDepreciationCalculationOrderTests extends BaseTest {

    @Test(dataProvider = "testDataProvider",
            description = "ECC-3636 Calculations order of 'Post_depreciation_logic' claims")
    public void ecc3636_manualItem(User user, Claim claim, ClaimItem claimItem)  {
        String lineDescription = "test";
        double purchasePrice = 1000.00;
        double replacementPrice = 870.00;
        double depreciationAmount = 130.00;
        int depreciationPercentage = 13;

        loginAndCreateClaim(user, claim)
                .openAddManuallyDialog()
                .fillBaseData(lineDescription, claimItem.getExistingCat1_Born(), claimItem.getExistingSubCat1_Babyudstyr(), purchasePrice)
                .fillDepreciation(depreciationPercentage)
                .selectValuation(SettlementDialog.Valuation.NEW_PRICE)
                .assertAmountOfValuationEqualTo(purchasePrice, SettlementDialog.Valuation.NEW_PRICE)
                .assertCashValueIs(replacementPrice)
                .assertDepreciationAmountIs(depreciationAmount)
                .closeSidWithOk()
                .findClaimLine(lineDescription)
                .assertPurchasePriceIs(purchasePrice)
                .assertReplacementPriceIs(replacementPrice);
    }

    @Test(dataProvider = "testDataProvider",
            description = "ECC-3636 Calculations order of 'Post_depreciation_logic' claims")
    public void ecc3636_manualLineWithVoucherDefaultDD(User user, Claim claim, ClaimItem claimItem) {

        String lineDescription = "test";
        double purchasePrice = 1000.00;
        double discountedVoucherAmount = 900.00;
        double voucherFaceValue = 870.00;
        double replacementPrice = 783.00;
        double depreciationAmount = 117.00;
        int depreciationPercentage = 13;

        loginAndCreateClaim(user, claim)
                .openAddManuallyDialog()
                .fillBaseData(lineDescription, claimItem.getExistingCat1_Born(), claimItem.getExistingSubCat1_Babyudstyr(), purchasePrice)
                .fillVoucher(claimItem.getExistingVoucher1())
                .fillDepreciation(depreciationPercentage)
                .assertAmountOfValuationEqualTo(discountedVoucherAmount, SettlementDialog.Valuation.VOUCHER)
                .assertAmountOfValuationEqualTo(purchasePrice, SettlementDialog.Valuation.NEW_PRICE)
                .assertCashValueIs(replacementPrice)
                .assertDepreciationAmountIs(depreciationAmount)
                .assertVoucherFaceValueIs(voucherFaceValue)
                .assertVoucherCashValueIs(replacementPrice)
                .closeSidWithOk()
                .findClaimLine(lineDescription)
                .assertPurchasePriceIs(discountedVoucherAmount)
                .assertReplacementPriceIs(replacementPrice);
    }


    @Test(dataProvider = "testDataProvider",
            description = "ECC-3636 Calculations order of 'Post_depreciation_logic' claims")
    public void ecc3636_manualLineWithVoucherCustomDD(User user, Claim claim, ClaimItem claimItem) {

        String lineDescription = "test";
        double purchasePrice = 1000.00;
        double discountedVoucherAmount = 960;
        double voucherFaceValue = 928.00;
        double replacementPrice = 835.20;
        double depreciationAmount = 124.80;
        int depreciationPercentage = 13;

        loginAndCreateClaim(user, claim)
                .openAddManuallyDialog()
                .fillBaseData(lineDescription, claimItem.getExistingCat1_Born(), claimItem.getExistingSubCat1_Babyudstyr(), purchasePrice)
                .fillVoucher(claimItem.getExistingVoucher1())
                .fillDepreciation(depreciationPercentage)
                .openEditDiscountDistributionForVoucher()
                .updateCustomerPercentage(6)
                .save()
                .assertAmountOfValuationEqualTo(discountedVoucherAmount, SettlementDialog.Valuation.VOUCHER)
                .assertAmountOfValuationEqualTo(purchasePrice, SettlementDialog.Valuation.NEW_PRICE)
                .assertCashValueIs(replacementPrice)
                .assertDepreciationAmountIs(depreciationAmount)
                .assertVoucherFaceValueIs(voucherFaceValue)
                .assertVoucherCashValueIs(replacementPrice)
                .closeSidWithOk()
                .findClaimLine(lineDescription)
                .assertPurchasePriceIs(discountedVoucherAmount)
                .assertReplacementPriceIs(replacementPrice);
    }

    @Test(dataProvider = "testDataProvider",
            description = "ECC-3638 Calculations order of PRE-depreciation_logic claims")
    public void ecc3636_productWithVoucherDefaultDD(User user, Claim claim, ClaimItem claimItem, TextSearch textSearch) {

        ProductInfo product = SolrApi.findBaOProduct();

        EditDiscountDistributionDialog editDiscountDistributionDialog = loginAndCreateClaim(user, claim)
                .toTextSearchPage(product.getModel())
                .sortOrderableFirst()
                .openSidForFirstProduct()
                .openEditDiscountDistributionForVoucher();

        int voucherPercentage = editDiscountDistributionDialog.getVoucherPercentage();

        SettlementDialog settlementDialog = editDiscountDistributionDialog.save();
        String valuationColumnValue = settlementDialog.getValuationColumnValue(SettlementDialog.Valuation.VOUCHER, SettlementDialog.ValuationGridColumn.AMOUNT_OF_VALUATION);
        logger.info("Voucher valuation: {}", valuationColumnValue);

        int depreciationPercentage = 13;
        double voucherCashValue = OperationalUtils.toNumber(valuationColumnValue);
        double depreciationAmount = product.getInvoice() * (double)depreciationPercentage/100;
        double replacementPrice = voucherCashValue - depreciationAmount;
        double voucherFaceValue = (replacementPrice*100)/(100-voucherPercentage);

        settlementDialog.closeSidWithOk()
                .findFirstClaimLine()
                .editLine()
                .selectValuation(SettlementDialog.Valuation.VOUCHER)
                .fillDepreciation(depreciationPercentage)
                .assertAmountOfValuationEqualTo(voucherCashValue, SettlementDialog.Valuation.VOUCHER)
                .assertCashValueIs(replacementPrice)
                .assertDepreciationAmountIs(depreciationAmount)
                .assertVoucherFaceValueIs(voucherFaceValue)
                .assertVoucherCashValueIs(replacementPrice)
                .closeSidWithOk()
                .findFirstClaimLine()
                .assertPurchasePriceIs(voucherCashValue)
                .assertReplacementPriceIs(replacementPrice);
    }
}
