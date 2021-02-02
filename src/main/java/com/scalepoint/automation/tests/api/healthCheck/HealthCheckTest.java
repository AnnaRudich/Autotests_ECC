package com.scalepoint.automation.tests.api.healthCheck;

import com.scalepoint.automation.services.restService.HealthCheckService;
import com.scalepoint.automation.services.restService.UnifiedIntegrationService;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.tests.api.BaseApiTest;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class HealthCheckTest extends BaseApiTest {

    @Test(dataProvider = "testDataProvider", dataProviderClass = BaseTest.class, groups = {"healthCheck"})
    public void eccHealthCheckTest() {

        Response response = new HealthCheckService()
                .healthCheckStatus()
                .getResponse();

        String status = response.jsonPath().get("status");
        assertThat(status).isEqualTo("OK");
    }

    @Test(dataProvider = "testDataProvider", dataProviderClass = BaseTest.class, groups = {"healthCheck"})
    public void uniHealthCheckTest() {

        Response response = new UnifiedIntegrationService()
                .healthCheck();

        String status = response.jsonPath().get("status");
        assertThat(status).isEqualTo("pass");
    }
}
