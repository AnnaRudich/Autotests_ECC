package com.scalepoint.automation.tests.rnv.rnv2;


import com.scalepoint.automation.pageobjects.modules.ClaimNavigationMenu;
import com.scalepoint.automation.pageobjects.pages.CustomerDetailsPage;
import com.scalepoint.automation.pageobjects.pages.MyPage;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.pageobjects.pages.rnv.EvaluateTaskDialog;
import com.scalepoint.automation.pageobjects.pages.rnv.ProjectsPage;
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
import static com.scalepoint.automation.pageobjects.pages.rnv.ProjectsPage.AuditResultEvaluationStatus.*;

@RequiredSetting(type = FTSetting.ENABLE_DAMAGE_TYPE, enabled = false)
public class IntelligentRepair2 extends BaseTest {
    @RunOn(DriverType.CHROME)
    @Test(dataProvider = "testDataProvider", description = "IntelligentRepair2. Audit Approved")
    public void feedback_Approved(User user, Claim claim, ServiceAgreement agreement, Translations translations) {
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

        new RnvService().sendFeedbackWithRepairPrice(BigDecimal.valueOf(Constants.PRICE_50), claim);

        Page.to(MyPage.class).openRecentClaim().toMailsPage()

                .doAssert(mail -> {
                    mail.isMailExist(REPAIR_AND_VALUATION, "Faktura godkendt");
                    mail.isMailExist(CUSTOMER_WELCOME);
                });

        new CustomerDetailsPage().toRepairValuationProjectsPage()
                .expandTopTaskDetails()
                .getAssertion()
                .assertTaskHasFeedbackReceivedStatus(agreement)
                .assertAuditResponseText(APPROVE);
    }
    @RunOn(DriverType.CHROME)
    @Test(dataProvider = "testDataProvider", description = "IntelligentRepair2. Audit Reject")
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

        new RnvService().sendFeedbackWithRepairPrice(BigDecimal.valueOf(Constants.PRICE_10), claim);

        new ClaimNavigationMenu().toRepairValuationProjectsPage()
                .expandTopTaskDetails()
                .getAssertion()
                .assertTaskHasFeedbackReceivedStatus(agreement);

        new ProjectsPage().getAssertion().assertAuditResponseText(REJECT);
    }

    @Test(dataProvider = "testDataProvider", description = "IntelligentRepair2. Audit Manual")
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

        new RnvService().sendFeedbackWithRepairPrice(BigDecimal.valueOf(Constants.PRICE_100), claim);
        new ClaimNavigationMenu().toRepairValuationProjectsPage()
                .expandTopTaskDetails()
                .getAssertion()
                .assertTaskHasFeedbackReceivedStatus(agreement);

        new ProjectsPage().getAssertion().assertAuditResponseText(MANUAL);
    }

    @RunOn(DriverType.CHROME)
    @Test(dataProvider = "testDataProvider", description = "IntelligentRepair2. Audit Approved")
    public void feedback_noInvoice(User user, Claim claim, ServiceAgreement agreement, Translations translations) {
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

        new RnvService().sendFeedbackWithoutInvoiceWithRepairPrice(BigDecimal.valueOf(Constants.PRICE_100), claim);

        new CustomerDetailsPage().toRepairValuationProjectsPage()
                .openEvaluateTaskDialog()
                .doAssert(evaluateTask -> {
                    evaluateTask.assertRepairPriceForTheTaskWithIndexIs(1, 100.00);
                    evaluateTask.assertTotalIs(0.00);
                    evaluateTask.assertThereIsNoInvoiceLines();
                });
        new EvaluateTaskDialog().closeDialog()
                .expandTopTaskDetails()
                .getAssertion()
                .assertTaskHasFeedbackReceivedStatus(agreement)
                .assertAuditResponseText(APPROVE);
    }
}
