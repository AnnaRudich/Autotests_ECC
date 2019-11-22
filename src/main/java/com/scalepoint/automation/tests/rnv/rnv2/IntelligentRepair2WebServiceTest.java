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
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.ServiceAgreement;
import com.scalepoint.automation.utils.data.entity.Translations;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.rnv.serviceTask.ServiceTasksExport;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.math.BigDecimal;

import static com.scalepoint.automation.pageobjects.pages.rnv.ProjectsPage.AuditResultEvaluationStatus.*;

@RequiredSetting(type = FTSetting.ENABLE_DAMAGE_TYPE, enabled = false)
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

    @Test(dataProvider = "testDataProvider", description = "Feedback(with invoice) evaluation status: Approved. Claim auto-completed")
    public void feedbackWithInvoice_approved_claim_auto_completed(User user, Claim claim, ServiceAgreement agreement, Translations translations) {
        String lineDescription = RandomUtils.randomName("RnVLine");

        loginAndCreateClaim(user, claim)
                .toCompleteClaimPage()
                .fillClaimForm(claim)
                .completeWithEmail(claim)
                .openRecentClaim()
                .reopenClaim()
                .openSid()
                .fill(lineDescription, agreement.getClaimLineCat_PersonligPleje(), agreement.getClaimLineSubCat_Medicin(), 100.00)
                .closeSidWithOk()
                .findClaimLine(lineDescription)
                .selectLine()
                .sendToRnV()
                .selectRnvType(lineDescription, translations.getRnvTaskType().getRepair())
                .nextRnVstep()
                .sendRnV(agreement)
                .findClaimLine(lineDescription)
                .doAssert(SettlementPage.ClaimLine.Asserts::assertLineIsSentToRepair);

        ServiceTasksExport serviceTasksExport = rnvStub.waitForServiceTask(claim.getClaimNumber());

        new RnvService()
                .sendFeedbackWithInvoiceWithRepairPrice(BigDecimal.valueOf(Constants.PRICE_50), claim, serviceTasksExport);

        Page.to(MyPage.class)
                .doAssert(myPage -> myPage.assertClaimHasStatus(claim.getStatusCompleted()));
//                .openRecentClaim()
//                .toMailsPage()
//                .doAssert(mail -> {
//                    mail.isMailExist(REPAIR_AND_VALUATION, "Faktura godkendt");
//                    mail.isMailExist(CUSTOMER_WELCOME);
//                });

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

    @Test(dataProvider = "testDataProvider", description = "Feedback(no invoice) evaluation status: Approved. Claim auto-completed")
    public void feedbackNoInvoice_approved_claim_auto_completed(User user, Claim claim, ServiceAgreement agreement, Translations translations) {
        String lineDescription = RandomUtils.randomName("RnVLine");

        loginAndCreateClaim(user, claim)
                .toCompleteClaimPage()
                .fillClaimForm(claim)
                .completeWithEmail(claim)
                .openRecentClaim()
                .reopenClaim()
                .openSid()
                .fill(lineDescription, agreement.getClaimLineCat_PersonligPleje(), agreement.getClaimLineSubCat_Medicin(), 100.00)
                .closeSidWithOk()
                .findClaimLine(lineDescription)
                .selectLine()
                .sendToRnV()
                .selectRnvType(lineDescription, translations.getRnvTaskType().getRepair())
                .nextRnVstep()
                .sendRnV(agreement)
                .findClaimLine(lineDescription)
                .doAssert(SettlementPage.ClaimLine.Asserts::assertLineIsSentToRepair);


        ServiceTasksExport serviceTasksExport = rnvStub.waitForServiceTask(claim.getClaimNumber());

        new RnvService()
                .sendFeedbackWithoutInvoiceWithRepairPrice(BigDecimal.valueOf(Constants.PRICE_50), claim, serviceTasksExport);

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
    @Test(dataProvider = "testDataProvider", description = "Feedback evaluation status: Reject")
    public void feedback_Rejected(User user, Claim claim, ServiceAgreement agreement, Translations translations) {
        String lineDescription = RandomUtils.randomName("RnVLine");

        loginAndCreateClaim(user, claim)
                .toCompleteClaimPage()
                .fillClaimForm(claim)
                .completeWithEmail(claim)
                .openRecentClaim()
                .reopenClaim()
                .openSid()
                .fill(lineDescription, agreement.getClaimLineCat_PersonligPleje(), agreement.getClaimLineSubCat_Medicin(), 100.00)
                .closeSidWithOk()
                .findClaimLine(lineDescription)
                .selectLine()
                .sendToRnV()
                .selectRnvType(lineDescription, translations.getRnvTaskType().getRepair())
                .nextRnVstep()
                .sendRnV(agreement)
                .findClaimLine(lineDescription)
                .doAssert(SettlementPage.ClaimLine.Asserts::assertLineIsSentToRepair);

        ServiceTasksExport serviceTasksExport = rnvStub.waitForServiceTask(claim.getClaimNumber());

        new RnvService()
                .sendFeedbackWithInvoiceWithRepairPrice(BigDecimal.valueOf(Constants.PRICE_10), claim, serviceTasksExport);

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
    @Test(dataProvider = "testDataProvider", description = "Feedback evaluation status: Manual")
    public void feedback_Manual(User user, Claim claim, ServiceAgreement agreement, Translations translations) {
        String lineDescription = RandomUtils.randomName("RnVLine");

        loginAndCreateClaim(user, claim)
                .toCompleteClaimPage()
                .fillClaimForm(claim)
                .completeWithEmail(claim)
                .openRecentClaim()
                .reopenClaim()
                .openSid()
                .fill(lineDescription, agreement.getClaimLineCat_PersonligPleje(), agreement.getClaimLineSubCat_Medicin(), 100.00)
                .closeSidWithOk()
                .findClaimLine(lineDescription)
                .selectLine()
                .sendToRnV()
                .selectRnvType(lineDescription, translations.getRnvTaskType().getRepair())
                .nextRnVstep()
                .sendRnV(agreement)
                .findClaimLine(lineDescription)
                .doAssert(SettlementPage.ClaimLine.Asserts::assertLineIsSentToRepair);

        ServiceTasksExport serviceTasksExport = rnvStub.waitForServiceTask(claim.getClaimNumber());

        new RnvService()
                .sendFeedbackWithInvoiceWithRepairPrice(BigDecimal.valueOf(Constants.PRICE_100), claim, serviceTasksExport);

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
}
