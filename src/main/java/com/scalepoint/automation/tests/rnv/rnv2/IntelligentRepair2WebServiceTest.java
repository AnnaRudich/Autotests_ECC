package com.scalepoint.automation.tests.rnv.rnv2;


import com.scalepoint.automation.pageobjects.modules.ClaimNavigationMenu;
import com.scalepoint.automation.pageobjects.pages.CustomerDetailsPage;
import com.scalepoint.automation.pageobjects.pages.MyPage;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.pageobjects.pages.rnv.ProjectsPage;
import com.scalepoint.automation.pageobjects.pages.rnv.tabs.InvoiceTab;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.services.restService.RnvService;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.utils.Constants;
import com.scalepoint.automation.utils.RandomUtils;
import com.scalepoint.automation.utils.annotations.RunOn;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.ServiceAgreement;
import com.scalepoint.automation.utils.data.entity.Translations;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.driver.DriverType;
import org.testng.annotations.Test;

import java.math.BigDecimal;

import static com.scalepoint.automation.pageobjects.pages.MailsPage.MailType.CUSTOMER_WELCOME;
import static com.scalepoint.automation.pageobjects.pages.MailsPage.MailType.REPAIR_AND_VALUATION;
import static com.scalepoint.automation.pageobjects.pages.rnv.ProjectsPage.AuditResultEvaluationStatus.APPROVE;
import static com.scalepoint.automation.pageobjects.pages.rnv.ProjectsPage.AuditResultEvaluationStatus.MANUAL;
import static com.scalepoint.automation.pageobjects.pages.rnv.ProjectsPage.AuditResultEvaluationStatus.REJECT;

@RequiredSetting(type = FTSetting.ENABLE_DAMAGE_TYPE, enabled = false)
public class IntelligentRepair2WebServiceTest extends BaseTest {

/*
 * send line to RnV
 * send feedback via RnV Rest service (with Invoice and RepairPrice =50)
 * Assert: the task will be auto accepted (ECC/wiremock/mappings/RnVFeedbackApprove.json)
 *          and claim auto completed (ECC/wiremock/mappings/ClaimAutoComplete.json)
 * Assert: evaluateTaskButton is disabled when task is auto accepted
 * Assert: InvoiceTab/InvoiceDialog. Invoice total is correct
 * Assert: Repair Price in Invoice is correct
 * Assert: Mail "Invoice is accepted" (Faktura godkendt) is sent
 */
@RunOn(DriverType.CHROME)
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

        new RnvService().sendFeedbackWithInvoiceWithRepairPrice(BigDecimal.valueOf(Constants.PRICE_50), claim);

        Page.to(MyPage.class)
                .doAssert(myPage -> myPage.assertClaimHasStatus(claim.getStatusCompleted()))
                .openRecentClaim()
                .toMailsPage()
                .doAssert(mail -> {
                    mail.isMailExist(REPAIR_AND_VALUATION, "Faktura godkendt");
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

        new RnvService().sendFeedbackWithoutInvoiceWithRepairPrice(BigDecimal.valueOf(Constants.PRICE_50), claim);

        Page.to(MyPage.class)
                .doAssert(myPage -> myPage.assertClaimHasStatus(claim.getStatusCompleted()))
                .openRecentClaim().toMailsPage();

        new CustomerDetailsPage().toRepairValuationProjectsPage()
                .getAssertion()
                .assertEvaluateTaskButtonIsDisabled();

        new ProjectsPage().expandTopTaskDetails()
                .getAssertion()
                .assertTaskHasFeedbackReceivedStatus(agreement)
                .assertAuditResponseText(APPROVE);

        new ProjectsPage().toInvoiceTab()
                .doAssert(InvoiceTab.Asserts::assertThereIsNoInvoiceGrid);

    }
@RunOn(DriverType.CHROME)
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

        new RnvService().sendFeedbackWithoutInvoiceWithRepairPrice(BigDecimal.valueOf(Constants.PRICE_10), claim);

        new ClaimNavigationMenu().toRepairValuationProjectsPage()
                .expandTopTaskDetails()
                .getAssertion()
                .assertEvaluateTaskButtonIsDisabled()
                .assertTaskHasFeedbackReceivedStatus(agreement);


        new ProjectsPage().getAssertion().assertAuditResponseText(REJECT);
    }
/*
 * audit response is MANUAL
 * evaluateTaskButton is enabled
 */
    @RunOn(DriverType.CHROME)
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

        new RnvService().sendFeedbackWithInvoiceWithRepairPrice(BigDecimal.valueOf(Constants.PRICE_100), claim);

        /*
          new CustomerDetailsPage().toRepairValuationProjectsPage()
                .openEvaluateTaskDialog()
                .doAssert(evaluateTask -> {
                    evaluateTask.assertRepairPriceForTheFirstTaskIs(1, 50.00);
                    evaluateTask.assertTotalIs(500.00);
                });
        new EvaluateTaskDialog()
                .closeDialog()

                .expandTopTaskDetails()
                .getAssertion()
                .assertTaskHasFeedbackReceivedStatus(agreement)
                .assertAuditResponseText(APPROVE);

        new ProjectsPage().toInvoiceTab()
                .openInvoiceDialogForLineWithIndex(0)
                .doAssert(InvoiceDialog.Asserts::assertThereIsInvoiceLinesList);
         */

        new ClaimNavigationMenu().toRepairValuationProjectsPage()
                .expandTopTaskDetails()
                .getAssertion()
                .assertTaskHasFeedbackReceivedStatus(agreement);

        new ProjectsPage().getAssertion()
                .assertAuditResponseText(MANUAL);
    }
}
