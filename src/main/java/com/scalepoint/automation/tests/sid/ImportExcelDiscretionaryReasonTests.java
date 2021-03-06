package com.scalepoint.automation.tests.sid;

import com.scalepoint.automation.pageobjects.dialogs.SettlementDialog;
import com.scalepoint.automation.pageobjects.dialogs.SettlementDialog.DepreciationType;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.testGroups.TestGroups;
import com.scalepoint.automation.testGroups.UserCompanyGroups;
import com.scalepoint.automation.tests.BaseUITest;
import com.scalepoint.automation.utils.annotations.Jira;
import com.scalepoint.automation.utils.annotations.UserAttributes;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.input.Claim;
import com.scalepoint.automation.utils.data.entity.input.Translations;
import org.testng.annotations.Test;

import java.io.File;

import static com.scalepoint.automation.grid.ValuationGrid.Valuation.DISCRETIONARY;
import static com.scalepoint.automation.grid.ValuationGrid.Valuation.NEW_PRICE;
import static com.scalepoint.automation.services.usersmanagement.CompanyCode.TRYGFORSIKRING;

@Jira("https://jira.scalepoint.com/browse/CHARLIE-508")
public class ImportExcelDiscretionaryReasonTests extends BaseUITest {

    private String excelImportPath = new File("src\\main\\resources\\excelImport\\DK_NYT ARK(3)(a).xls").getAbsolutePath();

    /*
     * WHEN: Import excel file with discretionary valuation
     * AND: Open SID
     * THEN: Drop-down for choosing reason is enabled
     */

    @RequiredSetting(type = FTSetting.SHOW_DISCREATIONARY_REASON)
    @RequiredSetting(type = FTSetting.SHOW_POLICY_TYPE, enabled = false)
    @Test(groups = {TestGroups.SID, TestGroups.IMPORT_EXCEL_DISCRETIONARY_REASON, UserCompanyGroups.TRYGFORSIKRING},
            dataProvider = "testDataProvider",
            description = "CHARLIE-508 Verify that after importing excel with discretionary valuation" +
                    " drop-down for choosing reason is enabled")
    public void charlie508_1_ImportExcelWithDiscretionaryValuation(@UserAttributes(company = TRYGFORSIKRING) User trygUser,
                                                                   Claim claim) {
        loginFlow.loginAndCreateClaim(trygUser, claim)
                .importExcelFile(excelImportPath)
                .findClaimLine("test1")
                .editLine()
                .doAssert(SettlementDialog.Asserts::assertDiscretionaryReasonEnabled);
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
    @Test(groups = {TestGroups.SID, TestGroups.IMPORT_EXCEL_DISCRETIONARY_REASON, UserCompanyGroups.TRYGFORSIKRING},
            dataProvider = "testDataProvider",
            description = "CHARLIE-508 Verify that after importing excel and adding manually discretionary valuation" +
                    " Selected reason 1 is still applicable for the converted item")
    public void charlie508_2_ImportEcxelAddManuallyDiscrValuation(@UserAttributes(company = TRYGFORSIKRING) User trygUser,
                                                                  Claim claim, Translations translations) {
        String claimLineDescription = "APPLE iphone 1";
        String maxCoverageReason = translations.getDiscretionaryReason().getMaxCoverage();

        loginFlow.loginAndCreateClaim(trygUser, claim)
                .importExcelFile(excelImportPath)
                .findClaimLine(claimLineDescription)
                .editLine()
                .setDiscretionaryPrice(400.00)
                .setValuation(DISCRETIONARY)
                .selectDiscretionaryReason(maxCoverageReason)
                .closeSidWithOk()
                .findClaimLine(claimLineDescription)
                .selectLine()
                .getToolBarMenu()
                .toProductMatchPage()
                .openSidForFirstProduct()
                .setValuation(DISCRETIONARY)
                .doAssert(row -> row.assertDiscretionaryReasonEqualTo(maxCoverageReason));

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
    @Test(groups = {TestGroups.SID, TestGroups.IMPORT_EXCEL_DISCRETIONARY_REASON, UserCompanyGroups.TRYGFORSIKRING},
            dataProvider = "testDataProvider",
            description = "CHARLIE-508 Verify that after importing excel and adding manually discretionary depreciation" +
                    " Selected reason 1 is still applicable for the converted item")
    public void charlie508_3_ImportEcxelAddManuallyDiscrDepreciation(@UserAttributes(company = TRYGFORSIKRING) User trygUser,
                                                                     Claim claim, Translations translations) {
        String claimLineDescription = "APPLE iphone 2";
        String maxCoverageReason = translations.getDiscretionaryReason().getMaxCoverage();

        loginFlow.loginAndCreateClaim(trygUser, claim)
                .importExcelFile(excelImportPath)
                .findClaimLine(claimLineDescription)
                .editLine()
                .setDepreciation(10)
                .setDepreciationType(DepreciationType.DISCRETIONARY)
                .setValuation(NEW_PRICE)
                .selectDiscretionaryReason(maxCoverageReason)
                .closeSidWithOk()
                .findClaimLine(claimLineDescription)
                .selectLine()
                .getToolBarMenu()
                .toProductMatchPage()
                .sortOrderableFirst()
                .openSidForFirstProduct()
                .setValuation(NEW_PRICE)
                .doAssert(row -> row.assertDiscretionaryReasonEqualTo(maxCoverageReason));
    }

}