package com.scalepoint.automation.tests.fraudAlert;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.scalepoint.automation.services.externalapi.EventApiService;
import com.scalepoint.automation.services.restService.ClaimSettlementItemsService;
import com.scalepoint.automation.services.restService.Common.BaseService;
import com.scalepoint.automation.services.restService.UnifiedIntegrationService;
import com.scalepoint.automation.stubs.FraudAlertMock;
import com.scalepoint.automation.tests.api.BaseApiTest;
import com.scalepoint.automation.utils.data.TestData;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.eventsApiEntity.changed.Case;
import com.scalepoint.automation.utils.data.entity.eventsApiEntity.changed.Item;
import com.scalepoint.automation.utils.data.request.ClaimRequest;
import com.scalepoint.automation.utils.data.request.InsertSettlementItem;
import com.scalepoint.automation.utils.data.response.Token;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.lang.reflect.Method;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.TemporalUnit;
import java.util.List;
import java.util.stream.Collectors;

public class FraudAlertPerformanceTest extends BaseApiTest {

    static final String TENANT = "topdanmark";
    static final String COUNTRY = "dk";
    FraudAlertMock.FraudAlertStubs fraudAlertStubs;

    @BeforeClass
    public void startWireMock() throws IOException {
        WireMock.configureFor(wireMock);
        wireMock.resetMappings();
        fraudAlertStubs = new FraudAlertMock(wireMock).addStub(TENANT);
        wireMock.allStubMappings()
                .getMappings()
                .stream()
                .forEach(m -> log.info(String.format("Registered stubs: %s",m.getRequest())));
        new EventApiService().scheduleSubscription("4");
        new EventApiService().scheduleSubscription("9");
    }

    @Test(dataProvider = "usersDataProvider")
    public void testAdd(User user) throws IOException {

        ClaimRequest claimRequest = TestData.getClaimRequestFraudAlert();


        ClaimSettlementItemsService claimSettlementItemsService = BaseService.loginAndOpenClaimWithItems(user, claimRequest, TestData.getInsertSettlementItem());
        LocalDateTime start = LocalDateTime.now();
        String token = claimSettlementItemsService.getData().getClaimToken();
                fraudAlertStubs
                .waitForClaimUpdatedEvents(token, 1);

        Case caseChanged = new UnifiedIntegrationService()
                .getCaseEndpointByToken(COUNTRY, TENANT, token);

        new EventApiService().sendFraudStatus(caseChanged, "FRAUDULENT");
        databaseApi.waitForFraudStatusChange(1, claimRequest.getCaseNumber());
        LocalDateTime end = LocalDateTime.now();

        long duration = Duration.between(end, start).getSeconds();
        log.info("Duration: {}", duration);
    }

    @Test(enabled = false)
    public void testRemove() {

        TestData.getInsertSettlementItem();
        User user = TestData
                .getSystemUsers()
                .getUsers()
                .stream()
                .filter(u -> u.getCompanyCode() != null)
                .filter(u -> u.getCompanyCode().equals("TOPDANMARK"))
                .findFirst()
                .get();

        InsertSettlementItem insertSettlementItem = TestData.getInsertSettlementItem();

        ClaimSettlementItemsService claimSettlementItemsService = BaseService
                .loginAndOpenClaimWithItems(user, TestData.getClaimRequestFraudAlert(), insertSettlementItem)
                .removeLines(insertSettlementItem);
    }

    @DataProvider(name = "usersDataProvider", parallel = true)
    public static Object[][] usersDataProvider(Method method) {

        int size = 10;

        Object[][] objects = new Object[size][1];

        for(int i =0;i<size;i++){

            objects[i][0] = new User("autotest-topdanmark".concat(new Integer(i+1).toString()), "12341234");
        }

        return objects;
    }
}
