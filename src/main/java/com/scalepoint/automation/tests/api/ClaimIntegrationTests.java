package com.scalepoint.automation.tests.api;

import com.scalepoint.automation.services.restService.EccIntegrationService;
import com.scalepoint.automation.services.restService.SettlementClaimService;
import com.scalepoint.automation.testGroups.TestGroups;
import com.scalepoint.automation.tests.BaseUITest;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.eccIntegration.EccIntegration;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static com.scalepoint.automation.services.restService.SettlementClaimService.CloseCaseReason.CLOSE_EXTERNAL;
import static com.scalepoint.automation.services.restService.common.BaseService.loginUser;

public class ClaimIntegrationTests extends BaseApiTest {

    private EccIntegrationService eccIntegrationService;

    @BeforeMethod(alwaysRun = true)
    public void setUp(Object[] objects) {

        List parameters = Arrays.asList(objects);

        User user = getObjectByClass(parameters, User.class).get(0);

        eccIntegrationService = new EccIntegrationService();

        loginUser(user);
    }

    @Test(groups = {TestGroups.UNIFIEDPAYMENTS,
            TestGroups.BACKEND,
            TestGroups.CLAIM_INTEGRATION},
            dataProvider = BaseUITest.TEST_DATA_PROVIDER, dataProviderClass = BaseUITest.class)
    public void createClaimUsingXmlIntegration(User user, EccIntegration eccIntegration) {

        createClaimXmlIntegration(eccIntegration).statusCode(HttpStatus.SC_MOVED_TEMPORARILY);
        openClaim().statusCode(HttpStatus.SC_MOVED_TEMPORARILY);

    }

    @Test(groups = {TestGroups.UNIFIEDPAYMENTS,
            TestGroups.BACKEND,
            TestGroups.CLAIM_INTEGRATION},
            dataProvider = BaseUITest.TEST_DATA_PROVIDER, dataProviderClass = BaseUITest.class)
    public void createClaimUsingXmlIntegrationWhenThisClaimWasPreviouslyClosed(User user, EccIntegration eccIntegration) {

        createClaimXmlIntegration(eccIntegration).statusCode(HttpStatus.SC_MOVED_TEMPORARILY);
        openClaim().statusCode(HttpStatus.SC_MOVED_TEMPORARILY);

        new SettlementClaimService().close(eccIntegration, CLOSE_EXTERNAL);

        createClaimXmlIntegration(eccIntegration);
        openClaim().statusCode(HttpStatus.SC_OK);

    }

    @Test(groups = {TestGroups.UNIFIEDPAYMENTS,
            TestGroups.BACKEND,
            TestGroups.CLAIM_INTEGRATION},
            dataProvider = BaseUITest.TEST_DATA_PROVIDER, dataProviderClass = BaseUITest.class)
    public void updateClaimUsingXmlIntegration(User user, EccIntegration eccIntegration) {

        createClaimXmlIntegration(eccIntegration).statusCode(HttpStatus.SC_MOVED_TEMPORARILY);
        openClaim().statusCode(HttpStatus.SC_MOVED_TEMPORARILY);

        createClaimXmlIntegration(eccIntegration);
        openClaim().statusCode(HttpStatus.SC_MOVED_TEMPORARILY);

    }

    @Test(groups = {TestGroups.UNIFIEDPAYMENTS,
            TestGroups.BACKEND,
            TestGroups.CLAIM_INTEGRATION},
            dataProvider = BaseUITest.TEST_DATA_PROVIDER, dataProviderClass = BaseUITest.class)
    public void createClaimUsingGetIntegration(User user, EccIntegration eccIntegration) {

        createClaimGetIntegration(eccIntegration).statusCode(HttpStatus.SC_MOVED_TEMPORARILY);
        openClaim().statusCode(HttpStatus.SC_MOVED_TEMPORARILY);
    }

    @Test(groups = {TestGroups.UNIFIEDPAYMENTS,
            TestGroups.BACKEND,
            TestGroups.CLAIM_INTEGRATION},
            dataProvider = BaseUITest.TEST_DATA_PROVIDER, dataProviderClass = BaseUITest.class)
    public void createClaimUsingGetIntegrationWhenThisClaimWasPreviouslyClosed(User user, EccIntegration eccIntegration) {

        createClaimGetIntegration(eccIntegration).statusCode(HttpStatus.SC_MOVED_TEMPORARILY);
        openClaim().statusCode(HttpStatus.SC_MOVED_TEMPORARILY);

        new SettlementClaimService().close(eccIntegration, CLOSE_EXTERNAL);

        createClaimGetIntegration(eccIntegration);
        openClaim().statusCode(HttpStatus.SC_OK);

    }

    @Test(groups = {TestGroups.UNIFIEDPAYMENTS,
            TestGroups.BACKEND,
            TestGroups.CLAIM_INTEGRATION},
            dataProvider = BaseUITest.TEST_DATA_PROVIDER, dataProviderClass = BaseUITest.class)
    public void updateClaimUsingGetIntegration(User user, EccIntegration eccIntegration) {

        createClaimGetIntegration(eccIntegration).statusCode(HttpStatus.SC_MOVED_TEMPORARILY);
        openClaim().statusCode(HttpStatus.SC_MOVED_TEMPORARILY);

        createClaimGetIntegration(eccIntegration);
        openClaim().statusCode(HttpStatus.SC_MOVED_TEMPORARILY);

    }

    private ValidatableResponse createClaimXmlIntegration(EccIntegration eccIntegration) {
        return eccIntegrationService.createClaim(eccIntegration)
                .getResponse();
    }

    private ValidatableResponse createClaimGetIntegration(EccIntegration eccIntegration) {
        return eccIntegrationService.createAndOpenClaim(eccIntegration)
                .getResponse();
    }

    private ValidatableResponse openClaim() {
        return eccIntegrationService.openCaseAndRedirect()
                .getResponse();
    }
}
