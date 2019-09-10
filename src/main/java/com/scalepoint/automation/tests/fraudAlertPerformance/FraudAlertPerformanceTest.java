package com.scalepoint.automation.tests.fraudAlertPerformance;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.scalepoint.automation.services.externalapi.EventApiService;
import com.scalepoint.automation.services.restService.ClaimSettlementItemsService;
import com.scalepoint.automation.services.restService.Common.BaseService;
import com.scalepoint.automation.services.restService.FraudStatusService;
import com.scalepoint.automation.services.restService.SelfServiceService;
import com.scalepoint.automation.stubs.FraudAlertMock;
import com.scalepoint.automation.tests.api.BaseApiTest;
import com.scalepoint.automation.utils.data.TestData;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.eventsApiEntity.fraudStatus.ClaimLineChanged;
import com.scalepoint.automation.utils.data.request.ClaimRequest;
import com.scalepoint.automation.utils.data.request.InsertSettlementItem;
import com.scalepoint.automation.utils.data.request.SelfServiceLossItems;
import com.scalepoint.automation.utils.data.request.SelfServiceRequest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

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

    @Test(dataProvider = "usersDataProvider", enabled = true)
    public void testAdd(User user) {
        ClaimRequest claimRequest = TestData.getClaimRequestFraudAlert();

        ClaimSettlementItemsService claimSettlementItemsService = BaseService.loginAndOpenClaimWithItems(user, claimRequest, TestData.getInsertSettlementItem());
        LocalDateTime start = LocalDateTime.now();
        String token = claimSettlementItemsService.getData().getClaimToken();
        List<ClaimLineChanged> events = fraudAlertStubs
                .waitForClaimUpdatedEvents(token, 1);

        new EventApiService().sendFraudStatus(events.get(0), "FRAUDULENT");
        new FraudStatusService().waitForFraudStatus("FRAUDULENT");

        LocalDateTime end = LocalDateTime.now();

        long duration = Duration.between(end, start).getSeconds();
        log.info("Duration: {}", duration);
    }

    @Test(dataProvider = "usersDataProvider", enabled = false)
    public void testRemove(User user) {

        ClaimRequest claimRequest = TestData.getClaimRequestFraudAlert();

        InsertSettlementItem insertSettlementItem = TestData.getInsertSettlementItem();

        ClaimSettlementItemsService claimSettlementItemsService = BaseService
                .loginAndOpenClaimWithItems(user, claimRequest, insertSettlementItem)
                .removeLines(insertSettlementItem);

        LocalDateTime start = LocalDateTime.now();
        String token = claimSettlementItemsService.getData().getClaimToken();
        List<ClaimLineChanged> events = fraudAlertStubs
                .waitForClaimUpdatedEvents(token, 1);

        new EventApiService().sendFraudStatus(events.get(0), "FRAUDULENT");
        new FraudStatusService().waitForFraudStatus("FRAUDULENT");

        LocalDateTime end = LocalDateTime.now();

        long duration = Duration.between(end, start).getSeconds();
        log.info("Duration: {}", duration);
    }

    @Test(dataProvider = "usersDataProvider", enabled = true)
    public void testSelfService(User user) {

        ClaimRequest claimRequest = TestData.getClaimRequestFraudAlert();
        SelfServiceRequest selfServiceRequest = TestData.getSelfServiceRequest();
        SelfServiceLossItems selfServiceLossItems = TestData.getSelfServiceLossItems();

        selfServiceRequest.setClaimsNo(claimRequest.getCaseNumber());
        SelfServiceService claimSettlementItemsService = BaseService
                .loginAndOpenClaim(user, claimRequest)
                .requestSelfService(selfServiceRequest)
                .loginToSS(selfServiceRequest.getPassword())
                .addLossItem(selfServiceLossItems)
                .submitted();

        LocalDateTime start = LocalDateTime.now();
        String token = claimSettlementItemsService.getData().getClaimToken();
        List<ClaimLineChanged> events = fraudAlertStubs
                .waitForClaimUpdatedEvents(token, 1);

        new EventApiService().sendFraudStatus(events.get(0), "FRAUDULENT");
        new FraudStatusService().waitForFraudStatus("FRAUDULENT");

        LocalDateTime end = LocalDateTime.now();

        long duration = Duration.between(end, start).getSeconds();
        log.info("Duration: {}", duration);
    }

    @Test(dataProvider = "usersDataProvider", enabled = true)
    public void testBulk(User user) throws IOException {

        ClaimRequest claimRequest = TestData.getClaimRequestFraudAlert();
        SelfServiceRequest selfServiceRequest = TestData.getSelfServiceRequest();

        selfServiceRequest.setClaimsNo(claimRequest.getCaseNumber());
        SelfServiceService claimSettlementItemsService = BaseService
                .loginAndOpenClaim(user, claimRequest)
                .importExcel();


        LocalDateTime start = LocalDateTime.now();
        String token = claimSettlementItemsService.getData().getClaimToken();
        List<ClaimLineChanged> events = fraudAlertStubs
                .waitForClaimUpdatedEvents(token, 1);

        new EventApiService().sendFraudStatus(events.get(0), "FRAUDULENT");
        new FraudStatusService().waitForFraudStatus("FRAUDULENT");

        LocalDateTime end = LocalDateTime.now();

        long duration = Duration.between(end, start).getSeconds();
        log.info("Duration: {}", duration);
    }

    @DataProvider(name = "usersDataProvider", parallel = true)
    public static Object[][] usersDataProvider(Method method) {

        int size = 1;

        Object[][] objects = new Object[size][1];

        for(int i =0;i<size;i++){

            objects[i][0] = new User("autotest-topdanmark".concat(new Integer(i + 1).toString()), "12341234");
        }

        return objects;
    }
}
