package com.scalepoint.automation.services.restService;

import com.scalepoint.automation.services.restService.common.BaseService;
import com.scalepoint.automation.utils.data.request.ClaimRequest;
import com.scalepoint.automation.utils.data.response.Token;
import io.restassured.response.Response;

import static com.scalepoint.automation.services.restService.common.BasePath.HEALTCH_CHECK;
import static com.scalepoint.automation.utils.Configuration.getEccUrl;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class HealthCheckService extends BaseService {

    private Response response;

    public HealthCheckService() {
    }

    public Response getResponse() {
        return this.response;
    }

    public HealthCheckService healthCheckStatus() {

        this.response = given().baseUri(getEccUrl())
                .log().all()
                .basePath(HEALTCH_CHECK)
                .when()
                .get()
                .then()
                .log().all()
                .extract().response();

        return this;
    }
}





