package com.scalepoint.automation.tests.rnv.rnv2;


import com.github.tomakehurst.wiremock.client.WireMock;
import com.scalepoint.automation.pageobjects.modules.ClaimNavigationMenu;
import com.scalepoint.automation.pageobjects.pages.CustomerDetailsPage;
import com.scalepoint.automation.pageobjects.pages.MyPage;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.pageobjects.pages.rnv.ProjectsPage;
import com.scalepoint.automation.pageobjects.pages.rnv.tabs.InvoiceTab;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.services.restService.RnvService;
import com.scalepoint.automation.stubs.RnVMock;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.utils.Constants;
import com.scalepoint.automation.utils.RandomUtils;
import com.scalepoint.automation.utils.annotations.RunOn;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.input.Claim;
import com.scalepoint.automation.utils.data.entity.input.ClaimItem;
import com.scalepoint.automation.utils.data.entity.input.ServiceAgreement;
import com.scalepoint.automation.utils.data.entity.input.Translations;
import com.scalepoint.automation.utils.driver.DriverType;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.math.BigDecimal;

import static com.scalepoint.automation.pageobjects.pages.MailsPage.MailType.CUSTOMER_WELCOME;
import static com.scalepoint.automation.pageobjects.pages.rnv.ProjectsPage.AuditResultEvaluationStatus.*;
import static com.scalepoint.automation.services.externalapi.ftemplates.FTSetting.SHOW_DAMAGE_TYPE_CONTROLS_IN_SID;

public class IntelligentRepair2WebServiceTest extends BaseTest {

    RnVMock.RnvStub rnvStub;

    @BeforeClass
    public void startWireMock() {
        WireMock.configureFor(wireMock);
        wireMock.resetMappings();
        rnvStub = new RnVMock(wireMock)
                .addStub();
        wireMock.allStubMappings()
                .getMappings()
                .stream()
                .forEach(m -> log.info(String.format("Registered stubs: %s",m.getRequest())));
    }

    /*
     * send line to RnV
     * send feedback via RnV Rest service (with Invoice)
     * Assert: the task will be auto accepted if RepairPrice=50 (ECC/wiremock/mappings/RnVFeedbackApprove.json)
     *          and claim auto completed (ECC/wiremock/mappings/ClaimAutoComplete.json)
     * Assert: evaluateTaskButton is disabled when task is auto completed by Acceptation
     * Assert: InvoiceTab/InvoiceDialog. Invoice total is correct
     * Assert: Repair Price in Invoice is correct
     * Assert: Mail "Invoice is accepted" (Faktura godkendt) is sent
     *
     */
    @RequiredSetting(type = FTSetting.ENABLE_DAMAGE_TYPE, enabled = false)
    @Test(dataProvider = "testDataProvider", description = "Feedback(with invoice) evaluation status: Approved. Claim auto-completed")
    public void feedbackWithInvoice_approved_claim_auto_completed(User user, Claim claim, ServiceAgreement agreement, Translations translations) {
        String lineDescription = RandomUtils.randomName("RnVLine");

        loginAndCreateClaim(user, claim)
                .toCompleteClaimPage()
                .fillClaimForm(claim)
                .completeWithEmail(claim, databaseApi, true)
                .openRecentClaim()
                .reopenClaim()
                .openSid()
                .fill(lineDescription, agreement.getLineCategory(), agreement.getLineSubCategory(), RnVMock.OK_PRICE)
                .closeSidWithOk()
                .findClaimLine(lineDescription)
                .selectLine()
                .sendToRnV()
                .selectRnvType(lineDescription, translations.getRnvTaskType().getRepair())
                .nextRnVstep()
                .sendRnvIsSuccess(agreement)
                .findClaimLine(lineDescription)
                .doAssert(SettlementPage.ClaimLine.Asserts::assertLineIsSentToRepair);

        new RnvService()
                .sendFeedbackWithInvoiceWithRepairPrice(BigDecimal.valueOf(Constants.PRICE_50), claim, rnvStub);

        Page.to(MyPage.class)
                .doAssert(myPage -> myPage.assertClaimHasStatus(claim.getStatusCompleted()))
                .openRecentClaim()
                .toMailsPage()
                .doAssert(mail -> {
                    mail.isMailExist(CUSTOMER_WELCOME);
                });

        new CustomerDetailsPage()
                .toRepairValuationProjectsPage()
                .getAssertion()
                .assertEvaluateTaskButtonIsDisabled();

        new ProjectsPage().expandTopTaskDetails()
                .getAssertion()
                .assertTaskHasCompletedStatus(agreement)
                .assertAuditResponseText(APPROVE);

        new ProjectsPage().toInvoiceTab()
                .openInvoiceDialogForLineWithIndex(0)
                .findInvoiceLineByIndex(1)
                .assertTotalForTheLineWithIndex(1, Constants.PRICE_50);
    }

    /*
     * send line to RnV
     * send feedback via RnV Rest service (withOUT Invoice)
     * Assert: the task will be auto accepted if RepairPrice=50(ECC/wiremock/mappings/RnVFeedbackApprove.json)
     *          and claim auto completed (ECC/wiremock/mappings/ClaimAutoComplete.json)
     * Assert: evaluateTaskButton is disabled when task is auto completed by Acceptation
     * Assert: InvoiceTab/InvoiceDialog is opened without errors
     */

    @RequiredSetting(type = FTSetting.ENABLE_DAMAGE_TYPE, enabled = false)
    @Test(dataProvider = "testDataProvider", description = "Feedback(no invoice) evaluation status: Approved. Claim auto-completed")
    public void feedbackNoInvoice_approved_claim_auto_completed(User user, Claim claim, ServiceAgreement agreement, Translations translations) {
        String lineDescription = RandomUtils.randomName("RnVLine");

        loginAndCreateClaim(user, claim)
                .toCompleteClaimPage()
                .fillClaimForm(claim)
                .completeWithEmail(claim, databaseApi, true)
                .openRecentClaim()
                .reopenClaim()
                .openSid()
                .fill(lineDescription, agreement.getLineCategory(), agreement.getLineSubCategory(), RnVMock.OK_PRICE)
                .closeSidWithOk()
                .findClaimLine(lineDescription)
                .selectLine()
                .sendToRnV()
                .selectRnvType(lineDescription, translations.getRnvTaskType().getRepair())
                .nextRnVstep()
                .sendRnvIsSuccess(agreement)
                .findClaimLine(lineDescription)
                .doAssert(SettlementPage.ClaimLine.Asserts::assertLineIsSentToRepair);

        new RnvService()
                .sendFeedbackWithoutInvoiceWithRepairPrice(BigDecimal.valueOf(Constants.PRICE_50), claim, rnvStub);

        Page.to(MyPage.class)
                .doAssert(myPage -> myPage.assertClaimHasStatus(claim.getStatusCompleted()))
                .openRecentClaim().toMailsPage();

        new CustomerDetailsPage().toRepairValuationProjectsPage()
                .getAssertion()
                .assertEvaluateTaskButtonIsDisabled();

        new ProjectsPage().expandTopTaskDetails()
                .getAssertion()
                .assertTaskHasCompletedStatus(agreement)
                .assertAuditResponseText(APPROVE);

        new ProjectsPage().toInvoiceTab()
                .doAssert(InvoiceTab.Asserts::assertThereIsNoInvoiceGrid);

    }
    /*
     * send line to RnV
     * send feedback via RnV Rest service (withOUT Invoice)
     * Assert: the task will be auto rejected if RepairPrice=10(ECC/wiremock/mappings/RnVFeedbackRejected.json)
     * Assert: evaluateTaskButton is disabled when task is auto completed by Rejection
     */

    @RequiredSetting(type = FTSetting.ENABLE_DAMAGE_TYPE, enabled = false)
    @Test(dataProvider = "testDataProvider", description = "Feedback evaluation status: Reject")
    public void feedback_Rejected(User user, Claim claim, ServiceAgreement agreement, Translations translations) {
        String lineDescription = RandomUtils.randomName("RnVLine");

        loginAndCreateClaim(user, claim)
                .toCompleteClaimPage()
                .fillClaimForm(claim)
                .completeWithEmail(claim,databaseApi, true)
                .openRecentClaim()
                .reopenClaim()
                .openSid()
                .fill(lineDescription, agreement.getLineCategory(), agreement.getLineSubCategory(), RnVMock.OK_PRICE)
                .closeSidWithOk()
                .findClaimLine(lineDescription)
                .selectLine()
                .sendToRnV()
                .selectRnvType(lineDescription, translations.getRnvTaskType().getRepair())
                .nextRnVstep()
                .sendRnvIsSuccess(agreement)
                .findClaimLine(lineDescription)
                .doAssert(SettlementPage.ClaimLine.Asserts::assertLineIsSentToRepair);


        new RnvService()
                .sendFeedbackWithInvoiceWithRepairPrice(BigDecimal.valueOf(Constants.PRICE_10), claim, rnvStub);

        new ClaimNavigationMenu().toRepairValuationProjectsPage()
                .expandTopTaskDetails()
                .getAssertion()
                .assertEvaluateTaskButtonIsDisabled()
                .assertTaskHasCompletedStatus(agreement);


        new ProjectsPage().getAssertion().assertAuditResponseText(REJECT);
    }
    /*
     * send line to RnV
     * send feedback via RnV Rest service (withOUT Invoice)
     * Assert: audit response is Manual and the task will still be active if RepairPrice=100(ECC/wiremock/mappings/RnVFeedbackManual.json)
     * Assert: evaluateTaskButton is enabled when audit replied "MANUAL"
     * acceptFeedback manually through Evaluate Task dialog
     * Assert: task is Completed, evaluateTaskButton is disabled
     */
    @RequiredSetting(type = FTSetting.ENABLE_DAMAGE_TYPE, enabled = false)
    @Test(dataProvider = "testDataProvider", description = "Feedback evaluation status: Manual")
    public void feedback_Manual(User user, Claim claim, ServiceAgreement agreement, Translations translations) {
        String lineDescription = RandomUtils.randomName("RnVLine");

        loginAndCreateClaim(user, claim)
                .toCompleteClaimPage()
                .fillClaimForm(claim)
                .completeWithEmail(claim, databaseApi, true)
                .openRecentClaim()
                .reopenClaim()
                .openSid()
                .fill(lineDescription, agreement.getLineCategory(), agreement.getLineSubCategory(), RnVMock.OK_PRICE)
                .closeSidWithOk()
                .findClaimLine(lineDescription)
                .selectLine()
                .sendToRnV()
                .selectRnvType(lineDescription, translations.getRnvTaskType().getRepair())
                .nextRnVstep()
                .sendRnvIsSuccess(agreement)
                .findClaimLine(lineDescription)
                .doAssert(SettlementPage.ClaimLine.Asserts::assertLineIsSentToRepair);

        new RnvService()
                .sendFeedbackWithInvoiceWithRepairPrice(BigDecimal.valueOf(Constants.PRICE_100), claim, rnvStub);

        new ClaimNavigationMenu().toRepairValuationProjectsPage()
                .expandTopTaskDetails()
                .getAssertion()
                .assertTaskHasFeedbackReceivedStatus(agreement)
                .assertAuditResponseText(MANUAL);

        new ProjectsPage()
                .openEvaluateTaskDialog()
                .doAssert(evaluateTaskDialog->
                        evaluateTaskDialog.assertRepairPriceForTheTaskWithIndexIs(0, Constants.PRICE_100))
                .acceptFeedback()
                .getAssertion()
                .assertTaskHasCompletedStatus(agreement)
                .assertEvaluateTaskButtonIsDisabled();
    }
    @RequiredSetting(type = FTSetting.SHOW_NOT_CHEAPEST_CHOICE_POPUP, enabled = false)
    @RequiredSetting(type = SHOW_DAMAGE_TYPE_CONTROLS_IN_SID)
    @RequiredSetting(type = FTSetting.ENABLE_DAMAGE_TYPE)
    @Test(dataProvider = "testDataProvider", description = "damageType is actualized in SID when it was changed in RnV wizard")
    public void damageTypeEditedInRnv(User user, Claim claim, ServiceAgreement agreement, Translations translations, ClaimItem claimItem) {

        String lineDescription = RandomUtils.randomName("RnVLine");

        loginAndCreateClaim(user, claim)
                .toCompleteClaimPage()
                .fillClaimForm(claim)
                .completeWithEmail(claim, databaseApi, true)
                .openRecentClaim()
                .reopenClaim()
                .openSid()
                .fill(lineDescription, agreement.getLineCategory(), agreement.getLineSubCategory(), RnVMock.OK_PRICE)
                .enableDamage()
                .selectDamageType(claimItem.getCategoryPersonalMedicine().getDamageTypes().get(0))
                .closeSidWithOk()
                .findClaimLine(lineDescription)
                .selectLine()
                .sendToRnV()
                .selectRnvType(lineDescription, translations.getRnvTaskType().getRepair())
                .selectDamageType(lineDescription, claimItem.getCategoryPersonalMedicine().getDamageTypes().get(1))
                .nextRnVstep()
                .sendRnvIsSuccess(agreement)
                .findClaimLine(lineDescription)
                .editLine()
                .doAssert(claimLine -> {
                    claimLine.assertDamageTypeEqualTo(claimItem.getCategoryPersonalMedicine().getDamageTypes().get(1));
                });
    }

    /*
     * send line to RnV
     * Service Partner response is 500
     * Assert: the error is displayed while sending
     * Assert: the task has 'fail' status on projects page in RnV Wizard
     */
    @Test(dataProvider = "testDataProvider", description = "RnV1. SendLine to RnV, send Service Partner feedback")
    public void sendLineToRnvFailsOnServicePartnerSide(User user, Claim claim, ServiceAgreement agreement, Translations translations) {

        String lineDescription = RandomUtils.randomName("RnVLine");

        loginAndCreateClaim(user, claim)
                .toCompleteClaimPage()
                .fillClaimForm(claim)
                .completeWithEmail(claim, databaseApi, true)
                .openRecentClaim()
                .reopenClaim()
                .openSid()
                .fill(lineDescription, agreement.getLineCategory(), agreement.getLineSubCategory(), RnVMock.UNAUTHORIZED_PRICE)
                .closeSidWithOk()
                .findClaimLine(lineDescription)
                .selectLine()
                .sendToRnV()
                .selectRnvType(lineDescription, translations.getRnvTaskType().getRepair())
                .nextRnVstep()
                .sendRnvIsFailOnServicePartnerSide(agreement)

                .findClaimLine(lineDescription)
                .doAssert(SettlementPage.ClaimLine.Asserts::assertLineIsNotSentToRepair);



        new ClaimNavigationMenu().toRepairValuationProjectsPage()
                .expandTopTaskDetails()
                .getAssertion()
                .assertTaskHasFailStatus(agreement);
    }

    @RequiredSetting(type = SHOW_DAMAGE_TYPE_CONTROLS_IN_SID)
    @RequiredSetting(type = FTSetting.ENABLE_DAMAGE_TYPE)
    @Test(dataProvider = "testDataProvider", description = "One line is set as repair type in RnV")
    public void onLineSentToRnVTaskLineTests(User user, Claim claim, ServiceAgreement agreement, Translations translations, ClaimItem claimItem) {

        String lineDescription1 = RandomUtils.randomName("RnVLine");
        String lineDescription2 = RandomUtils.randomName("RnVLine");

        SettlementPage settlementPage = loginAndCreateClaim(user, claim)
                .toCompleteClaimPage()
                .fillClaimForm(claim)
                .completeWithEmail(claim, databaseApi, true)
                .openRecentClaim()
                .reopenClaim()
                .openSid()
                .fill(lineDescription1, agreement.getLineCategory(), agreement.getLineSubCategory(), RnVMock.OK_PRICE)
                .enableDamage()
                .selectDamageType(claimItem.getCategoryPersonalMedicine().getDamageTypes().get(0))
                .closeSidWithOk()
                .openSid()
                .fill(lineDescription2, agreement.getLineCategory(), agreement.getLineSubCategory(), RnVMock.OK_PRICE)
                .enableDamage()
                .selectDamageType(claimItem.getCategoryPersonalMedicine().getDamageTypes().get(0))
                .closeSidWithOk()
                .selectLinesByDescriptions(lineDescription1, lineDescription2)
                .sendToRnV()
                .selectRnvType(lineDescription1, translations.getRnvTaskType().getRepair())
                .nextRnVstep()
                .sendRnvIsSuccess(agreement);

        settlementPage
                .findClaimLine(lineDescription1)
                .doAssert(SettlementPage.ClaimLine.Asserts::assertLineIsSentToRepair);
        settlementPage
                .findClaimLine(lineDescription2)
                .doAssert(SettlementPage.ClaimLine.Asserts::assertLineIsNotSentToRepair);

        settlementPage
                .selectLinesByDescriptions(lineDescription2)
                .sendToRnV()
                .selectRnvType(lineDescription2,translations.getRnvTaskType().getRepair())
                .nextRnVstep()
                .sendRnvIsSuccess(agreement);

        settlementPage
                .findClaimLine(lineDescription1)
                .doAssert(SettlementPage.ClaimLine.Asserts::assertLineIsSentToRepair);
        settlementPage
                .findClaimLine(lineDescription2)
                .doAssert(SettlementPage.ClaimLine.Asserts::assertLineIsSentToRepair);
    }

    @RequiredSetting(type = SHOW_DAMAGE_TYPE_CONTROLS_IN_SID)
    @RequiredSetting(type = FTSetting.ENABLE_DAMAGE_TYPE)
    @Test(dataProvider = "testDataProvider", description = "Two lines are set as repair type in RnV")
    public void multipleLinesSentToRnVTaskLineTests(User user, Claim claim, ServiceAgreement agreement, Translations translations, ClaimItem claimItem) {

        String lineDescription1 = RandomUtils.randomName("RnVLine");
        String lineDescription2 = RandomUtils.randomName("RnVLine");

        SettlementPage settlementPage = loginAndCreateClaim(user, claim)
                .toCompleteClaimPage()
                .fillClaimForm(claim)
                .completeWithEmail(claim, databaseApi, true)
                .openRecentClaim()
                .reopenClaim()
                .openSid()
                .fill(lineDescription1, agreement.getLineCategory(), agreement.getLineSubCategory(), RnVMock.OK_PRICE)
                .enableDamage()
                .selectDamageType(claimItem.getCategoryPersonalMedicine().getDamageTypes().get(0))
                .closeSidWithOk()
                .openSid()
                .fill(lineDescription2, agreement.getLineCategory(), agreement.getLineSubCategory(), RnVMock.OK_PRICE)
                .enableDamage()
                .selectDamageType(claimItem.getCategoryPersonalMedicine().getDamageTypes().get(0))
                .closeSidWithOk()
                .selectLinesByDescriptions(lineDescription1, lineDescription2)
                .sendToRnV()
                .selectRnvType(lineDescription1, translations.getRnvTaskType().getRepair())
                .selectRnvType(lineDescription2, translations.getRnvTaskType().getRepair())
                .nextRnVstep()
                .sendRnvIsSuccess(agreement);

        settlementPage
                .findClaimLine(lineDescription1)
                .doAssert(SettlementPage.ClaimLine.Asserts::assertLineIsSentToRepair);
        settlementPage
                .findClaimLine(lineDescription2)
                .doAssert(SettlementPage.ClaimLine.Asserts::assertLineIsSentToRepair);
    }
}
