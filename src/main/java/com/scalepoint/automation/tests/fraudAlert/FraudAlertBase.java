package com.scalepoint.automation.tests.fraudAlert;

import com.scalepoint.automation.services.externalapi.EventApiService;
import com.scalepoint.automation.stubs.FraudAlertMock.FraudAlertStubs;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.utils.data.entity.eventsApiEntity.fraudStatus.ClaimLineChanged;
import com.scalepoint.automation.utils.data.request.ClaimRequest;
import org.testng.annotations.BeforeClass;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

import static com.scalepoint.automation.utils.DateUtils.ISO8601;
import static com.scalepoint.automation.utils.DateUtils.format;

public class FraudAlertBase extends BaseTest {

    static final String SONY_HDR_CX450 = "Sony Handycam HDR-CX450";
    static final String IPHONE = "iPhone 8";

    static final String TENANT = "topdanmark";
    static final String COUNTRY = "dk";
    FraudAlertStubs fraudAlertStubs;

    @BeforeClass(alwaysRun = true)
    public void startWireMock() throws IOException {

        fraudAlertStubs = fraudAlertMock.addStub(TENANT);

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