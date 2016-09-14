/*
package com.ecc.fip.tests.settlementDialog;

import com.ecc.fip.api.ApiHelper;
import com.ecc.fip.pages.LoginPage;
import com.ecc.fip.pages.NewCustomerPage;
import com.ecc.fip.pages.blocks.settlementItemDialog.SettlementDialog;
import com.ecc.fip.pages.blocks.settlementpage.MainMenu;
import com.ecc.fip.pages.blocks.settlementpage.SettlementPage;
import com.ecc.fip.tests.ATest;
import com.ecc.fip.util.OperationalUtils;
import com.ecc.flows.fastflows.FastFlows;
import com.ecc.util.data.entity.ClaimItem;
import com.ecc.util.data.entity.Client;
import com.ecc.util.data.entity.ReductionRule;
import com.ecc.util.data.entity.Voucher;
import com.google.inject.Inject;
import org.apache.http.client.fluent.Executor;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.testng.Assert.assertEquals;

*/
/**
 * Created by asa on 2016-07-13.
 *//*

public class ReductionRulesDiscretionaryTypeSIDTests extends ATest {
    @Inject
    LoginPage loginPage;
    @Inject
    SettlementPage settlementPage;
    @Inject
    SettlementDialog settlementDialog;
    @Inject
    FastFlows fastFlows;
    @Inject
    Client client;
    @Inject
    ClaimItem claimItem;
    @Inject
    ApiHelper apiHelper;
    @Inject
    Voucher voucher;
    @Inject
    MainMenu mainMenu;
    @Inject
    NewCustomerPage newCustomerPage;
    @Inject
    ReductionRule reductionRule;


    private Map<String, String> categories = new HashMap<>();

    @BeforeClass
    public void beforeClass() {
        Executor executor = fastFlows.login(credentials.getAlkauser1Login(), credentials.getAlkauser1Pass());
        apiHelper.addMappedVoucher(executor, voucher, categories);
        fastFlows.createClient(executor, client);
    }

    @BeforeMethod
    public void beforeMethod() {
        loginPage.Login(credentials.getAlkauser1Login(), credentials.getAlkauser1Pass());
    }

    @AfterMethod
    public void afterMethod() {
        driver.quit();
    }

    */
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
     *//*


    @Test(description = "ECC-3031 Verify reduction rule discretionary type after clicking Reduction rule button")
    public void ecc3031_3_reductionRulePolicyTypeDiscretionary() {
        mainMenu
                .newCustomer();
        newCustomerPage
                .EnterFirstName(client.getFirstName())
                .EnterSurname(client.getLastName())
                .EnterClaimNumber(client.getClaimNumber())
                .SelectPolicyType(client.getPolicyTypeFF())
                .Continue();
        mainMenu
                .claimInfoOpen();
        settlementPage
                .AddManually();
        settlementDialog
                .automaticDepreciation(false)
                .fillDescription(claimItem.getTextFieldSP())
                .fillCustomerDemand(Integer.valueOf(claimItem.getBigCustomDemandPrice()))
                .fillNewPrice(Integer.valueOf(claimItem.getNewPriceSP()))
                .fillCategory(claimItem.getAlkaCategory())
                .fillSubCategory(claimItem.getAlkaSubCategory())
                .enableAge(claimItem.getAgeStatus())
                .enterAgeYears(reductionRule.getAgeFrom2())
                .SelectValuation(claimItem.getValuationType1());
        String fetchedCashValue = String.format("%.2f", settlementDialog.cashCompensationValue());
        String calculatedCashValue = String.format("%.2f", OperationalUtils.doubleString(calculateCashCompensation()));
        String fetchedDepreciation = String.format("%.2f",settlementDialog.fetchDepreciation());
        assertEquals(fetchedCashValue, calculatedCashValue, "Cash compensation incorrect");
        assertEquals(fetchedDepreciation, calculateDepreciation(), "Depreciation incorrect");
        assertEquals(settlementDialog.getDepreciationValue(),"0");
        settlementDialog
                .applyReductionRuleByValue(claimItem.getAlkaUserReductionRule());
        String fetchedCashValueWithReduction = String.format("%.2f", settlementDialog.cashCompensationValue());
        String fetchedDepreciationWithReduction = String.format("%.2f",settlementDialog.fetchDepreciation());
        assertEquals(fetchedCashValueWithReduction, calculateCashCompensationWithReduction(), "Cash compensation incorrect");
        assertEquals(fetchedDepreciationWithReduction, calculatedReduction(), "Depreciation incorrect");
        assertEquals(settlementDialog.getDepreciationValue(),claimItem.getReductionRule());
        settlementDialog
                .Cancel();
    }

    */
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
     *//*


    @Test(description = "ECC-3031 Verify reduction rule discretionary type after ticking Depreciation automatically updated checkbox")
    public void ecc3031_4_reductionRulePolicyTypeDiscretionaryAutomatic() {
        mainMenu
                .newCustomer();
        newCustomerPage
                .EnterFirstName(client.getFirstName())
                .EnterSurname(client.getLastName())
                .EnterClaimNumber(client.getClaimNumber())
                .SelectPolicyType(client.getPolicyTypeFF())
                .Continue();
        mainMenu
                .claimInfoOpen();
        settlementPage
                .AddManually();
        settlementDialog
                .automaticDepreciation(false)
                .fillDescription(claimItem.getTextFieldSP())
                .fillCustomerDemand(Integer.valueOf(claimItem.getBigCustomDemandPrice()))
                .fillNewPrice(Integer.valueOf(claimItem.getNewPriceSP()))
                .fillCategory(claimItem.getAlkaCategory())
                .fillSubCategory(claimItem.getAlkaSubCategory())
                .enableAge(claimItem.getAgeStatus())
                .enterAgeYears(reductionRule.getAgeFrom2())
                .SelectValuation(claimItem.getValuationType1());
        String fetchedCashValue = String.format("%.2f", settlementDialog.cashCompensationValue());
        String calculatedCashValue = String.format("%.2f", OperationalUtils.doubleString(calculateCashCompensation()));
        String fetchedDepreciation = String.format("%.2f",settlementDialog.fetchDepreciation());
        assertEquals(fetchedCashValue, calculatedCashValue, "Cash compensation incorrect");
        assertEquals(fetchedDepreciation, calculateDepreciation(), "Depreciation incorrect");
        assertEquals(settlementDialog.getDepreciationValue(),"0");
        settlementDialog
                .automaticDepreciation(true)
                .SelectValuation(claimItem.getValuationType1());
        String fetchedCashValueWithReduction = String.format("%.2f", settlementDialog.cashCompensationValue());
        assertEquals(fetchedCashValueWithReduction, calculateCashCompensationWithReduction(), "Cash compensation incorrect");
        String fetchedDepreciationWithReduction = String.format("%.2f",settlementDialog.fetchDepreciation());
        assertEquals(fetchedDepreciationWithReduction, calculatedReduction(), "Depreciation incorrect");
        assertEquals(settlementDialog.getDepreciationValue(),claimItem.getAlkaUserReductionRule());
        settlementDialog
                .Cancel();
    }

    */
/**
     * GIVEN: User logs in as alkauser1
     * WHEN: Enter Category
     * AND: Enter Subcategory
     * AND: Enter new Price
     * AND: age indicated in unpublished policy Rule parameters
     * THEN: Reduction rule row with button does not appear
     * THEN: Value generated according rule settings is not added to the field
     *//*


    @Test(description = "ECC-3031 Verify unpublished rule")
    public void ecc3031_5_reductionRuleUnpublishedPolicy() {
        mainMenu
                .newCustomer();
        newCustomerPage
                .EnterFirstName(client.getFirstName())
                .EnterSurname(client.getLastName())
                .EnterClaimNumber(client.getClaimNumber())
                .SelectPolicyType(client.getPolicyTypeFF())
                .Continue();
        mainMenu
                .claimInfoOpen();
        settlementPage
                .AddManually();
        settlementDialog
                .automaticDepreciation(false)
                .fillDescription(claimItem.getTextFieldSP())
                .fillCustomerDemand(Integer.valueOf(claimItem.getBigCustomDemandPrice()))
                .fillNewPrice(Integer.valueOf(claimItem.getNewPriceSP()))
                .fillCategory(claimItem.getAlkaCategoryUnpublishedPolicy())
                .fillSubCategory(claimItem.getAlkaSubCategoryUnpublishedPolicy())
                .enableAge(claimItem.getAgeStatus())
                .enterAgeYears(reductionRule.getAgeFrom2())
                .SelectValuation(claimItem.getValuationType1());
        String fetchedCashValue = String.format("%.2f", settlementDialog.cashCompensationValue());
        String calculatedCashValue = String.format("%.2f", OperationalUtils.doubleString(calculateCashCompensation()));
        String fetchedDepreciation = String.format("%.2f",settlementDialog.fetchDepreciation());
        assertEquals(fetchedCashValue, calculatedCashValue, "Cash compensation incorrect");
        assertEquals(fetchedDepreciation, calculateDepreciation(), "Depreciation incorrect");
        assertEquals(settlementDialog.getDepreciationValue(),"0");
        settlementDialog
                .Cancel();
    }

    */
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
     *//*


    @Test(description = "ECC-3031 Verify rule with type of Policy indicated after clicking Reduction rule button")
    public void ecc3031_6_reductionRulePolicyTypeIndicated() {
        mainMenu
                .newCustomer();
        newCustomerPage
                .EnterFirstName(client.getFirstName())
                .EnterSurname(client.getLastName())
                .EnterClaimNumber(client.getClaimNumber())
                .SelectPolicyType(client.getPolicyTypeAF())
                .Continue();
        mainMenu
                .claimInfoOpen();
        settlementPage
                .AddManually();
        settlementDialog
                .automaticDepreciation(false)
                .fillDescription(claimItem.getTextFieldSP())
                .fillCustomerDemand(Integer.valueOf(claimItem.getBigCustomDemandPrice()))
                .fillNewPrice(Integer.valueOf(claimItem.getNewPriceSP()))
                .fillCategory(claimItem.getExistingCat3())
                .fillSubCategory(claimItem.getExistingSubCat3())
                .enableAge(claimItem.getAgeStatus())
                .enterAgeYears(reductionRule.getAgeFrom2())
                .SelectValuation(claimItem.getValuationType1());
        String fetchedCashValue = String.format("%.2f", settlementDialog.cashCompensationValue());
        String calculatedCashValue = String.format("%.2f", OperationalUtils.doubleString(calculateCashCompensation()));
        String fetchedDepreciation = String.format("%.2f",settlementDialog.fetchDepreciation());
        assertEquals(fetchedCashValue, calculatedCashValue, "Cash compensation incorrect");
        assertEquals(fetchedDepreciation, calculateDepreciation(), "Depreciation incorrect");
        assertEquals(settlementDialog.getDepreciationValue(),"0");
        settlementDialog
                .applyReductionRuleByValue(claimItem.getAlkaUserReductionRule());
        String fetchedCashValueWithReduction = String.format("%.2f", settlementDialog.cashCompensationValue());
        String fetchedDepreciationWithReduction = String.format("%.2f",settlementDialog.fetchDepreciation());
        assertEquals(fetchedCashValueWithReduction, calculateCashCompensationWithPolicy(), "Cash compensation incorrect");
        assertEquals(fetchedDepreciationWithReduction, calculatedReductionWithPolicy(), "Depreciation incorrect");
        assertEquals(settlementDialog.getDepreciationValue(),claimItem.getAlkaUserReductionRule40());
        settlementDialog
                .Cancel();
    }


     */
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
     *//*


    @Test(description = "ECC-3031 Verify rule with type of Policy indicated after ticking Depreciation automatically updated checkbox")
    public void ecc3031_7_reductionRulePolicyTypeIndicatedAutomatic() {
        mainMenu
                .newCustomer();
        newCustomerPage
                .EnterFirstName(client.getFirstName())
                .EnterSurname(client.getLastName())
                .EnterClaimNumber(client.getClaimNumber())
                .SelectPolicyType(client.getPolicyTypeAF())
                .Continue();
        mainMenu
                .claimInfoOpen();
        settlementPage
                .AddManually();
        settlementDialog
                .automaticDepreciation(false)
                .fillDescription(claimItem.getTextFieldSP())
                .fillCustomerDemand(Integer.valueOf(claimItem.getBigCustomDemandPrice()))
                .fillNewPrice(Integer.valueOf(claimItem.getNewPriceSP()))
                .fillCategory(claimItem.getExistingCat3())
                .fillSubCategory(claimItem.getExistingSubCat3())
                .enableAge(claimItem.getAgeStatus())
                .enterAgeYears(reductionRule.getAgeFrom2())
                .SelectValuation(claimItem.getValuationType1());
        String fetchedCashValue = String.format("%.2f", settlementDialog.cashCompensationValue());
        String calculatedCashValue = String.format("%.2f", OperationalUtils.doubleString(calculateCashCompensation()));
        String fetchedDepreciation = String.format("%.2f",settlementDialog.fetchDepreciation());
        assertEquals(fetchedCashValue, calculatedCashValue, "Cash compensation incorrect");
        assertEquals(fetchedDepreciation, calculateDepreciation(), "Depreciation incorrect");
        assertEquals(settlementDialog.getDepreciationValue(),"0");
        settlementDialog
                .automaticDepreciation(true)
                .SelectValuation(claimItem.getValuationType1());
        String fetchedCashValueWithReduction = String.format("%.2f", settlementDialog.cashCompensationValue());
        String fetchedDepreciationWithReduction = String.format("%.2f",settlementDialog.fetchDepreciation());
        assertEquals(fetchedCashValueWithReduction, calculateCashCompensationWithPolicy(), "Cash compensation incorrect");
        assertEquals(fetchedDepreciationWithReduction, calculatedReductionWithPolicy(), "Depreciation incorrect");
        assertEquals(settlementDialog.getDepreciationValue(),claimItem.getAlkaUserReductionRule40());
        settlementDialog
                .Cancel();
    }

    private String calculateCashCompensation() {
        Double cashCompensation = Double.valueOf(claimItem.getNewPriceSP()) - Double.valueOf(calculateDepreciation());
        return String.valueOf(cashCompensation);
    }

    private String calculateCashCompensationWithReduction() {
        Double cashCompensation = Double.valueOf(claimItem.getNewPriceSP()) - Double.valueOf(calculatedReduction());
        return String.format("%.2f", cashCompensation);
    }

    private String calculateDepreciation() {
        Double depreciation = Double.valueOf(claimItem.getNewPriceSP())* Double.valueOf(settlementDialog.getDepreciationValue()) / 100;
        return String.format("%.2f", depreciation);
    }

    private String calculatedReduction() {
        Double depreciation = Double.valueOf(claimItem.getNewPriceSP())* Double.valueOf(claimItem.getAlkaUserReductionRule()) / 100;
        return String.format("%.2f", depreciation);
    }

    private String calculatedReductionWithPolicy() {
        Double depreciation = Double.valueOf(claimItem.getNewPriceSP())* Double.valueOf(claimItem.getAlkaUserReductionRule40()) / 100;
        return String.format("%.2f", depreciation);
    }

    private String calculateCashCompensationWithPolicy() {
        Double cashCompensation = Double.valueOf(claimItem.getNewPriceSP()) - Double.valueOf(calculatedReductionWithPolicy());
        return String.format("%.2f", cashCompensation);
    }

}*/
