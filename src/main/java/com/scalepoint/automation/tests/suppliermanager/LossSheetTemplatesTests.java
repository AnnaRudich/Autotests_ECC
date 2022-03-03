package com.scalepoint.automation.tests.suppliermanager;

import com.scalepoint.automation.pageobjects.pages.MailsPage;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.testGroups.TestGroups;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.utils.RandomUtils;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.input.Claim;
import com.scalepoint.automation.utils.data.entity.input.LossSheetTemplates;
import org.testng.annotations.Test;

import java.io.File;

@RequiredSetting(type = FTSetting.SELF_SERVICE_EXCEL_CHOICE)
public class LossSheetTemplatesTests extends BaseTest {

    @Test(groups = {TestGroups.SUPPLIER_MANAGER, TestGroups.LOSS_SHEET_TEMPLATES}, dataProvider = "testDataProvider",
            description = "Verifies single template available for company")
    public void singleTemplateTest(User user, Claim claim, LossSheetTemplates lossSheetTemplates) {

        File claimSheetDKExcel2003v1 = new File(lossSheetTemplates.getClaimSheetDKExcel2003v1());
        String claimSheetDKExcel2003v1TemplateName = RandomUtils.randomName(claimSheetDKExcel2003v1.getName());

        loginToEccAdmin(user)
                .toLossSheetTemplates()
                .uploadNewTemplate()
                .setTemplateName(claimSheetDKExcel2003v1TemplateName)
                .uploadTemplate(claimSheetDKExcel2003v1)
                .clickUploadButton()
                .doAssert(lossSheetTemplatesPage ->
                        lossSheetTemplatesPage.assertTemplateExists(claimSheetDKExcel2003v1TemplateName))
                .getTemplateRowByName(claimSheetDKExcel2003v1TemplateName)
                .selectTemplate()
                .doAssert(lossSheetTemplatesPage ->
                        lossSheetTemplatesPage.assertActive())
                .reassignTemplate(user.getCompanyName(), claimSheetDKExcel2003v1TemplateName)
                .doAssert(lossSheetTemplatesPage ->
                        lossSheetTemplatesPage.assertInUse())
                .logout();

        verifyLossAdjusterSheet(user, claim, claimSheetDKExcel2003v1TemplateName);
    }

    @Test(groups = {TestGroups.SUPPLIER_MANAGER, TestGroups.LOSS_SHEET_TEMPLATES}, dataProvider = "testDataProvider",
            description = "Verifies multiple templates available for company")
    public void multipleTemplatesTest(User user, Claim claim1, Claim claim2, LossSheetTemplates lossSheetTemplates) {

        File claimSheetDKExcel2003v1 = new File(lossSheetTemplates.getClaimSheetDKExcel2003v1());
        String claimSheetDKExcel2003v1TemplateName = RandomUtils.randomName(claimSheetDKExcel2003v1.getName());
        File claimSheetDKExcel2003v11 = new File(lossSheetTemplates.getClaimSheetDKExcel2007v11());
        String claimSheetDKExcel2003v11TemplateName = RandomUtils.randomName(claimSheetDKExcel2003v11.getName());

        loginToEccAdmin(user)
                .toLossSheetTemplates()
                .uploadTemplate(claimSheetDKExcel2003v1TemplateName, claimSheetDKExcel2003v1)
                .doAssert(lstPage ->
                        lstPage.assertTemplateExists(claimSheetDKExcel2003v1TemplateName))
                .uploadTemplate(claimSheetDKExcel2003v11TemplateName, claimSheetDKExcel2003v11)
                .doAssert(lstPage ->
                        lstPage.assertTemplateExists(claimSheetDKExcel2003v1TemplateName))
                .getTemplateRowByName(claimSheetDKExcel2003v1TemplateName)
                .selectTemplate()
                .doAssert(lstPage ->
                        lstPage.assertActive())
                .reassignTemplate(user.getCompanyName(), claimSheetDKExcel2003v1TemplateName)
                .doAssert(lstPage ->
                        lstPage.assertInUse())
                .getTemplateRowByName(claimSheetDKExcel2003v11TemplateName)
                .selectTemplate()
                .doAssert(lstPage ->
                        lstPage.assertActive())
                .reassignTemplate(user.getCompanyName(), claimSheetDKExcel2003v11TemplateName)
                .doAssert(lstPage ->
                        lstPage.assertInUse())
                .logout();

        verifyLossAdjusterSheet(user, claim1, claimSheetDKExcel2003v1TemplateName);
        verifyLossAdjusterSheet(user, claim2, claimSheetDKExcel2003v11TemplateName);
    }

    @Test(groups = {TestGroups.SUPPLIER_MANAGER, TestGroups.LOSS_SHEET_TEMPLATES}, dataProvider = "testDataProvider",
            description = "Verifies multiple templates shared across many companies")
    public void sharedTemplatesTest(User user1, User user2, Claim claim1, Claim claim2, LossSheetTemplates lossSheetTemplates) {

        File claimSheetDKExcel2003v1 = new File(lossSheetTemplates.getClaimSheetDKExcel2003v1());
        String claimSheetDKExcel2003v1TemplateName = RandomUtils.randomName(claimSheetDKExcel2003v1.getName());
        File claimSheetDKExcel2003v11 = new File(lossSheetTemplates.getClaimSheetDKExcel2007v11());
        String claimSheetDKExcel2003v11TemplateName = RandomUtils.randomName(claimSheetDKExcel2003v11.getName());
        File claimSheetDKExcel2003v12 = new File(lossSheetTemplates.getClaimSheetDKExcel2007v12());
        String claimSheetDKExcel2003v12TemplateName = RandomUtils.randomName(claimSheetDKExcel2003v12.getName());
        File excel400DK = new File(lossSheetTemplates.getExcel400DK());
        String excel400DKTemplateName = RandomUtils.randomName(excel400DK.getName());

        loginToEccAdmin(user2)
                .toLossSheetTemplates()
                .uploadTemplate(claimSheetDKExcel2003v11TemplateName, claimSheetDKExcel2003v11)
                .doAssert(lstPage ->
                        lstPage.assertTemplateExists(claimSheetDKExcel2003v11TemplateName))
                .uploadTemplate(excel400DKTemplateName, excel400DK)
                .doAssert(lstPage ->
                        lstPage.assertTemplateExists(excel400DKTemplateName))
                .getTemplateRowByName(excel400DKTemplateName)
                .selectTemplate()
                .doAssert(lstPage ->
                        lstPage.assertActive())
                .reassignTemplate()
                .getCompanyByName(user2.getCompanyName())
                .selectCompany()
                .addItem()
                .getCompanyByName(user1.getCompanyName())
                .selectCompany()
                .addItem()
                .clickSaveButton()
                .getTemplateRowByName(excel400DKTemplateName)
                .doAssert(lstPage ->
                        lstPage.assertInUse())
                .getTemplateRowByName(claimSheetDKExcel2003v11TemplateName)
                .selectTemplate()
                .doAssert(lstPage ->
                        lstPage.assertActive())
                .reassignTemplate()
                .getCompanyByName(user2.getCompanyName())
                .selectCompany()
                .addItem()
                .getCompanyByName(user1.getCompanyName())
                .selectCompany()
                .addItem()
                .clickSaveButton()
                .getTemplateRowByName(claimSheetDKExcel2003v11TemplateName)
                .doAssert(lstPage ->
                        lstPage.assertInUse())
                .logout();

        loginToEccAdmin(user1)
                .toLossSheetTemplates()
                .uploadTemplate(claimSheetDKExcel2003v1TemplateName, claimSheetDKExcel2003v1)
                .doAssert(lstPage ->
                        lstPage.assertTemplateExists(claimSheetDKExcel2003v1TemplateName))
                .uploadTemplate(claimSheetDKExcel2003v12TemplateName, claimSheetDKExcel2003v12)
                .doAssert(lstPage ->
                        lstPage.assertTemplateExists(claimSheetDKExcel2003v12TemplateName))
                .getTemplateRowByName(claimSheetDKExcel2003v12TemplateName)
                .selectTemplate()
                .doAssert(lstPage ->
                        lstPage.assertActive())
                .reassignTemplate()
                .getCompanyByName(user2.getCompanyName())
                .selectCompany()
                .addItem()
                .getCompanyByName(user1.getCompanyName())
                .selectCompany()
                .addItem()
                .clickSaveButton()
                .getTemplateRowByName(claimSheetDKExcel2003v12TemplateName)
                .doAssert(lstPage ->
                        lstPage.assertInUse())
                .getTemplateRowByName(claimSheetDKExcel2003v1TemplateName)
                .selectTemplate()
                .doAssert(lstPage ->
                        lstPage.assertActive())
                .reassignTemplate()
                .getCompanyByName(user2.getCompanyName())
                .selectCompany()
                .addItem()
                .getCompanyByName(user1.getCompanyName())
                .selectCompany()
                .addItem()
                .clickSaveButton()
                .getTemplateRowByName(claimSheetDKExcel2003v1TemplateName)
                .doAssert(lstPage ->
                        lstPage.assertInUse())
                .logout();

        verifyLossAdjusterSheet(user1, claim1, excel400DKTemplateName);
        verifyLossAdjusterSheet(user1, claim2, claimSheetDKExcel2003v12TemplateName);
    }

    public void verifyLossAdjusterSheet(User user, Claim claim, String templateName){

        loginAndCreateClaim(user, claim)
                .getClaimOperationsMenu()
                .requestSelfService()
                .setSendLossAdjusterSheet(templateName)
                .enterEmail(claim.getEmail())
                .send()
                .toMailsPage()
                .doAssert(mailsPage -> mailsPage.isMailExist(MailsPage.MailType.LOSS_ADJUSTER_SHEET));
    }
}
