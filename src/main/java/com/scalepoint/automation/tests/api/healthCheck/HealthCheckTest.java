package com.scalepoint.automation.tests.api.healthCheck;

import com.scalepoint.automation.services.restService.HealthCheckService;
import com.scalepoint.automation.services.restService.UnifiedIntegrationService;
import com.scalepoint.automation.testGroups.TestGroups;
import com.scalepoint.automation.tests.api.BaseApiTest;
import io.restassured.response.Response;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class HealthCheckTest extends BaseApiTest {

    @Test(groups = {TestGroups.HEALTH_CHECK})
    @Parameters({"excludedHealthChecks"})
    public void eccHealthCheckTest(String excludedHealthChecks) {

       new HealthCheckService(excludedHealthChecks)
                .healthCheckStatus()
                .doAssert(asserts -> asserts.assertHealthChecks());

    }

    @Test(groups = {TestGroups.HEALTH_CHECK})
    public void uniHealthCheckTest() {

        Response response = new UnifiedIntegrationService()
                .healthCheck();

        String status = response.jsonPath().get("status");
        assertThat(status).isEqualTo("pass");
    }
}
