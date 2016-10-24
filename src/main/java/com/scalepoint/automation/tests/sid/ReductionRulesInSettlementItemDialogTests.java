package com.scalepoint.automation.tests.sid;

import com.scalepoint.automation.BaseTest;
import com.scalepoint.automation.pageobjects.dialogs.SettlementDialog;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.services.usersmanagement.CompanyCode;
import com.scalepoint.automation.utils.annotations.UserCompany;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.ClaimItem;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.testng.annotations.Test;

@RequiredSetting(type = FTSetting.ENABLE_NEW_SETTLEMENT_ITEM_DIALOG)
@RequiredSetting(type = FTSetting.SHOW_SUGGESTED_DEPRECIATION_SECTION)
public class ReductionRulesInSettlementItemDialogTests extends BaseTest {

    /**
     * GIVEN: User logs in as tryg user
     * WHEN: Enter Category
     * AND: Enter Subcategory
     * AND: Enter new Price
     * AND: age indicated in policy Rule parameters
     * THEN: Reduction rule row with button appears
     * WHEN: click button
     * THEN: Value generated according rule settings added to the field
     * THEN: Value in depreciation field is changed to value of reduction rule
     * <p>
     * GIVEN: User logs in as tryg user
     * WHEN: Enter Category
     * AND: Enter Subcategory
     * AND: Enter new Price
     * AND: age indicated in policy Rule parameters
     * THEN: Reduction rule row with button appears
     * WHEN: tick Automatically depreciation updated
     * THEN: Value generated according rule settings added to the field
     * THEN: Value in depreciation field is changed to value of reduction rule
     */
    @Test(dataProvider = "testDataProvider", description = "ECC-3031 Verify reduction rule policy type after clicking Reduction rule button")
    public void ecc3031_1_reductionRulePolicyType(@UserCompany(CompanyCode.TRYGFORSIKRING) User user, Claim claim, ClaimItem claimItem) {

        SettlementDialog settlementDialog = loginAndCreateClaim(user, claim).
                addManually().
                fillDescription(claimItem.getTextFieldSP()).
                fillNewPrice(claimItem.getNewPriceSP_2400()).
                fillCustomerDemand(claimItem.getBigCustomDemandPrice()).
                fillCategory(claimItem.getExistingCat3_Telefoni()).
                fillSubCategory(claimItem.getExistingSubCat3_Mobiltelefoner()).
                fillDepreciation(claimItem.getDepAmount1_10()).
                enableAge("2").
                selectValuation(SettlementDialog.Valuation.NEW_PRICE);

        SidCalculator.ValuationWithReduction valuationWithReduction = SidCalculator.calculatePriceValuationWithReduction(claimItem.getNewPriceSP_2400(), claimItem.getDepAmount1_10(), claimItem.getReductionRule_30());

        Double fetchedCashValue = settlementDialog.cashCompensationValue();
        Double calculatedCashValue = valuationWithReduction.getCashCompensation();
        Double fetchedDepreciation = settlementDialog.fetchDepreciation();
        Double calculatedDepreciation = valuationWithReduction.getDepreciation();

        assertEqualsDouble(fetchedCashValue, calculatedCashValue, "Cash compensation incorrect");
        assertEqualsDouble(fetchedDepreciation, calculatedDepreciation, "Depreciation incorrect");
        assertEqualsDouble(settlementDialog.getDepreciationValue(), claimItem.getDepAmount1_10().doubleValue(), "Depreciation percentage incorrect");

        settlementDialog.applyReductionRuleByValue(claimItem.getReductionRule_30());

        Double fetchedCashValueWithReduction = settlementDialog.cashCompensationValue();
        Double fetchedDepreciationWithReduction = settlementDialog.fetchDepreciation();
        Double calculatedReduction = valuationWithReduction.getReduction();
        Double calculatedCashValueReduction = valuationWithReduction.getCashCompensationWithReduction();

        assertEqualsDouble(fetchedCashValueWithReduction, calculatedCashValueReduction, "Cash compensation incorrect");
        assertEqualsDouble(fetchedDepreciationWithReduction, calculatedReduction, "Depreciation incorrect");
        assertEqualsDouble(settlementDialog.getDepreciationValue(), claimItem.getReductionRule_30().doubleValue(), "Reduction percentage incorrect");
        settlementDialog.cancel();
    }


    /**
     * GIVEN: User logs in as tryg user
     * WHEN: Enter Category
     * AND: Enter Subcategory
     * AND: Enter new Price
     * AND: age indicated in policy Rule parameters
     * THEN: Reduction rule row with button appears
     * WHEN: tick Automatically depreciation updated
     * THEN: Value generated according rule settings added to the field
     * THEN: Value in depreciation field is changed to value of reduction rule
     */
    @Test(dataProvider = "testDataProvider", description = "ECC-3031 Verify reduction rule policy type after ticking Depreciation automatically updated checkbox")
    @RequiredSetting(type = FTSetting.SHOW_POLICY_TYPE, enabled = false)
    public void ecc3031_2_reductionRulePolicyTypeAutomatic(@UserCompany(CompanyCode.TRYGFORSIKRING) User user, Claim claim, ClaimItem claimItem) {
        SettlementDialog settlementDialog = loginAndCreateClaim(user, claim).
                addManually().
                fillDescription(claimItem.getTextFieldSP()).
                fillCustomerDemand(claimItem.getBigCustomDemandPrice()).
                fillNewPrice(claimItem.getNewPriceSP_2400()).
                fillCategory(claimItem.getExistingCat3_Telefoni()).
                fillSubCategory(claimItem.getExistingSubCat3_Mobiltelefoner()).
                fillDepreciation(claimItem.getDepAmount1_10()).
                enableAge().
                enterAgeYears("2").
                selectValuation(SettlementDialog.Valuation.NEW_PRICE);

        SidCalculator.ValuationWithReduction valuationWithReduction = SidCalculator.calculatePriceValuationWithReduction(claimItem.getNewPriceSP_2400(), claimItem.getDepAmount1_10(), claimItem.getReductionRule_30());

        Double fetchedCashValue = settlementDialog.cashCompensationValue();
        Double fetchedDepreciation = settlementDialog.fetchDepreciation();
        Double calculatedCashValue = valuationWithReduction.getCashCompensation();
        Double calculatedDepreciation = valuationWithReduction.getDepreciation();

        assertEqualsDouble(fetchedCashValue, calculatedCashValue, "Cash compensation incorrect");
        assertEqualsDouble(fetchedDepreciation, calculatedDepreciation, "Depreciation incorrect");
        assertEqualsDouble(settlementDialog.getDepreciationValue(), claimItem.getDepAmount1_10().doubleValue(), "Depreciation percentage incorrect");

        settlementDialog.automaticDepreciation(true);

        Double fetchedCashValueWithReduction = settlementDialog.cashCompensationValue();
        Double fetchedDepreciationWithReduction = settlementDialog.fetchDepreciation();
        Double calculatedReduction = valuationWithReduction.getReduction();
        Double calculatedCashValueReduction = valuationWithReduction.getCashCompensationWithReduction();

        assertEqualsDouble(fetchedCashValueWithReduction, calculatedCashValueReduction, "Cash compensation incorrect");
        assertEqualsDouble(fetchedDepreciationWithReduction, calculatedReduction, "Depreciation incorrect");
        assertEqualsDouble(settlementDialog.getDepreciationValue(), claimItem.getReductionRule_30().doubleValue(), "Reduction percentage incorrect");
        settlementDialog.cancel();
    }
}

