package com.scalepoint.automation.tests.api;

import com.scalepoint.automation.services.restService.EccIntegrationService;
import com.scalepoint.automation.services.restService.SettlementClaimService;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.eccIntegration.EccIntegration;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.scalepoint.automation.services.restService.Common.BaseService.loginUser;
import static com.scalepoint.automation.services.restService.SettlementClaimService.CloseCaseReason.CLOSE_EXTERNAL;

public class ClaimIntegrationTests extends BaseApiTest {

    private EccIntegrationService eccIntegrationService;

    @BeforeMethod
    public void setUp(){
        eccIntegrationService = new EccIntegrationService();
    }

    @Test(dataProvider = "testDataProvider", dataProviderClass = BaseTest.class)
    public void createClaimUsingXmlIntegration(User user, EccIntegration eccIntegration){

        loginUser(user);
        createClaimXmlIntegration(eccIntegration).statusCode(HttpStatus.SC_MOVED_TEMPORARILY);
        openClaim().statusCode(HttpStatus.SC_MOVED_TEMPORARILY);

    }

    @Test(dataProvider = "testDataProvider", dataProviderClass = BaseTest.class)
    public void createClaimUsingXmlIntegrationWhenThisClaimWasPreviouslyClosed(User user, EccIntegration eccIntegration){

        loginUser(user);
        createClaimXmlIntegration(eccIntegration).statusCode(HttpStatus.SC_MOVED_TEMPORARILY);
        openClaim().statusCode(HttpStatus.SC_MOVED_TEMPORARILY);

        new SettlementClaimService().close(eccIntegration, CLOSE_EXTERNAL);

        createClaimXmlIntegration(eccIntegration);
        openClaim().statusCode(HttpStatus.SC_OK);

    }

    @Test(dataProvider = "testDataProvider", dataProviderClass = BaseTest.class)
    public void updateClaimUsingXmlIntegration(User user, EccIntegration eccIntegration){

        loginUser(user);
        createClaimXmlIntegration(eccIntegration).statusCode(HttpStatus.SC_MOVED_TEMPORARILY);
        openClaim().statusCode(HttpStatus.SC_MOVED_TEMPORARILY);

        createClaimXmlIntegration(eccIntegration);
        openClaim().statusCode(HttpStatus.SC_MOVED_TEMPORARILY);

    }

    @Test(dataProvider = "testDataProvider", dataProviderClass = BaseTest.class)
    public void createClaimUsingGetIntegration(User user, EccIntegration eccIntegration){

        loginUser(user);
        createClaimGetIntegration(eccIntegration).statusCode(HttpStatus.SC_MOVED_TEMPORARILY);
        openClaim().statusCode(HttpStatus.SC_MOVED_TEMPORARILY);
    }

    @Test(dataProvider = "testDataProvider", dataProviderClass = BaseTest.class)
    public void createClaimUsingGetIntegrationWhenThisClaimWasPreviouslyClosed(User user, EccIntegration eccIntegration){

        loginUser(user);
        createClaimGetIntegration(eccIntegration).statusCode(HttpStatus.SC_MOVED_TEMPORARILY);
        openClaim().statusCode(HttpStatus.SC_MOVED_TEMPORARILY);

        new SettlementClaimService().close(eccIntegration, CLOSE_EXTERNAL);

        createClaimGetIntegration(eccIntegration);
        openClaim().statusCode(HttpStatus.SC_OK);

    }

    @Test(dataProvider = "testDataProvider", dataProviderClass = BaseTest.class)
    public void updateClaimUsingGetIntegration(User user, EccIntegration eccIntegration){

        loginUser(user);
        createClaimGetIntegration(eccIntegration).statusCode(HttpStatus.SC_MOVED_TEMPORARILY);
        openClaim().statusCode(HttpStatus.SC_MOVED_TEMPORARILY);

        createClaimGetIntegration(eccIntegration);
        openClaim().statusCode(HttpStatus.SC_MOVED_TEMPORARILY);

    }

    private ValidatableResponse createClaimXmlIntegration(EccIntegration eccIntegration){
        return eccIntegrationService.createClaim(eccIntegration)
                .getResponse();
    }

    private ValidatableResponse createClaimGetIntegration(EccIntegration eccIntegration){
        return eccIntegrationService.createAndOpenClaim(eccIntegration)
                .getResponse();
    }

    private ValidatableResponse openClaim(){
        return eccIntegrationService.openCaseAndRedirect()
                .getResponse();
    }
}
