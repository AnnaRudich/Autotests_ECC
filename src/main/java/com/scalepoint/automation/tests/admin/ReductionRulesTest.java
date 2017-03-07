package com.scalepoint.automation.tests.admin;

import com.scalepoint.automation.BaseTest;
import com.scalepoint.automation.pageobjects.pages.admin.AddEditReductionRulePage;
import com.scalepoint.automation.utils.annotations.Jira;
import com.scalepoint.automation.utils.data.entity.RRLinesFields;
import com.scalepoint.automation.utils.data.entity.ReductionRule;
import org.testng.annotations.Test;

import static com.scalepoint.automation.services.usersmanagement.UsersManager.getSystemUser;
import static org.testng.Assert.assertEquals;

@SuppressWarnings("AccessStaticViaInstance")
@Jira("https://jira.scalepoint.com/browse/CHARLIE-546")
public class ReductionRulesTest extends BaseTest {

    private static String sufficientDocumentation = "Sufficient documentation";
    private static String undefined = "Undefined";
    private static String insufficient = "Insufficient documentation";
    private static String yellow = "Yellow";
    private static String green = "Green";
    private static String red = "Red";
    private static String description = "test description";

    /**
     * GIVEN: SP user U1 with Admin permissions
     * WHEN: U1 add reduction rule RR1 form
     * THEN: all necessary fields display with proper naming
     */
    @Test(dataProvider = "testDataProvider",
            description = "ECCD-629, 635  Extend reduction rules lines with PriceRange fields + Documentation and ClaimantRating")
    public void ecc4007_verifyRRLineFields(RRLinesFields ruleFields) {
        AddEditReductionRulePage editPage = toNewReductionRulePage();

        assertEquals(editPage.getDescriptionColumnHeader(), ruleFields.getDescription());
        assertEquals(editPage.getAgeFromColumnHeader(), ruleFields.getAgeFrom());
        assertEquals(editPage.getAgeToColumnHeader(), ruleFields.getAgeTo());
        assertEquals(editPage.getNewItemColumnHeader(), ruleFields.getNewItem());
        assertEquals(editPage.getPriceFromColumnHeader(), ruleFields.getPriceFrom());
        assertEquals(editPage.getPriceToColumnHeader(), ruleFields.getPriceTo());
        assertEquals(editPage.getDocumetationColumnHeader(), ruleFields.getDocumentation());
        assertEquals(editPage.getClaimantRatingColumnHeader(), ruleFields.getClaimantRating());
        assertEquals(editPage.getClaimReductionColumnHeader(), ruleFields.getClaimReduction());
        assertEquals(editPage.getCashReductionColumnHeader(), ruleFields.getCashReduction());
    }

    /**
     * GIVEN: SP user U1 with Admin permissions
     * WHEN: U1 add reduction rule RR1 without overlapping age range and price range
     * THEN: Validation passed RR1 succesfully created
     */
    @Test(dataProvider = "testDataProvider",
            description = "ECCD-629 Extend reduction rules lines with PriceRange fields")
    public void ecc4007_verifyAddRRValidationNoOverlapping(ReductionRule rule) {
        toNewReductionRulePage()
                .fillGeneralForTwoLines(rule)
                .fillAgeRangeForTwoLines("1", "2", "3", "4")
                .fillPriceRangeForTwoLines("0", "1000", "1100", "3000")
                .save()
                .assertRuleDisplayed(rule)
                .deleteRule(rule);
    }


    /**
     * GIVEN: SP user U1 with Admin permissions
     * WHEN: U1 add reduction rule RR1 with same Price Range for two lines; different age
     * THEN: Validation passed RR1 succesfully created
     */
    @Test(dataProvider = "testDataProvider",
            description = "ECCD-629 Extend reduction rules lines with PriceRange fields")
    public void ecc4007_verifyAddRRValidationSamePriceRange(ReductionRule rule) {
        toNewReductionRulePage()
                .fillGeneralForTwoLines(rule)
                .fillAgeRangeForTwoLines("1", "2", "3", "4")
                .fillPriceRangeForTwoLines("0", "1000", "0", "1000")
                .save()
                .assertRuleDisplayed(rule)
                .deleteRule(rule);
    }

    /**
     * GIVEN: SP user U1 with Admin permissions
     * WHEN: U1 add reduction rule RR1 with same age for 2 lines; different priceRange, Rating and documantation Undefined
     * THEN: Validation passed RR1 succesfully created
     */
    @Test(dataProvider = "testDataProvider",
            description = "ECCD-629, 635 Extend reduction rules lines with PriceRange fields")
    public void ecc4007_verifyAddRRValidationSameAgeRange(ReductionRule rule) {
        toNewReductionRulePage()
                .fillGeneralForTwoLines(rule)
                .fillAgeRangeForTwoLines("1", "2", "1", "2")
                .fillPriceRangeForTwoLines("0", "1000", "1001", "3000")
                .save()
                .assertRuleDisplayed(rule)
                .deleteRule(rule);
    }

    /**
     * GIVEN: SP user U1 with Admin permissions
     * WHEN: U1 add reduction rule RR1 with overlap age for 2 lines; different priceRange
     * THEN: Warning appears, but validation passed RR1 succesfully created
     */
    @Test(dataProvider = "testDataProvider",
            description = "ECCD-629 Extend reduction rules lines with PriceRange fields")
    public void ecc4007_verifyAddRRValidationAgeRangeOverlap(ReductionRule rule) throws Exception {
        toNewReductionRulePage()
                .fillGeneralForTwoLines(rule)
                .fillAgeRangeForTwoLines("1", "3", "2", "4")
                .fillPriceRangeForTwoLines("0", "1000", "1001", "3000")
                .saveAndExpectSuccess()
                .assertRuleDisplayed(rule)
                .deleteRule(rule);
    }

    /**
     * GIVEN: SP user U1 with Admin permissions
     * WHEN: U1 add reduction rule RR1 with overlap priceRange; different age
     * THEN: Validation passed RR1 successfully created
     */
    @Test(dataProvider = "testDataProvider",
            description = "ECCD-629 Extend reduction rules lines with PriceRange fields")
    public void ecc4007_verifyAddRRValidationPriceRangeOverlap(ReductionRule rule) {
        toNewReductionRulePage()
                .fillGeneralForTwoLines(rule)
                .fillAgeRangeForTwoLines("1", "2", "3", "4")
                .fillPriceRangeForTwoLines("0", "1000", "500", "2000")
                .save()
                .assertRuleDisplayed(rule)
                .deleteRule(rule);
    }

    /**
     * GIVEN: SP user U1 with Admin permissions
     * WHEN: U1 add reduction rule RR1 with  priceRange from 0 to max in both lines; different age
     * THEN: Validation passed RR1 successfully created
     */
    @Test(dataProvider = "testDataProvider", description = "ECCD-629 Extend reduction rules lines with PriceRange fields")
    public void ecc4007_verifyAddRRValidationForMinToMaxPriceRange(ReductionRule rule) {
        toNewReductionRulePage()
                .fillGeneralForTwoLines(rule)
                .fillAgeRangeForTwoLines("1", "2", "3", "4")
                .fillPriceRangeForTwoLines("0", " ", "0", " ")
                .save()
                .assertRuleDisplayed(rule)
                .deleteRule(rule);
    }

    /**
     * GIVEN: SP user U1 with Admin permissions
     * WHEN: U1 add reduction rule RR1 with  priceRange from 0 to max in one lines overlapping with first line; different age
     * THEN: Validation passed RR1 successfully created
     */
    @Test(dataProvider = "testDataProvider", description = "ECCD-629 Extend reduction rules lines with PriceRange fields")
    public void ecc4007_verifyAddRRValidationForMinToMaxPriceRangeOverlap(ReductionRule rule) {
        toNewReductionRulePage()
                .fillGeneralForTwoLines(rule)
                .fillAgeRangeForTwoLines("1", "2", "3", "4")
                .fillPriceRangeForTwoLines("0", "1000", "0", " ")
                .save()
                .assertRuleDisplayed(rule)
                .deleteRule(rule);
    }

    /**
     * GIVEN: SP user U1 with Admin permissions
     * WHEN: U1 add reduction rule RR1 with  incorrect age range("from" later then "to")
     * THEN: Validation failed; RR1 not created
     */
    @Test(dataProvider = "testDataProvider", description = "ECCD-629 Extend reduction rules lines with PriceRange fields")
    public void ecc4007_verifyFailRRValidationForIncorrectAgeRange(ReductionRule rule) {
        toNewReductionRulePage()
                .fillGeneralForTwoLines(rule)
                .fillAgeRangeForTwoLines("2", "1", "3", "4")
                .fillPriceRangeForTwoLines("0", " ", "0", " ")
                .saveAndExpectWarning()
                .assertDescriptionColumnHeaderPresent()
                .cancel()
                .searchRule(rule)
                .assertRuleNotDisplayed(rule);
    }

    /**
     * GIVEN: SP user U1 with Admin permissions
     * WHEN: U1 add reduction rule RR1 with  incorrect price range("from" bigger then "to")
     * THEN: Validation failed; RR1 not created
     */
    @Test(dataProvider = "testDataProvider", description = "ECCD-629 Extend reduction rules lines with PriceRange fields")
    public void ecc4007_verifyFailRRValidationForIncorrectPriceRange(ReductionRule rule) {
        toNewReductionRulePage()
                .fillGeneralForTwoLines(rule)
                .fillAgeRangeForTwoLines("1", "2", "3", "4")
                .fillPriceRangeForTwoLines("2000", "1000", "1001", "2000")
                .saveAndExpectWarning()
                .assertDescriptionColumnHeaderPresent()
                .cancel()
                .searchRule(rule)
                .assertRuleNotDisplayed(rule);
    }

    /**
     * GIVEN: SP user U1 with Admin permissions
     * WHEN: U1 add reduction rule RR1 with  different documentation and same age
     * THEN: Validation passed; RR1 created
     */
    @Test(dataProvider = "testDataProvider", description = "ECCD-629 Extend reduction rules lines with PriceRange fields")
    public void ecc4007_verifyAddRRValidationDocumentationDiffersSameAge(ReductionRule rule) {
        toNewReductionRulePage()
                .fillGeneralForTwoLines(rule)
                .fillAgeRangeForTwoLines("1", "2", "1", "2")
                .fillPriceRangeForTwoLines("0", "1000", "500", "3000")
                .selectDocumentationDropValue(1, sufficientDocumentation)
                .selectRatingDropValue(0, green)
                .selectRatingDropValue(1, yellow)
                .save()
                .assertRuleDisplayed(rule)
                .deleteRule(rule);
    }

    /**
     * GIVEN: SP user U1 with Admin permissions
     * WHEN: U1 add reduction rule RR1 with  different documentation and same Price range
     * THEN: Validation passed; RR1 created
     */
    @Test(dataProvider = "testDataProvider", description = "ECCD-629 Extend reduction rules lines with PriceRange fields")
    public void ecc4007_verifyAddRRValidationDocumentationDiffersSamePrice(ReductionRule rule) {
        toNewReductionRulePage()
                .fillGeneralForTwoLines(rule)
                .fillAgeRangeForTwoLines("1", "2", "3", "4")
                .fillPriceRangeForTwoLines("0", "1000", "0", "1000")
                .selectDocumentationDropValue(1, sufficientDocumentation)
                .save()
                .assertRuleDisplayed(rule)
                .deleteRule(rule);
    }

    /**
     * GIVEN: SP user U1 with Admin permissions
     * WHEN: U1 add reduction rule RR1 with  different claimant rating and same age
     * THEN: Validation passed; RR1 created
     */
    @Test(dataProvider = "testDataProvider", description = "ECCD-629 Extend reduction rules lines with PriceRange fields")
    public void ecc4007_verifyAddRRValidationRatingDiffersSameAge(ReductionRule rule) {
        toNewReductionRulePage()
                .fillGeneralForTwoLines(rule)
                .fillAgeRangeForTwoLines("1", "2", "1", "2")
                .fillPriceRangeForTwoLines("0", "1000", "0", " ")
                .selectDocumentationDropValue(1, sufficientDocumentation)
                .selectRatingDropValue(0, green)
                .selectRatingDropValue(1, yellow)
                .save()
                .assertRuleDisplayed(rule)
                .deleteRule(rule);
    }

    /**
     * GIVEN: SP user U1 with Admin permissions
     * WHEN: U1 add reduction rule RR1 with  different claimant rating and price range/age overlap
     * THEN: Validation passed, warning appears; RR1 created
     */
    @Test(dataProvider = "testDataProvider", description = "ECCD-629 Extend reduction rules lines with PriceRange fields")
    public void ecc4007_verifyAddRRValidationRatingDiffersOverlapPriceAge(ReductionRule rule) {
        toNewReductionRulePage()
                .fillGeneralForTwoLines(rule)
                .fillAgeRangeForTwoLines("1", "3", "2", "4")
                .fillPriceRangeForTwoLines("0", "1000", "0", " ")
                .selectDocumentationDropValue(1, sufficientDocumentation)
                .selectRatingDropValue(0, green)
                .selectRatingDropValue(1, yellow)
                .saveAndExpectSuccess()
                .assertRuleDisplayed(rule)
                .deleteRule(rule);
    }

    /**
     * GIVEN: SP user U1 with Admin permissions
     * WHEN: U1 add reduction rule RR1 with 3 lines  different claimant rating and price range/age overlap
     * THEN: Validation failed; RR1 NOT created
     */
    @Test(dataProvider = "testDataProvider", description = "ECCD-629 Extend reduction rules lines with PriceRange fields")
    public void ecc4007_verifyFailRRValidationThreeLinesOverlap(ReductionRule rule) {
        toNewReductionRulePage()
                .fillGeneralForTwoLines(rule)
                .fillDescriptionForLine(2, description)
                .fillClaimReductionForLine(2, "32")
                .fillAgeRangeForLine(0, "1", "2")
                .fillAgeRangeForLine(1, "1", "2")
                .fillAgeRangeForLine(2, "1", "2")
                .fillPriceRangeForLine(0, "0", "1000")
                .fillPriceRangeForLine(1, "0", "1000")
                .fillPriceRangeForLine(2, "1000", "2000")
                .selectDocumentationDropValue(0, sufficientDocumentation)
                .selectDocumentationDropValue(1, sufficientDocumentation)
                .selectDocumentationDropValue(2, sufficientDocumentation)
                .selectRatingDropValue(0, green)
                .selectRatingDropValue(1, red)
                .selectRatingDropValue(2, green)
                .saveAndExpectWarning()
                .cancel()
                .searchRule(rule)
                .assertRuleNotDisplayed(rule);
    }


    /**
     * GIVEN: SP user U1 with Admin permissions
     * WHEN: U1 add reduction rule RR1
     * THEN: Documentation combobox contains necessary fields
     */
    @Test(description = "ECCD-635 Extend reduction rule line with Documentation and Claimant rating fields")
    public void ecc3951_verifyAddRRDocumentationComboboxValues() {
        String[] expectedDocumentationComboboxValues = {undefined, sufficientDocumentation, insufficient};
        toNewReductionRulePage()
                .assertDocumentationComboboxValuesAre(expectedDocumentationComboboxValues);
    }

    /**
     * GIVEN: SP user U1 with Admin permissions
     * WHEN: U1 add reduction rule RR1
     * THEN: rating combobox contains necessary fields
     */
    @Test(description = "ECCD-635 Extend reduction rule line with Documentation and Claimant rating fields")
    public void ecc3951_verifyAddRRRatingComboboxValues() {
        String[] expectedRatingComboboxValues = {undefined, green, yellow, red};
        toNewReductionRulePage()
                .assertRatingComboboxValuesAre(expectedRatingComboboxValues);
    }

    /**
     * GIVEN: SP user U1 with Admin permissions
     * WHEN: U1 add reduction rule RR1 with  same 2 lines
     * THEN: Validation failed; RR1 NOT created
     */
    @Test(dataProvider = "testDataProvider", description = "ECCD-635 Extend reduction rule line with Documentation and Claimant rating fields")
    public void ecc3951_verifyFailRRValidationDuplicatedLines(ReductionRule rule) {
        toNewReductionRulePage()
                .fillGeneralForTwoLines(rule)
                .fillAgeRangeForTwoLines("0", "12", "0", "12")
                .fillPriceRangeForTwoLines("100", "200", "100", "200")
                .saveAndExpectWarning()
                .cancel()
                .searchRule(rule)
                .assertRuleNotDisplayed(rule);
    }

    /**
     * GIVEN: SP user U1 with Admin permissions
     * WHEN: U1 add reduction rule RR1 with  same 2 lines (Underfined doc containes Sufficient; underfined rating contains Green )
     * THEN: Validation failed; RR1 NOT created
     */
    @Test(dataProvider = "testDataProvider", description = "ECCD-635 Extend reduction rule line with Documentation and Claimant rating fields")
    public void ecc3951_verifyFailRRValidationDocumentationAndRatingOverlap(ReductionRule rule) {
        toNewReductionRulePage()
                .fillGeneralForTwoLines(rule)
                .fillAgeRangeForTwoLines("0", "12", "0", "12")
                .fillPriceRangeForTwoLines("100", "200", "100", "200")
                .selectDocumentationDropValue(0, sufficientDocumentation)
                .selectRatingDropValue(1, green)
                .saveAndExpectWarning()
                .cancel()
                .searchRule(rule)
                .assertRuleNotDisplayed(rule);
    }

    /**
     * GIVEN: SP user U1 with Admin permissions
     * WHEN: U1 add reduction rule RR1 with  same 2 lines documetation differs (Underfined doc containes Sufficient)
     * THEN: Validation failed; RR1 NOT created
     */
    @Test(dataProvider = "testDataProvider", description = "ECCD-635 Extend reduction rule line with Documentation and Claimant rating fields")
    public void ecc3951_verifyFailRRValidationSameRatingDocumentationOverlap(ReductionRule rule) {
        toNewReductionRulePage()
                .fillGeneralForTwoLines(rule)
                .fillAgeRangeForTwoLines("0", "12", "0", "12")
                .fillPriceRangeForTwoLines("100", "1000", "100", "1000")
                .selectDocumentationDropValue(0, sufficientDocumentation)
                .selectRatingDropValue(0, green)
                .selectRatingDropValue(1, green)
                .saveAndExpectWarning()
                .cancel()
                .searchRule(rule)
                .assertRuleNotDisplayed(rule);
    }

    /**
     * GIVEN: SP user U1 with Admin permissions
     * WHEN: U1 add reduction rule RR1 with  same 2 lines same documetation, rating overlap
     * THEN: Validation failed; RR1 NOT created
     */
    @Test(dataProvider = "testDataProvider", description = "ECCD-635 Extend reduction rule line with Documentation and Claimant rating fields")
    public void ecc3951_verifyFailRRValidationSameDocumentationRatingOverlap(ReductionRule rule) {
        toNewReductionRulePage()
                .fillGeneralForTwoLines(rule)
                .fillAgeRangeForTwoLines("0", "12", "0", "12")
                .fillPriceRangeForTwoLines("100", "1000", "100", "1000")
                .selectRatingDropValue(1, green)
                .saveAndExpectWarning()
                .cancel()
                .searchRule(rule)
                .assertRuleNotDisplayed(rule);
    }

    /**
     * GIVEN: SP user U1 with Admin permissions
     * WHEN: U1 add reduction rule RR1 with  different claimant rating and overlap documentation
     * THEN: Validation passed, warning appears; RR1 created
     */
    @Test(dataProvider = "testDataProvider", description = "ECCD-635 Extend reduction rule line with Documentation and Claimant rating fields")
    public void ecc3951_verifyAddRRValidationRatingDiffersDocumentationOverlap(ReductionRule rule) {
        toNewReductionRulePage()
                .fillGeneralForTwoLines(rule)
                .fillAgeRangeForTwoLines("0", "12", "0", "12")
                .fillPriceRangeForTwoLines("100", "1000", "100", "1000")
                .selectDocumentationDropValue(1, sufficientDocumentation)
                .selectRatingDropValue(0, green)
                .selectRatingDropValue(1, yellow)
                .save()
                .assertRuleDisplayed(rule)
                .deleteRule(rule);
    }

    /**
     * GIVEN: SP user U1 with Admin permissions
     * WHEN: U1 add reduction rule RR1 with  different documentation  and rating overlap
     * THEN: Validation passed, warning appears; RR1 created
     */
    @Test(dataProvider = "testDataProvider", description = "ECCD-635 Extend reduction rule line with Documentation and Claimant rating fields")
    public void ecc3951_verifyAddRRValidationDocumentationDiffersRatingOverlap(ReductionRule rule) {
        toNewReductionRulePage()
                .fillGeneralForTwoLines(rule)
                .fillAgeRangeForTwoLines("0", "12", "0", "12")
                .fillPriceRangeForTwoLines("100", "1000", "100", "1000")
                .selectDocumentationDropValue(0, sufficientDocumentation)
                .selectDocumentationDropValue(1, insufficient)
                .selectRatingDropValue(1, green)
                .save()
                .assertRuleDisplayed(rule)
                .deleteRule(rule);
    }

    /**
     * GIVEN: SP user U1 with Admin permissions
     * WHEN: U1 add reduction rule RR1 with  different claimant rating and different documentation
     * THEN: Validation passed, warning appears; RR1 created
     */
    @Test(dataProvider = "testDataProvider", description = "ECCD-635 Extend reduction rule line with Documentation and Claimant rating fields")
    public void ecc3951_verifyAddRRValidationRatingAndDocumentationDiffers(ReductionRule rule) {
        toNewReductionRulePage()
                .fillGeneralForTwoLines(rule)
                .fillAgeRangeForTwoLines("0", "12", "0", "12")
                .fillPriceRangeForTwoLines("100", "1000", "100", "1000")
                .selectDocumentationDropValue(0, sufficientDocumentation)
                .selectDocumentationDropValue(1, insufficient)
                .selectRatingDropValue(0, green)
                .selectRatingDropValue(1, yellow)
                .save()
                .assertRuleDisplayed(rule)
                .deleteRule(rule);
    }

    /**
     * GIVEN: SP user U1 with Admin permissions
     * WHEN: U1 add reduction rule RR1 with  same claimant rating and different documentation
     * THEN: Validation passed, warning appears; RR1 created
     */
    @Test(dataProvider = "testDataProvider", description = "ECCD-635 Extend reduction rule line with Documentation and Claimant rating fields")
    public void ecc3951_verifyAddRRValidationSameRatingAndDocumentationDiffers(ReductionRule rule) {
        toNewReductionRulePage()
                .fillGeneralForTwoLines(rule)
                .fillAgeRangeForTwoLines("0", "12", "0", "12")
                .fillPriceRangeForTwoLines("100", "1000", "100", "1000")
                .selectDocumentationDropValue(0, sufficientDocumentation)
                .selectDocumentationDropValue(1, insufficient)
                .selectRatingDropValue(0, green)
                .selectRatingDropValue(1, green)
                .save()
                .assertRuleDisplayed(rule)
                .deleteRule(rule);
    }

    /**
     * GIVEN: SP user U1 with Admin permissions
     * WHEN: U1 add reduction rule RR1 with  same claimant rating and overlap documentation, same age and price range differs
     * THEN: Validation passed, warning appears; RR1 created
     */
    @Test(dataProvider = "testDataProvider", description = "ECCD-635 Extend reduction rule line with Documentation and Claimant rating fields")
    public void ecc3951_verifyAddRRValidationSameRatingAndAgeDocumentationOverlapPriceRangeDiffers(ReductionRule rule) {
        toNewReductionRulePage()
                .fillGeneralForTwoLines(rule)
                .fillAgeRangeForTwoLines("0", "12", "0", "12")
                .fillPriceRangeForTwoLines("100", "1000", "1001", "2000")
                .selectDocumentationDropValue(0, sufficientDocumentation)
                .selectRatingDropValue(0, green)
                .selectRatingDropValue(1, green)
                .save()
                .assertRuleDisplayed(rule)
                .deleteRule(rule);
    }

    /**
     * GIVEN: SP user U1 with Admin permissions
     * WHEN: U1 add reduction rule RR1 with  same claimant rating and overlap documentation, same age and price range differs
     * THEN: Validation passed, warning appears; RR1 created
     */
    @Test(dataProvider = "testDataProvider", description = "ECCD-635 Extend reduction rule line with Documentation and Claimant rating fields")
    public void ecc3951_verifyAddRRValidationSameRatingAndAgeAndDocumentationButPriceRangeDiffers(ReductionRule rule) {
        toNewReductionRulePage()
                .fillGeneralForTwoLines(rule)
                .fillAgeRangeForTwoLines("0", "12", "0", "12")
                .fillPriceRangeForTwoLines("0", "1000", "1001", "3000")
                .save()
                .assertRuleDisplayed(rule)
                .deleteRule(rule);
    }

    /**
     * GIVEN: SP user U1 with Admin permissions
     * WHEN: U1 add reduction rule RR1 with  overlap claimant rating and overlap documentation, same age and price range differs
     * THEN: Validation passed, warning appears; RR1 created
     */
    @Test(dataProvider = "testDataProvider", description = "ECCD-635 Extend reduction rule line with Documentation and Claimant rating fields")
    public void ecc3951_verifyAddRRValidationSameAgeOverlapDocumentationAndRatingButPriceRangeDiffers(ReductionRule rule) {
        toNewReductionRulePage()
                .fillGeneralForTwoLines(rule)
                .fillAgeRangeForTwoLines("0", "12", "0", "12")
                .fillPriceRangeForTwoLines("0", "1000", "1001", "3000")
                .selectDocumentationDropValue(0, sufficientDocumentation)
                .selectRatingDropValue(1, green)
                .save()
                .assertRuleDisplayed(rule)
                .deleteRule(rule);
    }

    /**
     * GIVEN: SP user U1 with Admin permissions
     * WHEN: U1 add reduction rule RR1 with  same claimant rating and same price range,and overlap documentation, but age differs
     * THEN: Validation passed, warning appears; RR1 created
     */
    @Test(dataProvider = "testDataProvider", description = "ECCD-635 Extend reduction rule line with Documentation and Claimant rating fields")
    public void ecc3951_verifyAddRRValidationSameRatingAndPriceRangeAndDocumentationOverlapButAgeDiffers(ReductionRule rule) {
        toNewReductionRulePage()
                .fillGeneralForTwoLines(rule)
                .fillAgeRangeForTwoLines("0", "12", "13", "24")
                .fillPriceRangeForTwoLines("100", "1000", "100", "1000")
                .selectDocumentationDropValue(0, sufficientDocumentation)
                .selectRatingDropValue(0, green)
                .selectRatingDropValue(1, green)
                .save()
                .assertRuleDisplayed(rule)
                .deleteRule(rule);
    }

    /**
     * GIVEN: SP user U1 with Admin permissions
     * WHEN: U1 add reduction rule RR1 with  same claimant rating and same price range and documentation, but age differs
     * THEN: Validation passed, warning appears; RR1 created
     */
    @Test(dataProvider = "testDataProvider", description = "ECCD-635 Extend reduction rule line with Documentation and Claimant rating fields")
    public void ecc3951_verifyAddRRValidationSamePriceAndDocumentationAndRatingButAgeDiffers(ReductionRule rule) {
        toNewReductionRulePage()
                .fillGeneralForTwoLines(rule)
                .fillAgeRangeForTwoLines("0", "12", "13", "24")
                .fillPriceRangeForTwoLines("100", "1000", "100", "1000")
                .selectDocumentationDropValue(0, sufficientDocumentation)
                .selectDocumentationDropValue(1, sufficientDocumentation)
                .selectRatingDropValue(0, green)
                .selectRatingDropValue(1, green)
                .save()
                .assertRuleDisplayed(rule)
                .deleteRule(rule);
    }

    /**
     * GIVEN: SP user U1 with Admin permissions
     * WHEN: U1 add reduction rule RR1 with  same same price range and documentation overlap, but age and claimant rating differs
     * THEN: Validation passed, warning appears; RR1 created
     */
    @Test(dataProvider = "testDataProvider", description = "ECCD-635 Extend reduction rule line with Documentation and Claimant rating fields")
    public void ecc3951_verifyAddRRValidationSamePriceAndDocumentationOverlapButRatingAndAgeDiffers(ReductionRule rule) {
        toNewReductionRulePage()
                .fillGeneralForTwoLines(rule)
                .fillAgeRangeForTwoLines("0", "12", "13", "24")
                .fillPriceRangeForTwoLines("100", "1000", "100", "1000")
                .selectDocumentationDropValue(1, sufficientDocumentation)
                .selectRatingDropValue(0, green)
                .selectRatingDropValue(1, yellow)
                .save()
                .assertRuleDisplayed(rule)
                .deleteRule(rule);
    }

    /**
     * GIVEN: SP user U1 with Admin permissions
     * WHEN: U1 add reduction rule RR1 with  same same price range but documentation overlap and age, and claimant rating differs
     * THEN: Validation passed, warning appears; RR1 created
     */
    @Test(dataProvider = "testDataProvider", description = "ECCD-635 Extend reduction rule line with Documentation and Claimant rating fields")
    public void ecc3951_verifyAddRRValidationSamePriceButDocumentationAndRatingAndAgeDiffers(ReductionRule rule) {
        toNewReductionRulePage()
                .fillGeneralForTwoLines(rule)
                .fillAgeRangeForTwoLines("0", "12", "13", "24")
                .fillPriceRangeForTwoLines("100", "1000", "100", "1000")
                .selectDocumentationDropValue(0, sufficientDocumentation)
                .selectDocumentationDropValue(1, insufficient)
                .selectRatingDropValue(0, green)
                .selectRatingDropValue(1, yellow)
                .save()
                .assertRuleDisplayed(rule)
                .deleteRule(rule);
    }

    /**
     * GIVEN: SP user U1 with Admin permissions
     * WHEN: U1 add reduction rule RR1 with 3 lines  different age and same all other fields
     * THEN: Validation passed; RR1  created
     */
    @Test(dataProvider = "testDataProvider", description = "ECCD-635 Extend reduction rule line with Documentation and Claimant rating fields")
    public void ecc3951_verifyAddRRValidationThreeLinesSamePriceAndDocumentationAndRatingButAgeDiffers(ReductionRule rule) {
        toNewReductionRulePage()
                .fillGeneralForTwoLines(rule)
                .fillDescriptionForLine(2, description)
                .fillClaimReductionForLine(2, "32")
                .fillAgeRangeForLine(0, "1", "12")
                .fillAgeRangeForLine(1, "13", "24")
                .fillAgeRangeForLine(2, "25", "32")
                .fillPriceRangeForLine(0, "0", "1000")
                .fillPriceRangeForLine(1, "0", "1000")
                .fillPriceRangeForLine(2, "0", "1000")
                .save()
                .assertRuleDisplayed(rule)
                .deleteRule(rule);
    }

    /**
     * GIVEN: SP user U1 with Admin permissions
     * WHEN: U1 add reduction rule RR1 with 3 lines  different age and same all other fields
     * THEN: Validation passed; RR1  created
     */
    @Test(dataProvider = "testDataProvider", description = "ECCD-635 Extend reduction rule line with Documentation and Claimant rating fields")
    public void ecc3951_verifyAddRRValidationSameAgeAndRatingPriceRangeOverlapButDocumentationDiffers(ReductionRule rule) {
        toNewReductionRulePage()
                .fillGeneralForTwoLines(rule)
                .fillAgeRangeForTwoLines("0", "12", "0", "12")
                .fillPriceRangeForTwoLines("100", "1000", "500", "4000")
                .selectDocumentationDropValue(0, sufficientDocumentation)
                .selectDocumentationDropValue(1, insufficient)
                .selectRatingDropValue(0, green)
                .selectRatingDropValue(1, green)
                .save()
                .assertRuleDisplayed(rule)
                .deleteRule(rule);
    }

    /**
     * GIVEN: SP user U1 with Admin permissions
     * WHEN: U1 add reduction rule RR1 with 3 lines overlap Pricerange and same all other fields
     * THEN: Validation failed; RR1 Not created
     */
    @Test(dataProvider = "testDataProvider", description = "ECCD-635 Extend reduction rule line with Documentation and Claimant rating fields")
    public void ecc3951_verifyFailRRValidationThreeLinesSameAgeAndRatingAndDocumentationButPriceRangeOverlap(ReductionRule rule) {
        toNewReductionRulePage()
                .fillGeneralForTwoLines(rule)
                .fillDescriptionForLine(2, description)
                .fillClaimReductionForLine(2, "32")
                .fillAgeRangeForLine(0, "0", "12")
                .fillAgeRangeForLine(1, "0", "12")
                .fillAgeRangeForLine(2, "0", "12")
                .fillPriceRangeForLine(0, "100", "1000")
                .fillPriceRangeForLine(1, "500", "4000")
                .fillPriceRangeForLine(2, "4100", "6000")
                .saveAndExpectWarning()
                .cancel()
                .searchRule(rule)
                .assertRuleNotDisplayed(rule);
    }

    private AddEditReductionRulePage toNewReductionRulePage() {
        return login(getSystemUser(), AddEditReductionRulePage.class);
    }
}
