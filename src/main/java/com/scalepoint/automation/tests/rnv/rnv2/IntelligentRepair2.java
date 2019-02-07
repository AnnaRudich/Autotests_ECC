package com.scalepoint.automation.tests.rnv.rnv2;


import com.scalepoint.automation.pageobjects.modules.ClaimNavigationMenu;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.pageobjects.pages.rnv1.RnvProjectsPage;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.services.restService.RnvService;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.utils.Constants;
import com.scalepoint.automation.utils.RandomUtils;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.RnvTaskType;
import com.scalepoint.automation.utils.data.entity.ServiceAgreement;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.testng.annotations.Test;

import java.math.BigDecimal;

import static com.scalepoint.automation.pageobjects.pages.MailsPage.MailType.REPAIR_AND_VALUATION;
import static com.scalepoint.automation.pageobjects.pages.rnv1.RnvProjectsPage.AuditResultEvaluationStatus.APPROVE;
import static com.scalepoint.automation.pageobjects.pages.rnv1.RnvProjectsPage.AuditResultEvaluationStatus.MANUAL;
import static com.scalepoint.automation.pageobjects.pages.rnv1.RnvProjectsPage.AuditResultEvaluationStatus.REJECT;

@RequiredSetting(type = FTSetting.ENABLE_REPAIR_VALUATION_AUTO_SETTLING)
public class IntelligentRepair2 extends BaseTest {

    @Test(enabled = false, dataProvider = "testDataProvider", description = "IntelligentRepair2. Audit Approved")
    public void feedback_Approved(User user, Claim claim, ServiceAgreement agreement, RnvTaskType rnvTaskType) {
        String lineDescription = RandomUtils.randomName("RnVLine");

        loginAndCreateClaim(user, claim)
                .toCompleteClaimPage()
                .fillClaimForm(claim)
                .completeWithEmail()
                .openRecentClaim()
                .reopenClaim()
                .openSid()
                .fill(lineDescription, agreement.getClaimLineCat_PersonligPleje(), agreement.getClaimLineSubCat_Medicin(), 100.00)
                .closeSidWithOk()
                .findClaimLine(lineDescription)
                .selectLine()
                .sendToRnV()
                .changeTask(lineDescription, rnvTaskType.getRepair())
                .nextRnVstep()
                .sendRnV(agreement)
                .findClaimLine(lineDescription)
                .doAssert(SettlementPage.ClaimLine.Asserts::assertLineIsSentToRepair);

        new RnvService().sendFeedbackWithRepairPrice(BigDecimal.valueOf(Constants.PRICE_50), claim);

        new ClaimNavigationMenu().toRepairValuationProjectsPage().getAssertion()
                .assertTaskHasFeedbackReceivedStatus(agreement);

        new RnvProjectsPage().expandTopTaskDetails()
                .getAssertion().assertAuditResponseText(APPROVE);

        Page.to(SettlementPage.class)
            .toMailsPage()
            .doAssert(mail ->  mail.isMailExist(REPAIR_AND_VALUATION, "Faktura godkendt"));
    }

    @Test(dataProvider = "testDataProvider", description = "IntelligentRepair2. Audit Reject")
    public void feedback_Rejected(User user, Claim claim, ServiceAgreement agreement, RnvTaskType rnvTaskType) {
        String lineDescription = RandomUtils.randomName("RnVLine");

        loginAndCreateClaim(user, claim)
                .openSid()
                .fill(lineDescription, agreement.getClaimLineCat_PersonligPleje(), agreement.getClaimLineSubCat_Medicin(), 100.00)
                .closeSidWithOk()
                .findClaimLine(lineDescription)
                .selectLine()
                .sendToRnV()
                .changeTask(lineDescription, rnvTaskType.getRepair())
                .nextRnVstep()
                .sendRnV(agreement)
                .findClaimLine(lineDescription)
                .doAssert(SettlementPage.ClaimLine.Asserts::assertLineIsSentToRepair);

        new RnvService().sendFeedbackWithRepairPrice(BigDecimal.valueOf(Constants.PRICE_10), claim);

        new ClaimNavigationMenu().toRepairValuationProjectsPage().getAssertion()
                .assertTaskHasFeedbackReceivedStatus(agreement);

        new RnvProjectsPage().expandTopTaskDetails()
                .getAssertion().assertAuditResponseText(REJECT);
    }

    @Test(dataProvider = "testDataProvider", description = "IntelligentRepair2. Audit Manual")
    public void feedback_Manual(User user, Claim claim, ServiceAgreement agreement, RnvTaskType rnvTaskType) {
        String lineDescription = RandomUtils.randomName("RnVLine");

        loginAndCreateClaim(user, claim)
                .openSid()
                .fill(lineDescription, agreement.getClaimLineCat_PersonligPleje(), agreement.getClaimLineSubCat_Medicin(), 100.00)
                .closeSidWithOk()
                .findClaimLine(lineDescription)
                .selectLine()
                .sendToRnV()
                .changeTask(lineDescription, rnvTaskType.getRepair())
                .nextRnVstep()
                .sendRnV(agreement)
                .findClaimLine(lineDescription)
                .doAssert(SettlementPage.ClaimLine.Asserts::assertLineIsSentToRepair);

        new RnvService().sendFeedbackWithRepairPrice(BigDecimal.valueOf(Constants.PRICE_100), claim);

        new ClaimNavigationMenu().toRepairValuationProjectsPage().getAssertion()
                .assertTaskHasFeedbackReceivedStatus(agreement);

        new RnvProjectsPage().expandTopTaskDetails()
                .getAssertion().assertAuditResponseText(MANUAL);
    }
}
