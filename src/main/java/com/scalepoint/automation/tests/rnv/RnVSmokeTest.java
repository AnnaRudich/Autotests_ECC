package com.scalepoint.automation.tests.rnv;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.scalepoint.automation.pageobjects.modules.ClaimNavigationMenu;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.pageobjects.pages.rnv.ProjectsPage;
import com.scalepoint.automation.services.restService.RnvService;
import com.scalepoint.automation.stubs.RnVMock;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.utils.Constants;
import com.scalepoint.automation.utils.RandomUtils;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.ServiceAgreement;
import com.scalepoint.automation.utils.data.entity.Translations;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.math.BigDecimal;

public class RnVSmokeTest extends BaseTest {

    RnVMock.RnvStub rnvStub;

    @BeforeClass
    public void startWireMock() throws IOException {
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
     * send feedback with Invoice
     * Assert: task has feedback received status
     * Assert: there are lines in Invoice dialog opened from Invoice tab
     */
    @Test(dataProvider = "testDataProvider", description = "RnV1. SendLine to RnV, send Service Partner feedback")
    public void sendLineToRnv_SendFeedbackIsSuccess(User user, Claim claim, ServiceAgreement agreement, Translations translations) {

        String lineDescription = RandomUtils.randomName("RnVLine");

        loginAndCreateClaim(user, claim)
                .toCompleteClaimPage()
                .fillClaimForm(claim)
                .completeWithEmail(claim, databaseApi)
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
                .sendRnvIsSuccess(agreement)

                .findClaimLine(lineDescription)
                .doAssert(SettlementPage.ClaimLine.Asserts::assertLineIsSentToRepair);

        new RnvService()
                .sendFeedbackWithInvoiceWithRepairPrice(BigDecimal.valueOf(Constants.PRICE_30),claim, rnvStub);

        new ClaimNavigationMenu().toRepairValuationProjectsPage()
                .expandTopTaskDetails()
                .getAssertion()
                .assertTaskHasFeedbackReceivedStatus(agreement);

        new ProjectsPage().toInvoiceTab()
                .openInvoiceDialogForLineWithIndex(0)
                .findInvoiceLineByIndex(1)
                .assertTotalForTheLineWithIndex(1, Constants.PRICE_50);
    }

    @Test(dataProvider = "testDataProvider", description = "RnV1. SendLine to RnV, send Service Partner feedback")
    public void messagesTest(User user, Claim claim, ServiceAgreement agreement) {

        String lineDescription = RandomUtils.randomName("RnVLine");

        loginAndCreateClaim(user, claim)
                .toCompleteClaimPage()
                .fillClaimForm(claim)
                .completeWithEmail(claim, databaseApi)
                .openRecentClaim()
                .reopenClaim()
                .openSid()
                .fill(lineDescription, agreement.getClaimLineCat_PersonligPleje(), agreement.getClaimLineSubCat_Medicin(), 100.00)
                .closeSidWithOk()
                .findClaimLine(lineDescription)
                .selectLine()
                .sendToRnV()
                .nextRnVstep()
                .sendRnvIsSuccess(agreement);

        final String testMessage = "Test message";
        new ClaimNavigationMenu()
                .toRepairValuationProjectsPage()
                .toCommunicationTab()
                .sendTextMailToSePa(testMessage)
                .assertLatestMessageContains(testMessage);
    }
}

