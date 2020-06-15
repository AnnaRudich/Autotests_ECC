package com.scalepoint.automation.tests.sid;

import com.scalepoint.automation.pageobjects.dialogs.SettlementDialog;
import com.scalepoint.automation.services.usersmanagement.CompanyCode;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.tests.sid.SidCalculator.ValuationWithReduction;
import com.scalepoint.automation.utils.Constants;
import com.scalepoint.automation.utils.annotations.Jira;
import com.scalepoint.automation.utils.annotations.UserCompany;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.ClaimItem;
import com.scalepoint.automation.utils.data.entity.ReductionRule;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.testng.annotations.Test;

import static com.scalepoint.automation.grid.ValuationGrid.Valuation.NEW_PRICE;

@Jira("https://jira.scalepoint.com/browse/CHARLIE-613")
public class SidReductionRulesDiscretionaryTests extends BaseTest {

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
        SettlementDialog settlementDialog = loginAndCreateClaim(user, claim, claim.getPolicyTypeFF())
                .openSid()
                .automaticDepreciation(false)
                .setDescription(claimItem.getTextFieldSP())
                .setCustomerDemand(Constants.PRICE_100_000)
                .setNewPrice(Constants.PRICE_2400)
                .setCategory(claimItem.getCategoryLuxuryWatches())
                .enableAge(reductionRule.getAgeFrom2())
                .setValuation(NEW_PRICE);

        Integer depreciationPercentage = settlementDialog.getDepreciationPercentage();
        ValuationWithReduction valuationWithReduction =
                SidCalculator.calculatePriceValuationWithReduction(Constants.PRICE_2400, depreciationPercentage, claimItem.getAlkaUserReductionRule());

        Double calculatedCashValue = valuationWithReduction.getCashCompensation();
        Double calculatedDepreciation = valuationWithReduction.getDepreciation();
        Double calculatedCashWithReduction = valuationWithReduction.getCashCompensationWithReduction();
        Double calculatedReduction = valuationWithReduction.getReduction();

        settlementDialog
                .doAssert(sid -> {
                    sid.assertCashValueIs(calculatedCashValue);
                    sid.assertDepreciationAmountIs(calculatedDepreciation);
                    sid.assertDepreciationValueIs(0d);
                })
                .applyReductionRuleByValue(claimItem.getAlkaUserReductionRule())
                .doAssert(sid -> {
                    sid.assertCashValueIs(calculatedCashWithReduction);
                    sid.assertDepreciationAmountIs(calculatedReduction);
                    sid.assertDepreciationValueIs(claimItem.getAlkaUserReductionRule().doubleValue());
                })
                .cancel();
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
        SettlementDialog settlementDialog = loginAndCreateClaim(user, claim, claim.getPolicyTypeFF())
                .openSid()
                .automaticDepreciation(false)
                .setDescription(claimItem.getTextFieldSP())
                .setCustomerDemand(Constants.PRICE_100_000)
                .setNewPrice(Constants.PRICE_2400)
                .setCategory(claimItem.getCategoryLuxuryWatches())
                .enableAge(reductionRule.getAgeFrom2())
                .setValuation(NEW_PRICE);

        Integer depreciationPercentage = settlementDialog.getDepreciationPercentage();
        ValuationWithReduction valuationWithReduction =
                SidCalculator.calculatePriceValuationWithReduction(Constants.PRICE_2400, depreciationPercentage, claimItem.getAlkaUserReductionRule());

        Double calculatedCashValue = valuationWithReduction.getCashCompensation();
        Double calculatedDepreciation = valuationWithReduction.getDepreciation();
        Double calculatedCashWithReduction = valuationWithReduction.getCashCompensationWithReduction();
        Double calculatedReduction = valuationWithReduction.getReduction();

        settlementDialog
                .doAssert(sid -> {
                    sid.assertCashValueIs(calculatedCashValue);
                    sid.assertDepreciationAmountIs(calculatedDepreciation);
                    sid.assertDepreciationValueIs(0d);
                })
                .automaticDepreciation(true)
                .setValuation(NEW_PRICE)
                .doAssert(sid -> {
                    sid.assertCashValueIs(calculatedCashWithReduction);
                    sid.assertDepreciationAmountIs(calculatedReduction);
                    sid.assertDepreciationValueIs(claimItem.getAlkaUserReductionRule().doubleValue());
                })
                .cancel();
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
        SettlementDialog settlementDialog = loginAndCreateClaim(user, claim, claim.getPolicyTypeFF())
                .openSid()
                .automaticDepreciation(false)
                .setDescription(claimItem.getTextFieldSP())
                .setCustomerDemand(Constants.PRICE_100_000)
                .setNewPrice(Constants.PRICE_2400)
                .setCategory(claimItem.getCategoryHearingAids())
                .enableAge(reductionRule.getAgeFrom2())
                .setValuation(NEW_PRICE);

        Integer depreciationPercentage = settlementDialog.getDepreciationPercentage();
        ValuationWithReduction valuationWithReduction =
                SidCalculator.calculatePriceValuationWithReduction(Constants.PRICE_2400, depreciationPercentage, 0);
        Double calculatedCashValue = valuationWithReduction.getCashCompensation();
        Double calculatedDepreciation = valuationWithReduction.getDepreciation();

        settlementDialog
                .doAssert(sid -> {
                    sid.assertCashValueIs(calculatedCashValue);
                    sid.assertDepreciationAmountIs(calculatedDepreciation);
                    sid.assertDepreciationValueIs(0d);
                })
                .cancel();
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
        SettlementDialog settlementDialog = loginAndCreateClaim(user, claim, claim.getPolicyTypeAF())
                .openSid()
                .automaticDepreciation(false)
                .setDescription(claimItem.getTextFieldSP())
                .setCustomerDemand(Constants.PRICE_100_000)
                .setNewPrice(Constants.PRICE_2400)
                .setCategory(claimItem.getCategoryMobilePhones())
                .enableAge(reductionRule.getAgeFrom2())
                .setValuation(NEW_PRICE);

        Integer depreciationPercentage = settlementDialog.getDepreciationPercentage();
        ValuationWithReduction valuationWithReduction =
                SidCalculator.calculatePriceValuationWithReduction(Constants.PRICE_2400,
                        depreciationPercentage, claimItem.getAlkaUserReductionRule40());

        Double calculatedCashValue = valuationWithReduction.getCashCompensation();
        Double calculatedDepreciation = valuationWithReduction.getDepreciation();

        settlementDialog
                .doAssert(sid -> {
                    sid.assertCashValueIs(calculatedCashValue);
                    sid.assertDepreciationAmountIs(calculatedDepreciation);
                    sid.assertDepreciationValueIs(0d);
                })
                .applyReductionRuleByValue(claimItem.getAlkaUserReductionRule40())
                .doAssert(sid -> {
                    sid.assertCashValueIs(valuationWithReduction.getCashCompensationWithReduction());
                    sid.assertDepreciationAmountIs(valuationWithReduction.getReduction());
                    sid.assertDepreciationValueIs(claimItem.getAlkaUserReductionRule40().doubleValue());
                })
                .cancel();
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
        SettlementDialog settlementDialog = loginAndCreateClaim(user, claim, claim.getPolicyTypeAF()).openSid()
                .automaticDepreciation(false)
                .setDescription(claimItem.getTextFieldSP())
                .setCustomerDemand(Constants.PRICE_100_000)
                .setNewPrice(Constants.PRICE_2400)
                .setCategory(claimItem.getCategoryMobilePhones())
                .enableAge(reductionRule.getAgeFrom2())
                .setValuation(NEW_PRICE);

        Integer depreciationPercentage = settlementDialog.getDepreciationPercentage();

        ValuationWithReduction calculation = SidCalculator.calculatePriceValuationWithReduction(Constants.PRICE_2400,
                depreciationPercentage,
                claimItem.getAlkaUserReductionRule40());
        Double calculatedCashValue = calculation.getCashCompensation();

        settlementDialog
                .doAssert(sid -> {
                    sid.assertCashValueIs(calculatedCashValue);
                    sid.assertDepreciationAmountIs(calculation.getDepreciation());
                    sid.assertDepreciationValueIs(0d);
                })
                .automaticDepreciation(true)
                .setValuation(NEW_PRICE)
                .doAssert(sid -> {
                    sid.assertCashValueIs(calculation.getCashCompensationWithReduction());
                    sid.assertDepreciationAmountIs(calculation.getReduction());
                    sid.assertDepreciationValueIs(claimItem.getAlkaUserReductionRule40().doubleValue());
                })
                .cancel();
    }
}
