package com.scalepoint.automation.tests.performance;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.scalepoint.automation.services.restService.SettlementClaimService;
import com.scalepoint.automation.services.restService.TextSearchService;
import com.scalepoint.automation.services.restService.common.BaseService;
import com.scalepoint.automation.spring.PerformanceTestConfig;
import com.scalepoint.automation.tests.api.BaseApiTest;
import com.scalepoint.automation.utils.data.TestData;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.request.ClaimRequest;
import com.scalepoint.automation.utils.data.request.InsertSettlementItem;
import com.scalepoint.automation.utils.data.request.SelfServiceLossItems;
import com.scalepoint.automation.utils.data.request.SelfServiceRequest;
import com.scalepoint.automation.utils.data.response.TextSearchResult.ProductResult;
import org.testng.*;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;

public class PerformanceTest  extends BaseApiTest{

    static int users;

    @Parameters({"tests.performance.users"})
    @BeforeTest
    public void startWireMock(String users, ITestContext iTestContext) {

        this.users = Integer.valueOf(users);
        LocalDateTime start = LocalDateTime.now();
        PerformanceUsers.init(this.users);

        log.info("Duration: {}", Duration.between(start, LocalDateTime.now()).getNano());
    }

    @AfterMethod
    public void releaseUser(Object[] objects){

        PerformanceUsers.releaseUser((User) objects[0]);
    }

    @Test(dataProvider = "usersDataProvider", groups = {PerformanceTestConfig.TEST_LOGIN_USER})
    public void loginUser(User user) {

        BaseService.loginUser(user);
    }

    @Test(dataProvider = "usersDataProvider", groups = {PerformanceTestConfig.TEST_LOGIN_AND_OPEN_CLAIM})
    public void loginAndOpenClaim(User user) {

        ClaimRequest claimRequest = TestData.getClaimRequest();
        claimRequest.setTenant(user.getCompanyName().toLowerCase());
        claimRequest.setCompany(user.getCompanyName().toLowerCase());

        BaseService.loginAndOpenClaim(user, claimRequest);
    }

    @Test(dataProvider = "usersDataProvider", groups = {PerformanceTestConfig.TEST_SELFSERVICE})
    public void selfService(User user) {

        ClaimRequest claimRequest = TestData.getClaimRequest();
        claimRequest.setTenant(user.getCompanyName().toLowerCase());
        claimRequest.setCompany(user.getCompanyName().toLowerCase());
        SelfServiceRequest selfServiceRequest = TestData.getSelfServiceRequest();
        SelfServiceLossItems selfServiceLossItems = TestData.getSelfServiceLossItems();

        BaseService
                .loginAndOpenClaim(user, claimRequest)
                .requestSelfService(selfServiceRequest)
                .loginToSS(selfServiceRequest.getPassword())
                .addLossItem(selfServiceLossItems)
                .submitted();
    }

    @Test(dataProvider = "usersDataProvider", groups = {PerformanceTestConfig.TEST_LOGIN_AND_OPEN_CLAIM_WITH_ITEMS})
    public void loginAndOpenClaimWithItems(User user) {

        ClaimRequest claimRequest = TestData.getClaimRequest();
        claimRequest.setTenant(user.getCompanyName().toLowerCase());
        claimRequest.setCompany(user.getCompanyName().toLowerCase());
        InsertSettlementItem insertSettlementItem = TestData.getInsertSettlementItem();

        BaseService.loginAndOpenClaimWithItems(user, claimRequest,insertSettlementItem);
    }

    @Test(dataProvider = "usersDataProvider", groups = {PerformanceTestConfig.TEST_CLOSE_WITH_EMAIL})
    public void closeWithEmail(User user) {

        ClaimRequest claimRequest = TestData.getClaimRequest();
        claimRequest.setTenant(user.getCompanyName().toLowerCase());
        claimRequest.setCompany(user.getCompanyName().toLowerCase());
        InsertSettlementItem insertSettlementItem = TestData.getInsertSettlementItem();

        BaseService
                .loginAndOpenClaimWithItems(user, claimRequest, insertSettlementItem)
                .closeCase()
                .close(claimRequest, SettlementClaimService.CloseCaseReason.CLOSE_WITH_MAIL);
    }

    @Test(dataProvider = "usersDataProvider", groups = {PerformanceTestConfig.TEST_REOPEN_SAVED_CLAIM})
    public void reopenSavedClaim(User user) {

        ClaimRequest claimRequest = TestData.getClaimRequest();
        claimRequest.setTenant(user.getCompanyName().toLowerCase());
        claimRequest.setCompany(user.getCompanyName().toLowerCase());

        BaseService
                .loginAndOpenClaim(user, claimRequest)
                .saveClaim(claimRequest)
                .reopenClaim();
    }

    @Test(dataProvider = "usersDataProvider", groups = {PerformanceTestConfig.TEST_CANCEL_CLAIM})
    public void cancelClaim(User user) {

        ClaimRequest claimRequest = TestData.getClaimRequest();
        claimRequest.setTenant(user.getCompanyName().toLowerCase());
        claimRequest.setCompany(user.getCompanyName().toLowerCase());

        BaseService
                .loginAndOpenClaim(user, claimRequest)
                .saveClaim(claimRequest)
                .claimLines()
                .closeCase()
                .cancel(claimRequest);
    }

    @Test(dataProvider = "usersDataProvider", groups = {PerformanceTestConfig.TEST_TEXT_SEARCH})
    public void textSearch(User user) throws JsonProcessingException {

        ClaimRequest claimRequest = TestData.getClaimRequest();
        claimRequest.setTenant(user.getCompanyName().toLowerCase());
        claimRequest.setCompany(user.getCompanyName().toLowerCase());

        TextSearchService textSearchService = BaseService
                .loginAndOpenClaim(user, claimRequest)
                .searchText("samsung galaxy s7");

        ProductResult productResult = textSearchService
                .getTextSearchResult()
                .getProductResultList()
                .getProductResults()
                .get(0);

        textSearchService
                .sid(productResult)
                .addLines();
    }

    @DataProvider(name = "usersDataProvider", parallel = true)
    public static Object[][] usersDataProvider(Method method) throws InterruptedException {

        Object[][] objects = new Object[users][1];

        for (int i = 0; i < users; i++) {

            objects[i][0] = PerformanceUsers.takeUser();
        }

        return objects;
    }
}
