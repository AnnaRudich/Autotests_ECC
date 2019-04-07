package com.scalepoint.automation.tests.sid;

import com.scalepoint.automation.pageobjects.dialogs.SettlementDialog;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.services.usersmanagement.CompanyCode;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.utils.Constants;
import com.scalepoint.automation.utils.annotations.Jira;
import com.scalepoint.automation.utils.annotations.UserCompany;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.ClaimItem;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.testng.annotations.Test;

import static com.scalepoint.automation.pageobjects.dialogs.SettlementDialog.Valuation.NEW_PRICE;

@Jira("https://jira.scalepoint.com/browse/CHARLIE-505")
@RequiredSetting(type = FTSetting.SHOW_POLICY_TYPE, enabled = false)
public class FillCalcDepr2RulesAndDepreciationTests extends BaseTest {

    /**
     * GIVEN: Checkbox 'Automatic update of depreciation' is checked in SID
     * AND: log in as *tryguser1*
     * WHEN: Open SID and select sategory - Cykler & Tilbehør - Cykler
     * AND: Set age 5 y + 6 m  New Price=200
     * AND: IF all the rule conditions are met
     * THEN: depreciation is applied automatically
     * AND: the depreciation value is displayed in the depreciation input =41%
     */
    @Test(dataProvider = "testDataProvider", description = "CHARLIE-505 Verify automatic overwrite of the depreciation field")
    public void charlie_505_1_verifyAutomaticOverwriteDepreciationField(@UserCompany(CompanyCode.TRYGFORSIKRING) User user, Claim claim, ClaimItem claimItem) {
        createClaimAndPrepareSid(user, claim, claimItem)
                .parseValuationRow(NEW_PRICE)
                .doAssert(row -> row.assertDepreciationPercentageIs(41));
    }

    /**
     * GIVEN: Checkbox 'Automatic update of depreciation' is checked in SID
     * AND: log in as *tryguser1*
     * WHEN: Open SID and select sategory - Cykler & Tilbehør - Cykler
     * AND: Set age 5 y + 6 m  New Price=200
     * AND: IF all the rule conditions are met
     * THEN: depreciation is applied automatically
     * AND: the depreciation value is displayed in the depreciation input =41%
     * WNEN: change age to be 6 y + 6 m
     * THEN: the depreciation value is displayed in the depreciation input =47%
     * WHEN: Save SID
     * AND: Reopen SID
     * THEN: the depreciation value is displayed in the depreciation input =47%
     */
    @Test(dataProvider = "testDataProvider", description = "CHARLIE-505 Update automatic overwrite of the depreciation field")
    public void charlie_505_2_3_updateAgeAutomaticOverwriteDepreciationField(@UserCompany(CompanyCode.TRYGFORSIKRING) User user, Claim claim, ClaimItem claimItem) {
        createClaimAndPrepareSid(user, claim, claimItem)
                .parseValuationRow(NEW_PRICE)
                .doAssert(sid -> sid.assertDepreciationPercentageIs(41))
                .enterAgeYears("6")
                .automaticDepreciation(true)
                .parseValuationRow(NEW_PRICE)
                .makeActive()
                .doAssert(row -> row.assertDepreciationPercentageIs(47))
                .closeSidWithOk()
                .findClaimLine(claimItem.getTextFieldSP())
                .editLine()
                .parseValuationRow(NEW_PRICE)
                .doAssert(row -> row.assertDepreciationPercentageIs(47));
    }

    /**
     * GIVEN: Checkbox 'Automatic update of depreciation' is checked in SID
     * AND: log in as *tryguser1*
     * WHEN: Open SID and select sategory - Cykler & Tilbehør - Cykler
     * AND: Set age 5 y + 6 m  New Price=200
     * AND: IF all the rule conditions are met
     * THEN: depreciation is applied automatically
     * AND: the depreciation value is displayed in the depreciation input =41%
     * WHEN: change category to be Personlig Pleje-> Medicin (no rule is mapped to this category)
     * THEN: depreciation field is automatically reset to 0
     */
    @Test(dataProvider = "testDataProvider", description = "CHARLIE-505 Verify that changing category automatically reset of the depreciation field to 0")
    public void charlie_505_4_changeCategoryResetDepreciationField(@UserCompany(CompanyCode.TRYGFORSIKRING) User user, Claim claim, ClaimItem claimItem) {
        createClaimAndPrepareSid(user, claim, claimItem)
                .parseValuationRow(NEW_PRICE)
                .doAssert(row -> row.assertDepreciationPercentageIs(41))
                .setCategory(claimItem.getCategoryPersonalMedicine())
                .automaticDepreciation(true)
                .parseValuationRow(NEW_PRICE)
                .makeActive()
                .doAssert(row -> row.assertDepreciationPercentageIs(0));
    }

    /**
     * GIVEN: Checkbox 'Automatic update of depreciation' is checked in SID
     * AND: log in as *tryguser1*
     * WHEN: Open SID and select sategory - Cykler & Tilbehør - Cykler
     * AND: Set age 5 y + 6 m  New Price=200
     * AND: IF all the rule conditions are met
     * THEN: depreciation is applied automatically
     * AND: the depreciation value is displayed in the depreciation input =41%
     * WHEN: Select other age other than specified in the rules (no rule is mapped to this category)
     * THEN: depreciation field is automatically reset to 0
     * SUMMARY: change any other parameter so that no match to rule age
     */
    @Test(dataProvider = "testDataProvider", description = "CHARLIE-505 Verify that select other age than specified in the rules automatically reset of the depreciation field to 0")
    public void charlie_505_5_changeAgeResetDepreciationField(@UserCompany(CompanyCode.TRYGFORSIKRING) User user, Claim claim, ClaimItem claimItem) {
        createClaimAndPrepareSid(user, claim, claimItem)
                .parseValuationRow(NEW_PRICE)
                .doAssert(row -> row.assertDepreciationPercentageIs(41))
                .enterAgeYears("0")
                .automaticDepreciation(true)
                .parseValuationRow(NEW_PRICE)
                .makeActive()
                .doAssert(row -> row.assertDepreciationPercentageIs(0));
    }

    private SettlementDialog createClaimAndPrepareSid(User user, Claim claim, ClaimItem claimItem) {
        return loginAndCreateClaim(user, claim).
                openSid().
                setDescription(claimItem.getTextFieldSP()).
                setCustomerDemand(Constants.PRICE_100_000).
                setNewPrice(claimItem.getTrygNewPrice()).
                setCategory(claimItem.getCategoryBicycles()).
                automaticDepreciation(true).
                enableAge().
                enterAgeYears("5").
                selectMonth("6").
                setValuation(NEW_PRICE);
    }
}
