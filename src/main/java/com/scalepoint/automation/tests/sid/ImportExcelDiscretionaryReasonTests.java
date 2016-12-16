package com.scalepoint.automation.tests.sid;

import com.scalepoint.automation.BaseTest;
import com.scalepoint.automation.pageobjects.dialogs.SettlementDialog;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.utils.annotations.UserCompany;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.*;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.testng.annotations.Test;

import static com.scalepoint.automation.pageobjects.dialogs.SettlementDialog.Valuation.ANDEN_VURDERING;
import static com.scalepoint.automation.pageobjects.dialogs.SettlementDialog.Valuation.NEW_PRICE;
import static com.scalepoint.automation.services.usersmanagement.CompanyCode.TRYGFORSIKRING;
import static org.testng.Assert.*;


/**
 * Created by asa on 12/12/2016.
 */
public class ImportExcelDiscretionaryReasonTests extends BaseTest {

    private String excelImportPath = "C:\\ExcelImport\\DK_NYT ARK(3)(a).xls";

    /*
     * WHEN: Import excel file with discretionary valuation
     * AND: Open SID
     * THEN: Drop-down for choosing reason is enabled
     */
    @RequiredSetting(type = FTSetting.SHOW_DISCREATIONARY_REASON)
    @RequiredSetting(type = FTSetting.SHOW_POLICY_TYPE, enabled = false)
    @Test(dataProvider = "testDataProvider", description = "CHARLIE-508 Verify that after importing excel with discretionary valuation" +
            " drop-down for choosing reason is enabled")
    public void charlie508_1_ImportEcxelWithDiscretionaryValuation(@UserCompany(TRYGFORSIKRING) User trygUser,
                                                                   Claim claim) {
        String claimLineDescription = "test1";

        SettlementDialog settlementDialog = loginAndCreateClaim(trygUser, claim).
        importExcelFile(excelImportPath).
        findClaimLine(claimLineDescription).editLine();
        assertTrue(settlementDialog.isDiscretionaryReasonEnabled(),"Discretionary reason field should be enabled");
    }


    /* WHEN:Import excel file and add the manual discretionary valuation.
     * AND: Add a reason 1
     * AND: Save the item and press Product match button
     * AND: Select any item form catalog.
     * THEN: Selected reason 1 is still applicable for the converted item.
     */
    @RequiredSetting(type = FTSetting.SHOW_DISCREATIONARY_REASON)
    @RequiredSetting(type = FTSetting.SHOW_POLICY_TYPE, enabled = false)
    @RequiredSetting(type = FTSetting.SHOW_NOT_CHEAPEST_CHOICE_POPUP, enabled = false)
    @Test(dataProvider = "testDataProvider", description = "CHARLIE-508 Verify that after importing excel and adding manually discretionary valuation" +
            " Selected reason 1 is still applicable for the converted item")
    public void charlie508_2_ImportEcxelAddManuallyDiscrValuation(@UserCompany(TRYGFORSIKRING) User trygUser,
                                                                  Claim claim, DiscretionaryReason discretionaryReason) {
        String claimLineDescription = "APPLE iphone 1";

        SettlementDialog settlementDialog = loginAndCreateClaim(trygUser, claim).
                importExcelFile(excelImportPath).
                findClaimLine(claimLineDescription).editLine().
                fillDiscretionaryPrice(400).
                selectValuation(ANDEN_VURDERING).
                selectDiscretionaryReason(discretionaryReason.getDiscretionaryReason2()).
                ok().
                findClaimLine(claimLineDescription).
                selectLine().
                getToolBarMenu().
                toProductMatchPage().
                matchFirst().
                selectValuation(ANDEN_VURDERING);
        assertEquals(settlementDialog.getDiscretionaryReasonText(), discretionaryReason.getDiscretionaryReason2(), "Selected reason should be visible in match dialog");

    }

    /* WHEN:Import excel file and add the manual discretionary depreciation.
     * AND: Add a reason 1
     * AND: Save the item and press Product match button
     * AND: Select any item form catalog.
     * THEN: Selected reason 1 is still applicable for the converted item.
     */
    @RequiredSetting(type = FTSetting.SHOW_DISCREATIONARY_REASON)
    @RequiredSetting(type = FTSetting.SHOW_POLICY_TYPE, enabled = false)
    @RequiredSetting(type = FTSetting.SHOW_NOT_CHEAPEST_CHOICE_POPUP, enabled = false)
    @Test(dataProvider = "testDataProvider", description = "CHARLIE-508 Verify that after importing excel and adding manually discretionary depreciation" +
            " Selected reason 1 is still applicable for the converted item")
    public void charlie508_3_ImportEcxelAddManuallyDiscrDepreciation(@UserCompany(TRYGFORSIKRING) User trygUser,
                                                                     Claim claim, DiscretionaryReason discretionaryReason) {
        String claimLineDescription = "APPLE iphone 2";

        SettlementDialog settlementDialog = loginAndCreateClaim(trygUser, claim).
                importExcelFile(excelImportPath).
                findClaimLine(claimLineDescription).editLine().
                fillDepreciation(10).
                selectDepreciationType(1).
                selectValuation(NEW_PRICE).
                selectDiscretionaryReason(discretionaryReason.getDiscretionaryReason2()).
                ok().
                findClaimLine(claimLineDescription).
                selectLine().
                getToolBarMenu().
                toProductMatchPage().
                matchFirst().
                selectValuation(NEW_PRICE);
        assertEquals(settlementDialog.getDiscretionaryReasonText(), discretionaryReason.getDiscretionaryReason2(),"Selected reason should be visible in match dialog");
    }

}