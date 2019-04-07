package com.scalepoint.automation.tests.sid;

import com.scalepoint.automation.pageobjects.dialogs.SettlementDialog;
import com.scalepoint.automation.pageobjects.dialogs.SettlementDialog.DepreciationType;
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

import static com.scalepoint.automation.pageobjects.dialogs.SettlementDialog.Valuation.DISCRETIONARY;
import static com.scalepoint.automation.pageobjects.dialogs.SettlementDialog.Valuation.NEW_PRICE;

@Jira("https://jira.scalepoint.com/browse/CHARLIE-508")
@RequiredSetting(type = FTSetting.SHOW_DISCREATIONARY_REASON)
@RequiredSetting(type = FTSetting.SHOW_POLICY_TYPE, enabled = false)
@RequiredSetting(type = FTSetting.MAKE_DISCREATIONARY_REASON_MANDATORY)
@RequiredSetting(type = FTSetting.SHOW_NOT_CHEAPEST_CHOICE_POPUP, enabled = false)
public class DiscretionaryReasonMandatoryTests extends BaseTest {

    /*
     * WHEN: Enable the Make "Discretionary reason" mandatory to fill option in FT
     * AND: Add the item with discretionary depreciation
     * AND: Select depreciable valuation
     * AND: click OK button
     * THEN: the field has red frame; the dialog is not closed
     */
    @Test(dataProvider = "testDataProvider", description = "CHARLIE-508 Verify FT Make 'Discretionary reason' mandatory to fill option while adding discretionary depreciation")
    public void charlie_508_1_verifyDiscretionaryReasonField(@UserCompany(CompanyCode.TRYGFORSIKRING) User user,
                                                             Claim claim,
                                                             ClaimItem claimItem) {
        createClaimAndFillSid(user, claim, claimItem)
                .setDepreciation(Constants.DEPRECIATION_10)
                .setDepreciationType(DepreciationType.DISCRETIONARY)
                .setValuation(NEW_PRICE)
                .clickOK()
                .doAssert(sid -> {
                    sid.assertDiscretionaryReasonEnabled();
                    sid.assertDiscretionaryReasonHasRedBorder();
                });
    }

    /*
     * WHEN: Enable the Make "Discretionary reason" mandatory to fill option in FT
     * AND: Add the item with discretionary valuation
     * AND: Select depreciable valuation
     * AND: click OK button
     * THEN: the field has red frame; the dialog is not closed
     */
    @Test(dataProvider = "testDataProvider", description = "CHARLIE-508 Verify FT Make 'Discretionary reason' mandatory to fill option")
    public void charlie_508_2_verifyDiscretionaryReasonField(@UserCompany(CompanyCode.TRYGFORSIKRING) User user,
                                                             Claim claim,
                                                             ClaimItem claimItem) {
        createClaimAndFillSid(user, claim, claimItem)
                .setDiscretionaryPrice(300.00)
                .setValuation(DISCRETIONARY)
                .clickOK()
                .doAssert(sid -> {
                    sid.assertDiscretionaryReasonEnabled();
                    sid.assertDiscretionaryReasonHasRedBorder();
                });
    }

    /*
     * WHEN: Enable the Make "Discretionary reason" mandatory to fill option in FT
     * AND: Add the item with discretionary depreciation
     * AND: Select non-depreciable valuation
     * AND: click OK button
     * THEN: the field is disabled; the dialog is closed
     */
    @Test(dataProvider = "testDataProvider", description = "CHARLIE-508 Verify FT Make 'Discretionary reason' mandatory to fill option while adding discretionary depreciation")
    public void charlie_508_3_verifyDiscretionaryReasonField(@UserCompany(CompanyCode.TRYGFORSIKRING) User user,
                                                             Claim claim,
                                                             ClaimItem claimItem) {
        createClaimAndFillSid(user, claim, claimItem)
                .setDepreciation(Constants.DEPRECIATION_10)
                .setDepreciationType(DepreciationType.DISCRETIONARY)
                .doAssert(sid -> {
                    sid.assertDiscretionaryReasonDisabled();
                    sid.assertDiscretionaryReasonHasNormalBorder();
                });
    }


    /*
     * WHEN: Enable the Make "Discretionary reason" mandatory to fill option in FT
     * AND: Add the item with policy depreciation
     * AND: Select depreciable valuation
     * AND: click OK button
     * THEN: the field is disabled; the dialog is closed
     */
    @Test(dataProvider = "testDataProvider", description = "CHARLIE-508 Verify FT Make 'Discretionary reason' mandatory to fill option while adding policy depreciation")
    public void charlie_508_4_verifyDiscretionaryReasonField(@UserCompany(CompanyCode.TRYGFORSIKRING) User user,
                                                             Claim claim,
                                                             ClaimItem claimItem) {
        createClaimAndFillSid(user, claim, claimItem)
                .setDepreciation(Constants.DEPRECIATION_10)
                .setDepreciationType(DepreciationType.POLICY)
                .setValuation(NEW_PRICE)
                .doAssert(sid -> {
                    sid.assertDiscretionaryReasonDisabled();
                    sid.assertDiscretionaryReasonHasNormalBorder();
                });
    }

    /*
     * WHEN: Enable the Make "Discretionary reason" mandatory to fill option in FT
     * AND: Add the item with discretionary depreciation = 0%
     * AND: Select depreciable valuation
     * AND: click OK button
     * THEN: the field is disabled; the dialog is closed
     */
    @Test(dataProvider = "testDataProvider", description = "CHARLIE-508 Verify FT Make 'Discretionary reason' mandatory to fill option while adding discretionary depreciation=0%")
    public void charlie_508_5_verifyDiscretionaryReasonField(@UserCompany(CompanyCode.TRYGFORSIKRING) User user,
                                                             Claim claim,
                                                             ClaimItem claimItem) {
        createClaimAndFillSid(user, claim, claimItem)
                .setDepreciation(0)
                .setDepreciationType(DepreciationType.DISCRETIONARY)
                .setValuation(NEW_PRICE)
                .doAssert(sid -> {
                    sid.assertDiscretionaryReasonDisabled();
                    sid.assertDiscretionaryReasonHasNormalBorder();
                });
    }

    private SettlementDialog createClaimAndFillSid(User user, Claim claim, ClaimItem claimItem) {
        return loginAndCreateClaim(user, claim)
                .openSidAndFill(claimItem.getCategoryShoes(), sidForm -> {
                    sidForm.withCustomerDemandPrice(1000.00)
                            .withNewPrice(100.00)
                            .withAge(0, 6);
                });
    }
}
