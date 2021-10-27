package com.scalepoint.automation.tests;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import com.github.tomakehurst.wiremock.verification.LoggedRequest;
import com.scalepoint.automation.pageobjects.pages.CustomerDetailsPage;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.testGroups.TestGroups;
import com.scalepoint.automation.utils.annotations.Jira;
import com.scalepoint.automation.utils.annotations.RunOn;
import com.scalepoint.automation.utils.annotations.ftoggle.FeatureToggleSetting;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.input.Claim;
import com.scalepoint.automation.utils.driver.DriverType;
import org.testng.annotations.Test;

import java.util.List;

import static com.scalepoint.automation.pageobjects.pages.MailsPage.MailType.SETTLEMENT_NOTIFICATION_CLOSED_EXTERNAL;
import static com.scalepoint.automation.services.externalapi.ftoggle.FeatureIds.SCALEPOINTID_LOGIN_ENABLED;

public class CompleteClaimExternally extends BaseTest {

    //    MailserviceMock.MailserviceStub mailserviceStub;
//
//    @BeforeClass
//    public void startWireMock() {
//
//        WireMock.configureFor(wireMock);
//        wireMock.resetMappings();
//        mailserviceStub = new MailserviceMock(wireMock, databaseApi).addStub();
//        wireMock.allStubMappings()
//                .getMappings()
//                .stream()
//                .forEach(m -> log.info(String.format("Registered stubs: %s",m.getRequest())));
//    }
    @RunOn(DriverType.CHROME)
    @RequiredSetting(type = FTSetting.SETTLE_EXTERNALLY)
    @Jira("https://jira.scalepoint.com/browse/CHARLIE-515")
    @Test(groups = {TestGroups.COMPLETE_CLAIM_EXTERNALLY}, dataProvider = "testDataProvider",
            description = "CHARLIE-515 Completing of claim Externally (External email)" +
                    "Possible to Complete claim Externally from Base info page. Completed externally claim is added to the latest claims list with Closed Externally status")
    public void charlie515_completeClaimExternallyFromBaseInfoPage(User user, Claim claim) {

        wireMock.startStubRecording("https://qa-shr-ms.spcph.local");

        CustomerDetailsPage customerDetailsPage = loginAndCreateClaim(user, claim)
                .toCompleteClaimPage()
                .fillClaimForm(claim)
                .completeExternally(claim, databaseApi)
                .doAssert(myPage ->
                        myPage.assertClaimHasStatus(claim.getStatusClosedExternally())
                )
                .openRecentClaim();



        List<LoggedRequest> list = wireMock.find(WireMock.anyRequestedFor(WireMock.urlMatching("/api/v1/email.*")));


        customerDetailsPage
                .toMailsPage(mailserviceStub)
                .doAssert(mail ->
                        mail.isMailExist(SETTLEMENT_NOTIFICATION_CLOSED_EXTERNAL));



        List<StubMapping> test = wireMock.takeSnapshotRecording();
        list.get(0);

    }

    @RequiredSetting(type = FTSetting.ENABLE_SETTLE_EXTERNALLY_BUTTON_IN_SETTLEMENT_PAGE)
    @RequiredSetting(type = FTSetting.SETTLE_EXTERNALLY)
    @Jira("https://jira.scalepoint.com/browse/CHARLIE-515")
    @Jira("https://jira.scalepoint.com/browse/CHARLIE-2152")
    @Test(groups = {TestGroups.COMPLETE_CLAIM_EXTERNALLY}, dataProvider = "testDataProvider",
            description = "CHARLIE-515 Completing of claim Externally (External email)" +
                    "Possible to Complete claim Externally from Settlement page")
    public void completeClaimExternallyFromSettlementPageTest(User user, Claim claim) {
        loginAndCreateClaim(user, claim)
                .completeClaimWithoutMail(claim)
                .doAssert(myPage ->
                        myPage.assertClaimHasStatus(claim.getStatusClosedExternally())
                )
                .openRecentClaim()
                .toMailsPage(mailserviceStub)
                .doAssert(mail ->
                        mail.isMailExist(SETTLEMENT_NOTIFICATION_CLOSED_EXTERNAL));
    }

    @FeatureToggleSetting(type = SCALEPOINTID_LOGIN_ENABLED)
    @RequiredSetting(type = FTSetting.ENABLE_SETTLE_EXTERNALLY_BUTTON_IN_SETTLEMENT_PAGE)
    @RequiredSetting(type = FTSetting.SETTLE_EXTERNALLY)
    @Jira("https://jira.scalepoint.com/browse/CHARLIE-515")
    @Jira("https://jira.scalepoint.com/browse/CHARLIE-2152")
    @Test(groups = {TestGroups.COMPLETE_CLAIM_EXTERNALLY, TestGroups.SCALEPOINT_ID}, dataProvider = "testDataProvider",
            description = "CHARLIE-515 Completing of claim Externally (External email)" +
                    "Possible to Complete claim Externally from Settlement page")
    public void completeClaimExternallyFromSettlementPageScalepointIdTest(User user, Claim claim) {
        completeClaimExternallyFromSettlementPageTest(user, claim);
    }
}




