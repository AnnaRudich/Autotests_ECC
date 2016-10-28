package com.scalepoint.automation.tests.sid;

import com.scalepoint.automation.BaseTest;
import com.scalepoint.automation.pageobjects.dialogs.SettlementDialog;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.services.usersmanagement.CompanyCode;
import com.scalepoint.automation.tests.sid.SidCalculator.ValuationWithReduction;
import com.scalepoint.automation.utils.annotations.UserCompany;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.ClaimItem;
import com.scalepoint.automation.utils.data.entity.ReductionRule;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.testng.annotations.Test;

@RequiredSetting(type = FTSetting.ENABLE_NEW_SETTLEMENT_ITEM_DIALOG)
public class ReductionRulesDiscretionaryTypeSIDTests extends BaseTest {

    /**
     * GIVEN: User logs in as alkauser1
     * WHEN: Enter Category
     * AND: Enter Subcategory
     * AND: Enter new Price
     * AND: age indicated in policy Rule parameters
     * THEN: Reduction rule row with button appears
     * WHEN: click button
     * THEN: Value generated according rule settings added to the field
     * THEN: Value in depreciation field is changed to value of reduction rule
     * <p>
     * GIVEN: User logs in as alkauser1
     * WHEN: Enter Category
     * AND: Enter Subcategory
     * AND: Enter new Price
     * AND: age indicated in policy Rule parameters
     * THEN: Reduction rule row with button appears
     * WHEN: tick Automatically depreciation updated
     * THEN: Value generated according rule settings added to the field
     * THEN: Value in depreciation field is changed to value of reduction rule
     * <p>
     * GIVEN: User logs in as alkauser1
     * WHEN: Enter Category
     * AND: Enter Subcategory
     * AND: Enter new Price
     * AND: age indicated in unpublished policy Rule parameters
     * THEN: Reduction rule row with button does not appear
     * THEN: Value generated according rule settings is not added to the field
     * <p>
     * GIVEN: User logs in as alkauser1
     * WHEN: Enter Category
     * AND: Enter Subcategory
     * AND: Enter new Price
     * AND: age indicated in policy Rule parameters
     * AND: Claim with policy type A
     * AND: Rule with policy Type B
     * THEN: Reduction rule row with button appears
     * WHEN: click button
     * THEN: Value generated by the rule should NOT be added to the field
     * <p>
     * GIVEN: User logs in as alkauser1
     * WHEN: Enter Category
     * AND: Enter Subcategory
     * AND: Enter new Price
     * AND: age indicated in policy Rule parameters
     * AND: Claim with policy type A
     * AND: Rule with policy Type B
     * THEN: Reduction rule row with button appears
     * WHEN: tick Automatically depreciation updated
     * THEN: Value generated by the rule should NOT be added to the field
     */

    @Test(dataProvider = "testDataProvider", description = "ECC-3031 Verify reduction rule discretionary type after clicking Reduction rule button")
    public void ecc3031_3_reductionRulePolicyTypeDiscretionary(@UserCompany(CompanyCode.ALKA) User user,
                                                               Claim claim,
                                                               ClaimItem claimItem,
                                                               ReductionRule reductionRule) {
        SettlementDialog settlementDialog = loginAndCreateClaim(user, claim, claim.getPolicyTypeFF()).addManually().
                automaticDepreciation(false).
                fillDescription(claimItem.getTextFieldSP()).
                fillCustomerDemand(claimItem.getBigCustomDemandPrice()).
                fillNewPrice(claimItem.getNewPriceSP_2400()).
                fillCategory(claimItem.getAlkaCategory()).
                fillSubCategory(claimItem.getAlkaSubCategory()).
                enableAge(claimItem.getAgeStatus()).
                enterAgeYears(reductionRule.getAgeFrom2()).
                selectValuation(SettlementDialog.Valuation.NEW_PRICE);

        Double depreciationValue = settlementDialog.getDepreciationValue();
        ValuationWithReduction valuationWithReduction =
                SidCalculator.calculatePriceValuationWithReduction(claimItem.getNewPriceSP_2400(), depreciationValue, claimItem.getAlkaUserReductionRule_25());

        Double fetchedCashValue = settlementDialog.getCashCompensationValue();
        Double calculatedCashValue = valuationWithReduction.getCashCompensation();
        Double fetchedDepreciation = settlementDialog.fetchDepreciation();
        Double calculatedDepreciation = valuationWithReduction.getDepreciation();

        assertEqualsDouble(fetchedCashValue, calculatedCashValue, "Cash compensation incorrect");
        assertEqualsDouble(fetchedDepreciation, calculatedDepreciation, "Depreciation incorrect");
        assertEqualsDouble(settlementDialog.getDepreciationValue(), 0d, "Depreciation value is not correct");

        settlementDialog.applyReductionRuleByValue(claimItem.getAlkaUserReductionRule_25());
        Double fetchedCashValueWithReduction = settlementDialog.getCashCompensationValue();
        Double fetchedDepreciationWithReduction = settlementDialog.fetchDepreciation();
        Double calculatedCashWithReduction = valuationWithReduction.getCashCompensationWithReduction();
        Double calculatedReduction = valuationWithReduction.getReduction();

        assertEqualsDouble(fetchedCashValueWithReduction, calculatedCashWithReduction, "Cash compensation incorrect");
        assertEqualsDouble(fetchedDepreciationWithReduction, calculatedReduction, "Depreciation incorrect");
        assertEqualsDouble(settlementDialog.getDepreciationValue(), claimItem.getAlkaUserReductionRule_25().doubleValue(), "Depreciation value is not correct");
        settlementDialog.cancel();
    }

    /**
     * GIVEN: User logs in as alkauser1
     * WHEN: Enter Category
     * AND: Enter Subcategory
     * AND: Enter new Price
     * AND: age indicated in policy Rule parameters
     * THEN: Reduction rule row with button appears
     * WHEN: tick Automatically depreciation updated
     * THEN: Value generated according rule settings added to the field
     * THEN: Value in depreciation field is changed to value of reduction rule
     */
    @Test(dataProvider = "testDataProvider", description = "ECC-3031 Verify reduction rule discretionary type after ticking Depreciation automatically updated checkbox")
    public void ecc3031_4_reductionRulePolicyTypeDiscretionaryAutomatic(@UserCompany(CompanyCode.ALKA) User user,
                                                                        Claim claim,
                                                                        ClaimItem claimItem,
                                                                        ReductionRule reductionRule) {
        SettlementDialog settlementDialog = loginAndCreateClaim(user, claim, claim.getPolicyTypeFF()).addManually()
                .automaticDepreciation(false)
                .fillDescription(claimItem.getTextFieldSP())
                .fillCustomerDemand(claimItem.getBigCustomDemandPrice())
                .fillNewPrice(claimItem.getNewPriceSP_2400())
                .fillCategory(claimItem.getAlkaCategory())
                .fillSubCategory(claimItem.getAlkaSubCategory())
                .enableAge(claimItem.getAgeStatus())
                .enterAgeYears(reductionRule.getAgeFrom2())
                .selectValuation(SettlementDialog.Valuation.NEW_PRICE);

        double depreciation = settlementDialog.getDepreciationValue();
        ValuationWithReduction valuationWithReduction =
                SidCalculator.calculatePriceValuationWithReduction(claimItem.getNewPriceSP_2400(), depreciation, claimItem.getAlkaUserReductionRule_25());

        Double fetchedCashValue = settlementDialog.getCashCompensationValue();
        Double calculatedCashValue = valuationWithReduction.getCashCompensation();
        Double fetchedDepreciation = settlementDialog.fetchDepreciation();
        Double calculatedDepreciation = valuationWithReduction.getDepreciation();

        assertEqualsDouble(fetchedCashValue, calculatedCashValue, "Cash compensation incorrect");
        assertEqualsDouble(fetchedDepreciation, calculatedDepreciation, "Depreciation incorrect");
        assertEqualsDouble(settlementDialog.getDepreciationValue(), 0d, "Depreciation value is not correct");

        settlementDialog
                .automaticDepreciation(true)
                .selectValuation(SettlementDialog.Valuation.NEW_PRICE);

        Double fetchedCashValueWithReduction = settlementDialog.getCashCompensationValue();
        Double calculatedCashWithReduction = valuationWithReduction.getCashCompensationWithReduction();

        assertEqualsDouble(fetchedCashValueWithReduction, calculatedCashWithReduction, "Cash compensation incorrect");

        Double fetchedDepreciationWithReduction = settlementDialog.fetchDepreciation();
        Double calculatedReduction = valuationWithReduction.getReduction();

        assertEqualsDouble(fetchedDepreciationWithReduction, calculatedReduction, "Depreciation incorrect");
        assertEqualsDouble(settlementDialog.getDepreciationValue(), claimItem.getAlkaUserReductionRule_25().doubleValue(), "Depreciation value is not correct");
        settlementDialog.cancel();
    }

    /**
     * GIVEN: User logs in as alkauser1
     * WHEN: Enter Category
     * AND: Enter Subcategory
     * AND: Enter new Price
     * AND: age indicated in unpublished policy Rule parameters
     * THEN: Reduction rule row with button does not appear
     * THEN: Value generated according rule settings is not added to the field
     */
    @Test(dataProvider = "testDataProvider", description = "ECC-3031 Verify unpublished rule")
    public void ecc3031_5_reductionRuleUnpublishedPolicy(@UserCompany(CompanyCode.ALKA) User user,
                                                         Claim claim,
                                                         ClaimItem claimItem,
                                                         ReductionRule reductionRule) {
        SettlementDialog settlementDialog = loginAndCreateClaim(user, claim, claim.getPolicyTypeFF()).addManually()
                .automaticDepreciation(false)
                .fillDescription(claimItem.getTextFieldSP())
                .fillCustomerDemand(claimItem.getBigCustomDemandPrice())
                .fillNewPrice(claimItem.getNewPriceSP_2400())
                .fillCategory(claimItem.getAlkaCategoryUnpublishedPolicy())
                .fillSubCategory(claimItem.getAlkaSubCategoryUnpublishedPolicy())
                .enableAge(claimItem.getAgeStatus())
                .enterAgeYears(reductionRule.getAgeFrom2())
                .selectValuation(SettlementDialog.Valuation.NEW_PRICE);

        double depreciation = settlementDialog.getDepreciationValue();
        ValuationWithReduction valuationWithReduction =
                SidCalculator.calculatePriceValuationWithReduction(claimItem.getNewPriceSP_2400(), depreciation, 0);

        Double calculatedCashValue = valuationWithReduction.getCashCompensation();
        Double fetchedCashValue = settlementDialog.getCashCompensationValue();
        Double fetchedDepreciation = settlementDialog.fetchDepreciation();
        Double calculatedDepreciation = valuationWithReduction.getDepreciation();

        assertEqualsDouble(fetchedCashValue, calculatedCashValue, "Cash compensation incorrect");
        assertEqualsDouble(fetchedDepreciation, calculatedDepreciation, "Depreciation incorrect");
        assertEqualsDouble(settlementDialog.getDepreciationValue(), 0d, "Depreciation value is incorrect");
        settlementDialog.cancel();
    }

    /**
     * GIVEN: User logs in as alkauser1
     * WHEN: Enter Category
     * AND: Enter Subcategory
     * AND: Enter new Price
     * AND: age indicated in policy Rule parameters
     * AND: Claim with policy type A
     * AND: Rule with policy Type B
     * THEN: Reduction rule row with button appears
     * WHEN: click button
     * THEN: Value generated by the rule should NOT be added to the field
     */


    @Test(dataProvider = "testDataProvider", description = "ECC-3031 Verify rule with type of Policy indicated after clicking Reduction rule button")
    public void ecc3031_6_reductionRulePolicyTypeIndicated(@UserCompany(CompanyCode.ALKA) User user,
                                                           Claim claim,
                                                           ClaimItem claimItem,
                                                           ReductionRule reductionRule) {
        SettlementDialog settlementDialog = loginAndCreateClaim(user, claim, claim.getPolicyTypeAF()).addManually()
                .automaticDepreciation(false)
                .fillDescription(claimItem.getTextFieldSP())
                .fillCustomerDemand(claimItem.getBigCustomDemandPrice())
                .fillNewPrice(claimItem.getNewPriceSP_2400())
                .fillCategory(claimItem.getExistingCat3_Telefoni())
                .fillSubCategory(claimItem.getExistingSubCat3_Mobiltelefoner())
                .enableAge(claimItem.getAgeStatus())
                .enterAgeYears(reductionRule.getAgeFrom2())
                .selectValuation(SettlementDialog.Valuation.NEW_PRICE);
        double depreciation = settlementDialog.getDepreciationValue();
        ValuationWithReduction valuationWithReduction =
                SidCalculator.calculatePriceValuationWithReduction(claimItem.getNewPriceSP_2400(),
                        depreciation, claimItem.getAlkaUserReductionRule40());

        Double fetchedCashValue = settlementDialog.getCashCompensationValue();
        Double calculatedCashValue = valuationWithReduction.getCashCompensation();
        Double fetchedDepreciation = settlementDialog.fetchDepreciation();
        Double calculatedDepreciation = valuationWithReduction.getDepreciation();

        assertEqualsDouble(fetchedCashValue, calculatedCashValue, "Cash compensation incorrect");
        assertEqualsDouble(fetchedDepreciation, calculatedDepreciation, "Depreciation incorrect");
        assertEqualsDouble(settlementDialog.getDepreciationValue(), 0d, "Depreciation incorrect");

        settlementDialog.applyReductionRuleByValue(claimItem.getAlkaUserReductionRule40());
        Double fetchedCashValueWithReduction = settlementDialog.getCashCompensationValue();
        Double fetchedDepreciationWithReduction = settlementDialog.fetchDepreciation();

        assertEqualsDouble(fetchedCashValueWithReduction, valuationWithReduction.getCashCompensationWithReduction(), "Cash compensation incorrect");
        assertEqualsDouble(fetchedDepreciationWithReduction, valuationWithReduction.getReduction(), "Depreciation incorrect");
        assertEqualsDouble(settlementDialog.getDepreciationValue(), claimItem.getAlkaUserReductionRule40().doubleValue(), "Depreciation incorrect");

        settlementDialog.cancel();
    }


    /**
     * GIVEN: User logs in as alkauser1
     * WHEN: Enter Category
     * AND: Enter Subcategory
     * AND: Enter new Price
     * AND: age indicated in policy Rule parameters
     * AND: Claim with policy type A
     * AND: Rule with policy Type B
     * THEN: Reduction rule row with button appears
     * WHEN: tick Automatically depreciation updated
     * THEN: Value generated by the rule should NOT be added to the field
     */
    @Test(dataProvider = "testDataProvider", description = "ECC-3031 Verify rule with type of Policy indicated after ticking Depreciation automatically updated checkbox")
    public void ecc3031_7_reductionRulePolicyTypeIndicatedAutomatic(@UserCompany(CompanyCode.ALKA) User user,
                                                                    Claim claim,
                                                                    ClaimItem claimItem,
                                                                    ReductionRule reductionRule) {
        SettlementDialog settlementDialog = loginAndCreateClaim(user, claim, claim.getPolicyTypeAF()).addManually()
                .automaticDepreciation(false)
                .fillDescription(claimItem.getTextFieldSP())
                .fillCustomerDemand(claimItem.getBigCustomDemandPrice())
                .fillNewPrice(claimItem.getNewPriceSP_2400())
                .fillCategory(claimItem.getExistingCat3_Telefoni())
                .fillSubCategory(claimItem.getExistingSubCat3_Mobiltelefoner())
                .enableAge(claimItem.getAgeStatus())
                .enterAgeYears(reductionRule.getAgeFrom2())
                .selectValuation(SettlementDialog.Valuation.NEW_PRICE);

        double depreciation = settlementDialog.getDepreciationValue();

        ValuationWithReduction calculation = SidCalculator.calculatePriceValuationWithReduction(claimItem.getNewPriceSP_2400(),
                depreciation,
                claimItem.getAlkaUserReductionRule40());

        Double fetchedCashValue = settlementDialog.getCashCompensationValue();
        Double calculatedCashValue = calculation.getCashCompensation();
        Double fetchedDepreciation = settlementDialog.fetchDepreciation();

        assertEqualsDouble(fetchedCashValue, calculatedCashValue, "Cash compensation incorrect");
        assertEqualsDouble(fetchedDepreciation, calculation.getDepreciation(), "Depreciation incorrect");
        assertEqualsDouble(settlementDialog.getDepreciationValue(), 0d, "Depreciation value is incorrect");

        settlementDialog
                .automaticDepreciation(true)
                .selectValuation(SettlementDialog.Valuation.NEW_PRICE);

        Double fetchedCashValueWithReduction = settlementDialog.getCashCompensationValue();
        Double fetchedDepreciationWithReduction = settlementDialog.fetchDepreciation();

        assertEqualsDouble(fetchedCashValueWithReduction, calculation.getCashCompensationWithReduction(), "Cash compensation incorrect");
        assertEqualsDouble(fetchedDepreciationWithReduction, calculation.getReduction(), "Depreciation incorrect");
        assertEqualsDouble(settlementDialog.getDepreciationValue(), claimItem.getAlkaUserReductionRule40().doubleValue(), "Depreciation value incorrect");
        settlementDialog.cancel();
    }
}
