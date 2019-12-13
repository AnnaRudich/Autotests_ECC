package com.scalepoint.automation.tests.rnv.rnv2;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.scalepoint.automation.pageobjects.modules.ClaimNavigationMenu;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.stubs.RnVMock;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.utils.RandomUtils;
import com.scalepoint.automation.utils.annotations.RunOn;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.ServiceAgreement;
import com.scalepoint.automation.utils.data.entity.Translations;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.driver.DriverType;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;

public class RnvServicePartnerErrorResponses extends BaseTest {

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
     * Service Partner response is 500
     * Assert: the error is displayed while sending
     * Assert: the task has 'fail' status on projects page in RnV Wizard
     */
    @RunOn(DriverType.CHROME)
    @Test(dataProvider = "testDataProvider", description = "RnV1. SendLine to RnV, send Service Partner feedback")
    public void sendLineToRnvFailsOnServicePartnerSide(User user, Claim claim, ServiceAgreement agreement, Translations translations) {

        String lineDescription = RandomUtils.randomName("RnVLine");

        loginAndCreateClaim(user, claim)
                .toCompleteClaimPage()
                .fillClaimForm(claim)
                .completeWithEmail(claim, databaseApi)
                .openRecentClaim()
                .reopenClaim()
                .openSid()
                .fill(lineDescription, agreement.getClaimLineCat_PersonligPleje(), agreement.getClaimLineSubCat_Medicin(), 122.00)
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
}
