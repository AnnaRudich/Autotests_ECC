package com.scalepoint.automation.tests.performance;

import com.scalepoint.automation.services.restService.common.BaseService;
import com.scalepoint.automation.spring.PerformanceTestConfig;
import com.scalepoint.automation.tests.api.BaseApiTest;
import com.scalepoint.automation.utils.data.TestData;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.request.ClaimRequest;
import com.scalepoint.automation.utils.data.request.InsertSettlementItem;
import com.scalepoint.automation.utils.data.request.SelfServiceRequest;
import com.scalepoint.automation.utils.testng.PerformanceSuiteListener;
import org.testng.*;
import org.testng.annotations.*;
import org.testng.xml.XmlTest;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static com.scalepoint.automation.utils.testng.PerformanceSuiteListener.PerformanceSuite.INCREMENTAL;

public class PerformanceTest  extends BaseApiTest{

    static int users;

    @Parameters({"tests.performance.users"})
    @BeforeTest
    public void startWireMock(String users, ITestContext iTestContext) {

        this.users = Integer.valueOf(users);
    }

    @AfterMethod
    public void releaseUser(Object[] objects){

        PerformanceUsers.releaseUser((User) objects[0]);
    }

    @Test(dataProvider = "usersDataProvider", priority=1, groups = {PerformanceTestConfig.TEST_LOGIN_USER})
    public void loginUser(User user) {
        BaseService
                .loginUser(user);
    }

    @Test(dataProvider = "usersDataProvider", priority=2, groups = {PerformanceTestConfig.TEST_LOGIN_AND_OPEN_CLAIM_WITH_ITEMS})
    public void loginAndOpenClaim(User user) {

        ClaimRequest claimRequest = TestData.getClaimRequest();
        claimRequest.setTenant(user.getCompanyName().toLowerCase());
        claimRequest.setCompany(user.getCompanyName().toLowerCase());

        BaseService
                .loginAndOpenClaim(user, claimRequest);
    }

    @Test(dataProvider = "usersDataProvider", priority=3, groups = {PerformanceTestConfig.TEST_SELFSERVICE})
    public void selfService(User user) {

        ClaimRequest claimRequest = TestData.getClaimRequest();
        claimRequest.setTenant(user.getCompanyName().toLowerCase());
        claimRequest.setCompany(user.getCompanyName().toLowerCase());
        SelfServiceRequest selfServiceRequest = TestData.getSelfServiceRequest();

        BaseService
                .loginAndOpenClaim(user, claimRequest)
                .requestSelfService(selfServiceRequest);
    }

    @Test(dataProvider = "usersDataProvider", priority = 4, groups = {PerformanceTestConfig.TEST_LOGIN_AND_OPEN_CLAIM_WITH_ITEMS})
    public void loginAndOpenClaimWithItems(User user) {

        ClaimRequest claimRequest = TestData.getClaimRequest();
        claimRequest.setTenant(user.getCompanyName().toLowerCase());
        claimRequest.setCompany(user.getCompanyName().toLowerCase());
        InsertSettlementItem insertSettlementItem = TestData.getInsertSettlementItem();

        BaseService
                .loginAndOpenClaimWithItems(user, claimRequest,insertSettlementItem);
    }

    @DataProvider(name = "usersDataProvider", parallel = true)
    public static Object[][] usersDataProvider(Method method) throws InterruptedException {

        Object[][] objects = new Object[users][1];

        for (int i = 0; i < users; i++) {

            objects[i][0] = new PerformanceUsers().takeUser();
        }

        return objects;
    }
}
