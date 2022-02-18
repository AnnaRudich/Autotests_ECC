package com.scalepoint.automation.tests.api;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.scalepoint.automation.services.externalapi.DatabaseApi;
import com.scalepoint.automation.services.externalapi.EventDatabaseApi;
import com.scalepoint.automation.services.externalapi.OauthTestAccountsApi;
import com.scalepoint.automation.services.restService.CaseSettlementDataService;
import com.scalepoint.automation.services.restService.common.ServiceData;
import com.scalepoint.automation.spring.*;
import com.scalepoint.automation.stubs.FraudAlertMock;
import com.scalepoint.automation.stubs.RnVMock;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.tests.widget.LoginFlow;
import com.scalepoint.automation.utils.data.request.ClaimRequest;
import com.scalepoint.automation.utils.threadlocal.CurrentUser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.scalepoint.automation.services.externalapi.OauthTestAccountsApi.Scope.PLATFORM_CASE_READ;

@SpringBootTest(classes = Application.class)
@TestExecutionListeners(inheritListeners = false, listeners = {
        DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class})
@Import({BeansConfiguration.class, EventApiDatabaseConfig.class, WireMockConfig.class, WireMockStubsConfig.class, LoginFlow.class})
public class BaseApiTest extends AbstractTestNGSpringContextTests {

    protected Logger log = LogManager.getLogger(BaseApiTest.class);

    @Autowired
    protected DatabaseApi databaseApi;

    @Autowired
    protected EventDatabaseApi eventDatabaseApi;

    @Autowired
    protected WireMock wireMock;

    @Autowired
    protected FraudAlertMock fraudAlertMock;

    @Autowired
    protected RnVMock.RnvStub rnvStub;

    @Value("${driver.type}")
    protected String browserMode;

    @Value("${subscription.claimline_changed.id}")
    protected String claimLineChangedSubscriptionId;

    @Value("${subscription.fraud_status.id}")
    protected String fraudStatusSubscriptionId;

    @BeforeMethod(alwaysRun = true)
    public void setUpData(Method method, Object[] objects) {

        Thread.currentThread().setName("Thread " + method.getName());
        ThreadContext.put("sessionid", method.getName());
        log.info("Starting {}, thread {}", method.getName(), Thread.currentThread().getId());
        ServiceData.init(databaseApi);
    }

    @AfterMethod(alwaysRun = true)
    public void cleanup(Method method) {
        log.info("Clean up after: {}", method.toString());
        CurrentUser.cleanUp();
        ThreadContext.clearMap();
    }

    protected CaseSettlementDataService getSettlementData(ClaimRequest claimRequest) {
        return new CaseSettlementDataService(new OauthTestAccountsApi().sendRequest(PLATFORM_CASE_READ).getToken())
                .getSettlementData(databaseApi.getSettlementRevisionTokenByClaimNumber(claimRequest.getCaseNumber()), claimRequest.getTenant());
    }

    protected CaseSettlementDataService getSettlementDataForSettledClaims(ClaimRequest claimRequest) {
        return new CaseSettlementDataService(new OauthTestAccountsApi().sendRequest(PLATFORM_CASE_READ).getToken())
                .getSettlementData(databaseApi.getSettlementRevisionTokenByClaimNumberAndClaimStatusSettled(claimRequest.getCaseNumber()), claimRequest.getTenant());
    }

    public static Object[][] addNewParameters(List parameters, Object ...array){

        Object[][] params = new Object[1][];
        try {
            parameters.addAll(Arrays.asList(array));
            params[0] = parameters.toArray();
        } catch (Exception ex) {
            LogManager.getLogger(BaseTest.class).error(ex);
        }
        return params;
    }

    protected static  <T> List<T> getObjectByClass(List objects, Class<T> clazz){

        return (List<T>) objects
                .stream()
                .filter(object -> object.getClass().equals(clazz))
                .map(object -> (T)object)
                .collect(Collectors.toList());
    }

    protected static  <T> List<T> getLisOfObjectByClass(List objects, Class<T> clazz){

        return (List<T>) objects
                .stream()
                .filter(o -> o.getClass().equals(clazz))
                .collect(Collectors.toList());
    }

    protected static  <T> List<T> getObjectBySuperClass(List objects, Class<T> clazz){

        return (List<T>) objects
                .stream()
                .filter(object -> object.getClass().getSuperclass().equals(clazz))
                .collect(Collectors.toList());
    }
}
