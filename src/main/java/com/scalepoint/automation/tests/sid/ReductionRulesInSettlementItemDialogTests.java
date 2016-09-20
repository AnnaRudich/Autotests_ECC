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
import com.scalepoint.automation.utils.listeners.InvokedMethodListener;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

@Listeners({InvokedMethodListener.class})
@RequiredSetting(type = FTSetting.ENABLE_NEW_SETTLEMENT_ITEM_DIALOG)
@RequiredSetting(type = FTSetting.SHOW_COMPACT_SETTLEMENT_ITEM_DIALOG)
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
    @Test(description = "ECC-3031 Verify reduction rule policy type after clicking Reduction rule button", dataProvider = "testDataProvider")
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

        SidCalculations.ValuationWithReduction valuationWithReduction = SidCalculations.calculateWithReduction(claimItem.getNewPriceSP_2400(), claimItem.getDepAmount1_10(), claimItem.getReductionRule_30());

        String fetchedCashValue = String.format("%.2f", settlementDialog.cashCompensationValue());
        String calculatedCashValue = String.format("%.2f", valuationWithReduction.getCashCompensation());
        String fetchedDepreciation = String.format("%.2f", settlementDialog.fetchDepreciation());
        String calculatedDepreciation = String.format("%.2f", valuationWithReduction.getDepreciation());

        assertEquals(fetchedCashValue, calculatedCashValue, "Cash compensation incorrect");
        assertEquals(fetchedDepreciation, calculatedDepreciation, "Depreciation incorrect");
        assertEquals(settlementDialog.getDepreciationValue(), claimItem.getDepAmount1_10().toString());

        settlementDialog.applyReductionRuleByValue(claimItem.getReductionRule_30().toString());
        String fetchedCashValueWithReduction = String.format("%.2f", settlementDialog.cashCompensationValue());
        String fetchedDepreciationWithReduction = String.format("%.2f", settlementDialog.fetchDepreciation());
        String calculatedReduction = String.format("%.2f", valuationWithReduction.getReduction());
        String calculatedCashValueReduction = String.format("%.2f", valuationWithReduction.getCashCompensationWithReduction());

        assertEquals(fetchedCashValueWithReduction, calculatedCashValueReduction, "Cash compensation incorrect");
        assertEquals(fetchedDepreciationWithReduction, calculatedReduction, "Depreciation incorrect");
        assertEquals(settlementDialog.getDepreciationValue(), claimItem.getReductionRule_30().toString());
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
    @Test(description = "ECC-3031 Verify reduction rule policy type after ticking Depreciation automatically updated checkbox", dataProvider = "testDataProvider")
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

        SidCalculations.ValuationWithReduction valuationWithReduction = SidCalculations.calculateWithReduction(claimItem.getNewPriceSP_2400(), claimItem.getDepAmount1_10(), claimItem.getReductionRule_30());

        String fetchedCashValue = String.format("%.2f", settlementDialog.cashCompensationValue());
        String fetchedDepreciation = String.format("%.2f", settlementDialog.fetchDepreciation());
        String calculatedCashValue = String.format("%.2f", valuationWithReduction.getCashCompensation());
        String calculatedDepreciation = String.format("%.2f", valuationWithReduction.getDepreciation());

        assertEquals(fetchedCashValue, calculatedCashValue, "Cash compensation incorrect");
        assertEquals(fetchedDepreciation, calculatedDepreciation, "Depreciation incorrect");
        assertEquals(settlementDialog.getDepreciationValue(), claimItem.getDepAmount1_10().toString());

        settlementDialog.automaticDepreciation(true);
        String fetchedCashValueWithReduction = String.format("%.2f", settlementDialog.cashCompensationValue());
        String fetchedDepreciationWithReduction = String.format("%.2f", settlementDialog.fetchDepreciation());
        String calculatedReduction = String.format("%.2f", valuationWithReduction.getReduction());
        String calculatedCashValueReduction = String.format("%.2f", valuationWithReduction.getCashCompensationWithReduction());

        assertEquals(fetchedCashValueWithReduction, calculatedCashValueReduction, "Cash compensation incorrect");
        assertEquals(fetchedDepreciationWithReduction, calculatedReduction, "Depreciation incorrect");
        assertEquals(settlementDialog.getDepreciationValue(), claimItem.getReductionRule_30().toString());
        settlementDialog.cancel();
    }
}

