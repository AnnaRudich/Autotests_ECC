package com.scalepoint.automation.tests.sid;

import com.scalepoint.automation.BaseTest;
import com.scalepoint.automation.pageobjects.dialogs.SettlementDialog;
import com.scalepoint.automation.services.externalapi.ftemplates.FT;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.services.usersmanagement.CompanyCode;
import com.scalepoint.automation.utils.OperationalUtils;
import com.scalepoint.automation.utils.annotations.UserCompany;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.ClaimItem;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

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
                fillCategory(claimItem.getExistingCat3()).
                fillSubCategory(claimItem.getExistingSubCat3()).
                fillNewPrice(claimItem.getNewPriceSP()).
                fillCustomerDemand(claimItem.getBigCustomDemandPrice()).
                fillDepreciation(claimItem.getDepAmount1()).
                enableAge("2").
                selectValuation(SettlementDialog.Valuation.NEW_PRICE);

        String fetchedCashValue = String.format("%.2f", settlementDialog.cashCompensationValue());
        String calculatedCashValue = String.format("%.2f", OperationalUtils.doubleString(calculateCashCompensation(claimItem)));
        String fetchedDepreciation = String.format("%.2f", settlementDialog.fetchDepreciation());
        assertEquals(fetchedCashValue, calculatedCashValue, "Cash compensation incorrect");
        assertEquals(fetchedDepreciation, calculateDepreciation(claimItem), "Depreciation incorrect");
        assertEquals(settlementDialog.getDepreciationValue(), claimItem.getDepAmount1().toString());

        settlementDialog.applyReductionRuleByValue(claimItem.getReductionRule());
        String fetchedCashValueWithReduction = String.format("%.2f", settlementDialog.cashCompensationValue());
        String fetchedDepreciationWithReduction = String.format("%.2f", settlementDialog.fetchDepreciation());
        assertEquals(fetchedCashValueWithReduction, calculateCashCompensationWithReduction(claimItem), "Cash compensation incorrect");
        assertEquals(fetchedDepreciationWithReduction, calculatedReduction(claimItem), "Depreciation incorrect");
        assertEquals(settlementDialog.getDepreciationValue(), claimItem.getReductionRule());
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
    public void ecc3031_2_reductionRulePolicyTypeAutomatic(@UserCompany(CompanyCode.TRYGFORSIKRING) User user, Claim claim, ClaimItem claimItem) {
        enableNewSid(user);
        updateFT(user, FT.disable(FTSetting.SHOW_POLICY_TYPE));
        SettlementDialog settlementDialog = loginAndCreateClaim(user, claim).
                addManually().
                fillCategory(claimItem.getExistingCat3()).
                fillSubCategory(claimItem.getExistingSubCat3()).
                fillDescription(claimItem.getTextFieldSP()).
                fillCustomerDemand(claimItem.getBigCustomDemandPrice()).
                fillNewPrice(claimItem.getNewPriceSP()).
                fillDepreciation(claimItem.getDepAmount1()).
                enableAge().
                enterAgeYears("2").
                selectValuation(SettlementDialog.Valuation.NEW_PRICE);

        String fetchedCashValue = String.format("%.2f", settlementDialog.cashCompensationValue());
        String fetchedDepreciation = String.format("%.2f", settlementDialog.fetchDepreciation());
        String calculatedCashValue = String.format("%.2f", OperationalUtils.doubleString(calculateCashCompensation(claimItem)));
        assertEquals(fetchedCashValue, calculatedCashValue, "Cash compensation incorrect");
        assertEquals(fetchedDepreciation, calculateDepreciation(claimItem), "Depreciation incorrect");
        assertEquals(settlementDialog.getDepreciationValue(), claimItem.getDepAmount1().toString());

        settlementDialog.automaticDepreciation(true);
        String fetchedCashValueWithReduction = String.format("%.2f", settlementDialog.cashCompensationValue());
        String fetchedDepreciationWithReduction = String.format("%.2f", settlementDialog.fetchDepreciation());
        assertEquals(fetchedCashValueWithReduction, calculateCashCompensationWithReduction(claimItem), "Cash compensation incorrect");
        assertEquals(fetchedDepreciationWithReduction, calculatedReduction(claimItem), "Depreciation incorrect");
        assertEquals(settlementDialog.getDepreciationValue(), claimItem.getReductionRule());
        settlementDialog.cancel();
    }

    private String calculateCashCompensation(ClaimItem claimItem) {
        Double cashCompensation = Double.valueOf(claimItem.getNewPriceSP()) - Double.valueOf(calculateDepreciation(claimItem));
        return String.valueOf(cashCompensation);
    }

    private String calculateCashCompensationWithReduction(ClaimItem claimItem) {
        Double cashCompensation = Double.valueOf(claimItem.getNewPriceSP()) - Double.valueOf(calculatedReduction(claimItem));
        return String.format("%.2f", cashCompensation);
    }

    private String calculateDepreciation(ClaimItem claimItem) {
        Double depreciation = Double.valueOf(claimItem.getNewPriceSP()) * Double.valueOf(claimItem.getDepAmount1()) / 100;
        return String.format("%.2f", depreciation);
    }

    private String calculatedReduction(ClaimItem claimItem) {
        Double depreciation = Double.valueOf(claimItem.getNewPriceSP()) * Double.valueOf(claimItem.getReductionRule()) / 100;
        return String.format("%.2f", depreciation);
    }
}

