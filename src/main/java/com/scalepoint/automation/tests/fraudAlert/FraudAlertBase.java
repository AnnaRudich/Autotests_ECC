package com.scalepoint.automation.tests.fraudAlert;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.scalepoint.automation.pageobjects.pages.MailsPage;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.services.externalapi.EventApiService;
import com.scalepoint.automation.stubs.FraudAlertMock;
import com.scalepoint.automation.stubs.FraudAlertMock.FraudAlertStubs;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.utils.Constants;
import com.scalepoint.automation.utils.annotations.UserCompany;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.eventsApiEntity.fraudStatus.ClaimLineChanged;
import com.scalepoint.automation.utils.data.entity.input.Claim;
import com.scalepoint.automation.utils.data.entity.input.ClaimItem;
import com.scalepoint.automation.utils.data.request.ClaimRequest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static com.scalepoint.automation.services.usersmanagement.CompanyCode.TOPDANMARK;
import static com.scalepoint.automation.utils.DateUtils.ISO8601;
import static com.scalepoint.automation.utils.DateUtils.format;

public class FraudAlertBase extends BaseTest {

    static final String SONY_HDR_CX450 = "Sony Handycam HDR-CX450";
    static final String IPHONE = "iPhone 8";

    static final String TENANT = "topdanmark";
    static final String COUNTRY = "dk";
    FraudAlertStubs fraudAlertStubs;

    @BeforeClass
    public void startWireMock() throws IOException {
        WireMock.configureFor(wireMock);
        wireMock.resetMappings();
        fraudAlertStubs = new FraudAlertMock(wireMock).addStub(TENANT);
        wireMock.allStubMappings()
                .getMappings()
                .stream()
                .forEach(m -> log.info(String.format("Registered stubs: %s",m.getRequest())));
        new EventApiService().scheduleSubscription(claimLineChangedSubscriptionId);
        new EventApiService().scheduleSubscription(fraudStatusSubscriptionId);
    }

    String excelImportPath = new File("src\\main\\resources\\excelImport\\DK_NYT ARK(3)(a).xls").getAbsolutePath();

    void fraudStatus(ClaimLineChanged event, String caseNumber, EventApiService.FraudStatus fraudStatus){

        new EventApiService().sendFraudStatus(event, fraudStatus.name());
        databaseApi.waitForFraudStatusChange(fraudStatus.getStatus(), caseNumber);
    }

    String getToken(ClaimRequest claimRequest){

        claimRequest.setAccidentDate(format(LocalDateTime.now().minusDays(2L), ISO8601));
        return createCwaClaimAndGetClaimToken(claimRequest);
    }
}