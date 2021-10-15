package com.scalepoint.automation.tests.fraudAlert;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.scalepoint.automation.services.externalapi.EventApiService;
import com.scalepoint.automation.stubs.FraudAlertMock.FraudAlertStubs;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.utils.data.entity.eventsApiEntity.fraudStatus.ClaimLineChanged;
import com.scalepoint.automation.utils.data.request.ClaimRequest;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static com.scalepoint.automation.utils.DateUtils.ISO8601;
import static com.scalepoint.automation.utils.DateUtils.format;
import static io.restassured.RestAssured.given;

public class FraudAlertBase extends BaseTest {

    static final String SONY_HDR_CX450 = "Sony Handycam HDR-CX450";
    static final String IPHONE = "iPhone 8";

    static final String TENANT = "topdanmark";
    static final String COUNTRY = "dk";
    FraudAlertStubs fraudAlertStubs;

    WireMockServer wireMockServer;

    @BeforeClass
    public void startWireMock() throws IOException {

        int httpPort = 8080;

         wireMockServer = new WireMockServer(wireMockConfig().port(httpPort));
        wireMockServer.start();
        WireMock.configureFor("localhost", httpPort);
        wireMock = new WireMock("localhost", httpPort);
        wireMockServer.stubFor(WireMock.get("/test").willReturn(aResponse().withStatus(200)));
//        given().log().all()
//                .get("http://124.0.0.1:7777/test")
//                .then().log().all();
        log.info(wireMockServer.isRunning());


        fraudAlertStubs = fraudAlertMock.addStub(TENANT);

        new EventApiService().scheduleSubscription(claimLineChangedSubscriptionId);
        new EventApiService().scheduleSubscription(fraudStatusSubscriptionId);


    }

    @AfterClass
            public void test(){
        wireMockServer.stop();
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