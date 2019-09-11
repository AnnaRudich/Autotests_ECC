package com.scalepoint.automation.tests.sid;

import com.scalepoint.automation.pageobjects.dialogs.SettlementDialog;
import com.scalepoint.automation.pageobjects.dialogs.SettlementDialog.DepreciationType;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.utils.annotations.Jira;
import com.scalepoint.automation.utils.annotations.UserCompany;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.Translations;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.testng.annotations.Test;

import static com.scalepoint.automation.pageobjects.dialogs.SettlementDialog.Valuation.DISCRETIONARY;
import static com.scalepoint.automation.pageobjects.dialogs.SettlementDialog.Valuation.NEW_PRICE;
import static com.scalepoint.automation.services.usersmanagement.CompanyCode.TRYGFORSIKRING;

@Jira("https://jira.scalepoint.com/browse/CHARLIE-508")
public class ImportExcelDiscretionaryReasonTests extends BaseTest {

    private String excelImportPath = "C:\\ExcelImportTest\\DK_NYT ARK(3)(a).xls";

    /*
     * WHEN: Import excel file with discretionary valuation
     * AND: Open SID
     * THEN: Drop-down for choosing reason is enabled
     */
    @RequiredSetting(type = FTSetting.SHOW_DISCREATIONARY_REASON)
    @RequiredSetting(type = FTSetting.SHOW_POLICY_TYPE, enabled = false)
    @Test(dataProvider = "testDataProvider", description = "CHARLIE-508 Verify that after importing excel with discretionary valuation" +
            " drop-down for choosing reason is enabled")
    public void charlie508_1_ImportExcelWithDiscretionaryValuation(@UserCompany(TRYGFORSIKRING) User trygUser,
                                                                   Claim claim) {
        loginAndCreateClaim(trygUser, claim)
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
    @Test(dataProvider = "testDataProvider", description = "CHARLIE-508 Verify that after importing excel and adding manually discretionary valuation" +
            " Selected reason 1 is still applicable for the converted item")
    public void charlie508_2_ImportEcxelAddManuallyDiscrValuation(@UserCompany(TRYGFORSIKRING) User trygUser,
                                                                  Claim claim, Translations translations) {
        String claimLineDescription = "APPLE iphone 1";
        String maxCoverageReason = translations.getDiscretionaryReason().getMaxCoverage();

        loginAndCreateClaim(trygUser, claim)
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
    @Test(dataProvider = "testDataProvider", description = "CHARLIE-508 Verify that after importing excel and adding manually discretionary depreciation" +
            " Selected reason 1 is still applicable for the converted item")
    public void charlie508_3_ImportEcxelAddManuallyDiscrDepreciation(@UserCompany(TRYGFORSIKRING) User trygUser,
                                                                     Claim claim, Translations translations) {
        String claimLineDescription = "APPLE iphone 2";
        String maxCoverageReason = translations.getDiscretionaryReason().getMaxCoverage();

        loginAndCreateClaim(trygUser, claim)
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