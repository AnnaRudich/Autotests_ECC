package com.scalepoint.automation.tests.sid;

import com.scalepoint.automation.BaseTest;
import com.scalepoint.automation.pageobjects.dialogs.SettlementDialog;
import com.scalepoint.automation.pageobjects.dialogs.SettlementDialog.DepreciationType;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.services.usersmanagement.CompanyCode;
import com.scalepoint.automation.utils.Constants;
import com.scalepoint.automation.utils.annotations.UserCompany;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.ClaimItem;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.testng.annotations.Test;

import static com.scalepoint.automation.pageobjects.dialogs.SettlementDialog.Valuation.ANDEN_VURDERING;
import static com.scalepoint.automation.pageobjects.dialogs.SettlementDialog.Valuation.NEW_PRICE;

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
                .fillDepreciation(Constants.DEPRECIATION_10)
                .selectDepreciationType(DepreciationType.DISCRETIONARY)
                .fillDescription(claimItem.getTextFieldSP())
                .selectValuation(NEW_PRICE)
                .clickOK()
                .doAssert(sid->{
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
                .fillDiscretionaryPrice(300.00)
                .fillDescription(claimItem.getTextFieldSP())
                .selectValuation(ANDEN_VURDERING)
                .clickOK()
                .doAssert(sid->{
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
                .fillDepreciation(Constants.DEPRECIATION_10)
                .selectDepreciationType(DepreciationType.DISCRETIONARY)
                .fillDescription(claimItem.getTextFieldSP())
                .doAssert(sid->{
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
                .fillDepreciation(Constants.DEPRECIATION_10)
                .selectDepreciationType(DepreciationType.POLICY)
                .fillDescription(claimItem.getTextFieldSP())
                .selectValuation(NEW_PRICE)
                .doAssert(sid->{
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
                .fillDepreciation(0)
                .selectDepreciationType(DepreciationType.DISCRETIONARY)
                .fillDescription(claimItem.getTextFieldSP())
                .selectValuation(NEW_PRICE)
                .doAssert(sid->{
                    sid.assertDiscretionaryReasonDisabled();
                    sid.assertDiscretionaryReasonHasNormalBorder();
                });
    }

    private SettlementDialog createClaimAndFillSid(User user, Claim claim, ClaimItem claimItem) {
        return loginAndCreateClaim(user, claim)
                .openSid()
                .fillCategory(claimItem.getExistingCat4())
                .fillSubCategory(claimItem.getExistingSubCat4())
                .fillCustomerDemand(1000.00)
                .fillNewPrice(100.00)
                .enableAge()
                .selectMonth("6");
    }
}
