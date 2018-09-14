package com.scalepoint.automation.tests.api;

import com.scalepoint.automation.services.externalapi.DatabaseApi;
import com.scalepoint.automation.services.externalapi.EventDatabaseApi;
import com.scalepoint.automation.services.externalapi.TestAccountsApi;
import com.scalepoint.automation.services.restService.CaseSettlementDataService;
import com.scalepoint.automation.services.restService.Common.ServiceData;
import com.scalepoint.automation.spring.Application;
import com.scalepoint.automation.utils.data.request.ClaimRequest;
import com.scalepoint.automation.utils.threadlocal.CurrentUser;
import org.apache.log4j.MDC;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.lang.reflect.Method;

import static com.scalepoint.automation.services.externalapi.TestAccountsApi.Scope.PLATFORM_CASE_READ;

@SpringApplicationConfiguration(classes = Application.class)
@TestExecutionListeners(inheritListeners = false, listeners = {
        DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class})
@IntegrationTest
public class BaseApiTest extends AbstractTestNGSpringContextTests {

    protected Logger log = LogManager.getLogger(BaseApiTest.class);

    @Autowired
    protected DatabaseApi databaseApi;

    @Autowired
    protected EventDatabaseApi eventDatabaseApi;

    @Value("${driver.type}")
    protected String browserMode;

    @BeforeMethod
    public void setUpData(Method method){
        Thread.currentThread().setName("Thread "+method.getName());
        MDC.put("sessionid", method.getName());
        log.info("Starting {}, thread {}", method.getName(), Thread.currentThread().getId());
        ServiceData.init(databaseApi);
    }

    @AfterMethod
    public void cleanup(Method method) {
        log.info("Clean up after: {}", method.toString());
        CurrentUser.cleanUp();
        MDC.clear();
    }

    protected CaseSettlementDataService getSettlementData(ClaimRequest claimRequest){
        return new CaseSettlementDataService(new TestAccountsApi().sendRequest(PLATFORM_CASE_READ).getToken())
                .getSettlementData(databaseApi.getSettlementRevisionTokenByClaimNumber(claimRequest.getCaseNumber()), claimRequest.getTenant());
    }

    protected CaseSettlementDataService getSettlementDataForSettledClaims(ClaimRequest claimRequest){
        return new CaseSettlementDataService(new TestAccountsApi().sendRequest(PLATFORM_CASE_READ).getToken())
                .getSettlementData(databaseApi.getSettlementRevisionTokenByClaimNumberAndClaimStatusSettled(claimRequest.getCaseNumber()), claimRequest.getTenant());
    }
}
