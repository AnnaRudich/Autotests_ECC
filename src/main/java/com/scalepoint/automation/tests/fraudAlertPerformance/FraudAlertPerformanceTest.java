package com.scalepoint.automation.tests.fraudAlertPerformance;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.opencsv.CSVWriter;
import com.scalepoint.automation.services.externalapi.EventApiService;
import com.scalepoint.automation.services.restService.*;
import com.scalepoint.automation.services.restService.Common.BaseService;
import com.scalepoint.automation.stubs.FraudAlertMock;
import com.scalepoint.automation.tests.api.BaseApiTest;
import com.scalepoint.automation.utils.data.TestData;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.eventsApiEntity.changed.Case;
import com.scalepoint.automation.utils.data.entity.eventsApiEntity.fraudStatus.ClaimLineChanged;
import com.scalepoint.automation.utils.data.request.ClaimRequest;
import com.scalepoint.automation.utils.data.request.InsertSettlementItem;
import com.scalepoint.automation.utils.data.request.SelfServiceLossItems;
import com.scalepoint.automation.utils.data.request.SelfServiceRequest;
import org.json.simple.parser.ParseException;
import org.testng.annotations.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class FraudAlertPerformanceTest extends BaseApiTest {

    static final String TENANT = "topdanmark";
    static final String COUNTRY = "dk";
    FraudAlertMock.FraudAlertStubs fraudAlertStubs;

    private static int users;
    private Map<String, List<Duration>> manual = new HashMap();
    private Map<String, List<Duration>> selfService = new HashMap();
    private Map<String, List<Duration>> bulk = new HashMap();

    @Parameters({"users"})
    @BeforeClass
    public void startWireMock(String users) throws IOException {

        this.users = Integer.valueOf(users);

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

    @AfterClass
    public void measurement() throws IOException {

        printResults(manual, "manual");
        printResults(selfService, "selfService");
        printResults(bulk, "bulk");
    }

    private void printResults(Map<String, List<Duration>> map, String name) throws IOException {
        if(map.size() == 0){
            return;
        }

        CSVWriter writer = new CSVWriter(new FileWriter(new File(name.concat(".csv"))));

        map.entrySet().stream().forEach(
                entry -> {
                    String key = entry.getKey();
                    List<Duration> value = entry.getValue();
                    Duration firstStep = value.get(0);
                    Duration secondStep = value.get(1);
                    Duration caseTime = value.get(2);
                    Duration sum = firstStep.plus(secondStep).plus(caseTime);

                    log.info("[{}] Case number: {}, duration {}, {}, {}, sum {}", name, key, firstStep, caseTime, secondStep, sum);
                    writer.writeNext(new String[] {key, String.valueOf(firstStep.toNanos()/1000000), String.valueOf(caseTime.toNanos()/1000000), String.valueOf(secondStep.toNanos()/1000000), String.valueOf(sum)});
                });

        Double average = map.values().stream().map(durations -> durations.get(0).plus(durations.get(1)).toNanos()).mapToLong(Long::longValue).average().getAsDouble()/1000000000.0;
        Double max = map.values().stream().map(durations -> durations.get(0).plus(durations.get(1)).toNanos()).mapToDouble(Long::doubleValue).max().getAsDouble()/1000000000.0;

        Double firstStepAverage = map.values().stream().map(durations -> durations.get(0).toNanos()).mapToLong(Long::longValue).average().getAsDouble()/1000000000.0;
        Double firstStepMax = map.values().stream().map(durations -> durations.get(0).toNanos()).mapToDouble(Long::doubleValue).max().getAsDouble()/1000000000.0;

        Double secondStepAverage = map.values().stream().map(durations -> durations.get(1).toNanos()).mapToLong(Long::longValue).average().getAsDouble()/1000000000.0;
        Double secondStepMax = map.values().stream().map(durations -> durations.get(1).toNanos()).mapToDouble(Long::doubleValue).max().getAsDouble()/1000000000.0;

        Double caseAverage = map.values().stream().map(durations -> durations.get(2).toNanos()).mapToLong(Long::longValue).average().getAsDouble()/1000000000.0;
        Double caseMax = map.values().stream().map(durations -> durations.get(2).toNanos()).mapToDouble(Long::doubleValue).max().getAsDouble()/1000000000.0;

        log.info("[{}] FirstStep Average {}",name ,firstStepAverage);
        log.info("[{}] FirstStep Max {}", name, firstStepMax);

        log.info("[{}] FirstStep Average {}",name ,secondStepAverage);
        log.info("[{}] FirstStep Max {}", name, secondStepMax);

        log.info("[{}] Case Average {}",name ,caseAverage);
        log.info("[{}] Case Max {}", name, caseMax);

        log.info("[{}] Average {}",name ,average);
        log.info("[{}] Max {}", name, max);

        writer.writeNext(new String[]{"FirstStep Average", String.valueOf(firstStepAverage)});
        writer.writeNext(new String[]{"FirstStep Max", String.valueOf(firstStepMax)});

        writer.writeNext(new String[]{"SecondStep Average", String.valueOf(secondStepAverage)});
        writer.writeNext(new String[]{"SecondStep Max", String.valueOf(secondStepMax)});

        writer.writeNext(new String[]{"Case Average", String.valueOf(caseAverage)});
        writer.writeNext(new String[]{"Vase Max", String.valueOf(caseMax)});

        writer.writeNext(new String[]{"Average", String.valueOf(average)});
        writer.writeNext(new String[]{"Max", String.valueOf(max)});
        writer.close();
    }

    @Test(dataProvider = "usersDataProvider", enabled = true)
    public void testAdd(User user) throws IOException {
        ClaimRequest claimRequest = TestData.getClaimRequestFraudAlert();

        ClaimSettlementItemsService claimSettlementItemsService = BaseService
                .loginAndOpenClaimWithItems(user, claimRequest, TestData.getInsertSettlementItem());

        topdanmark(claimSettlementItemsService, manual);
    }

    @Test(dataProvider = "usersDataProvider", enabled = false)
    public void testRemove(User user) throws IOException {

        ClaimRequest claimRequest = TestData.getClaimRequestFraudAlert();
        InsertSettlementItem insertSettlementItem = TestData.getInsertSettlementItem();

        ClaimSettlementItemsService claimSettlementItemsService = BaseService
                .loginAndOpenClaimWithItems(user, claimRequest, insertSettlementItem)
                .removeLines(insertSettlementItem);

        topdanmark(claimSettlementItemsService, manual);
    }

    @Test(dataProvider = "usersDataProvider", enabled = true)
    public void testSelfService(User user) throws IOException {

        ClaimRequest claimRequest = TestData.getClaimRequestFraudAlert();
        SelfServiceRequest selfServiceRequest = TestData.getSelfServiceRequest();
        SelfServiceLossItems selfServiceLossItems = TestData.getSelfServiceLossItems();
        selfServiceRequest.setClaimsNo(claimRequest.getCaseNumber());

        SelfServiceService selfServiceService = BaseService
                .loginAndOpenClaim(user, claimRequest)
                .requestSelfService(selfServiceRequest)
                .loginToSS(selfServiceRequest.getPassword())
                .addLossItem(selfServiceLossItems)
                .submitted();

        topdanmark(selfServiceService, selfService);
    }

    @Test(dataProvider = "usersDataProvider", enabled = true)
    public void testBulk(User user) throws IOException, ParseException {

        ClaimRequest claimRequest = TestData.getClaimRequestFraudAlert();
        SelfServiceRequest selfServiceRequest = TestData.getSelfServiceRequest();

        selfServiceRequest.setClaimsNo(claimRequest.getCaseNumber());
        ImportExcelService importExcelService = BaseService
                .loginAndOpenClaim(user, claimRequest)
                .importExcel()
                .match();

        topdanmark(importExcelService, bulk);
    }

    @DataProvider(name = "usersDataProvider", parallel = true)
    public static Object[][] usersDataProvider(Method method) {

        Object[][] objects = new Object[users][1];

        for(int i =0;i<users;i++){

            objects[i][0] = new User("autotest-topdanmark".concat(new Integer(i + 1).toString()), "12341234");
        }

        return objects;
    }

    private void topdanmark(BaseService claimSettlementItemsService, Map<String, List<Duration>> map) throws IOException {

        LocalDateTime start = LocalDateTime.now();
        String token = claimSettlementItemsService.getData().getClaimToken();
        List<ClaimLineChanged> events = fraudAlertStubs
                .waitForClaimUpdatedEvents(token, 1);
        LocalDateTime uni = LocalDateTime.now();

        UnifiedIntegrationService unifiedIntegrationService = new UnifiedIntegrationService();
        Case caseData = unifiedIntegrationService
                .getCaseEndpointByToken(COUNTRY, TENANT, token, events.get(0).getEventId());
        new EventApiService().sendFraudStatus(events.get(0), "FRAUDULENT");

        LocalDateTime fraudStatus = LocalDateTime.now();
        new FraudStatusService().waitForFraudStatus("FRAUDULENT");
        LocalDateTime end = LocalDateTime.now();

        List<Duration> list = new LinkedList<>();
        list.add(Duration.between(start, uni));
        list.add(Duration.between(fraudStatus, end));
        list.add(unifiedIntegrationService.getDuration());
        map.put(caseData.getCaseNumber(), list);
    }
}
