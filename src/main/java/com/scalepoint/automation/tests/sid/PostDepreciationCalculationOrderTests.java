package com.scalepoint.automation.tests.sid;

import com.scalepoint.automation.BaseTest;
import com.scalepoint.automation.pageobjects.dialogs.SettlementDialog;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.annotations.page.RequiredParameters;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.ClaimItem;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.testng.Assert;
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

        loginAndCreateClaim(user, claim)
                .openAddManuallyDialog()
                .fillBaseData(lineDescription, claimItem.getExistingCat1_Born(), claimItem.getExistingSubCat1_Babyudstyr(), purchasePrice)
                .fillDepreciation(13)
                .selectValuation(SettlementDialog.Valuation.NEW_PRICE)
                .assertAmountOfValuationEqualTo(purchasePrice, SettlementDialog.Valuation.NEW_PRICE)
                .assertCashValueIs(replacementPrice)
                .assertDepreciationAmountIs(130.00)
                .closeSidWithOk()
                .findClaimLine(lineDescription)
                .assertPurchasePriceIs(1000.00)
                .assertReplacementPriceIs(replacementPrice);

     /*

        //SETTLEMENT:
        Assert.assertTrue(Math.abs(pages.ME_SETTLEMENT.getClTotalNewPrice(claimItem.getTextFieldSP()) - 1000.0) < 0.001,
                "Get line Total New PRice  " + pages.ME_SETTLEMENT.getClTotalNewPrice(claimItem.getTextFieldSP()) + " is equal to depreciated New Price " + 1000.0);

        Assert.assertTrue(Math.abs(pages.ME_SETTLEMENT.getClValue(claimItem.getTextFieldSP()) - 870.0) < 0.001,
                "Get line Value  " + pages.ME_SETTLEMENT.getClValue(claimItem.getTextFieldSP()) + " is equal to depreciated New Price " + 870.0);
        flows.CLAIM.logout();*/
    }

    /*@Test(description = "ECC-3636 Calculations order of 'Post_depreciation_logic' claims", groups={"src", "sid"})
    public void ecc3636_manualLineWithVoucherDefaultDD() throws Exception {
        pages.LOGIN.loginToME(credentials.getMyAdmin3Login(), credentials.getMyAdmin3Pass());
        flows.CLAIM.createClient(client);
        pages.ME_SETTLEMENT.openAddEditDialog();
        settlementDialog.fillCategoriesVoucherPrice(claimItem.getTextFieldSP(), claimItem.getExistingCat1(),
                claimItem.getExistingSubCat1(), "1000", claimItem.getExistingVoucher1());
        settlementDialog.addDepreciationAmount("13");
        Assert.assertTrue(Math.abs(settlementDialog.getSelectedValuationValue() - 900.0) < 0.001,
                "Selected valuation  " + settlementDialog.getSelectedValuationValue() + " is equal to discounted New Price " + 900.0);
        Assert.assertTrue(Math.abs(settlementDialog.getGreedValueByNumberFromTheEnd(0, 1) - 1000.0) < 0.001,
                "New Price valuation  " + settlementDialog.getGreedValueByNumberFromTheEnd(0, 1) + " is equal to New Price " + 1000.0);
        Assert.assertTrue(Math.abs(settlementDialog.getCashCompensationFieldValue() - 783.0) < 0.001,
                "Cash compensation  " + settlementDialog.getCashCompensationFieldValue() + " is equal to depreciated New Price " + 783.0);
        Assert.assertTrue(Math.abs(settlementDialog.getDepreciationBelowCashFieldValue() - 117.0) < 0.001,
                "Depreciated cash  " + settlementDialog.getDepreciationBelowCashFieldValue() + " is equal to post depreciation value " + 117.0);
        //face, cash value
        Assert.assertTrue(Math.abs(settlementDialog.getVoucherCashValueFieldText()-900.0)<0.001,
                "Cash value "+settlementDialog.getVoucherCashValueFieldText()+" equals to discounted new price " + 900.0);
        Assert.assertTrue(Math.abs(settlementDialog.getVoucherFaceValueFieldText()-1000.0)<0.001,
                "Face value "+settlementDialog.getVoucherFaceValueFieldText()+" equals to new price "+1000.0);
        settlementDialog.saveAndCloseAddEditSetDialog();

        //SETTLEMENT:
        Assert.assertTrue(Math.abs(pages.ME_SETTLEMENT.getClTotalNewPrice(claimItem.getTextFieldSP()) - 900.0) < 0.001,
                "Get line Total New PRice  " + pages.ME_SETTLEMENT.getClTotalNewPrice(claimItem.getTextFieldSP()) + " is equal to depreciated New Price " + 900.0);
        Assert.assertTrue(Math.abs(pages.ME_SETTLEMENT.getClValue(claimItem.getTextFieldSP()) - 783.0) < 0.001,
                "Get line Value  " + pages.ME_SETTLEMENT.getClValue(claimItem.getTextFieldSP()) + " is equal to depreciated New Price " + 783.0);
        flows.CLAIM.logout();
    }


    @Test(description = "ECC-3636 Calculations order of 'Post_depreciation_logic' claims", groups={"src", "sid"})
    public void ecc3636_manualLineWithVoucherCustomDD() throws Exception {
        pages.LOGIN.loginToME(credentials.getMyAdmin3Login(), credentials.getMyAdmin3Pass());
        flows.CLAIM.createClient(client);
        pages.ME_SETTLEMENT.openAddEditDialog();
        settlementDialog.fillCategoriesVoucherPrice(claimItem.getTextFieldSP(), claimItem.getExistingCat1(),
                claimItem.getExistingSubCat1(), "1000", claimItem.getExistingVoucher1());
        settlementDialog.addDepreciationAmount("13");
        flows.CLAIM.setCustomDiscountDistribution("6");


        Assert.assertTrue(Math.abs(settlementDialog.getSelectedValuationValue() - 960.0) < 0.001,
                "Selected valuation  " + settlementDialog.getSelectedValuationValue() + " is equal to  IC discounted New Price " + 960.0);
        Assert.assertTrue(Math.abs(settlementDialog.getGreedValueByNumberFromTheEnd(0, 1)-1000.0) < 0.001,
                "New Price valuation  " + settlementDialog.getGreedValueByNumberFromTheEnd(0, 1) + " is equal to New Price " + 1000.0);
        Assert.assertTrue(Math.abs(settlementDialog.getCashCompensationFieldValue() - 835.2) < 0.001,
                "Cash compensation  " + settlementDialog.getCashCompensationFieldValue() + " is equal to depreciated New Price " + 835.2);
        Assert.assertTrue(Math.abs(settlementDialog.getDepreciationBelowCashFieldValue() - 124.8) < 0.001,
                "Depreciated cash  " + settlementDialog.getDepreciationBelowCashFieldValue() + " is equal to post depreciation value " + 117.0);
        //face, cash value
        Assert.assertTrue(Math.abs(settlementDialog.getVoucherCashValueFieldText()-960.0)<0.001,
                "Cash value "+settlementDialog.getVoucherCashValueFieldText()+" equals to discounted new price " + 960.0);
        Assert.assertTrue(Math.abs(settlementDialog.getVoucherFaceValueFieldText()-1066.67)<0.001,
                "Face value "+settlementDialog.getVoucherFaceValueFieldText()+" equals to new price "+1066.67);

        settlementDialog.saveAndCloseAddEditSetDialog();

        //SETTLEMENT:
        Assert.assertTrue(Math.abs(pages.ME_SETTLEMENT.getClTotalNewPrice(claimItem.getTextFieldSP()) - 960.0) < 0.001,
                "Get line Total New PRice  " + pages.ME_SETTLEMENT.getClTotalNewPrice(claimItem.getTextFieldSP()) + " is equal to depreciated New Price " + 960.0);
        Assert.assertTrue(Math.abs(pages.ME_SETTLEMENT.getClValue(claimItem.getTextFieldSP()) - 835.2) < 0.001,
                "Get line Value  " + pages.ME_SETTLEMENT.getClValue(claimItem.getTextFieldSP()) + " is equal to depreciated New Price " + 835.2);

        flows.CLAIM.logout();
    }


    @Test(description = "ECC-3638 Calculations order of PRE-depreciation_logic claims", groups={"src", "sid"})
    public void ecc3636_productWithVoucherDefaultDD() throws Exception {
        pages.LOGIN.loginToME(credentials.getMyAdmin3Login(), credentials.getMyAdmin3Pass());
        flows.CLAIM.createClient(client);
        flows.CLAIM.addProductFromCatalog(textSearch.getProductWithOnlyVoucherHandling());
        pages.ME_SETTLEMENT.openItemForEditing(textSearch.getProductWithOnlyVoucherHandling());
        settlementDialog.selectVoucherValuation();
        settlementDialog.addDepreciationAmount("13");

        //SID:
        Double voucherValuation_2ndColumn = settlementDialog.getValuationGreedSecondColumnValue(settlementDialog.getSelectedValuation());
        Assert.assertTrue((Math.abs(voucherValuation_2ndColumn - 282.0)) < 0.001,
                "Selected valuation " + voucherValuation_2ndColumn + " should be  depreciated product Price = " + 282.0);
        Double faceValue = settlementDialog.getVoucherFaceValueFieldText();
        Double cashValue = settlementDialog.getVoucherCashValueFieldText();
        Assert.assertTrue((Math.abs(faceValue-300.0))<0.001, "Face value "+faceValue+" should be  = "+300.0);
        Assert.assertTrue((Math.abs(cashValue - 282.0)) < 0.001, "Cash value " + cashValue + " should be  = " + 282.0);

        Assert.assertTrue((Math.abs(settlementDialog.getDepreciationBelowCashFieldValue() - 36.66)) < 0.001,
                "Depreciation value " + settlementDialog.getDepreciationBelowCashFieldValue() + " should be  = " + 36.66);
        settlementDialog.saveAndCloseAddEditSetDialog();
        //SETTLEMENT page
        Assert.assertTrue(Math.abs(pages.ME_SETTLEMENT.getClTotalNewPrice(textSearch.getProductWithOnlyVoucherHandling()) - 282.0) < 0.001,
                "Get line Total new Price  " + pages.ME_SETTLEMENT.getClTotalNewPrice(textSearch.getProductWithOnlyVoucherHandling()) + " is equal to product Price "+282.0);
        Assert.assertTrue((Math.abs(pages.ME_SETTLEMENT.getClValue(textSearch.getProductWithOnlyVoucherHandling()) - 245.34))< 0.001,
                "Get line Value  " + pages.ME_SETTLEMENT.getClValue(textSearch.getProductWithOnlyVoucherHandling()) + " is equal to depreciated product Price " + 245.34);
        pages.ME_SETTLEMENT.removeLine(textSearch.getProductWithOnlyVoucherHandling());
        flows.CLAIM.logout();
    }*/
}
