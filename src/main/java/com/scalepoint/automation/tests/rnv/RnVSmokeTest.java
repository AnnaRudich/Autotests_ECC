package com.scalepoint.automation.tests.rnv;

import com.scalepoint.automation.pageobjects.modules.ClaimNavigationMenu;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.services.restService.RnvService;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.utils.RandomUtils;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.ServiceAgreement;
import com.scalepoint.automation.utils.data.entity.Translations;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.testng.annotations.Test;

public class RnVSmokeTest extends BaseTest {

    @Test(dataProvider = "testDataProvider", description = "RnV1. SendLine to RnV, send Service Partner feedback")
    public void sendLineToRnv_SendFeedbackIsSuccess(User user, Claim claim, ServiceAgreement agreement, Translations translations) {

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

        new RnvService().sendDefaultFeedbackWithInvoice(claim);

        new ClaimNavigationMenu().toRepairValuationProjectsPage()
                .expandTopTaskDetails()
                .getAssertion()
                .assertTaskHasFeedbackReceivedStatus(agreement);
    }

    @Test(dataProvider = "testDataProvider", description = "RnV1. SendLine to RnV, send Service Partner feedback")
    public void messagesTest(User user, Claim claim, ServiceAgreement agreement) {

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
                .nextRnVstep()
                .sendRnV(agreement);

        final String testMessage = "Test message";
        new ClaimNavigationMenu()
                .toRepairValuationProjectsPage()
                .toCommunicationTab()
                .sendTextMailToSePa(testMessage)
                .assertLatestMessageContains(testMessage);
    }
}

