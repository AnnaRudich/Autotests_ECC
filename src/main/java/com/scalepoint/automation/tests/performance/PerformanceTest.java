package com.scalepoint.automation.tests.performance;

import com.scalepoint.automation.services.restService.common.BaseService;
import com.scalepoint.automation.tests.api.BaseApiTest;
import com.scalepoint.automation.utils.data.TestData;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.request.ClaimRequest;
import com.scalepoint.automation.utils.data.request.InsertSettlementItem;
import com.scalepoint.automation.utils.data.request.SelfServiceRequest;
import org.testng.annotations.*;

import java.lang.reflect.Method;

public class PerformanceTest extends BaseApiTest {

    String baseUri = "http://localhost:88/databot";
    String eccAdminUrl = "https://qa14.scalepoint.com/eccAdmin/dk";
    String selfService = "https://qa14.scalepoint.com/self-service/dk";

    static int users;

    @Parameters({"users"})
    @BeforeClass
    public void startWireMock(String users) {

        this.users = Integer.valueOf(users);
    }

    @AfterMethod
    public void releaseUser(Object[] objects){

        PerformanceUsers.releaseUser((User) objects[0]);
    }

    @Test(dataProvider = "usersDataProvider", enabled = false, priority=1)
    public void loginUser(User user) {

        BaseService
                .loginUser(user);
    }

    @Test(dataProvider = "usersDataProvider", enabled = true, priority=2)
    public void loginAndOpenClaim(User user) {

        ClaimRequest claimRequest = TestData.getClaimRequest();
        claimRequest.setTenant(user.getCompanyName().toLowerCase());
        claimRequest.setCompany(user.getCompanyName().toLowerCase());

        BaseService
                .loginAndOpenClaim(user, claimRequest);
    }

    @Test(dataProvider = "usersDataProvider", enabled = false, priority=3)
    public void selfService(User user) {

        ClaimRequest claimRequest = TestData.getClaimRequest();
        claimRequest.setTenant(user.getCompanyName().toLowerCase());
        claimRequest.setCompany(user.getCompanyName().toLowerCase());
        SelfServiceRequest selfServiceRequest = TestData.getSelfServiceRequest();

        BaseService
                .loginAndOpenClaim(user, claimRequest)
                .requestSelfService(selfServiceRequest);
    }

    @Test(dataProvider = "usersDataProvider", enabled = false, priority = 4)
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
